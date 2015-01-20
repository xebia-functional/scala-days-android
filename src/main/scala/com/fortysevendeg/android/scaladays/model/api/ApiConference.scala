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

package com.fortysevendeg.android.scaladays.model.api

case class ApiConference(
  info: ApiInformation,
  schedule: Seq[ApiEvent],
  sponsors: Seq[ApiSponsorType],
  speakers: Seq[ApiSpeaker])

case class ApiInformation(
  id: Int,
  name: String,
  longName: String,
  nameAndLocation: String,
  firstDay: String,
  lastDay: String,
  normalSite: String,
  registrationSite: String,
  utcTimezoneOffset: String,
  utcTimezoneOffsetMillis: Long)

case class ApiEvent(
  id: Int,
  title: String,
  description: String,
  `type`: Int,
  startTime: String,
  endTime: String,
  date: String,
  track: Option[ApiTrack],
  location: Option[ApiLocation],
  speakers: Option[Seq[ApiSpeaker]])

case class ApiTrack(
  id: Int,
  name: String,
  host: String,
  shortdescription: String,
  description: String)

case class ApiLocation(
  id: Int,
  name: String,
  mapUrl: String)

case class ApiSponsorType(
  `type`: String,
  items: Seq[ApiSponsor])

case class ApiSponsor(
  logo: String,
  url: String)

case class ApiSpeaker(
  id: Int,
  name: String,
  title: String,
  company: String,
  twitter: Option[String],
  picture: Option[String],
  bio: String)