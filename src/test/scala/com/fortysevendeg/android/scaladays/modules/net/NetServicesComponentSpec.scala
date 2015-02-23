package com.fortysevendeg.android.scaladays.modules.net

import java.io.File

import com.fortysevendeg.android.scaladays.modules.net.impl.NetServicesComponentImpl
import com.fortysevendeg.android.scaladays.utils.AsyncUtils._
import com.fortysevendeg.android.scaladays.{AppContextTestSupport, BaseTestSupport}
import macroid.AppContext
import org.specs2.mutable.Specification

import scala.util.{Failure, Success, Try}

trait NetServicesComponentSupport
  extends NetServicesComponentImpl
  with AppContextTestSupport {

  override def loadJsonFileName: String = "jsonFile"
  
  val fileExists = true

  override def loadJsonFile(appContext: AppContext): File = {
    val mockFile = mock[File]
    mockFile.exists() returns fileExists
    mockFile
  }
  
}

class NetServicesComponentSpec
    extends Specification
    with BaseTestSupport {
  
  val jsonFileContent = """{"message": "Hello World!"}"""

  "NetServices component" should {
    
    "return a positive response indicating file not downloaded when JSON file available and force download flag set to false" in
      new NetServicesComponentSupport {

        override val fileExists = true
        
        netServices.saveJsonInLocal(new NetRequest(false)) *=== NetResponse(success = true, downloaded = false)
    }

    "return a negative response when getting an error downloading json and force download flag set to false" in
      new NetServicesComponentSupport {

        override val fileExists = false
        
        override def getJson(url: String): Try[String] = Failure[String](new RuntimeException)

        netServices.saveJsonInLocal(new NetRequest(false)) *=== NetResponse(success = false, downloaded = false)
    }

    "return a negative response when getting an error downloading json and force download flag set to true" in
      new NetServicesComponentSupport {

        override val fileExists = false

        override def getJson(url: String): Try[String] = Failure[String](new RuntimeException)

        netServices.saveJsonInLocal(new NetRequest(true)) *=== NetResponse(success = false, downloaded = false)
    }

    "return a positive response indicating file downloaded when JSON file unavailable and force download flag set to false" in
      new NetServicesComponentSupport {

        override val fileExists = false

        override def getJson(url: String): Try[String] = Success[String](jsonFileContent)

        override def writeText(file: File, text: String): Try[Unit] = Success[Unit](None)

        netServices.saveJsonInLocal(new NetRequest(false)) *=== NetResponse(success = true, downloaded = true)
      }

    "return a positive response indicating file downloaded when JSON file unavailable and force download flag set to true" in
      new NetServicesComponentSupport {

        override val fileExists = false

        override def getJson(url: String): Try[String] = Success[String](jsonFileContent)

        override def writeText(file: File, text: String): Try[Unit] = Success[Unit](None)

        netServices.saveJsonInLocal(new NetRequest(true)) *=== NetResponse(success = true, downloaded = true)
      }

    "return a positive response indicating file downloaded when JSON file available and force download flag set to true" in
      new NetServicesComponentSupport {

        override def loadJsonFile(appContext: AppContext): File = {
          val mockFile = mock[File]
          mockFile.exists() returns true
          mockFile.delete() returns true
          mockFile
        }

        override def getJson(url: String): Try[String] = Success[String](jsonFileContent)

        override def writeText(file: File, text: String): Try[Unit] = Success[Unit](None)

        netServices.saveJsonInLocal(new NetRequest(true)) *=== NetResponse(success = true, downloaded = true)
      }
    
  }

}