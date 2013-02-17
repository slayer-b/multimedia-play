package controllers

import play.api._
import play.api.mvc._
import play.api.cache._
import play.api.Play.current

object MyTestEhCache {
  
  val ehPlugin = Play.current.plugin(classOf[EhCachePlugin])
  val cache = ehPlugin.get.cache
  
  def findAll() = {
    Logger.info("Find all")
    val keys = cache.getKeys
    var i = 0
    val size = keys.size
    var rez = List.empty[WallpaperEntry]
    while (i < size) {
      val key = keys.get(i).toString
      val value = Cache.getAs[WallpaperEntries](key).get.values
      value.foreach {
        case (k, v) => v.foreach {
          value => rez = WallpaperEntry(key, k, value) :: rez
        }
      }
      i += 1
    }
    rez
  }
  
  def remove(id: Long) {
    Logger.info("Remove entry: id=" + id)
    Cache.remove(id.toString)
  }
  
  def replace(entry: WallpaperEntry) {
    Logger.info("Adding new entry: id=" + entry.id)
    val newEntry = Cache.getAs[WallpaperEntries](entry.id.toString) match {
      case Some(old) =>
        val newValues = old.values.getOrElse(entry.field, List()) :+ entry.value
        WallpaperEntries(old.values + (entry.field -> newValues))
      case None =>
        WallpaperEntries(Map(entry.field -> List(entry.value)))
    }
    Cache.set(entry.id.toString, newEntry)
  }
  
}

case class WallpaperEntries(values: Map[String, List[String]])
