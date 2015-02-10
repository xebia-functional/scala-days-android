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

package com.fortysevendeg.android.scaladays.modules.twitter.impl

import android.content.{Context, SharedPreferences}
import android.provider.SyncStateContract.Constants
import com.fortysevendeg.android.scaladays.R
import com.fortysevendeg.android.scaladays.scaladays.Service
import com.fortysevendeg.macroid.extras.AppContextProvider
import twitter4j.{Twitter, Query, TwitterFactory}
import com.fortysevendeg.android.scaladays.modules.twitter._
import twitter4j.auth.AccessToken
import twitter4j.conf.ConfigurationBuilder
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Future
import scala.util.{Failure, Success, Try}
import scala.collection.JavaConverters._

trait TwitterServicesComponentImpl
    extends TwitterServicesComponent
    with TwitterConversions {

  self: AppContextProvider =>

  val twitterServices = new TwitterServicesImpl

  class TwitterServicesImpl
      extends TwitterServices {

    val namePreferencesKey = "twitter_preferences_key"

    val authKey = "twitter_auth_key"

    val authSecretKey = "twitter_auth_secret_key"

    lazy val sharedPreferences: SharedPreferences = appContextProvider.get.
        getSharedPreferences(namePreferencesKey, Context.MODE_PRIVATE)

    lazy val (twitterOAuth, requestTokenOAuth) = {
      val twitterOAuth = new TwitterFactory(getConfiguration()).getInstance
      val requestTokenOAuth = twitterOAuth.getOAuthRequestToken
      (twitterOAuth, requestTokenOAuth)
    }

    private def getConfiguration() = {
      val configurationBuilder = new ConfigurationBuilder()
      configurationBuilder.setOAuthConsumerKey(appContextProvider.get.getString(R.string.twitter_app_key))
      configurationBuilder.setOAuthConsumerSecret(appContextProvider.get.getString(R.string.twitter_app_secret))
      configurationBuilder.build()
    }

    private def getTwitter: Option[Twitter] = {
      for {
        authKey <- getAuthKey()
        auchSecret <- getAuthSecretKey()
      } yield {
        val at: AccessToken = new AccessToken(authKey, auchSecret)
        new TwitterFactory(getConfiguration).getInstance(at)
      }
    }

    private def setAuthKey(auth: String): Unit = sharedPreferences.edit().putString(authKey, auth).commit()

    private def getAuthKey(): Option[String] = Option(sharedPreferences.getString(authKey, null))

    private def setAuthSecretKey(auth: String): Unit = sharedPreferences.edit().putString(authSecretKey, auth).commit()

    private def getAuthSecretKey(): Option[String] = Option(sharedPreferences.getString(authSecretKey, null))

    override def getAuthenticationURL: Service[GetAuthenticationURLRequest, GetAuthenticationURLResponse] =
      request =>
        Future.successful {
          Try {
            requestTokenOAuth.getAuthorizationURL
          } match {
            case Success(url) => GetAuthenticationURLResponse(Some(url))
            case Failure(_) => GetAuthenticationURLResponse(None)
          }
        }

    override def finalizeAuthentication: Service[FinalizeAuthenticationRequest, FinalizeAuthenticationResponse] =
      request =>
        Future.successful {
          val verifier: String = request.uri.getQueryParameter("oauth_verifier")
          val accessTokenOAuth: AccessToken = twitterOAuth.getOAuthAccessToken(requestTokenOAuth, verifier)
          setAuthKey(accessTokenOAuth.getToken)
          setAuthSecretKey(accessTokenOAuth.getTokenSecret)
          twitterOAuth.setOAuthAccessToken(accessTokenOAuth)
          FinalizeAuthenticationResponse()
        }

    override def isConnected(): Boolean = getAuthKey().isDefined

    override def disconnected(): Unit = setAuthKey(null)

    override def search: Service[SearchRequest, SearchResponse] =
      request =>
          Future.successful {
            val query: Query = new Query
            query.setQuery(request.search)
            query.setCount(100)

            (getTwitter map {
              twitter =>
                val statuses = twitter.search(query).getTweets().asScala.toList
                SearchResponse(toSeqTwitterMessage(statuses))
            }).getOrElse(SearchResponse(Seq.empty))
          }
  }

}

