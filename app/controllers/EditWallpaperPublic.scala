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

  implicit val wallpaperEntryReads = Json.reads[WallpaperEntry]
  implicit val wallpaperEntryRezReads = Json.reads[WallpaperEntryRez]
  implicit val wallpaperEntryRezWrites = Json.writes[WallpaperEntryRez]
  
  def edit = Action(parse.json) { implicit request =>
    logger.info("edit")
    Json.fromJson[WallpaperEntry](request.body).map { data =>
      MyTestEhCache.replace(data)
      Ok(Json.toJson(
        Map("status" -> "OK")
      ))
    }.recoverTotal {
      e => BadRequest("Detected error:"+ JsError.toFlatJson(e))
    }
  }
  
  def view = Action(parse.json) {implicit request =>
    logger.info("view")
    Ok(JsArray(
      //MyTestEhCache.generateRandom().map(Json.toJson(_))
      MyTestEhCache.findAll().map(Json.toJson(_))
    ))
  }

  def accept = Action(parse.json) {implicit request =>
    logger.info("accept")
    Ok("Accepted")
  }

  def reject = Action(parse.json) {implicit request =>
    logger.info("reject")
    Json.fromJson[WallpaperEntryRez](request.body).map { data =>
      MyTestEhCache.removeEntry(data)
      Ok(Json.toJson(
        Map("status" -> "OK")
      ))
    }.recoverTotal {
      e => BadRequest("Detected error:"+ JsError.toFlatJson(e))
    }
  }

}

case class WallpaperEntry(id: String, field: String, value: String)
case class WallpaperEntryRez(id: String, entry_id: Int, field: String, value: String)