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

package com.fortysevendeg.android.scaladays.ui.social

import java.lang.Boolean

import android.annotation.SuppressLint
import android.app.Activity
import android.net.Uri
import android.net.http.SslError
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AppCompatActivity
import android.webkit._
import com.fortysevendeg.android.scaladays.R
import com.fortysevendeg.android.scaladays.modules.ComponentRegistryImpl
import com.fortysevendeg.android.scaladays.modules.twitter.{FinalizeAuthenticationRequest, FinalizeAuthenticationResponse, GetAuthenticationURLRequest}
import macroid.extras.DeviceVersion._
import macroid.extras.ViewTweaks._
import macroid.extras.WebViewTweaks._
import macroid._
import macroid.{ContextWrapper, Contexts}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class AuthorizationActivity
    extends AppCompatActivity
    with Contexts[FragmentActivity]
    with AuthorizationLayout
    with ComponentRegistryImpl {

  override lazy val contextProvider: ContextWrapper = activityContextWrapper

  lazy val twitterHost: String = activityContextWrapper.application.getString(R.string.twitter_app_callback_host)

  val webViewClient: WebViewClient = new WebViewClient {
    override def onLoadResource(view: WebView, url: String) {
      Option(Uri.parse(url)) map {
        case uri if uri.getHost == twitterHost =>
          val token: String = uri.getQueryParameter("oauth_token")
          if (null != token) {
            showRedirecting()
            twitterServices.finalizeAuthentication(FinalizeAuthenticationRequest(uri)) map {
              case FinalizeAuthenticationResponse() => success()
              case _ => failed()
            }
          }
        case _ => super.onLoadResource(view, url)
      }

    }
    override def onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
      handler.proceed()
    }
  }

  @SuppressLint(Array("NewApi"))
  private[this] def flushCookies(): Unit = {
    CookieManager.getInstance.removeAllCookies(new ValueCallback[Boolean] {
      override def onReceiveValue(t: Boolean): Unit = {}
    })
    CookieManager.getInstance.removeSessionCookies(new ValueCallback[Boolean] {
      override def onReceiveValue(t: Boolean): Unit = {}
    })
    CookieManager.getInstance.flush()
  }

  override def onCreate(savedInstanceState: Bundle): Unit = {
    super.onCreate(savedInstanceState)

    setContentView(layout)

    Ui.run(webView <~ wvClient(webViewClient))

    Lollipop ifSupportedThen flushCookies()
  }

  override def onResume(): Unit = {
    super.onResume()
    val urlOp = for {
      r <- twitterServices.getAuthenticationURL(GetAuthenticationURLRequest())
    } yield r.url
    urlOp map {
      case Some(url) => Ui.run(webView <~ wvLoadUrl(url))
      case None => failed()
    } recover {
      case _ => failed()
    }
  }

  def success(): Unit = {
    setResult(Activity.RESULT_OK)
    finish()
  }

  def failed(): Unit = {
    setResult(Activity.RESULT_CANCELED)
    finish()
  }

  def showRedirecting(): Future[_] = {
    Ui.run(
      (progressBar <~ vVisible) ~
          (webView <~ vGone))
  }

}
