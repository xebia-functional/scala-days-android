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

package com.fortysevendeg.android.scaladays.modules.json.impl

import com.fortysevendeg.android.scaladays.modules.json._
import com.fortysevendeg.android.scaladays.modules.json.models._
import com.fortysevendeg.android.scaladays.scaladays.Service
import com.fortysevendeg.android.scaladays.utils.FileUtils
import com.fortysevendeg.android.scaladays.commons.ContextWrapperProvider
import play.api.libs.json.Json

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

trait ApiReads {

  implicit val pictureReads = Json.reads[ApiPicture]
  implicit val conferenceReads = Json.reads[ApiInformation]
  implicit val trackReads = Json.reads[ApiTrack]
  implicit val locationReads = Json.reads[ApiLocation]
  implicit val speakerReads = Json.reads[ApiSpeaker]
  implicit val eventReads = Json.reads[ApiEvent]
  implicit val sponsorReads = Json.reads[ApiSponsor]
  implicit val sponsorTypeReads = Json.reads[ApiSponsorType]
  implicit val venuesReads = Json.reads[ApiVenue]
  implicit val responseReads = Json.reads[ApiConference]
  implicit val rootReads = Json.reads[ApiRoot]

}

trait ApiWrites {

  implicit val pictureWrites = Json.writes[ApiPicture]
  implicit val conferenceWrites = Json.writes[ApiInformation]
  implicit val trackWrites = Json.writes[ApiTrack]
  implicit val locationWrites = Json.writes[ApiLocation]
  implicit val speakerWrites = Json.writes[ApiSpeaker]
  implicit val eventWrites = Json.writes[ApiEvent]
  implicit val sponsorWrites = Json.writes[ApiSponsor]
  implicit val sponsorTypeWrites = Json.writes[ApiSponsorType]
  implicit val venuesWrites = Json.writes[ApiVenue]
  implicit val responseWrites = Json.writes[ApiConference]
  implicit val rootWrites = Json.writes[ApiRoot]

}

trait JsonServicesComponentImpl
    extends JsonServicesComponent
    with FileUtils {

  self: ContextWrapperProvider =>

  val jsonServices = new JsonServicesImpl

  class JsonServicesImpl
      extends JsonServices
      with ApiConversions
      with ApiReads {

    override def loadJson: Service[JsonRequest, JsonResponse] = request =>
      Future {
        (for {
          json <- getJson(loadJsonFile(contextProvider))
          apiRoot <- Try(Json.parse(json).as[ApiRoot])
        } yield apiRoot) match {
          case Success(apiRoot) => JsonResponse(Some(toRoot(apiRoot)))
          case Failure(ex) => JsonResponse(None)
        }
      }

  }

}
