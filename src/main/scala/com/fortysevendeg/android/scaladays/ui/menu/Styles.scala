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
import android.view.{ViewGroup, Gravity}
import android.view.ViewGroup.LayoutParams._
import android.widget.ImageView.ScaleType
import android.widget.{LinearLayout, FrameLayout, AbsListView}
import com.fortysevendeg.android.scaladays.R
import com.fortysevendeg.macroid.extras.FrameLayoutTweaks._
import com.fortysevendeg.macroid.extras.LinearLayoutTweaks._
import com.fortysevendeg.macroid.extras.TextTweaks._
import com.fortysevendeg.macroid.extras.ImageViewTweaks._
import com.fortysevendeg.macroid.extras.ViewTweaks._
import macroid.AppContext
import macroid.FullDsl._
import scala.language.postfixOps

trait Styles {

  val menuStyle = vMatchParent +
    llVertical

  def drawerMenuStyle(implicit appContext: AppContext) = lp[AbsListView](MATCH_PARENT, MATCH_PARENT) +
    vBackground(R.drawable.background_menu_transition)

  def bigImageLayoutStyle(implicit appContext: AppContext) = lp[FrameLayout](MATCH_PARENT, 169 dp) +
    flForeground(appContext.get.getResources.getDrawable(R.drawable.background_header_menu_default))

  val bigImageStyle = vMatchParent

  def bigImageActionLayout(implicit appContext: AppContext) = vMatchWidth +
    llGravity(Gravity.CENTER_VERTICAL) +
    flLayoutGravity(Gravity.BOTTOM) +
    vPadding(16 dp, 15 dp, 16 dp, 15 dp)

  def conferenceTitleStyle(implicit appContext: AppContext) = llWrapWeightHorizontal +
    tvSize(14) +
    tvColor(Color.WHITE) +
    tvBoldLight

  val conferenceSelectorStyle = vWrapContent +
    ivSrc(R.drawable.menu_header_select_arrow)

  def mainMenuItemStyle(implicit appContext: AppContext) = lp[AbsListView](MATCH_PARENT, 48 dp) +
      vPadding(18 dp, 14 dp, 18 dp, 14 dp) +
      vBackground(R.drawable.background_list_menu) +
      flForeground(appContext.get.getDrawable(R.drawable.foreground_list_menu))

  def textMenuItemStyle(implicit appContext: AppContext) = vMatchParent +
    tvSize(14) +
    tvColor(Color.WHITE) +
    tvGravity(Gravity.CENTER_VERTICAL) +
    tvDrawablePadding(34 dp) +
    tvBoldLight

  def conferenceMenuItemLayoutStyle(implicit appContext: AppContext) = lp[AbsListView](MATCH_PARENT, 74 dp) +
    llHorizontal +
    llGravity(Gravity.CENTER_VERTICAL) +
    vPadding(18 dp, 14 dp, 18 dp, 14 dp) +
    vBackground(R.drawable.foreground_list_menu)

  def conferenceMenuItemIconStyle(implicit appContext: AppContext) = lp[LinearLayout](38 dp, 38 dp) +
    ivScaleType(ScaleType.CENTER_CROP)

  def conferenceMenuItemStyle(implicit appContext: AppContext) = vWrapContent +
      tvSize(14) +
      tvColor(Color.WHITE) +
      tvGravity(Gravity.CENTER_VERTICAL) +
      tvDrawablePadding(34 dp) +
      vPadding(paddingLeft = 20 dp) +
      tvBoldLight

}
