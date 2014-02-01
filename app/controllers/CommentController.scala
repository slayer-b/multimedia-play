package controllers

import play.api.mvc.{Action, Controller}
import play.api.libs.json._

object CommentController extends Controller {

  def fields() = Action {
    Ok(Json.arr(
      "id", "id_photo", "text"
    ))
  }
}
