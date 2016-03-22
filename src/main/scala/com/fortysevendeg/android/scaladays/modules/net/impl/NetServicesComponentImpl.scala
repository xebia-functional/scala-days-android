/*
 * Copyright (C) 2015 47 Degrees, LLC http://47deg.com hello@47deg.com
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may
 *  not use this file except in compliance with the License. You may obtain
 *  a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.fortysevendeg.android.scaladays.modules.net.impl

import java.io.File

import com.fortysevendeg.android.scaladays.R
import com.fortysevendeg.android.scaladays.commons.ContextWrapperProvider
import com.fortysevendeg.android.scaladays.modules.net.client.ServiceClient
import com.fortysevendeg.android.scaladays.modules.net.client.http.OkHttpClient
import com.fortysevendeg.android.scaladays.modules.net.client.messages.ServiceClientStringRequest
import com.fortysevendeg.android.scaladays.modules.net.{NetRequest, NetResponse, NetServices, NetServicesComponent}
import com.fortysevendeg.android.scaladays.scaladays.Service
import com.fortysevendeg.android.scaladays.utils.FileUtils
import com.squareup.{okhttp => okHttp}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.control.NonFatal
import scala.util.{Failure, Success, Try}

trait NetServicesComponentImpl
    extends NetServicesComponent
    with FileUtils {

  self: ContextWrapperProvider =>

  val netServices = new NetServicesImpl

  val serviceClient = new ServiceClient(createHttpClient)
  
  def loadJsonFileName: String =
    contextProvider.application.getString(R.string.url_json_conference)

  class NetServicesImpl
      extends NetServices {

    override def saveJsonInLocal: Service[NetRequest, NetResponse] = request => {
      val file = loadJsonFile(contextProvider)
      if (request.forceDownload || !file.exists()) {
        val future = for {
          clientRequest <- serviceClient.getString(ServiceClientStringRequest(path = loadJsonFileName))
          result <- Future {
            clientRequest.data map (writeJsonFile(file, _)) match {
              case Some(true) => NetResponse(success = true, downloaded = true)
              case _ => NetResponse(success = false, downloaded = false)
            }
          }
        } yield result
        future.recoverWith {
          case NonFatal(e) =>
            // log
            Future.successful(NetResponse(success = false, downloaded = false))
        }
      } else {
        Future.successful(NetResponse(success = true, downloaded = false))
      }
    }

    private[this] def writeJsonFile(file: File, jsonContent: String): Boolean = {
      if (file.exists()) file.delete()
      Try {
        writeText(file, jsonContent)
      } match {
        case Success(response) => true
        case Failure(ex) => false
      }
    }

  }

  private[this] def createHttpClient = {
    val okHttpClient = new okHttp.OkHttpClient
    new OkHttpClient(okHttpClient)
  }

}
