package com.fortysevendeg.android.scaladays.modules.net.client.http

import com.fortysevendeg.android.scaladays.modules.net.client.http.Methods._
import com.fortysevendeg.android.scaladays.modules.net.client.messages.{HttpClientRequest, HttpClientResponse, HttpClientWithBodyRequest}
import com.fortysevendeg.android.scaladays.scaladays.Service
import okhttp3.MediaType

import scala.collection.JavaConverters._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class OkHttpClient(okHttpClient: okhttp3.OkHttpClient = new okhttp3.OkHttpClient)
  extends HttpClient {

  val textPlainMediaType: MediaType = okhttp3.MediaType.parse("text/plain")

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
    responseHandler: okhttp3.Response => T = defaultResponseHandler _): Future[T] = Future {
      val builder = createBuilderRequest(url, httpHeaders)
      val request = (method match {
        case GET => builder.get()
        case DELETE => builder.delete()
        case POST => builder.post(createBody(body))
        case PUT => builder.put(createBody(body))
      }).build()
      responseHandler(okHttpClient.newCall(request).execute())
  }

  private[this] def defaultResponseHandler(response: okhttp3.Response): HttpClientResponse =
    HttpClientResponse(response.code(), Option(response.body()) map (_.string()))

  private[this] def createBuilderRequest(url: String, httpHeaders: Seq[(String, String)]): okhttp3.Request.Builder =
    new okhttp3.Request.Builder()
      .url(url)
      .headers(createHeaders(httpHeaders))

  private[this] def createHeaders(httpHeaders: Seq[(String, String)]): okhttp3.Headers =
    okhttp3.Headers.of(httpHeaders.map {
      case (key, value) => key -> value
    }.toMap.asJava)

  private[this] def createBody(body: Option[Map[String, String]]) =
    body match {
      case Some(b) =>
        val requestBody = new okhttp3.FormBody.Builder()
        b foreach { case (key, value) => requestBody.add(key, value) }
        requestBody.build()
      case _ => okhttp3.RequestBody.create(textPlainMediaType, "")
    }

}

object Methods {

  sealed trait Method

  case object GET extends Method

  case object DELETE extends Method

  case object POST extends Method

  case object PUT extends Method

}