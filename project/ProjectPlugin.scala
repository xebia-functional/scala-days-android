import Crashlytics._
import android._
import android.Keys._
import Libraries._
import ReplacePropertiesGenerator.{replaceValuesTask, setDebugTask}
import sbt.Keys._
import sbt._

object ProjectPlugin extends AutoPlugin {

  override def trigger: PluginTrigger = allRequirements

  override def requires: Plugins = AndroidApp

  override def projectSettings: Seq[Def.Setting[_]] =
    Seq(
      name := "scala-days-android",
      organization := "com.fortysevendeg",
      organizationName := "47 Degrees",
      organizationHomepage := Some(new URL("http://47deg.com")),
      version := Versions.appV,
      javacOptions in Compile ++= Seq("-target", "1.7", "-source", "1.7"),
      scalaVersion := Versions.scalaV,
      scalacOptions ++= Seq("-feature", "-deprecation"),
      crashlyticsEnabled := (sys.env.getOrElse("CRASHLYTICS_ENABLED", default = "true") == "true")
    ) ++ dependenciesSettings ++ aliasSettings ++ androidSettings

  private[this] val dependenciesSettings = Seq(
    resolvers ++= Seq(
      "Maven Central Server" at "http://repo1.maven.org/maven2",
      DefaultMavenRepository,
      "jcenter" at "http://jcenter.bintray.com",
      "47 Degrees Bintray Repo" at "http://dl.bintray.com/47deg/maven",
      Resolver.typesafeRepo("releases"),
      Resolver.typesafeRepo("snapshots"),
      Resolver.typesafeIvyRepo("snapshots"),
      Resolver.sonatypeRepo("releases"),
      Resolver.sonatypeRepo("snapshots"),
      Resolver.defaultLocal,
      "Scalaz Bintray Repo" at "http://dl.bintray.com/scalaz/releases",
      "zxing Android Minimal" at "https://raw.github.com/embarkmobile/zxing-android-minimal/mvn-repo/maven-repository/",
      "crashlytics" at "https://maven.fabric.io/public",
      "localytics" at "http://maven.localytics.com/public"
    ),
    libraryDependencies ++= Seq(
      aar(macroidRoot),
      aar(macroidExtras),
      aar(androidSupportv4),
      aar(androidAppCompat),
      aar(androidCardView),
      aar(androidRecyclerview),
      aar(playServicesBase),
      aar(playServicesAds),
      aar(playServicesAnalytics),
      aar(playServicesGcm),
      aar(playServicesMaps),
      aar(zxingAndroid),
      okHttp,
      picasso,
      playJson,
      prettytime,
      twitter4j,
      zxingCore,
      crashlytics,
      localytics,
      androidTest,
      specs2,
      mockito
    )
  )

  private[this] val aliasSettings: Seq[Def.Setting[_]] = Seq(
    run <<= (run in Android).dependsOn(setDebugTask(true)),
    packageRelease <<= (packageRelease in Android).dependsOn(
      setDebugTask(false)),
    packageResources in Android <<= (packageResources in Android).dependsOn(replaceValuesTask),
    collectResources in Android <<= (collectResources in Android) dependsOn
      fixNameSpace dependsOn
      crashlyticsPreBuild dependsOn
      createFiles,
    zipalign in Android <<= (zipalign in Android) map { result =>
      crashlyticsPostPackage
      result
    },
    apkSigningConfig in Android := Option(
      PromptPasswordsSigningConfig(
        keystore =
          new File(Path.userHome.absolutePath + "/.android/signed.keystore"),
        alias = "47deg"))
  )

  private[this] val androidSettings: Seq[Def.Setting[_]] = Seq(
    platformTarget in Android := Versions.androidPlatformV,
    proguardCache in Android := Seq.empty,
    dexMaxHeap in Android := "2048m",
    proguardScala in Android := true,
    useProguard in Android := true,
    proguardOptions in Android ++= Seq(
      "-ignorewarnings",
      "-keep class scala.Dynamic",
      "-keep class twitter4j.** { *; }",
      "-keep class org.joda.** { *; }",
      "-keep class org.ocpsoft.prettytime.i18n.**",
      "-keep class macroid.** { *; }",
      "-keep class com.google.** { *; }",
      "-keep class com.localytics.android.** { *; }",
      "-keep class com.google.android.gms.ads.** { *; }",
      "-keep class * extends java.util.ListResourceBundle { protected Object[][] getContents(); }",
      "-keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable { public static final *** NULL; }",
      "-keepnames @com.google.android.gms.common.annotation.KeepName class *",
      "-keepclassmembernames class * { @com.google.android.gms.common.annotation.KeepName *; }",
      "-keepnames class * implements android.os.Parcelable { public static final ** CREATOR; }",
      "-keep class com.crashlytics.** { *; }",
      "-dontwarn com.crashlytics.**"
    ),
    packagingOptions in Android := PackagingOptions(
      excludes =
        Seq("META-INF/LICENSE",
          "META-INF/LICENSE.txt",
          "META-INF/NOTICE",
          "META-INF/NOTICE.txt",
          "META-INF/services/com.fasterxml.jackson.databind.Module"))
  )
}
