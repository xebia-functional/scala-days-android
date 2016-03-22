import Libraries.android._
import Libraries.macroid._
import Libraries.json._
import Libraries.test._
import Libraries.graphics._
import Libraries.social._
import Libraries.date._
import Libraries.qr._
import Libraries.playServices._
import Libraries.debug._
import Crashlytics._
import ReplacePropertiesGenerator._
import android.Keys._
import android.PromptPasswordsSigningConfig

android.Plugin.androidBuild

platformTarget in Android := Versions.androidPlatformV

name := """scala-days-android"""

organization := "com.fortysevendeg"

organizationName := "47 Degrees"

organizationHomepage := Some(new URL("http://47deg.com"))

version := Versions.appV

scalaVersion := Versions.scalaV

unmanagedBase := baseDirectory.value / "src" / "main" / "libs"

scalacOptions ++= Seq("-feature", "-deprecation")

resolvers ++= Settings.resolvers

libraryDependencies ++= Seq(
  aar(macroidRoot),
  aar(androidAppCompat),
  aar(androidCardView),
  aar(androidRecyclerview),
  aar(macroidExtras),
  aar(playServicesBase),
  aar(playServicesMaps),
  crashlytics,
  playJson,
  specs2,
  mockito,
  androidTest,
  picasso,
  twitter4j,
  prettytime,
  zxingCore,
  aar(zxingAndroid))

javacOptions in Compile ++= Seq("-target", "1.7", "-source", "1.7")

proguardCache in Android := Seq.empty

dexMaxHeap in Android := "2048m"

run <<= (run in Android).dependsOn(setDebugTask(true))

apkSigningConfig in Android := Option(
  PromptPasswordsSigningConfig(
    keystore = new File(Path.userHome.absolutePath + "/.android/signed.keystore"),
    alias = "47deg"))

packageRelease <<= (packageRelease in Android).dependsOn(setDebugTask(false))

proguardScala in Android := true

useProguard in Android := true

proguardOptions in Android ++= Settings.proguardCommons

packagingOptions in Android := PackagingOptions(excludes = Seq(
  "META-INF/LICENSE",
  "META-INF/LICENSE.txt",
  "META-INF/NOTICE",
  "META-INF/NOTICE.txt"))

packageResources in Android <<= (packageResources in Android).dependsOn(replaceValuesTask)

collectResources in Android <<= (collectResources in Android) dependsOn
  fixNameSpace dependsOn
  crashlyticsPreBuild dependsOn
  createFiles

zipalign in Android <<= (zipalign in Android) map { result =>
  crashlyticsPostPackage
  result
}
