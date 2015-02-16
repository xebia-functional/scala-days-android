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

package com.fortysevendeg.android.scaladays.ui.menu

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.ViewGroup.LayoutParams._
import android.widget.ImageView.ScaleType
import android.widget._
import com.fortysevendeg.android.scaladays.R
import com.fortysevendeg.macroid.extras.FrameLayoutTweaks._
import com.fortysevendeg.macroid.extras.ImageViewTweaks._
import com.fortysevendeg.macroid.extras.LinearLayoutTweaks._
import com.fortysevendeg.macroid.extras.TextTweaks._
import com.fortysevendeg.macroid.extras.ViewTweaks._
import com.fortysevendeg.macroid.extras.ResourcesExtras._
import macroid.FullDsl._
import macroid.{AppContext, Tweak}

import scala.language.postfixOps

trait Styles {

  val menuStyle: Tweak[LinearLayout] =
    vMatchParent +
      llVertical

  def drawerMenuStyle(implicit appContext: AppContext): Tweak[RecyclerView] =
    lp[AbsListView](MATCH_PARENT, MATCH_PARENT) +
      vBackground(R.drawable.background_menu_transition)

  def bigImageLayoutStyle(implicit appContext: AppContext): Tweak[FrameLayout] =
    lp[FrameLayout](MATCH_PARENT, resGetDimensionPixelSize(R.dimen.height_menu_image_header)) +
      flForeground(resGetDrawable(R.drawable.background_header_menu_default))

  val bigImageStyle: Tweak[ImageView] = 
    vMatchParent +
      ivScaleType(ScaleType.CENTER_CROP)

  def bigImageActionLayout(implicit appContext: AppContext): Tweak[LinearLayout] =
    vMatchWidth +
      llGravity(Gravity.CENTER_VERTICAL) +
      flLayoutGravity(Gravity.BOTTOM) +
      vPaddings(
        resGetDimensionPixelSize(R.dimen.padding_default), 
        resGetDimensionPixelSize(R.dimen.padding_menu_image_action_tb))

  def conferenceTitleStyle(implicit appContext: AppContext): Tweak[TextView] =
    llWrapWeightHorizontal +
      tvSize(resGetInteger(R.integer.text_medium)) +
      tvColor(Color.WHITE) +
      tvBoldLight

  val conferenceSelectorStyle: Tweak[ImageView] =
    vWrapContent +
      ivSrc(R.drawable.menu_header_select_arrow)
}

trait MainMenuAdapterStyles {

  def mainMenuItemStyle(implicit appContext: AppContext): Tweak[FrameLayout] =
    lp[AbsListView](MATCH_PARENT, resGetDimensionPixelSize(R.dimen.height_menu_item)) +
      vPaddings(paddingLeftRight = resGetDimensionPixelSize(R.dimen.padding_menu_item_lr), paddingTopBottom = 0) +
      vBackground(R.drawable.background_list_menu) +
      flForeground(resGetDrawable(R.drawable.foreground_list_menu))

  def textMenuItemStyle(implicit appContext: AppContext): Tweak[TextView] =
    vMatchParent +
      tvSize(resGetInteger(R.integer.text_medium)) +
      tvColor(Color.WHITE) +
      tvGravity(Gravity.CENTER_VERTICAL) +
      tvDrawablePadding(resGetDimensionPixelSize(R.dimen.padding_menu_item_icon)) +
      tvBoldLight
  
}

trait ConferenceMenuAdapterStyles {

  def conferenceMenuItemLayoutStyle(implicit appContext: AppContext): Tweak[LinearLayout] =
    lp[AbsListView](MATCH_PARENT, resGetDimensionPixelSize(R.dimen.height_menu_conference_item)) +
      llHorizontal +
      llGravity(Gravity.CENTER_VERTICAL) +
      vPaddings(paddingLeftRight = resGetDimensionPixelSize(R.dimen.padding_menu_item_lr), paddingTopBottom = 0) +
      vBackground(R.drawable.foreground_list_menu)

  def conferenceMenuItemIconStyle(implicit appContext: AppContext): Tweak[ImageView] = {
    val size = resGetDimensionPixelSize(R.dimen.size_menu_conference_icon)
    lp[LinearLayout](size, size) +
      ivScaleType(ScaleType.CENTER_CROP)
  }

  def conferenceMenuItemStyle(implicit appContext: AppContext): Tweak[TextView] =
    vWrapContent +
      tvSize(resGetInteger(R.integer.text_medium)) +
      tvColor(Color.WHITE) +
      tvGravity(Gravity.CENTER_VERTICAL) +
      tvDrawablePadding(resGetDimensionPixelSize(R.dimen.padding_menu_item_icon)) +
      vPaddings(paddingLeftRight = resGetDimensionPixelSize(R.dimen.padding_menu_item_lr), paddingTopBottom = 0) +
      tvBoldLight

}
