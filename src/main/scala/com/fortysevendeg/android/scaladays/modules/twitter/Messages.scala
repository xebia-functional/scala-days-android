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

package com.fortysevendeg.android.scaladays.modules.twitter

import android.net.Uri
import com.fortysevendeg.android.scaladays.model.TwitterMessage

case class GetAuthenticationURLRequest()

case class GetAuthenticationURLResponse(url: Option[String])

case class FinalizeAuthenticationRequest(uri: Uri)

case class FinalizeAuthenticationResponse()

case class SearchRequest(search: String, sinceId: Option[Long] = None)

case class SearchResponse(messages: Seq[TwitterMessage])