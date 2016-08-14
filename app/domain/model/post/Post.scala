package domain.model.post

import org.sisioh.dddbase.core.model.Entity

case class Post(
               identifier: PostID,
               name: String,
               email: String,
               comment: String
               ) extends Entity[PostID] {

}
