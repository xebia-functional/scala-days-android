package com.fortysevendeg.android.scaladays.ui.commons

import com.fortysevendeg.android.scaladays.model.{Conference, Root}
import com.fortysevendeg.android.scaladays.modules.json.{JsonRequest, JsonServicesComponent}
import com.fortysevendeg.android.scaladays.modules.net.{NetRequest, NetServicesComponent}
import com.fortysevendeg.android.scaladays.modules.preferences.{PreferenceRequest, PreferenceServicesComponent}
import com.fortysevendeg.macroid.extras.AppContextProvider
import scala.concurrent.ExecutionContext.Implicits.global
import macroid.Logging._

import scala.concurrent.Future

trait UiServices {
  
  self : PreferenceServicesComponent with JsonServicesComponent with NetServicesComponent with AppContextProvider =>
  
  val errorLogMessage = "Error loading conferences"
  
  val forceDownload = false
  
  def loadConferences(): Future[Seq[Conference]] = {
    for {
      _ <- netServices.saveJsonInLocal(NetRequest(forceDownload))
      jsonResponse <- jsonServices.loadJson(JsonRequest())
      root <- Future.successful(jsonResponse.apiResponse.getOrElse(throw InvalidJsonException()))
    } yield {
      root.conferences
    }
  }

  def loadSelectedConference(): Future[Conference] = {
    val conferenceId = loadSelectedConferenceId
    for {
      _ <- netServices.saveJsonInLocal(NetRequest(forceDownload))
      jsonResponse <- jsonServices.loadJson(JsonRequest())
      root <- Future.successful(jsonResponse.apiResponse.getOrElse(throw InvalidJsonException()))
      conference <- findConference(root.conferences, conferenceId)
    } yield {
      conference
    }
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
