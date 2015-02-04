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

import com.fortysevendeg.android.scaladays.modules.json.models._

trait TestConfig {
  
  val info = ApiInformation(
    nameAndLocation = "Scala Days San Francisco, March 16-18, San Francisco, California",
    id = 111,
    firstDay = "2015-03-16",
    lastDay = "2015-03-20",
    utcTimezoneOffset = "America/Los_Angeles",
    name = "scaladays-sanfran-2015",
    longName = "Scala Days San Francisco",
    registrationSite = "https://secure.trifork.com/scaladays-sanfran-2015/registration/",
    normalSite = "http://gotocon.com/scaladays-sanfran-2015",
    utcTimezoneOffsetMillis = -25200000,
    pictures = Seq.empty)
  
  val speaker1 = ApiSpeaker(id = 1111,
    picture = Some("http://event.scaladays.org/dl/photos/Scala%20Days%202015%20speakers/speaker1_square.png"),
    name = "Speaker 1",
    title = "",
    company = "Company of speaker 1",
    twitter = Some("@speaker1"),
    bio = "Speaker 1 biography\nhttp://event.scaladays.org")
  
  val speaker2 = ApiSpeaker(id = 2222,
    picture = Option("http://event.scaladays.org/dl/photos/Scala%20Days%202015%20speakers/speaker2_square.png"),
    name = "Speaker 2",
    title = "",
    company = "Company of speaker 2",
    twitter = Option("@speaker2"),
    bio = "Speaker 2 biography\nhttp://event.scaladays.org")
  
  val event1 = ApiEvent(
    id = 6520,
    title = "Registration Open",
    description = "",
    `type` = 3,
    startTime = "2015-03-16T23:00:00Z",
    endTime = "2015-03-16T23:00:00Z",
    date = "MONDAY MARCH 16",
    track = None,
    location = None,
    speakers = None)
  
  val event2 = ApiEvent(id = 6524,
    title = "Keynote: Scala - where it came from, where it's going",
    description = "",
    `type` = 2,
    startTime = "2015-03-17T00:00:00Z",
    endTime = "2015-03-17T00:00:00Z",
    date = "MONDAY MARCH 16",
    track = Some(ApiTrack(id = 1051,
      host = "",
      description = "",
      name = "Keynote",
      shortdescription = "")),
    location = Some(ApiLocation(id = 589,
      name = "Herbst Pavilion",
      mapUrl = "")),
    speakers = Some(Seq(speaker1)))
  
  val event3 = ApiEvent(id = 6525,
    title = "Keynote Tuesday",
    description = "",
    `type` = 2,
    startTime = "2015-03-17T16:00:00Z",
    endTime = "2015-03-17T16:00:00Z",
    date = "TUESDAY MARCH 17",
    track = None,
    location = Some(ApiLocation(id =  589,
      name =  "Herbst Pavilion",
      mapUrl =  ""
    )),
    speakers = Some(Seq(speaker2)))
  
  val events = Seq(event1, event2, event3)
  
  val speakers = Seq(speaker1, speaker2)
  
  val sponsors = Seq(
    ApiSponsorType(`type` = "Hosted by", 
    Seq(
      ApiSponsor(url = "http://www.scala-days-sponsor1.com",
        logo = "http://event.scaladays.org/dl/photos/sponsors/sponsor1.png"),
      ApiSponsor(url = "http://www.scala-days-sponsor2.com",
        logo = "http://event.scaladays.org/dl/photos/sponsors/sponsor2.png"))),
    ApiSponsorType(`type` = "Platinum", 
    Seq(
      ApiSponsor(url = "http://www.scala-days-sponsor3.com",
        logo = "http://event.scaladays.org/dl/photos/sponsors/sponsor3.png"))),
    ApiSponsorType(`type` = "Gold", 
    Nil),
    ApiSponsorType(`type` = "Silver",
      Seq(
        ApiSponsor(url = "http://www.scala-days-sponsor4.com",
          logo = "http://event.scaladays.org/dl/photos/sponsors/sponsor4.png"),
        ApiSponsor(url = "http://www.scala-days-sponsor5.com",
          logo = "http://event.scaladays.org/dl/photos/sponsors/sponsor5.png"))))

  val conference = ApiConference(
    info = info,
    schedule = events,
    sponsors = sponsors,
    speakers = speakers,
    venues = Seq.empty,
    codeOfConduct = None)

  val root = ApiRoot(Seq(conference))

}

object TestConfig extends TestConfig
