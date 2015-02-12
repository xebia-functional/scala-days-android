/*
 * Copyright (C) 2015 47 Degrees, LLC http://47deg.com hello@47deg.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fortysevendeg.android.scaladays.ui.commons

import android.view.ViewGroup.LayoutParams._
import android.view.{ViewGroup, Gravity}
import com.fortysevendeg.android.scaladays.R
import com.fortysevendeg.macroid.extras.FrameLayoutTweaks._
import com.fortysevendeg.macroid.extras.LinearLayoutTweaks._
import com.fortysevendeg.macroid.extras.ViewTweaks._
import com.fortysevendeg.macroid.extras.ImageViewTweaks._
import com.fortysevendeg.macroid.extras.TextTweaks._
import macroid.AppContext
import macroid.FullDsl._

import scala.language.postfixOps

trait ToolbarStyles {

  def toolbarStyle(height: Int)(implicit appContext: AppContext) = vContentSizeMatchWidth(height) +
      vBackground(R.color.primary)

}

trait PlaceHolderFailedStyles {

  val failedContentStyle = vWrapContent +
      flLayoutGravity(Gravity.CENTER) +
      llGravity(Gravity.CENTER_HORIZONTAL) +
      llVertical +
      vGone

  val failedImageStyle = vWrapContent +
      ivSrc(R.drawable.placeholder_error)

  def failedMessageStyle(text: Int)(implicit appContext: AppContext) = vWrapContent +
      tvText(text) +
      tvGravity(Gravity.CENTER) +
      tvColorResource(R.color.text_error_message) +
      tvSize(16) +
      vPaddings(30 dp)

  def failedButtonStyle(implicit appContext: AppContext) = vWrapContent +
      vMinWidth(160 dp) +
      tvText(R.string.reload) +
      tvColorResource(R.color.text_error_button) +
      vBackground(R.drawable.background_error_button) +
      tvAllCaps +
      tvSize(14) +
      tvGravity(Gravity.CENTER)

}

trait HeaderAdapterStyles {

  def headerContentStyle(implicit appContext: AppContext) = lp[ViewGroup](MATCH_PARENT, 44 dp) +
      llHorizontal +
      vBackgroundColorResource(R.color.background_list_schedule_header)

  def headerNameStyle(implicit appContext: AppContext) = vWrapContent +
      tvSize(14) +
      llLayoutGravity(Gravity.CENTER_VERTICAL) +
      tvColorResource(R.color.text_schedule_name) +
      tvBold +
      tvAllCaps +
      vPadding(16 dp, 0, 0, 0)

}
