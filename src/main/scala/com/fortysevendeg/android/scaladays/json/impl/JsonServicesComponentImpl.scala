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

package com.fortysevendeg.android.scaladays.json.impl

import com.fortysevendeg.android.scaladays.json._
import com.fortysevendeg.android.scaladays.model.api._
import com.fortysevendeg.android.scaladays.scaladays.Service
import com.fortysevendeg.android.scaladays.utils.FileUtils
import com.fortysevendeg.macroid.extras.AppContextProvider
import play.api.libs.json.{JsValue, Json}
import scala.concurrent.Future
import scala.util.{Try, Success}

import scala.concurrent.ExecutionContext.Implicits.global

trait ApiReads {

  implicit val conferenceReads = Json.reads[ApiInformation]
  implicit val trackReads = Json.reads[ApiTrack]
  implicit val locationReads = Json.reads[ApiLocation]
  implicit val speakerReads = Json.reads[ApiSpeaker]
  implicit val eventReads = Json.reads[ApiEvent]
  implicit val sponsorReads = Json.reads[ApiSponsor]
  implicit val sponsorTypeReads = Json.reads[ApiSponsorType]
  implicit val responseReads = Json.reads[ApiConference]

}

trait JsonServicesComponentImpl
    extends JsonServicesComponent {

  self: AppContextProvider =>

  val jsonServices = new JsonServicesImpl

  class JsonServicesImpl
      extends JsonServices
      with ApiReads {
    
    override def loadJson: Service[JsonRequest, JsonResponse] = request =>
      Future {
        val fileContent: Try[String] = loadFileContent(request.jsonPath, request.fromCache)
        fileContent map { jsonSource =>
          val json: JsValue = Json.parse(jsonSource)
          json.as[ApiConference]
        } match {
          case Success(apiConference) => JsonResponse(Some(apiConference))
          case _ => JsonResponse(None)
        }
      }
    
      def loadFileContent(jsonPath: String, fromCache: Boolean): Try[String] = {
        if (fromCache) FileUtils.getJsonCache(jsonPath) else FileUtils.getJsonAssets(jsonPath)
      }
    }

}
