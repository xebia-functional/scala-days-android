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

import android.app.Activity
import android.net.Uri
import android.net.http.SslError
import android.os.{Build, Bundle}
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AppCompatActivity
import android.webkit._
import com.fortysevendeg.android.scaladays.R
import com.fortysevendeg.android.scaladays.modules.ComponentRegistryImpl
import com.fortysevendeg.android.scaladays.modules.twitter.{GetAuthenticationURLRequest, FinalizeAuthenticationRequest, FinalizeAuthenticationResponse}
import com.fortysevendeg.macroid.extras.DeviceVersion._
import com.fortysevendeg.macroid.extras.ViewTweaks._
import com.fortysevendeg.macroid.extras.WebViewTweaks._
import macroid.FullDsl._
import macroid.{ContextWrapper, Contexts}

import scala.concurrent.ExecutionContext.Implicits.global

class AuthorizationActivity
    extends AppCompatActivity
    with Contexts[FragmentActivity]
    with AuthorizationLayout
    with ComponentRegistryImpl {

  override lazy val contextProvider: ContextWrapper = activityContextWrapper

  lazy val twitterHost = activityContextWrapper.application.getString(R.string.twitter_app_callback_host)

  val webViewClient: WebViewClient = new WebViewClient {
    override def onLoadResource(view: WebView, url: String) {
      Option(Uri.parse(url)) map {
        case uri if uri.getHost == twitterHost =>
          val token: String = uri.getQueryParameter("oauth_token")
          if (null != token) {
            showRedirecting
            twitterServices.finalizeAuthentication(FinalizeAuthenticationRequest(uri)) map {
              case FinalizeAuthenticationResponse() => success()
              case _ => failed()
            }
          }
        case _ => super.onLoadResource(view, url)
      }

    }
    override def onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
      handler.proceed
    }
  }

  override def onCreate(savedInstanceState: Bundle): Unit = {
    super.onCreate(savedInstanceState)

    Lollipop ifSupportedThen (CookieSyncManager.createInstance(this))

    setContentView(layout)

    runUi(webView <~ wvClient(webViewClient))

    Lollipop ifSupportedThen {
      CookieManager.getInstance.removeAllCookie
      CookieManager.getInstance.removeExpiredCookie
      CookieManager.getInstance.removeSessionCookie
      CookieSyncManager.getInstance.sync
    }

  }

  override def onResume(): Unit = {
    super.onResume()
    val urlOp = for {
      r <- twitterServices.getAuthenticationURL(GetAuthenticationURLRequest())
    } yield r.url
    urlOp map {
      case Some(url) => runUi(webView <~ wvLoadUrl(url))
      case None => failed()
    } recover {
      case _ => failed()
    }
  }

  def success() = {
    setResult(Activity.RESULT_OK)
    finish()
  }

  def failed() = {
    setResult(Activity.RESULT_CANCELED)
    finish()
  }

  def showRedirecting = {
    runUi(
      (progressBar <~ vVisible) ~
          (webView <~ vGone))
  }

}
