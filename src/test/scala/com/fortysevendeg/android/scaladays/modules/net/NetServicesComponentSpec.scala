package com.fortysevendeg.android.scaladays.modules.net

import java.io.File

import com.fortysevendeg.android.scaladays.modules.net.impl.NetServicesComponentImpl
import com.fortysevendeg.android.scaladays.utils.AsyncUtils._
import com.fortysevendeg.android.scaladays.{AppContextTestSupport, BaseTestSupport}
import org.specs2.mutable.Specification

import scala.util.Try

trait NetServicesComponentSupport
  extends NetServicesComponentImpl
  with AppContextTestSupport

class NetServicesComponentSpec
    extends Specification
    with BaseTestSupport {
  
  val jsonFileName = "jsonFile"
  val jsonFileContent = """{"message": "Hello World!"}"""

  "NetServices component" should {
    
    "return a positive response indicating file not downloaded when JSON file available and force download flag set to false" in
      new NetServicesComponentSupport {

        override def loadJsonFile: File = {
          val mockFile = mock[File]
          mockFile.exists() returns true
          mockFile
        }
        
        netServices.saveJsonInLocal(new NetRequest(false)) *=== NetResponse(success = true, downloaded = false)
    }

    "return a negative response when getting an error downloading json and force download flag set to false" in
      new NetServicesComponentSupport {

        override def loadJsonFileName: String = jsonFileName

        override def loadJsonFile: File = {
          val mockFile = mock[File]
          mockFile.exists() returns false
          mockFile
        }
        
        override def getJson(url: String): Option[String] = None

        netServices.saveJsonInLocal(new NetRequest(false)) *=== NetResponse(success = false, downloaded = false)
    }

    "return a negative response when getting an error downloading json and force download flag set to true" in
      new NetServicesComponentSupport {

        override def loadJsonFileName: String = jsonFileName

        override def loadJsonFile: File = {
          val mockFile = mock[File]
          mockFile.exists() returns false
          mockFile
        }

        override def getJson(url: String): Option[String] = None

        netServices.saveJsonInLocal(new NetRequest(true)) *=== NetResponse(success = false, downloaded = false)
    }

    "return a positive response indicating file downloaded when JSON file unavailable and force download flag set to false" in
      new NetServicesComponentSupport {

        override def loadJsonFileName: String = jsonFileName

        override def loadJsonFile: File = {
          val mockFile = mock[File]
          mockFile.exists() returns false
          mockFile
        }

        override def getJson(url: String): Option[String] = Some(jsonFileContent)

        override def writeText(file: File, text: String): Try[Unit] = Try[Unit](None)

        netServices.saveJsonInLocal(new NetRequest(false)) *=== NetResponse(success = true, downloaded = true)
      }

    "return a positive response indicating file downloaded when JSON file unavailable and force download flag set to true" in
      new NetServicesComponentSupport {

        override def loadJsonFileName: String = jsonFileName

        override def loadJsonFile: File = {
          val mockFile = mock[File]
          mockFile.exists() returns false
          mockFile
        }

        override def getJson(url: String): Option[String] = Some(jsonFileContent)

        override def writeText(file: File, text: String): Try[Unit] = Try[Unit](None)

        netServices.saveJsonInLocal(new NetRequest(true)) *=== NetResponse(success = true, downloaded = true)
      }

    "return a positive response indicating file downloaded when JSON file available and force download flag set to true" in
      new NetServicesComponentSupport {

        override def loadJsonFileName: String = jsonFileName

        override def loadJsonFile: File = {
          val mockFile = mock[File]
          mockFile.exists() returns true
          mockFile.delete() returns true
          mockFile
        }

        override def getJson(url: String): Option[String] = Some(jsonFileContent)

        override def writeText(file: File, text: String): Try[Unit] = Try[Unit](None)

        netServices.saveJsonInLocal(new NetRequest(true)) *=== NetResponse(success = true, downloaded = true)
      }
    
  }

}