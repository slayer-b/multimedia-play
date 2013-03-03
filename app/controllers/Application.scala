package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json.{Json, _}

object Application extends Controller {
  val logger = Logger

  def index = Action { implicit request =>
    logger.info("Rendering index")
    Ok(views.html.index("Your new application is ready."))
  }

  def testTable = Action { implicit request =>
    logger.info("Rendering table view")
    Ok(views.html.test_table())
  }
  
  def javascriptRoutes = Action { implicit request =>
  import routes.javascript._
    Ok(
      Routes.javascriptRouter("jsRoutes")(
        routes.javascript.EditWallpaperPublic.view,
        routes.javascript.EditWallpaperPublic.viewDB,
        routes.javascript.EditWallpaperPublic.edit
      )
    ).as("text/javascript")
  }


}
