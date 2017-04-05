/*
 *  Copyright (C) 2015 47 Degrees, LLC http://47deg.com hello@47deg.com
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

import sbt._

object Libraries {

  def onCompile(dep: ModuleID): ModuleID = dep % "compile"
  def onTest(dep: ModuleID): ModuleID = dep % "test"

  def androidDep(module: String, version: String = Versions.androidV): ModuleID =
    "com.android.support" % module % version
  def playServicesDep(module: String): ModuleID =
    "com.google.android.gms" % module % Versions.playServicesV
  def macroid(module: String = ""): ModuleID =
    "org.macroid" %% s"macroid${if (!module.isEmpty) s"-$module" else ""}" % Versions.macroidV

  lazy val androidSupportv4 = androidDep("support-v4")
  lazy val androidAppCompat = androidDep("appcompat-v7")
  lazy val androidRecyclerview = androidDep("recyclerview-v7")
  lazy val androidCardView = androidDep("cardview-v7")
  lazy val multiDexLib = androidDep("multidex", Versions.multiDexV)

  lazy val macroidRoot = macroid()
  lazy val macroidExtras = macroid("extras")

  lazy val okHttp = "com.squareup.okhttp3" % "okhttp" % Versions.okHttpV
  lazy val picasso = "com.squareup.picasso" % "picasso" % Versions.picassoV
  lazy val playJson = "com.typesafe.play" %% "play-json" % Versions.playJsonV
  lazy val prettytime = "org.ocpsoft.prettytime" % "prettytime" % Versions.prettytimeV
  lazy val twitter4j = "org.twitter4j" % "twitter4j-core" % Versions.twitter4jV
  lazy val zxingAndroid = "com.embarkmobile" % "zxing-android-minimal" % Versions.zxingAndroidV
  lazy val zxingCore = "com.google.zxing" % "core" % Versions.zxingCoreV

  lazy val playServicesAds = playServicesDep("play-services-ads")
  lazy val playServicesAnalytics = playServicesDep("play-services-analytics")
  lazy val playServicesBase = playServicesDep("play-services-base")
  lazy val playServicesGcm = playServicesDep("play-services-gcm")
  lazy val playServicesMaps = playServicesDep("play-services-maps")

  lazy val crashlytics = "com.crashlytics.sdk.android" % "crashlytics" % Versions.crashlyticsV
  lazy val localytics = "com.localytics.android" % "library" % Versions.localyticsV

  lazy val specs2 = "org.specs2" %% "specs2-core" % Versions.specs2V % "test"
  lazy val androidTest = "com.google.android" % "android" % "4.1.1.4" % "test"
  lazy val mockito = "org.specs2" % "specs2-mock_2.11" % Versions.mockitoV % "test"
}
