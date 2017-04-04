package com.fortysevendeg.android.scaladays.services

import android.app.Service
import android.os.Bundle
import com.fortysevendeg.android.scaladays.modules.ComponentRegistryImpl
import com.fortysevendeg.android.scaladays.modules.net.NetRequest
import com.localytics.android.GcmListenerService
import macroid.{Contexts, ServiceContextWrapper}
import play.api.libs.json.{Json, Reads}

import scala.util.{Failure, Success, Try}

class PushReceiverService
    extends GcmListenerService
    with Contexts[Service]
    with ComponentRegistryImpl {

  override lazy val contextProvider: ServiceContextWrapper = serviceContextWrapper

  case class PushMessage(jsonReload: Option[Boolean])

  val pushMessageReads: Reads[PushMessage] = Json.reads[PushMessage]

  override def onMessageReceived(from: String, data: Bundle): Unit = {

    def parseMessage(msg: String): Option[PushMessage] =
      Try(Json.parse(msg).as[PushMessage](pushMessageReads)) match {
        case Success(s) => Some(s)
        case Failure(e) => None
      }

    for {
      bundle <- Option(data)
      message <- Option(bundle.getString("message"))
      msg <- parseMessage(message)
    } yield {
      if (msg.jsonReload.contains(true)) {
        netServices.saveJsonInLocal(NetRequest(true))
      }
    }

  }
}
