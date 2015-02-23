import sbt._

object Settings {

  lazy val resolvers =
    Seq(
        Resolver.mavenLocal,
        DefaultMavenRepository,
        Resolver.typesafeRepo("releases"),
        Resolver.typesafeRepo("snapshots"),
        Resolver.typesafeIvyRepo("snapshots"),
        Resolver.sonatypeRepo("releases"),
        Resolver.sonatypeRepo("snapshots"),
        Resolver.defaultLocal,
        "jcenter" at "http://jcenter.bintray.com",
        "Scalaz Bintray Repo" at "http://dl.bintray.com/scalaz/releases",
        "47deg Public" at "http://clinker.47deg.com/nexus/content/groups/public",
        "zxing Android Minimal" at "https://raw.github.com/embarkmobile/zxing-android-minimal/mvn-repo/maven-repository/",
        "Crashlytics" at "http://download.crashlytics.com/maven"
       )

  lazy val proguardCommons = Seq(
    "-ignorewarnings",
    "-printmapping mapping.txt",
    "-keep class scala.Dynamic",
    "-keep class com.fortysevendeg.** { *; }",
    "-keep class twitter4j.** { *; }",
    "-keep class org.joda.** { *; }",
    "-keep class org.ocpsoft.prettytime.i18n.**",
    "-keep class macroid.** { *; }",
    "-keep class android.** { *; }",
    "-keep class com.google.** { *; }",
    "-keep class com.crashlytics.** { *; }")

}
