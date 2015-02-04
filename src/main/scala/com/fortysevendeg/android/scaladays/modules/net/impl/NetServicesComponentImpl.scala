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
import com.fortysevendeg.android.scaladays.commons.StringRes
import com.fortysevendeg.android.scaladays.modules.net.{NetResponse, NetRequest, NetServices, NetServicesComponent}
import com.fortysevendeg.android.scaladays.scaladays.Service
import com.fortysevendeg.android.scaladays.utils.{FileUtils, NetUtils}
import com.fortysevendeg.macroid.extras.AppContextProvider

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success, Try}

trait NetServicesComponentImpl
    extends NetServicesComponent
    with FileUtils {

  self: AppContextProvider =>

  val netServices = new NetServicesImpl

  class NetServicesImpl
      extends NetServices
      with NetUtils {

    override def saveJsonInLocal: Service[NetRequest, NetResponse] = request =>
      Future {
        val file = new File(appContextProvider.get.getFilesDir, StringRes.jsonFilename)
        if (request.forceDownload || !file.exists()) {
          getJson(appContextProvider.get.getString(R.string.url_json_conference)) map {
            json =>
              if (file.exists()) file.delete()
              Try {
                writeText(file, json)
              } match {
                case Success(response) => Some(response)
                case Failure(ex) => None
              }
          } match {
            case None => NetResponse(false)
            case _ => NetResponse(true)
          }
        } else {
          NetResponse(true)
        }
      }

  }

}
