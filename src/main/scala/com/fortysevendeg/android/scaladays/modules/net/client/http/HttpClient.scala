package com.fortysevendeg.android.scaladays.modules.net.client.http

import com.fortysevendeg.android.scaladays.modules.net.client.messages.{HttpClientRequest, HttpClientResponse, HttpClientWithBodyRequest}
import com.fortysevendeg.android.scaladays.scaladays.Service

trait HttpClient {

  def doGet: Service[HttpClientRequest, HttpClientResponse]

  def doDelete: Service[HttpClientRequest, HttpClientResponse]

  def doPost: Service[HttpClientRequest, HttpClientResponse]

  def doPostWithBody: Service[HttpClientWithBodyRequest, HttpClientResponse]

  def doPut: Service[HttpClientRequest, HttpClientResponse]

  def doPutWithBody: Service[HttpClientWithBodyRequest, HttpClientResponse]

}
