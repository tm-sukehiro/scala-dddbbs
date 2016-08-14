package infrastructure.db

import skinny.orm.SkinnyCRUDMapper

trait CRUDMapper[T] extends SkinnyCRUDMapper[T] {

  override def primaryKeyFieldName: String = "pk"

  def toNamedValues(record: T): Seq[(Symbol, Any)]

}
