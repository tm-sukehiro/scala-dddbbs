package domain.supoport

import infrastructure.db.{CRUDMapper, DaoSupport}
import org.sisioh.dddbase.core.model.{Entity, Identifier}
import scalikejdbc._

import scala.util.Try

case class EntityIOContextOnJDBC(session: DBSession) extends EntityIOContext

trait SimpleRepositoryOnDJBC[ID <: Identifier[Long], E <: Entity[ID]] extends RepositoryOnJDBC[ID, E] {
  self =>

  private object Dao extends DaoSupport[T] {
    override protected val mapper = self.mapper
  }

  protected def convertToEntity(record: T): E

  protected def convertToRecord(entity: E): T

  override def resolveById(id: ID)(implicit ctx: Ctx): Try[E] = ???

  override def store(entity: E)(implicit ctx: Ctx): Try[(This, E)] = ???

  override def deleteById(id: ID)(implicit ctx: Ctx): Try[(This, E)] = ???

  override def resolveByOffsetWithLimit(offset: Int, limit: Int)(implicit ctx: Ctx): Try[Seq[E]] = ???

}

abstract class RepositoryOnJDBC[ID <: Identifier[Long], E <: Entity[ID]] extends Repository[ID, E] with MultiIOSupport[ID, E] {

  type T

  protected val mapper: CRUDMapper[T]

  protected val idName: String = "id"

  protected def withDBSession[A](ctx: EntityIOContext)(f: DBSession => A): A = {
    ctx match {
      case EntityIOContextOnJDBC(dbSession) => f(dbSession)
      case _ => throw new IllegalStateException(s"Unexpected context is bound (expected: JDBCEntityIOContext, actual: $ctx)")
    }
  }

  override def existById(id: ID)(implicit ctx: Ctx): Try[Boolean] = withDBSession(ctx) {
    implicit s => Try {
      val count = mapper.countBy(sqls.eq(mapper.defaultAlias.field(idName), id.value))
      if (count == 0) false
      else if (count == 1) true
      else throw new IllegalStateException(s"$count entities are found for identifier: $id")
    }
  }

  override def existByIds(ids: ID*)(implicit ctx: Ctx): Try[Boolean] = withDBSession(ctx) {
    implicit s =>
      Try(mapper.countBy(sqls.in(mapper.defaultAlias.field(idName), ids.map(_.value))) > 0)
  }

}
