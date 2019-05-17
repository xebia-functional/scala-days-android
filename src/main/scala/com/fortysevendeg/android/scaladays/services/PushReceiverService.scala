package com.fortysevendeg.android.scaladays.services

import android.app.{Notification, PendingIntent, Service}
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.{NotificationCompat, NotificationManagerCompat}
import com.fortysevendeg.android.scaladays.R
import com.fortysevendeg.android.scaladays.modules.ComponentRegistryImpl
import com.fortysevendeg.android.scaladays.modules.net.NetRequest
import com.fortysevendeg.android.scaladays.ui.main.MainActivity
import com.google.android.gms.gcm.GcmListenerService
import macroid.{Contexts, ServiceContextWrapper}
import play.api.libs.json.{Json, Reads}

import scala.util.{Failure, Success, Try}
import scala.concurrent.ExecutionContext.Implicits.global

class PushReceiverService
    extends GcmListenerService
    with Contexts[Service]
    with ComponentRegistryImpl {

  override lazy val contextProvider: ServiceContextWrapper =
    serviceContextWrapper

  case class PushAps(alert: Option[String])
  case class PushMessage(aps: Option[PushAps], jsonReload: Option[Boolean])

  implicit val pushAppsReads: Reads[PushAps] = Json.reads[PushAps]
  implicit val pushMessageReads: Reads[PushMessage] = Json.reads[PushMessage]

  override def onMessageReceived(from: String, data: Bundle): Unit = {

    def parseMessage(msg: String): Option[PushMessage] =
      Try(Json.parse(msg).as[PushMessage]) match {
        case Success(s) => Some(s)
        case Failure(_) => None
      }

    def createNotification(msg: String): Unit = {
      val ctx = contextProvider.bestAvailable
      val intent = new Intent(ctx, classOf[MainActivity])
      val pendingIntent = PendingIntent.getActivity(ctx, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

      val builder = new NotificationCompat.Builder(ctx)
        .setAutoCancel(true)
        .setDefaults(Notification.DEFAULT_ALL)
        .setSmallIcon(R.drawable.icon_default_notification)
        .setContentTitle(ctx.getString(R.string.app_name))
        .setContentText(msg)
        .setContentIntent(pendingIntent)

      NotificationManagerCompat.from(ctx).notify(1, builder.build())
    }

    def processMessage(msg: PushMessage): Unit = {
      netServices.saveJsonInLocal(NetRequest(msg.jsonReload.contains(true))) onComplete {
        case Success(_) =>
          msg.aps.foreach(_.alert.foreach(createNotification))
        case Failure(e) =>
          android.util.Log.e("ScalaDays", "Error saving JSON", e)
      }
    }

    for {
      bundle <- Option(data)
      message <- Option(bundle.getString("message"))
      msg <- parseMessage(message)
    } yield processMessage(msg)

  }
}
