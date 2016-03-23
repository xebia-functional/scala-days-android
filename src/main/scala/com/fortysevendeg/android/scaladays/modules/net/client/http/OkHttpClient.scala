package com.fortysevendeg.android.scaladays.modules.net.client.http

import com.fortysevendeg.android.scaladays.modules.net.client.http.Methods._
import com.fortysevendeg.android.scaladays.modules.net.client.messages.{HttpClientWithBodyRequest, HttpClientRequest, HttpClientResponse}
import com.fortysevendeg.android.scaladays.scaladays.Service
import com.squareup.okhttp.FormEncodingBuilder
import com.squareup.{okhttp => okHttp}

import scala.collection.JavaConverters._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class OkHttpClient(okHttpClient: okHttp.OkHttpClient = new okHttp.OkHttpClient)
  extends HttpClient {

  val textPlainMediaType = okHttp.MediaType.parse("text/plain")

  override def doGet: Service[HttpClientRequest, HttpClientResponse] =
    (request: HttpClientRequest) => doMethod(GET, request.url, request.httpHeaders)

  override def doDelete: Service[HttpClientRequest, HttpClientResponse] =
    (request: HttpClientRequest) => doMethod(DELETE, request.url, request.httpHeaders)

  override def doPost: Service[HttpClientRequest, HttpClientResponse] =
    (request: HttpClientRequest) => doMethod(POST, request.url, request.httpHeaders)

  override def doPostWithBody: Service[HttpClientWithBodyRequest, HttpClientResponse] =
    (request: HttpClientWithBodyRequest) =>
      doMethod(POST, request.url, request.httpHeaders, Some(request.body))

  override def doPut: Service[HttpClientRequest, HttpClientResponse] =
    (request: HttpClientRequest) => doMethod(PUT, request.url, request.httpHeaders)

  override def doPutWithBody: Service[HttpClientWithBodyRequest, HttpClientResponse] =
    (request: HttpClientWithBodyRequest) =>
      doMethod(PUT, request.url, request.httpHeaders, Some(request.body))

  private[this] def doMethod[T](
    method: Method,
    url: String,
    httpHeaders: Seq[(String, String)],
    body: Option[Map[String, String]] = None,
    responseHandler: okHttp.Response => T = defaultResponseHandler _): Future[T] = Future {
      val builder = createBuilderRequest(url, httpHeaders)
      val request = (method match {
        case GET => builder.get()
        case DELETE => builder.delete()
        case POST => builder.post(createBody(body))
        case PUT => builder.put(createBody(body))
      }).build()
      responseHandler(okHttpClient.newCall(request).execute())
  }

  private[this] def defaultResponseHandler(response: okHttp.Response): HttpClientResponse =
    HttpClientResponse(response.code(), Option(response.body()) map (_.string()))

  private[this] def createBuilderRequest(url: String, httpHeaders: Seq[(String, String)]): okHttp.Request.Builder =
    new okHttp.Request.Builder()
      .url(url)
      .headers(createHeaders(httpHeaders))

  private[this] def createHeaders(httpHeaders: Seq[(String, String)]): okHttp.Headers =
    okHttp.Headers.of(httpHeaders.map {
      case (key, value) => key -> value
    }.toMap.asJava)

  private[this] def createBody(body: Option[Map[String, String]]) =
    body match {
      case Some(b) =>
        val requestBody = new FormEncodingBuilder()
        b foreach { case (key, value) => requestBody.add(key, value) }
        requestBody.build()
      case _ => okHttp.RequestBody.create(textPlainMediaType, "")
    }

}

object Methods {

  sealed trait Method

  case object GET extends Method

  case object DELETE extends Method

  case object POST extends Method

  case object PUT extends Method

}