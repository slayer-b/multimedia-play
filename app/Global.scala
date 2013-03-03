import play.api.db.DB
import play.api.GlobalSettings

import model.DataAccessLayer.profile.simple._

// Use the implicit threadLocalSession
import Database.threadLocalSession

import play.api.Application
import play.api.Play.current


object Global extends GlobalSettings {

  override def onStart(app: Application) {

    lazy val database = Database.forDataSource(DB.getDataSource())

  }
}
