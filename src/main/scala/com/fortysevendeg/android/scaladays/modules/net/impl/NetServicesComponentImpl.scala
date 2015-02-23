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
    with FileUtils
    with NetUtils {

  self: AppContextProvider =>

  val netServices = new NetServicesImpl
  
  def loadJsonFileName: String =
    appContextProvider.get.getString(R.string.url_json_conference)

  def loadJsonFile: File =
    new File(appContextProvider.get.getFilesDir, StringRes.jsonFilename)

  class NetServicesImpl
      extends NetServices {

    override def saveJsonInLocal: Service[NetRequest, NetResponse] = request =>
      Future {
        val file = loadJsonFile
        if (request.forceDownload || !file.exists()) {
          val result = getJson(loadJsonFileName) map (writeJsonFile(file, _))
          result match {
            case Some(true) => NetResponse(success = true, downloaded = true)
            case _ => NetResponse(success = false, downloaded = false)
          }
        } else {
          NetResponse(success = true, downloaded = false)
        }
      }
    
    def writeJsonFile(file: File, jsonContent: String): Boolean = {
      if (file.exists()) file.delete()
      Try {
        writeText(file, jsonContent)
      } match {
        case Success(response) => true
        case Failure(ex) => false
      }
    }

  }

}
