package domain.supoport

import org.sisioh.dddbase.core.model.{Entity, Identifier}

import scala.util.{Failure, Success, Try}

trait MultiIOSupport[ID <: Identifier[_], E <: Entity[ID]] {
  this: Repository[ID, E] =>

  def storeMulti(entities: E*)(implicit ctx: Ctx): Try[(This, Seq[E])] = {
    traverseWithThis(entities) {
      (repo, entity) => repo.store(entity).asInstanceOf[Try[(This, E)]]
    }
  }

  def resolveByIds(ids: ID*)(implicit ctx: Ctx): Try[Seq[E]] = traverse(ids)(resolveById)

  def resolveByOffsetWithLimit(offset: Int, limit: Int = 100)(implicit ctx: Ctx): Try[Seq[E]]

  def existByIds(ids: ID*)(implicit ctx: Ctx): Try[Boolean] = {
    traverse(ids)(existById).map(_.forall(_ == true))
  }

  def deleteByIds(ids: ID*)(implicit ctx: Ctx): Try[(This, Seq[E])] = {
    traverseWithThis(ids) {
      (repo, id) => repo.deleteById(id).asInstanceOf[Try[(This, E)]]
    }
  }

  protected final def traverseWithThis[A](values: Seq[A])
                                         (processor: (This, A) => Try[(This, E)])
                                         (implicit ctx: Ctx): Try[(This, Seq[E])] = Try {
    values.foldLeft((this.asInstanceOf[This], Seq.empty[E])) {
      case ((repo, entities), value) => processor(repo, value).map {
        case (r, e) => (r, entities :+ e)
      }.get
    }
  }

  protected def traverseWithoutFailures[A, R](values: Seq[A])(f: (A) => Try[R])(implicit ctx: Ctx): Try[Seq[R]] = {
    traverse[A, R](values, forceSuccess = true)(f)
  }

  protected def traverse[A, R](values: Seq[A], forceSuccess: Boolean = false)(f: (A) => Try[R])(implicit ctx: Ctx): Try[Seq[R]] = {
    values.map(f).foldLeft(Try(Seq.empty[R])) {
      (entitiesTry, entityTry) =>
        (for {entities <- entitiesTry; entity <- entityTry} yield entities :+ entity).recoverWith {
          case e => if (forceSuccess) Success(entitiesTry.getOrElse(Seq.empty[R])) else Failure(e)
        }
    }
  }
}

trait Repository[ID <: Identifier[_], E <: Entity[ID]] {

  type This <: Repository[ID, E]

  type Ctx = EntityIOContext

  def store(entity: E)(implicit ctx: Ctx): Try[(This, E)]

  def resolveById(id: ID)(implicit  ctx: Ctx): Try[E]

  def existById(id: ID)(implicit  ctx: Ctx): Try[Boolean]

  def deleteById(id: ID)(implicit ctx: Ctx): Try[(This, E)]

}
