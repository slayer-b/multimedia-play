
import play.api.{Logger, GlobalSettings, Application}

object Global extends GlobalSettings {

  override def onStart(app: Application) {
    Logger.info("Application started...")
  }

  override def onStop(app: Application) {
    Logger.info("Application shutdown...")
  }

  //TODO: implement some kind of an error page
//  override def onError(request: RequestHeader, ex: Throwable) = {
//    Logger.info("Application error")
//    InternalServerError(
//      views.html.errorPage(ex)
//    )
//  }

  //TODO: implement some kind of an not found page
//  override def onHandlerNotFound(request: RequestHeader): Result = {
//    NotFound(
//      views.html.notFoundPage(request.path)
//    )
//  }
}
