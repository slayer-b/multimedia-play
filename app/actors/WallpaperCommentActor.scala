package actors

import akka.actor.Actor
import play.api.{cache, Logger, Play}
import play.api.cache.{Cache, EhCachePlugin}
import play.api.Play.current

import controllers.{WallpaperEntry, WallpaperEntryRez}

import scala.annotation.tailrec
import controllers.WallpaperEntryRez
import controllers.WallpaperEntry

class WallpaperCommentActor extends Actor {

  val ehPlugin = Play.current.plugin(classOf[EhCachePlugin])
  val cache = ehPlugin.get.cache

  def receive = {
    case FindAll =>
      Logger.info("Find all")
      val keys = cache.getKeys
      var i = 0
      val size = keys.size
      var rez = List.empty[WallpaperEntryRez]
      while (i < size) {
        val key = keys.get(i).toString
        val value = Cache.getAs[WallpaperEntries](key).get.values
        value.foreach {
          case (k, v) => v.entries.foreach {
            case (k2, v2) => rez = WallpaperEntryRez(key, k2, k, v2) :: rez
          }
        }
        i += 1
      }
      sender ! WallpaperCommentList(rez)
    case GenerateRandom =>
      Logger.info("Generate random")
      @tailrec
      def generate(rez: List[WallpaperEntryRez] = List.empty[WallpaperEntryRez], n: Int): List[WallpaperEntryRez] = {
        if (n == 0) rez
        else generate(WallpaperEntryRez("1", n, "name", "name" + n) :: rez, n-1)
      }
      sender ! WallpaperCommentList(generate(n = 10))
    case Remove(id) =>
      Logger.info("Remove entry: id=" + id)
      Cache.remove(id.toString)
    case RemoveEntry(entry) =>
      Logger.info("Remove entry: id=" + entry.id + " entry_id=" + entry.entry_id)
      for {
        old <- Cache.getAs[WallpaperEntries](entry.id.toString)
        oldField <- old.values.get(entry.field)
      } {
        val newValues = oldField.entries - entry.entry_id
        val newField = WallpaperField(oldField.max, newValues)
        Cache.set(entry.id.toString, WallpaperEntries(old.values + (entry.field -> newField)))
      }
    case Replace(entry) =>
      Logger.info("Adding new entry: id=" + entry.id)
      val newEntry = Cache.getAs[WallpaperEntries](entry.id.toString) match {
        case Some(old) =>
          val oldField = old.values.getOrElse(entry.field, WallpaperField(0, Map.empty[Int, String]))
          val newValues = oldField.entries + (oldField.max -> entry.value)
          val newField = WallpaperField(oldField.max + 1, newValues)
          WallpaperEntries(old.values + (entry.field -> newField))
        case None =>
          WallpaperEntries(Map(entry.field -> WallpaperField(1, Map(0 -> entry.value))))
      }
      Cache.set(entry.id.toString, newEntry)
  }
}

sealed trait Message
case object FindAll extends Message
case object GenerateRandom extends Message
case class Remove(id: Long) extends Message
case class RemoveEntry(entry: WallpaperEntryRez) extends Message
case class Replace(entry: WallpaperEntry) extends Message

case class WallpaperCommentList(list: List[WallpaperEntryRez])

case class WallpaperEntries(values: Map[String, WallpaperField])
case class WallpaperField(max: Int, entries: Map[Int, String])