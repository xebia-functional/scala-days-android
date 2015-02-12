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

package com.fortysevendeg.android.scaladays.modules.json.models

import com.fortysevendeg.android.scaladays.model._
import com.fortysevendeg.android.scaladays.utils.DateTimeUtils
import com.fortysevendeg.android.scaladays.utils.DateTimeUtils._

trait ApiConversions {

  private def parseDate(date: String) =
    DateTimeUtils.parseDate(date, DateTimeUtils.ISODateFormatterDay)

  private def parseDateTime(date: String) =
    DateTimeUtils.parseDate(date, DateTimeUtils.ISODateFormatterDateTime)

  def toRoot(apiRoot: ApiRoot): Root =
    Root(apiRoot.conferences map toConference)
  
  def toConference(apiConference: ApiConference): Conference =
    Conference(
      toInformation(apiConference.info),
      apiConference.schedule map toEvent,
      apiConference.sponsors map toSponsorType,
      apiConference.speakers map toSpeaker)

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
      utcTimezoneOffsetMillis = apiConferenceInfo.utcTimezoneOffsetMillis,
      hashTag = apiConferenceInfo.hashtag,
      apiConferenceInfo.pictures map toPicture)
  
  def toPicture(apiPicture: ApiPicture): Picture =
    Picture(
      width = apiPicture.width,
      height = apiPicture.height,
      url = apiPicture.url)

  def toEvent(apiEvent: ApiEvent): Event =
    Event(
      id = apiEvent.id,
      title = apiEvent.title,
      description = apiEvent.description,
      eventType = apiEvent.`type`,
      startTime = parseDateTime(apiEvent.startTime),
      endTime = parseDateTime(apiEvent.endTime),
      date = apiEvent.date,
      track = apiEvent.track map toTrack,
      location = apiEvent.location map toLocation,
      speakers = apiEvent.speakers getOrElse Nil map toSpeaker)

  def toSponsorType(apiSponsorType: ApiSponsorType): SponsorType =
    SponsorType(
      name = apiSponsorType.`type`,
      sponsors = apiSponsorType.items map toSponsor)

  def toSponsor(apiSponsor: ApiSponsor): Sponsor =
    Sponsor(
      logo = apiSponsor.logo,
      url = apiSponsor.url)

  def toSpeaker(apiSpeaker: ApiSpeaker): Speaker =
    Speaker(
      id = apiSpeaker.id,
      name = apiSpeaker.name,
      title = apiSpeaker.title,
      company = apiSpeaker.company,
      twitter = apiSpeaker.twitter,
      picture = apiSpeaker.picture,
      bio = apiSpeaker.bio)

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
}
