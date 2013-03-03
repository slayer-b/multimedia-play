package controllers

import play.api.mvc._
import play.api.Logger
import play.api.libs.functional.syntax._
import play.api.libs.json.{Json, _}
import play.api.libs.concurrent.Execution.Implicits._

import actors._

import akka.util.Timeout
import akka.pattern.ask

import java.util.concurrent.TimeUnit
import actors.RemoveEntry
import actors.Replace
import play.api.libs.json.JsArray

object EditWallpaperPublic extends Controller {
  val logger = Logger

  implicit val timeout = Timeout(5, TimeUnit.SECONDS)

  implicit val wallpaperEntryReads = Json.reads[WallpaperEntry]
  implicit val wallpaperEntryRezReads = Json.reads[WallpaperEntryRez]
  implicit val wallpaperEntryRezWrites = Json.writes[WallpaperEntryRez]
  
  def edit = Action(parse.json) { implicit request =>
    logger.info("edit")
    Json.fromJson[WallpaperEntry](request.body).map { data =>
      MyActors.wallpaperComment ! Replace(data)
      Ok(Json.toJson(
        Map("status" -> "OK")
      ))
    }.recoverTotal {
      e => BadRequest("Detected error:"+ JsError.toFlatJson(e))
    }
  }
  
  def view = Action(parse.json) {implicit request =>
    Async {
      logger.info("view")
      val commentsFuture = MyActors.wallpaperComment ? FindAll
      for {
        comments <- commentsFuture
      } yield comments match {
        case WallpaperCommentList(list) =>
          Ok(JsArray(
            //MyTestEhCache.generateRandom().map(Json.toJson(_))
            list.map(Json.toJson(_))
          ))
      }
    }
  }

  def accept = Action(parse.json) {implicit request =>
    logger.info("accept")
    Ok("Accepted")
  }

  def reject = Action(parse.json) {implicit request =>
    logger.info("reject")
    Json.fromJson[WallpaperEntryRez](request.body).map { data =>
      MyActors.wallpaperComment ! RemoveEntry(data)
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