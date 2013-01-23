package controllers

import play.api._
import play.api.libs.concurrent._
import play.api.mvc._
import libs.openid.{UserInfo, OpenID}
import scala.concurrent.ExecutionContext.Implicits.global
import util.{Success, Failure}

object Application extends Controller {
  val logger = Logger

  def index = Action { implicit request =>
    logger.info("Rendering index")
    Ok(views.html.index("Your new application is ready."))
  }

  def login = Action { implicit request =>
    AsyncResult {
      logger.info("Before")
      OpenID.verifiedId.extend( _.value.get match {
        case Success(info: UserInfo) => {
          logger.info("Succeed : " + info.id.toString)
          Ok(info.id + "\n" + info.attributes)
        }
        case Failure(t) => {
          logger.info("Error")
          // Here you should look at the error, and give feedback to the user
          Ok("Error")
        }
      })
    }
  }

}
