/*
 * Copyright (C) 2015 47 Degrees, LLC http://47deg.com hello@47deg.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fortysevendeg.android.scaladays.conversions

import com.fortysevendeg.android.scaladays.model.api._
import com.fortysevendeg.android.scaladays.model.app._
import com.fortysevendeg.android.scaladays.utils.DateTimeUtils

trait ApiConversions {

  private def parseDate(date: String) =
    DateTimeUtils.parseDate(date, DateTimeUtils.ISODateFormatterDayPrecission)
  
  def toConference(apiConference: ApiConference): Conference = {
    implicit val speakerList = apiConference.speakerList map toSpeaker
    implicit val slotList = apiConference.slotList map toSlot
    implicit val trackList = apiConference.trackList map toTrack
    implicit val locationList = apiConference.locationList map toLocation
    Conference(
      toInformation(apiConference.conference),
      Nil,
      speakerList,
      apiConference.eventList flatMap toSeqEvent)
  
  }
  
  def toInformation(apiConferenceInfo: ApiInformation): Information =
    Information(
      id = apiConferenceInfo.id,
      name = apiConferenceInfo.name,
      longName = apiConferenceInfo.longName,
      nameAndLocation = apiConferenceInfo.nameAndLocation,
      firstDay = parseDate(apiConferenceInfo.firstDay),
      lastDay = parseDate(apiConferenceInfo.lastDay),
      normalSite = apiConferenceInfo.normalSite,
      registrationSite = apiConferenceInfo.registrationSite,
      utcTimezoneOffset = apiConferenceInfo.utcTimezoneOffset,
      utcTimezoneOffsetMillis = apiConferenceInfo.utcTimezoneOffsetMillis)
  
  def toSpeaker(apiSpeaker: ApiSpeaker): Speaker =
    Speaker(
      id = apiSpeaker.id,
      name = apiSpeaker.name,
      title = apiSpeaker.title,
      company = apiSpeaker.company,
      twitter = apiSpeaker.twitter,
      picture = apiSpeaker.picture,
      bio = apiSpeaker.bio)

  
  def toSlot(apiSlot: ApiSlot): Slot =
    Slot(
      id = apiSlot.id,
      startTime = apiSlot.startTime,
      endTime = apiSlot.endTime)

  def toTrack(apiTrack: ApiTrack): Track =
    Track(
      id = apiTrack.id,
      name = apiTrack.name,
      host = apiTrack.host,
      shortDescription = apiTrack.shortdescription,
      description = apiTrack.description)

  def toLocation(apiLocation: ApiLocation): Location =
    Location(
      id = apiLocation.id,
      name = apiLocation.name,
      mapUrl = apiLocation.mapUrl)
  
  def toSeqEvent(apiEvent: ApiEvent)(implicit slotList: Seq[Slot], trackList: Seq[Track], locationList: Seq[Location], speakerList: Seq[Speaker]): Seq[Event] = {
    apiEvent.slotIds map { slotId =>
      val speakerIds = apiEvent.speakerIds getOrElse Nil
      Event(
        id = apiEvent.id,
        title = apiEvent.title,
        description = apiEvent.aabstract,
        eventType = EventType(),
        slot = slotList.find(_.id == slotId),
        track = trackList.find(_.id == (apiEvent.trackId getOrElse None)),
        location = locationList.find(_.id == (apiEvent.locationId getOrElse None)),
        speakers = speakerList.filter(speaker => speakerIds.contains(speaker.id))
      )
    } filter(_.slot.nonEmpty)
  }
}
