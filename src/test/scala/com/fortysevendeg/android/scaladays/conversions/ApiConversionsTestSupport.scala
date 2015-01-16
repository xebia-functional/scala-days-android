package com.fortysevendeg.android.scaladays.conversions

import com.fortysevendeg.android.scaladays.model.app._
import org.specs2.execute.{Result, AsResult}
import org.specs2.mutable.Around
import org.specs2.specification.Scope

trait BaseTestSupport extends Around with Scope {

  override def around[T: AsResult](t: => T): Result = AsResult.effectively(t)

}

trait ApiConversionsTestSupport
  extends BaseTestSupport
  with TestConfig {

  val slot = Slot(slotId, startTime, endTime)

  val track = Track(trackId, name, host, shortDescription, description)

  val location = Location(locationId, name, mapUrl)

  val speaker = Speaker(speakerId, name, title, company, Some(twitter), Some(picture), bio)

  val information = Information(informationId, name, longName, nameAndLocation, firstDay, lastDay, normalSite, registrationSite, utcTimezoneOffset, utcTimezoneOffsetMillis)

  val simpleEvent = Event(eventId, name, aabstract, EventType(), Some(slot), None, None, Nil)

  val event = Event(eventId, name, aabstract, EventType(), Some(slot), Some(track), Some(location), Seq(speaker))

}
