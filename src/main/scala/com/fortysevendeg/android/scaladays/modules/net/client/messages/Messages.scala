package com.fortysevendeg.android.scaladays.modules.net.client.messages

import play.api.libs.json.Reads

case class ServiceClientStringRequest(
  path: String,
  headers: Seq[(String, String)] = Seq.empty,
  emptyResponse: Boolean = false)

case class ServiceClientStringResponse(statusCode: Int, data: Option[String])

case class ServiceClientRequest[Res](
  path: String,
  headers: Seq[(String, String)] = Seq.empty,
  reads: Option[Reads[Res]] = None,
  emptyResponse: Boolean = false)

case class ServiceClientWithBodyRequest[Res](
  path: String,
  body: Map[String, String],
  headers: Seq[(String, String)] = Seq.empty,
  reads: Option[Reads[Res]] = None,
  emptyResponse: Boolean = false)

case class ServiceClientResponse[T](statusCode: Int, data: Option[T])

case class HttpClientRequest(url: String, httpHeaders: Seq[(String, String)])

case class HttpClientWithBodyRequest(url: String, httpHeaders: Seq[(String, String)], body: Map[String, String])

case class HttpClientResponse(statusCode: Int, body: Option[String])
