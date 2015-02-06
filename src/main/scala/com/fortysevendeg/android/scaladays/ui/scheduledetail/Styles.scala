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

package com.fortysevendeg.android.scaladays.ui.scheduledetail

import android.view.Gravity
import android.view.ViewGroup.LayoutParams._
import android.widget.ImageView.ScaleType
import android.widget.LinearLayout
import com.fortysevendeg.android.scaladays.R
import com.fortysevendeg.macroid.extras.LinearLayoutTweaks._
import com.fortysevendeg.macroid.extras.TextTweaks._
import com.fortysevendeg.macroid.extras.ViewTweaks._
import com.fortysevendeg.macroid.extras.ImageViewTweaks._
import com.fortysevendeg.macroid.extras.ScrollViewTweaks._
import macroid.AppContext
import macroid.FullDsl._

import scala.language.postfixOps

trait Styles {

  val rootStyle = vMatchParent

  def scrollContentStyle(implicit appContext: AppContext) = vMatchParent + vPadding(16 dp, 124 dp, 16 dp, 0) +
      svRemoveVerticalScrollBar

  val contentStyle = vMatchParent + llVertical

  def horizontalLayoutStyle(implicit appContext: AppContext) = vMatchWidth + llHorizontal + vPadding(0, 36 dp, 0, 0)

  val verticalLayoutStyle = llWrapWeightHorizontal + llVertical

  def toolBarTitleStyle(implicit appContext: AppContext) = vMatchHeight + tvGravity(Gravity.BOTTOM) +
      tvColorResource(R.color.toolbar_title) + tvSize(24) + vPadding(16 dp, 0, 16 dp, 16 dp)

  def iconCalendarStyle(implicit appContext: AppContext) = lp[LinearLayout](54 dp, WRAP_CONTENT) +
      ivSrc(R.drawable.detail_icon_schedule) + ivScaleType(ScaleType.FIT_START)

  def dateStyle(implicit appContext: AppContext) = vWrapContent + tvSize(16) +
      tvColor(appContext.get.getResources.getColor(R.color.primary)) + vPadding(0, 0, 0, 4 dp)

  def roomStyle(implicit appContext: AppContext) = vWrapContent + tvSize(14) +
      tvColor(appContext.get.getResources.getColor(R.color.text_schedule_detail_room)) + vPadding(0, 0, 0, 12 dp)

  def descriptionStyle(implicit appContext: AppContext) = vWrapContent + tvSize(14) +
      tvColor(appContext.get.getResources.getColor(R.color.primary)) + vPadding(0, 0, 0, 16 dp)

}
