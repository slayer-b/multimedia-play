package model

import slick.driver.{ExtendedDriver, MySQLDriver}

/**
 * We need this to abstract away from database
 */
object DataAccessLayer {
  val profile: ExtendedDriver = MySQLDriver
}
