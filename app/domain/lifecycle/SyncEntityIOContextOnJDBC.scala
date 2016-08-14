package domain.lifecycle

import java.sql.Connection

import org.sisioh.dddbase.core.lifecycle.sync.SyncEntityIOContext
import play.api.Play.current
import play.api.db.DB

class SyncEntityIOContextOnJDBC(connection: Connection) extends SyncEntityIOContext

object SyncEntityIOContextOnJDBC {
  def setSession(dbname: String = "default"): SyncEntityIOContext =

    SyncEntityIOContextOnJDBC(DB.getConnection(dbname))
}
