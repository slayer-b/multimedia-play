package controllers

import play.api._
import libs.concurrent.Promise
import play.api.mvc._
import management.OperatingSystemMXBean

object Application extends Controller {

  def index = Action { implicit request =>
    Logger.info("Rendering index")

    Ok(views.html.index("Your new application is ready."))
  }

  def testTable = Action { implicit request =>
    Logger.info("Rendering table view")
    Ok(views.html.test_table())
  }

  def cms = Action { implicit request =>
    Logger.info("Rendering cms view")
    Ok(views.html.cms())
  }
  
  def javascriptRoutes = Action { implicit request =>
    Ok(
      Routes.javascriptRouter("jsRoutes")(
        routes.javascript.EditWallpaperPublic.view,
        routes.javascript.EditWallpaperPublic.viewDB,
        routes.javascript.EditWallpaperPublic.edit
      )
    ).as("text/javascript")
  }

}
