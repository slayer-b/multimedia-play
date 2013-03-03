package actors

import akka.actor.{Props, ActorSystem}

object MyActors {

  lazy val system = ActorSystem("custom")
  lazy val wallpaper = system.actorOf(Props[WallpaperActor])

}
