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

import java.io.{File, FileInputStream}
import java.util.Properties

import android.Keys._
import sbt._

import scala.annotation.tailrec
import scala.collection.JavaConverters._
import scala.collection.immutable.Iterable

object ReplacePropertiesGenerator {

  val debugProperties = "debug.properties"

  val defaultMap = Map(
    "google.map.key" -> "",
    "localytics.key" -> "",
    "google.project.number" -> "",
    "twitter.app.key" -> "",
    "twitter.app.secret" -> "",
    "twitter.app.callback.host" -> ""
  )

  lazy val propertiesMap: Map[String, String] = {
    (loadPropertiesFile map { file =>
      val properties = new Properties()
      properties.load(new FileInputStream(file))
      properties.asScala.toMap
    }) getOrElse defaultMap
  }

  private def namePropertyInConfig(name: String) = s"$${$name}"

  private def loadPropertiesFile: Option[File] = {
    val file = new File(debugProperties)
    if (file.exists()) Some(file) else None
  }

  def replaceContent(valuesFile: File) = {
    val content = IO.readLines(valuesFile) map replaceLine
    IO.write(valuesFile, content.mkString("\n"))
  }

  private def replaceLine(line: String) = {
    @tailrec
    def replace(properties: Map[String, String], line: String): String = {
      if (properties.isEmpty) {
        line
      } else {
        val (key, value) = properties.head
        val name = namePropertyInConfig(key)
        replace(properties.tail, if (line.contains(name)) line.replace(name, value) else line)
      }
    }
    replace(propertiesMap, line)
  }

  def replaceValuesTask() = Def.task[Seq[File]] {
    def Try(command: String) = try {
      command.!!
    } catch {
      case e: Exception => command + " failed: " + e.getMessage
    }
    try {
      val dir: File = (binPath in Android).value
      propertiesMap
      val valuesFile: File =  new File(dir, "resources/res/values/values.xml")
      replaceContent(valuesFile)
      Seq(valuesFile)
    } catch {
      case e: Throwable =>
        println("An error occurred loading values.xml")
        throw e
    }
  }

}
