import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "multimedia-play"
  val appVersion      = "1.0-" + System.getProperty("build.number")

  val appDependencies = Seq(
    "mysql" % "mysql-connector-java" % "5.1.18",
    jdbc,
    cache,
    "com.typesafe.slick" %% "slick" % "1.0.0",
    "com.typesafe.akka" % "akka-testkit_2.10" % "2.1.0" % "test"
  )


  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here
  )

}
