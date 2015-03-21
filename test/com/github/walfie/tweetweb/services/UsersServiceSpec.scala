package com.github.walfie.tweetweb.services

import com.github.walfie.tweetweb.models.{DAO, User}

import org.joda.time.DateTime
import org.specs2.mock._
import org.specs2.mutable._
import play.api.db.slick.Config.driver.simple._
import play.api.db.slick.DB
import play.api.test.Helpers._
import play.api.test._
import scala.slick.jdbc.JdbcBackend.Database

class UsersServiceSpec extends Specification with Mockito {
  "SlickUsersService" >> {
    def fakeApp(): FakeApplication =
      FakeApplication(additionalConfiguration = inMemoryDatabase())

    "save and find" should {
      "return the saved user" in new WithApplication(fakeApp) {
        val dao = new DAO(DB.driver)
        val usersService = new SlickUsersService(DB, dao)

        val user: User = User(
          id = "123",
          name = "example",
          iconUrl = "http://example.com/image.png",
          updatedAt = new DateTime(0))
        usersService.save(user)

        usersService.find(List("123")) must_== List(user)
      }
    }

    "save" should {
      "overwrite existing users" in new WithApplication(fakeApp) {
        val dao = new DAO(DB.driver)
        val usersService = new SlickUsersService(DB, dao)

        val user: User = User(id = "1", name = "example")
        usersService.save(user)

        val userUpdated: User = User(id = "1", name = "this_is_different_now")
        usersService.save(userUpdated)

        usersService.find(List("1")) must_== List(userUpdated)
      }
    }

    "find" should {
      "return found users" in new WithApplication(fakeApp) {
        val dao = new DAO(DB.driver)
        val usersService = new SlickUsersService(DB, dao)

        val user: User = User(id = "1")
        usersService.save(user)

        usersService.find(List("1", "2")) must_== List(user)
      }

      "return empty list if no users found" in new WithApplication(fakeApp) {
        val dao = new DAO(DB.driver)
        val usersService = new SlickUsersService(DB, dao)

        usersService.find(List("non existent id")) must have size(0)
      }
    }
  }
}
