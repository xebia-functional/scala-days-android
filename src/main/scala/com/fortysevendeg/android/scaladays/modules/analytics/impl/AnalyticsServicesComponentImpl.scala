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

package com.fortysevendeg.android.scaladays.modules.analytics.impl

import com.fortysevendeg.android.scaladays.R
import com.fortysevendeg.android.scaladays.modules.analytics.{AnalyticsServices, AnalyticsServicesComponent}
import com.fortysevendeg.android.scaladays.commons.ContextWrapperProvider
import com.google.android.gms.analytics.{HitBuilders, GoogleAnalytics}

trait AnalyticsServicesComponentImpl
  extends AnalyticsServicesComponent {

  self: ContextWrapperProvider =>

  lazy val analyticsServices = new AnalyticsServicesImpl

  class AnalyticsServicesImpl
    extends AnalyticsServices {

    lazy val tracker = {
      val track = GoogleAnalytics
        .getInstance(contextProvider.application)
        .newTracker(contextProvider.application.getString(R.string.google_analytics_key))
      track.setAppName(contextProvider.application.getString(R.string.app_name))
      track.enableAutoActivityTracking(false)
      track
    }


    override def sendScreenName(screenName: String): Unit = {
      tracker.setScreenName(screenName)
      val event = new HitBuilders.EventBuilder()
      tracker.send(event.build())
    }

    override def sendEvent(
        screenName: Option[String],
        category: String,
        action: String,
        label: Option[String] = None): Unit = {
      screenName map tracker.setScreenName
      val event = new HitBuilders.EventBuilder()
      event.setCategory(category)
      event.setAction(action)
      label map event.setLabel
      tracker.send(event.build())
    }

  }

}
