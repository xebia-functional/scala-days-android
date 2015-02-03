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

package com.fortysevendeg.android.scaladays.utils

import java.io._

import com.fortysevendeg.android.scaladays.utils.ResourceUtils._

import scala.util.Try

trait FileUtils {

  def getJson(file: File): Try[String] =
    Try {
      withResource[FileInputStream, String](new FileInputStream(file)) {
        file => scala.io.Source.fromInputStream(file, "UTF-8").mkString
      }
    }

  def writeText(file: File, text: String): Try[Unit] =
    Try {
      withResource[FileOutputStream, Unit](new FileOutputStream(file)) {
        outputStream =>
          val outputWriter = new OutputStreamWriter(outputStream)
          val bufferedWriter = new BufferedWriter(outputWriter, 16384)
          bufferedWriter.write(text)
          bufferedWriter.newLine()
          bufferedWriter.close()
          outputWriter.close()
      }
    }

}
