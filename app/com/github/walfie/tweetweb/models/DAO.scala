package com.github.walfie.tweetweb.models

import scala.slick.driver.JdbcProfile
import scala.slick.lifted.TableQuery
import play.api.db.slick.{Config, Profile}

class DAO(override val profile: JdbcProfile)
    extends UserComponent
    with Profile

object current {
  val dao = new DAO(Config.driver)
}

