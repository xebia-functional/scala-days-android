package com.fortysevendeg.android.scaladays.modules.net

import java.io.File

import com.fortysevendeg.android.scaladays.modules.net.client.ServiceClient
import com.fortysevendeg.android.scaladays.modules.net.client.messages.{ServiceClientStringRequest, ServiceClientStringResponse}
import com.fortysevendeg.android.scaladays.modules.net.impl.NetServicesComponentImpl
import com.fortysevendeg.android.scaladays.utils.AsyncUtils._
import com.fortysevendeg.android.scaladays.{BaseTestSupport, ContextWrapperTestSupport}
import macroid.ContextWrapper
import org.specs2.mutable.Specification

import scala.concurrent.Future
import scala.util.{Success, Try}

import scala.concurrent.ExecutionContext.Implicits.global

trait NetServicesComponentSupport
  extends NetServicesComponentImpl
  with ContextWrapperTestSupport {

  override def loadJsonFileName: String = "jsonFile"

  override val serviceClient = mock[ServiceClient]

  val fileExists = true

  override def loadJsonFile(context: ContextWrapper): File = {
    val mockFile = mock[File]
    mockFile.exists() returns fileExists
    mockFile
  }
  
}

class NetServicesComponentSpec
    extends Specification
    with BaseTestSupport {

  val jsonFileContent = """{"message": "Hello World!"}"""

  val stringRequest = ServiceClientStringRequest("jsonFile")

  val stringResponse = ServiceClientStringResponse(statusCode = 200, data = Some(jsonFileContent))

  "NetServices component" should {
    
    "return a positive response indicating file not downloaded when JSON file available and force download flag set to false" in
      new NetServicesComponentSupport {

        override val fileExists = true

        netServices.saveJsonInLocal(new NetRequest(false)) *=== NetResponse(success = true, downloaded = false)
    }

    "return a negative response when getting an error downloading json and force download flag set to false" in
      new NetServicesComponentSupport {

        override val fileExists = false

        serviceClient.getString returns {
          (stringRequest) => Future.failed(new RuntimeException(""))
        }

        netServices.saveJsonInLocal(new NetRequest(false)) *=== NetResponse(success = false, downloaded = false)
    }

    "return a negative response when getting an error downloading json and force download flag set to true" in
      new NetServicesComponentSupport {

        override val fileExists = false

        serviceClient.getString returns {
          (stringRequest) => Future.failed(new RuntimeException(""))
        }

        netServices.saveJsonInLocal(new NetRequest(true)) *=== NetResponse(success = false, downloaded = false)
    }

    "return a positive response indicating file downloaded when JSON file unavailable and force download flag set to false" in
      new NetServicesComponentSupport {

        override val fileExists = false

        serviceClient.getString returns {
          (stringRequest) => Future.successful(stringResponse)
        }

        override def writeText(file: File, text: String): Try[Unit] = Success[Unit](None)

        netServices.saveJsonInLocal(new NetRequest(false)) *=== NetResponse(success = true, downloaded = true)
      }

    "return a positive response indicating file downloaded when JSON file unavailable and force download flag set to true" in
      new NetServicesComponentSupport {

        override val fileExists = false

        serviceClient.getString returns {
          (stringRequest) => Future.successful(stringResponse)
        }

        override def writeText(file: File, text: String): Try[Unit] = Success[Unit](None)

        netServices.saveJsonInLocal(new NetRequest(true)) *=== NetResponse(success = true, downloaded = true)
      }

    "return a positive response indicating file downloaded when JSON file available and force download flag set to true" in
      new NetServicesComponentSupport {

        override def loadJsonFile(context: ContextWrapper): File = {
          val mockFile = mock[File]
          mockFile.exists() returns true
          mockFile.delete() returns true
          mockFile
        }

        serviceClient.getString returns {
          (stringRequest) => Future.successful(stringResponse)
        }
        override def writeText(file: File, text: String): Try[Unit] = Success[Unit](None)

        netServices.saveJsonInLocal(new NetRequest(true)) *=== NetResponse(success = true, downloaded = true)
      }
    
  }

}