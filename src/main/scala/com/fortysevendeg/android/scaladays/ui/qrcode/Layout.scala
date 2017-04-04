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

package com.fortysevendeg.android.scaladays.ui.qrcode

import android.content.Intent
import android.support.v4.app.Fragment
import android.widget._
import com.fortysevendeg.android.scaladays.modules.analytics.AnalyticsServicesComponent
import com.fortysevendeg.android.scaladays.ui.commons.AnalyticStrings._
import com.fortysevendeg.android.scaladays.ui.commons.IntegerResults._
import com.google.zxing.client.android.CaptureActivity
import macroid.FullDsl._
import macroid.{Ui, ActivityContextWrapper}

trait Layout
  extends Styles {

  self : Fragment with AnalyticsServicesComponent =>

  val DefaultDisplayMs: Long = 0L

  val CaptureActionScan: String = "com.google.zxing.client.android.SCAN"

  val DisplayDurationKey: String = "RESULT_DISPLAY_DURATION_MS"

  def content(implicit context: ActivityContextWrapper) = Ui.get(
    l[LinearLayout](
      w[ImageView] <~ qrImageStyle,
      w[TextView] <~ qrMessageStyle,
      w[Button] <~ qrButtonStyle <~ On.click {
        Ui {
          analyticsServices.sendEvent(
            screenName = Some(analyticsContactsScreen),
            category = analyticsCategoryNavigate,
            action = analyticsContactsActionScanContact)
          val intent = new Intent(getActivity, classOf[CaptureActivity])
          intent.setAction(CaptureActionScan)
          intent.putExtra(DisplayDurationKey, DefaultDisplayMs)
          startActivityForResult(intent, scanResult)
        }
      }
    ) <~ qrContentStyle
  )

}
