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

import android.graphics.Color
import android.view.Gravity
import android.view.ViewGroup.LayoutParams._
import android.widget.ImageView.ScaleType
import android.widget.{FrameLayout, LinearLayout}
import com.fortysevendeg.android.scaladays.R
import com.fortysevendeg.android.scaladays.ui.commons.GlideTweaks._
import com.fortysevendeg.android.scaladays.ui.components.{IconTypes, PathMorphDrawable}
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

  def scrollContentStyle(implicit appContext: AppContext) = vMatchParent +
      vPadding(16 dp, 124 dp, 16 dp, 0) +
      svRemoveVerticalScrollBar

  val contentStyle = vMatchParent +
      llVertical

  def descriptionContentLayoutStyle(implicit appContext: AppContext) = vMatchWidth +
      llHorizontal +
      vPadding(0, 36 dp, 0, 0)

  def speakersContentLayoutStyle(implicit appContext: AppContext) = vMatchWidth +
      llVertical +
      vPadding(0, 8 dp, 0, 0)

  val verticalLayoutStyle = llWrapWeightHorizontal +
      llVertical

  def toolBarTitleStyle(implicit appContext: AppContext) = vMatchHeight +
      tvGravity(Gravity.BOTTOM) +
      tvColorResource(R.color.toolbar_title) +
      tvSize(24) +
      vPadding(16 dp, 0, 16 dp, 16 dp)

  def fabStyle(implicit appContext: AppContext) = lp[FrameLayout](44 dp, 44 dp) +
      vMargin(12 dp, 102 dp, 0, 0) +
      vBackground(R.drawable.fab_button_no_check) +
      vPaddings(10 dp) +
      ivSrc(new PathMorphDrawable(
        defaultIcon = IconTypes.ADD,
        defaultStroke = 2 dp,
        defaultColor = Color.WHITE
      ))

  def iconCalendarStyle(implicit appContext: AppContext) = lp[LinearLayout](54 dp, WRAP_CONTENT) +
      ivSrc(R.drawable.detail_icon_schedule) +
      ivScaleType(ScaleType.FIT_START)

  def dateStyle(implicit appContext: AppContext) = vWrapContent +
      tvSize(16) +
      tvColor(appContext.get.getResources.getColor(R.color.primary)) +
      vPadding(0, 0, 0, 4 dp)

  def roomStyle(implicit appContext: AppContext) = vWrapContent +
      tvSize(14) +
      tvColorResource(R.color.text_schedule_detail_secondary) +
      vPadding(0, 0, 0, 12 dp)

  def descriptionStyle(implicit appContext: AppContext) = vWrapContent +
      tvSize(14) +
      tvColorResource(R.color.primary) +
      vPadding(0, 0, 0, 16 dp)

  def lineStyle(implicit appContext: AppContext) = lp[LinearLayout](MATCH_PARENT, 1) +
      vBackgroundColorResource(R.color.list_line_default)

  def speakerTitleStyle(implicit appContext: AppContext) = vWrapContent +
      tvSize(12) +
      tvText(R.string.speakers) +
      vPadding(0, 12 dp, 0, 0) +
      tvColorResource(R.color.text_schedule_detail_secondary)

  // Speaker layout

  def itemSpeakerContentStyle(implicit appContext: AppContext) = vMatchWidth +
      llHorizontal +
      vPadding(0, 16 dp, 0, 8 dp)

  def speakerAvatarStyle(picture: Option[String])(implicit appContext: AppContext) = lp[LinearLayout](40 dp, 40 dp) +
      ivScaleType(ScaleType.CENTER_CROP) +
      vMargin(0, 0, 15 dp, 0) +
      picture.map(glideRoundedImage(_, R.drawable.placeholder_circle)).getOrElse(ivSrc(R.drawable.placeholder_avatar_failed))

  def speakerNameItemStyle(name: String)(implicit appContext: AppContext) = vWrapContent +
      tvSize(16) +
      vPadding(0, 0, 4 dp, 0) +
      tvColorResource(R.color.primary) +
      tvText(name)

  def speakerCompanyItemStyle(company: String)(implicit appContext: AppContext) = vWrapContent +
      tvSize(14) +
      vPadding(0, 0, 4 dp, 0) +
      tvColorResource(R.color.text_schedule_detail_secondary) +
      tvText(company)

  def speakerTwitterItemStyle(twitter: Option[String])(implicit appContext: AppContext) = vWrapContent +
      tvSize(12) +
      tvColorResource(R.color.speakers_twitter) +
      twitter.map(tvText(_) + vVisible).getOrElse(vGone)

  def speakerBioItemStyle(bio: String)(implicit appContext: AppContext) = vWrapContent +
      tvSize(14) +
      vPadding(0, 0, 4 dp, 0) +
      tvColorResource(R.color.text_schedule_detail_secondary) +
      tvText(bio)

}
