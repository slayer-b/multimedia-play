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

}
