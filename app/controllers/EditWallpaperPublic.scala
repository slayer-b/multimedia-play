package controllers

import play.api.mvc._
import play.api.Logger
import play.api.libs.functional.syntax._
import play.api.libs.json.{Json, _}

/**
 * User: slayer
 * Date: 18.12.12
 */
object EditWallpaperPublic extends Controller {
  val logger = Logger

  val wallpaperEditDataReads = Json.reads[WallpaperEditData]


  def edit = Action(parse.json) { implicit request =>
    logger.info("edit")
    Json.fromJson[WallpaperEditData](request.body)(wallpaperEditDataReads).asOpt.map { data =>
      Ok(Json.toJson(
        Map("status" -> "OK")
      ))
    }.getOrElse {
      Ok(Json.toJson(
        Map("status" -> "FAIL")
      ))
    }
  }

}

case class WallpaperEditData(value: String, field: String, id: Long)