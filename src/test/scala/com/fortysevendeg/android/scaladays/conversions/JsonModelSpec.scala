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

package com.fortysevendeg.android.scaladays.conversions

import com.fortysevendeg.android.scaladays.modules.json.models.{ApiRoot, ApiConference}
import org.specs2.mutable._
import play.api.libs.json.{Json, JsValue}

class JsonModelSpec
  extends Specification
  with TestConfig
  with JsonModelTestSupportTestSupport {
  

  "load and map sample json" should {

    "return a Conference class with the right fields" in {
      
      import com.fortysevendeg.android.scaladays.conversions.JsonImplicits._
      
      val jsonSource = scala.io.Source.fromInputStream(JsonImplicits.getClass.getResourceAsStream("/scaladays2015.json")).mkString
      val json: JsValue = Json.parse(jsonSource)
      val jsonValue = json.as[ApiRoot]
      jsonValue.conferences(0).info.id shouldEqual 111
    }

  }

}
