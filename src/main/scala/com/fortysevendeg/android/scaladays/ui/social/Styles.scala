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

import android.view.Gravity
import android.view.ViewGroup.LayoutParams._
import android.webkit.WebView
import android.widget.ImageView.ScaleType
import android.widget._
import com.fortysevendeg.android.scaladays.R
import com.fortysevendeg.macroid.extras.FrameLayoutTweaks._
import com.fortysevendeg.macroid.extras.ImageViewTweaks._
import com.fortysevendeg.macroid.extras.LinearLayoutTweaks._
import com.fortysevendeg.macroid.extras.ResourcesExtras._
import com.fortysevendeg.macroid.extras.TextTweaks._
import com.fortysevendeg.macroid.extras.ViewTweaks._
import macroid.FullDsl._
import macroid.{ContextWrapper, Tweak}

import scala.language.postfixOps

trait AdapterStyles {
  
  def itemContentStyle(implicit context: ContextWrapper): Tweak[LinearLayout] =
    vMatchParent +
      llHorizontal +
      vPaddings(resGetDimensionPixelSize(R.dimen.padding_default)) +
      vBackground(R.drawable.background_list_default)

  def avatarStyle(implicit context: ContextWrapper): Tweak[ImageView] = {
    val size = resGetDimensionPixelSize(R.dimen.size_avatar)
    lp[LinearLayout](size, size) +
      ivScaleType(ScaleType.CENTER_CROP)
  }

  def itemNoAvatarContentStyle(implicit context: ContextWrapper): Tweak[LinearLayout] =
    vMatchWidth +
      llVertical +
      vPadding(resGetDimensionPixelSize(R.dimen.padding_default), 0, 0, 0)

  def titlesContentStyle(implicit context: ContextWrapper): Tweak[LinearLayout] =
    vMatchWidth +
      llHorizontal +
      vPadding(0, 0, 0, resGetDimensionPixelSize(R.dimen.padding_default_extra_small))

  def nameItemStyle(implicit context: ContextWrapper): Tweak[TextView] =
    lp[LinearLayout](0, WRAP_CONTENT, 1) +
      tvSizeResource(R.dimen.text_big) +
      tvColorResource(R.color.text_title_default)

  def dateItemStyle(implicit context: ContextWrapper): Tweak[TextView] =
    vWrapContent +
      tvSizeResource(R.dimen.text_small) +
      tvColorResource(R.color.text_date_default)

  def twitterItemStyle(implicit context: ContextWrapper): Tweak[TextView] =
    vWrapContent +
      tvSizeResource(R.dimen.text_small) +
      tvColorResource(R.color.text_twitter_default)

  def messageItemStyle(implicit context: ContextWrapper): Tweak[TextView] =
    vWrapContent +
      tvSizeResource(R.dimen.text_medium) +
      tvColorResource(R.color.text_title_default) +
      vPadding(0, resGetDimensionPixelSize(R.dimen.padding_default_extra_small), 0, 0)

}

trait AuthorizationStyles {

  val rootAuthStyle: Tweak[FrameLayout] = vMatchParent

  val webViewAuthStyle: Tweak[WebView] = vMatchParent

  val progressBarAuthStyle: Tweak[ProgressBar] =
    vWrapContent +
      flLayoutGravity(Gravity.CENTER) +
      vGone

}

