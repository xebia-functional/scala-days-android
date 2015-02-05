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

import com.fortysevendeg.android.scaladays.BaseTestSupport
import com.fortysevendeg.android.scaladays.modules.json.models._



trait JsonModelTestSupportTestSupport
  extends BaseTestSupport

object JsonImplicits {
  import play.api.libs.json._

  implicit val pictureReads = Json.reads[ApiPicture]
  implicit val conferenceReads = Json.reads[ApiInformation]
  implicit val sponsorReads = Json.reads[ApiSponsor]
  implicit val sponsorTypeReads = Json.reads[ApiSponsorType]
  implicit val locationReads = Json.reads[ApiLocation]
  implicit val speakerReads = Json.reads[ApiSpeaker]
  implicit val trackReads = Json.reads[ApiTrack]
  implicit val eventReads = Json.reads[ApiEvent]
  implicit val venuesReads = Json.reads[ApiVenues]
  implicit val responseReads = Json.reads[ApiConference]
  implicit val rootReads = Json.reads[ApiRoot]

}
