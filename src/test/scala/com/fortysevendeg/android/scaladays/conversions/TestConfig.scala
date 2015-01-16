package com.fortysevendeg.android.scaladays.conversions

import com.fortysevendeg.android.scaladays.utils.DateTimeUtils
import org.joda.time.DateTime

trait TestConfig {
  
  val slotId = 1

  val trackId = 2

  val locationId = 3

  val speakerId = 4

  val informationId = 5

  val eventId = 6

  val wrongId = -1000
  
  val name = "name"

  val longName = "longName"

  val nameAndLocation = "nameAndLocation"

  val firstDayString = "2015-03-16"

  val firstDayStringError = "unknown_date"

  lazy val firstDay = DateTime.parse(firstDayString, DateTimeUtils.ISODateFormatterDayPrecission)

  val lastDayString = "2015-03-20"

  val lastDayStringError = "unknown_date"

  lazy val lastDay = DateTime.parse(lastDayString, DateTimeUtils.ISODateFormatterDayPrecission)

  val title = "title"

  val company = "company"

  val twitter = "twitter"

  val picture = "picture"

  val bio = "bio"

  val host = "host"

  val description = "shortDescription"

  val shortDescription = "description"

  val startTime = "startTime"

  val endTime = "endTime"

  val mapUrl = "mapUrl"

  val normalSite = "http://gotocon.com/scaladays-sanfran-2015"

  val registrationSite = "https://secure.trifork.com/scaladays-sanfran-2015/registration/"

  val utcTimezoneOffset = "America/Los_Angeles"

  val utcTimezoneOffsetMillis = -25200000

  val typeInt = 1

  val aabstract = "aabstract"

}

object TestConfig extends TestConfig
