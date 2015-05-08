package com.fortysevendeg.android.scaladays.modules.json

import java.io.File

import com.fortysevendeg.android.scaladays.model.{Root, Conference}
import com.fortysevendeg.android.scaladays.modules.json.impl.JsonServicesComponentImpl
import com.fortysevendeg.android.scaladays.modules.json.models.{ApiConference, ApiRoot}
import com.fortysevendeg.android.scaladays.utils.AsyncUtils._
import com.fortysevendeg.android.scaladays.{ContextWrapperTestSupport, BaseTestSupport}
import macroid.ContextWrapper
import org.specs2.mutable.Specification

import scala.util.{Success, Failure, Try}

trait JsonServicesComponentSupport
  extends JsonServicesComponentImpl
  with ContextWrapperTestSupport {

  override def loadJsonFile(context: ContextWrapper): File = {
    mock[File]
  }

}

class JsonServicesComponentSpec
    extends Specification
    with BaseTestSupport {

  val wrongJsonFileContent = """{"message": "Hello World!"}"""
  
  val jsonFileContent = """{"conferences": []}"""

  "JsonServices component" should {
    
    "return a response with None when load file throws an exception" in new JsonServicesComponentSupport {
      
      override def getJson(file: File): Try[String] = Failure[String](new RuntimeException())
      
      jsonServices.loadJson(new JsonRequest) *=== JsonResponse(None)
    }

    "return a response with None when the json content is wrong" in new JsonServicesComponentSupport {

      override def getJson(file: File): Try[String] = Success[String](wrongJsonFileContent)

      jsonServices.loadJson(new JsonRequest) *=== JsonResponse(None)
    }

    "return a response with ApiRoot when the json content is correct" in new JsonServicesComponentSupport {

      override def getJson(file: File): Try[String] = Success[String](jsonFileContent)

      jsonServices.loadJson(new JsonRequest) *=== JsonResponse(Some(Root(Seq.empty[Conference])))
    }
    
  }

}