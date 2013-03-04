package model

import DataAccessLayer.profile.simple._
import java.sql.Timestamp

import play.api.db.DB
import play.api.Play.current
import play.api.Logger

import Database.threadLocalSession

case class Wallpaper(
  id: Option[Long] = None,
  id_pages: Option[Long] = None,
  id_users: Option[Long] = None,
  name: String,
  description: Option[String] = None,
  title: Option[String] = None,
  tags: Option[String] = None,
  width: Int,
  height: Int,
  views: Long,
  active: Boolean,
  optimized: Boolean,
  optimized_manual: Boolean,
  date_upload: Timestamp,
  folder: String
)

object WallpaperRepo {
  lazy val database = Database.forDataSource(DB.getDataSource())

  val FIELD_VALUES = Map(
    "description" -> description _,
    "title" -> title _
  )

  def findAll(quantity: Int) = {
    database withSession {
      val query = for (
        w <- Wallpapers
      ) yield w
      query.take(quantity).list()
    }
  }

  def update(id: Long, field: String, value: String) = {
    database withSession {
      val query = for (
        w <- Wallpapers if w.id === id
      ) yield FIELD_VALUES(field)(w)
      query.update(value)
    }
  }

  private def description(w: Wallpapers.type) = w.description
  private def title(w: Wallpapers.type) = w.title
}

object Wallpapers extends Table[Wallpaper]("photo") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def id_pages = column[Long]("id_pages")
  def id_users = column[Long]("id_users")
  def name = column[String]("name", O.NotNull, O.DBType("VARCHAR(255)"))
  def description = column[String]("description_ru", O.DBType("LONGTEXT")) //HtmlSpecialChars
  def title = column[String]("title_ru", O.DBType("LONGTEXT"))//HtmlSpecialChars
  def tags = column[String]("tags", O.DBType("LONGTEXT"))//HtmlSpecialChars
  def width = column[Int]("width", O.NotNull)
  def height = column[Int]("height", O.NotNull)
  def views = column[Long]("views", O.NotNull)
  def active = column[Boolean]("active", O.NotNull)
  def optimized = column[Boolean]("optimized", O.NotNull)
  def optimized_manual = column[Boolean]("optimized_manual", O.NotNull)
  def date_upload = column[Timestamp]("date_upload", O.NotNull)
  def folder = column[String]("folder", O.NotNull)

  def * =
    id.? ~ id_pages.? ~ id_users.? ~ name ~ description.? ~ title.? ~ tags.? ~ width ~ height ~ views ~
      active ~ optimized ~ optimized_manual ~ date_upload ~ folder <>(Wallpaper, Wallpaper.unapply _)
}
