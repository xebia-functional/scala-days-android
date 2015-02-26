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

package com.fortysevendeg.android.scaladays.ui.commons

import com.fortysevendeg.android.scaladays.model.Conference
import com.fortysevendeg.android.scaladays.modules.json.{JsonRequest, JsonServicesComponent}
import com.fortysevendeg.android.scaladays.modules.net.{NetRequest, NetServicesComponent}
import com.fortysevendeg.android.scaladays.modules.preferences.{PreferenceRequest, PreferenceServicesComponent}
import com.fortysevendeg.macroid.extras.AppContextProvider

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait UiServices {
  
  self : PreferenceServicesComponent 
    with JsonServicesComponent 
    with NetServicesComponent 
    with AppContextProvider =>

  def getNamePreferenceFavorite(eventId: Int) = "%d_%d".format(loadSelectedConferenceId, eventId)
  
  def loadConferences(forceDownload: Boolean = false): Future[Seq[Conference]] = {
    for {
      _ <- netServices.saveJsonInLocal(NetRequest(forceDownload))
      jsonResponse <- jsonServices.loadJson(JsonRequest())
      root <- Future.successful(jsonResponse.apiResponse.getOrElse(throw InvalidJsonException()))
    } yield {
      root.conferences
    }
  }

  def loadSelectedConference(forceDownload: Boolean = false): Future[Conference] = {
    val conferenceId = loadSelectedConferenceId
    for {
      _ <- netServices.saveJsonInLocal(NetRequest(forceDownload))
      jsonResponse <- jsonServices.loadJson(JsonRequest())
      root <- Future.successful(jsonResponse.apiResponse.getOrElse(throw InvalidJsonException()))
      conference <- findConference(root.conferences, conferenceId)
    } yield conference
  }
  
  def findConference(conferences: Seq[Conference], conferenceId: Int): Future[Conference] = {
    val conference = conferences.find(conferenceId == 0 || _.info.id == conferenceId)
    (conferences, conference) match {
      case (Nil, None) => throw ConferenceSequenceEmptyException()
      case (_, Some(conf)) => Future.successful(conf)
      case (_, None) => Future.successful(conferences(0))
    }
  }
  
  val conferenceIdPreference = "conferenceId"
  
  def loadSelectedConferenceId: Int =
    preferenceServices.fetchIntPreference(PreferenceRequest[Int](conferenceIdPreference, 0)).value
  
  def saveSelectedConferenceId(conferenceId: Int): Unit =
    preferenceServices.saveIntPreference(PreferenceRequest[Int](conferenceIdPreference, conferenceId))

}
