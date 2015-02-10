package com.fortysevendeg.android.scaladays.ui.commons

import com.fortysevendeg.android.scaladays.model.Root
import com.fortysevendeg.android.scaladays.modules.json.{JsonRequest, JsonServicesComponent}
import com.fortysevendeg.android.scaladays.modules.net.{NetRequest, NetServicesComponent}
import com.fortysevendeg.macroid.extras.AppContextProvider
import scala.concurrent.ExecutionContext.Implicits.global
import macroid.Logging._

trait UiServices {
  
  self : NetServicesComponent with JsonServicesComponent with AppContextProvider =>
  
  val errorLogMessage = "Error loading conferences"
  
  val forceDownload = false
  
  def loadConferences[R](success: (Root) => R, failed: (Option[Throwable]) => Any = _ => logE"$errorLogMessage") = {
    val saveJsonOp = for {
      _ <- netServices.saveJsonInLocal(NetRequest(forceDownload))
      jsonResponse <- jsonServices.loadJson(JsonRequest())
    } yield {
      jsonResponse.apiResponse
    }

    saveJsonOp map {
      case Some(root) => success(root)
      case None => failed(None)
    } recover {
      case error => failed(Some(error))
    }
  }
  

}
