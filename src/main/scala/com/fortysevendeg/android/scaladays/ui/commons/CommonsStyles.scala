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

import android.support.v7.widget.Toolbar
import android.view.ViewGroup.LayoutParams._
import android.view.{ViewGroup, Gravity}
import android.widget.{TextView, ImageView, LinearLayout}
import com.fortysevendeg.android.scaladays.R
import com.fortysevendeg.macroid.extras.FrameLayoutTweaks._
import com.fortysevendeg.macroid.extras.LinearLayoutTweaks._
import com.fortysevendeg.macroid.extras.ViewTweaks._
import com.fortysevendeg.macroid.extras.ImageViewTweaks._
import com.fortysevendeg.macroid.extras.TextTweaks._
import com.fortysevendeg.macroid.extras.ResourcesExtras._
import macroid.{Tweak, AppContext}
import macroid.FullDsl._

import scala.language.postfixOps

trait ToolbarStyles {

  def toolbarStyle(height: Int)(implicit appContext: AppContext): Tweak[Toolbar] =
    vContentSizeMatchWidth(height) +
      vBackground(R.color.primary)

}

trait PlaceHolderStyles {

  val placeholderContentStyle: Tweak[LinearLayout] =
    vWrapContent +
      flLayoutGravity(Gravity.CENTER) +
      llGravity(Gravity.CENTER_HORIZONTAL) +
      llVertical +
      vGone

  val placeholderImageStyle: Tweak[ImageView] =
    vWrapContent

  def placeholderMessageStyle(implicit appContext: AppContext): Tweak[TextView] =
    vWrapContent +
      tvGravity(Gravity.CENTER) +
      tvColorResource(R.color.text_error_message) +
      tvSize(resGetInteger(R.integer.text_big)) +
      vPaddings(resGetDimensionPixelSize(R.dimen.padding_default_big))

  def placeholderButtonStyle(implicit appContext: AppContext): Tweak[TextView] =
    vWrapContent +
      vMinWidth(resGetDimensionPixelSize(R.dimen.width_button)) +
      tvText(R.string.reload) +
      tvColorResource(R.color.text_error_button) +
      vBackground(R.drawable.background_error_button) +
      tvAllCaps +
      tvSize(resGetInteger(R.integer.text_medium)) +
      tvGravity(Gravity.CENTER)

}

trait HeaderAdapterStyles {

  def headerContentStyle(implicit appContext: AppContext): Tweak[LinearLayout] =
    lp[ViewGroup](MATCH_PARENT, resGetDimensionPixelSize(R.dimen.height_header)) +
      llHorizontal +
      vBackgroundColorResource(R.color.background_list_schedule_header)

  def headerNameStyle(implicit appContext: AppContext): Tweak[TextView] =
    vWrapContent +
      tvSize(resGetInteger(R.integer.text_medium)) +
      llLayoutGravity(Gravity.CENTER_VERTICAL) +
      tvColorResource(R.color.text_schedule_name) +
      tvBold +
      tvAllCaps +
      vPadding(resGetDimensionPixelSize(R.dimen.padding_default), 0, 0, 0)

}
