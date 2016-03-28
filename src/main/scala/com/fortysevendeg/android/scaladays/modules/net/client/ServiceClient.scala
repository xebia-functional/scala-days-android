package com.fortysevendeg.android.scaladays.modules.net.client

import com.fortysevendeg.android.scaladays.modules.net.client.http.HttpClient
import com.fortysevendeg.android.scaladays.modules.net.client.messages._
import com.fortysevendeg.android.scaladays.scaladays.Service
import play.api.libs.json.{Json, Reads}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

class ServiceClient(httpClient: HttpClient) {

  def getString: Service[ServiceClientStringRequest, ServiceClientStringResponse] =
    (request: ServiceClientStringRequest) =>
      for {
        clientResponse <- httpClient.doGet(HttpClientRequest(request.path, request.headers))
        response <- readString(clientResponse, request.emptyResponse)
      } yield ServiceClientStringResponse(clientResponse.statusCode.intValue, response)

  def get[Res]: Service[ServiceClientRequest[Res], ServiceClientResponse[Res]] =
    (request: ServiceClientRequest[Res]) =>
      for {
        clientResponse <- httpClient.doGet(HttpClientRequest(request.path, request.headers))
        response <- readResponse(clientResponse, request.reads, request.emptyResponse)
      } yield ServiceClientResponse(clientResponse.statusCode.intValue, response)

  def emptyPost[Res]: Service[ServiceClientRequest[Res], ServiceClientResponse[Res]] =
    (request: ServiceClientRequest[Res]) =>
      for {
        clientResponse <- httpClient.doPost(HttpClientRequest(request.path, request.headers))
        response <- readResponse(clientResponse, request.reads, request.emptyResponse)
      } yield ServiceClientResponse(clientResponse.statusCode, response)

  def post[Res]: Service[ServiceClientWithBodyRequest, ServiceClientWithBodyResponse] =
    (request: ServiceClientWithBodyRequest) =>
      for {
        clientResponse <- httpClient.doPostWithBody(HttpClientWithBodyRequest(request.path, Seq.empty, request.body))
      } yield ServiceClientWithBodyResponse(clientResponse.statusCode, clientResponse.body)

  def emptyPut[Res]: Service[ServiceClientRequest[Res], ServiceClientResponse[Res]] =
    (request: ServiceClientRequest[Res]) =>
      for {
        clientResponse <- httpClient.doPut(HttpClientRequest(request.path, request.headers))
        response <- readResponse(clientResponse, request.reads, request.emptyResponse)
      } yield ServiceClientResponse(clientResponse.statusCode, response)

  def delete[Res]: Service[ServiceClientRequest[Res], ServiceClientResponse[Res]] =
    (request: ServiceClientRequest[Res]) =>
      for {
        clientResponse <- httpClient.doDelete(HttpClientRequest(request.path, request.headers))
        response <- readResponse(clientResponse, request.reads, request.emptyResponse)
      } yield ServiceClientResponse(clientResponse.statusCode, response)

  private def readString(
    clientResponse: HttpClientResponse,
    emptyResponse: Boolean
  ): Future[Option[String]] = Future {
    (clientResponse.body, emptyResponse) match {
      case (Some(d), false) => Some(d)
      case (None, false) =>
        println("No content")
        None
      case (Some(d), false) =>
        println("No transformer found for type")
        None
      case _ => None
    }
  }

  private def readResponse[T](
    clientResponse: HttpClientResponse,
    maybeReads: Option[Reads[T]],
    emptyResponse: Boolean
    ): Future[Option[T]] = Future {
      (clientResponse.body, emptyResponse, maybeReads) match {
        case (Some(d), false, Some(r)) => transformResponse[T](d, r)
        case (None, false, _) =>
          println("No content")
          None
        case (Some(d), false, None) =>
          println("No transformer found for type")
          None
        case _ => None
      }
  }

  private def transformResponse[T](
    content: String,
    reads: Reads[T]
    ): Option[T] =
    Try(Json.parse(content).as[T](reads)) match {
      case Success(s) => Some(s)
      case Failure(e) =>
        e.printStackTrace()
        None
    }

}
