package domain.supoport

import org.sisioh.dddbase.core.model.Identifier

case class EntityNotFoundException(message: String) extends Exception(message)

object EntityNotFoundException {

  def apply(id: Identifier[Any]): EntityNotFoundException =
    EntityNotFoundException(s"Entity is not found(id = $id)")

}
