package test

import org.specs2.mutable._

import play.api.test._
import play.api.test.Helpers._
import play.api.libs.json.Json

class EditWallpaperPublicSpec extends Specification {
  val id = "1"
  val field = "description"

  "EditWallpaperPublic" should {

    "add new record into cache" in {
      running(FakeApplication()) {
        val result = addRecord("test-1")

        status(result) must equalTo(OK)
        contentAsString(result) must equalTo("{\"status\":\"OK\"}")
      }
    }

    "get records from cache" in {
      running(FakeApplication()) {
        addRecord("test-1")
        addRecord("test-2")

        val request = FakeRequest(POST, "/p/c/w/view_cache.json").withJsonBody(Json.toJson(""))
        val result = route(request).get

        status(result) must equalTo(OK)
        contentAsString(result) must equalTo("[{\"id\":\"1\",\"entry_id\":1,\"field\":\"description\",\"value\":\"test-2\"}," +
          "{\"id\":\"1\",\"entry_id\":0,\"field\":\"description\",\"value\":\"test-1\"}]")
      }
    }

  }

  def addRecord(value: String) = {
    val request = FakeRequest(POST, "/p/c/w/edit").withJsonBody(
      Json.toJson(
        Map("id" -> id, "field" -> field, "value" -> value)
      ))
    route(request).get
  }
}
