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
import model.WallpaperRepo

object EditWallpaperPublic extends Controller {

  implicit val timeout = Timeout(5, TimeUnit.SECONDS)

  implicit val wallpaperEntryReads = Json.reads[WallpaperEntry]
  implicit val wallpaperEntryRezReads = Json.reads[WallpaperEntryRez]
  implicit val wallpaperEntryRezWrites = Json.writes[WallpaperEntryRez]
  
  def edit = Action(parse.json) { implicit request =>
    Logger.info("edit")
    Json.fromJson[WallpaperEntry](request.body).map { data =>
      MyActors.wallpaper ! Replace(data)
      Ok(Json.toJson(
        Map("status" -> "OK")
      ))
    }.recoverTotal {
      e => BadRequest("Detected error:"+ JsError.toFlatJson(e))
    }
  }
  
  def view = Action.async(parse.json) {implicit request =>
      Logger.info("view")
      val wallpaperFuture = MyActors.wallpaper ? FindAll
      for {
        wallpapers <- wallpaperFuture
      } yield wallpapers match {
        case WallpaperList(list) =>
          Ok(JsArray(
            //MyTestEhCache.generateRandom().map(Json.toJson(_))
            list.map(Json.toJson(_))
          ))
      }
  }

  def viewDB = Action(parse.json) {implicit request =>
      Logger.info("view db")
      val wallpapers = WallpaperRepo.findAll(10)
      Ok(JsArray(
        wallpapers.map { w =>
          Json.obj(
            "id" -> w.id,
            "entry_id" -> JsNull,
            "field" -> "name",
            "value" -> w.name
          )
        }
      ))
  }

  def accept = Action(parse.json) {implicit request =>
    Logger.info("accept")
    Json.fromJson[WallpaperEntryRez](request.body).map { data =>
      MyActors.wallpaper ! AcceptEntry(data)
      Ok(Json.toJson(
        Map("status" -> "OK")
      ))
    }.recoverTotal {
      e => BadRequest("Detected error:"+ JsError.toFlatJson(e))
    }
  }

  def reject = Action(parse.json) {implicit request =>
    Logger.info("reject")
    Json.fromJson[WallpaperEntryRez](request.body).map { data =>
      MyActors.wallpaper ! RemoveEntry(data)
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
