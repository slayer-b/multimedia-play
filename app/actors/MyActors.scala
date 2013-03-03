package actors

import akka.actor.{Props, ActorSystem}

object MyActors {

  lazy val system = ActorSystem("custom")
  lazy val wallpaperComment = system.actorOf(Props[WallpaperCommentActor])

}
