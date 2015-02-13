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
import android.widget._
import com.fortysevendeg.android.scaladays.R
import com.fortysevendeg.android.scaladays.ui.commons.AsyncImageTweaks._
import com.fortysevendeg.android.scaladays.ui.commons.ResourceLoader
import com.fortysevendeg.android.scaladays.ui.components.{IconTypes, PathMorphDrawable}
import com.fortysevendeg.macroid.extras.LinearLayoutTweaks._
import com.fortysevendeg.macroid.extras.TextTweaks._
import com.fortysevendeg.macroid.extras.ViewTweaks._
import com.fortysevendeg.macroid.extras.ImageViewTweaks._
import com.fortysevendeg.macroid.extras.ScrollViewTweaks._
import macroid.{ActivityContext, Tweak, AppContext}
import macroid.FullDsl._

import scala.language.postfixOps

trait ActivityStyles extends ResourceLoader {

  val rootStyle: Tweak[FrameLayout] = vMatchParent

  def scrollContentStyle(implicit appContext: AppContext): Tweak[ScrollView] = {
    val paddingLeftRight = getDimension(R.dimen.padding_default)
    vMatchParent +
      vPadding(paddingLeftRight, getDimension(R.dimen.padding_schedule_detail_scroll_top), paddingLeftRight, 0) +
      svRemoveVerticalScrollBar
  }

  val contentStyle: Tweak[LinearLayout] =
    vMatchParent +
      llVertical

  def descriptionContentLayoutStyle(implicit appContext: AppContext): Tweak[LinearLayout] =
    vMatchWidth +
      llHorizontal +
      vPadding(0, getDimension(R.dimen.padding_schedule_detail_description_top), 0, 0)

  def speakersContentLayoutStyle(implicit appContext: AppContext): Tweak[LinearLayout] =
    vMatchWidth +
      llVertical +
      vPadding(0, getDimension(R.dimen.padding_schedule_detail_speaker_tb), 0, 0)

  val verticalLayoutStyle = llWrapWeightHorizontal +
    llVertical

  def toolBarTitleStyle(implicit appContext: AppContext): Tweak[TextView] = {
    val padding = getDimension(R.dimen.padding_default)
    vMatchHeight +
      tvGravity(Gravity.BOTTOM) +
      tvColorResource(R.color.toolbar_title) +
      tvSize(getInt(R.integer.text_huge)) +
      vPadding(padding, 0, padding, padding)
  }

  def fabStyle(implicit appContext: AppContext): Tweak[ImageView] = {
    val size = getDimension(R.dimen.size_schedule_detail_fab)
    lp[FrameLayout](size, size) +
      vMargin(getDimension(R.dimen.margin_schedule_detail_fab_left), getDimension(R.dimen.margin_schedule_detail_fab_top), 0, 0) +
      vBackground(R.drawable.fab_button_no_check) +
      vPaddings(getDimension(R.dimen.padding_schedule_detail_fab)) +
      ivSrc(new PathMorphDrawable(
        defaultIcon = IconTypes.ADD,
        defaultStroke = getDimension(R.dimen.stroke_schedule_detail_fab),
        defaultColor = Color.WHITE
      ))
  }

  def iconCalendarStyle(implicit appContext: AppContext): Tweak[ImageView] =
    lp[LinearLayout](getDimension(R.dimen.width_schedule_detail_calendar), WRAP_CONTENT) +
      ivSrc(R.drawable.detail_icon_schedule) +
      ivScaleType(ScaleType.FIT_START)

  def dateStyle(implicit appContext: AppContext): Tweak[TextView] =
    vWrapContent +
      tvSize(getInt(R.integer.text_big)) +
      tvColor(appContext.get.getResources.getColor(R.color.primary)) +
      vPadding(0, 0, 0, getDimension(R.dimen.padding_default_extra_small))

  def roomStyle(implicit appContext: AppContext): Tweak[TextView] =
    vWrapContent +
      tvSize(getInt(R.integer.text_medium)) +
      tvColorResource(R.color.text_schedule_detail_secondary) +
      vPadding(0, 0, 0, getDimension(R.dimen.padding_default_small))

  def descriptionStyle(implicit appContext: AppContext): Tweak[TextView] =
    vWrapContent +
      tvSize(getInt(R.integer.text_medium)) +
      tvColorResource(R.color.primary) +
      vPadding(0, 0, 0, getDimension(R.dimen.padding_default))

  def lineStyle(implicit appContext: AppContext): Tweak[ImageView] =
    lp[LinearLayout](MATCH_PARENT, 1) +
      vBackgroundColorResource(R.color.list_line_default)

  def speakerTitleStyle(implicit appContext: AppContext): Tweak[TextView] =
    vWrapContent +
      tvSize(getInt(R.integer.text_small)) +
      tvText(R.string.speakers) +
      vPadding(0, getDimension(R.dimen.padding_default_small), 0, 0) +
      tvColorResource(R.color.text_schedule_detail_secondary)

}

trait SpeakersDetailStyles extends ResourceLoader {

  def itemSpeakerContentStyle(implicit appContext: AppContext): Tweak[LinearLayout] =
    vMatchWidth +
      llHorizontal +
      vPadding(0, getDimension(R.dimen.padding_default), 0, getDimension(R.dimen.padding_schedule_detail_speaker_tb))

  def speakerAvatarStyle(picture: Option[String])(implicit appContext: AppContext, activityContext: ActivityContext): Tweak[ImageView] = {
    val size = getDimension(R.dimen.size_avatar)
    lp[LinearLayout](size, size) +
      ivScaleType(ScaleType.CENTER_CROP) +
      vMargin(0, 0, getDimension(R.dimen.padding_default), 0) +
      picture.map(roundedImage(_, R.drawable.placeholder_circle, size)).getOrElse(ivSrc(R.drawable.placeholder_avatar_failed))
  }

  def speakerNameItemStyle(name: String)(implicit appContext: AppContext): Tweak[TextView] =
    vWrapContent +
      tvSize(getInt(R.integer.text_big)) +
      vPadding(0, 0, getDimension(R.dimen.padding_default_extra_small), 0) +
      tvColorResource(R.color.primary) +
      tvText(name)

  def speakerCompanyItemStyle(company: String)(implicit appContext: AppContext): Tweak[TextView] =
    vWrapContent +
      tvSize(getInt(R.integer.text_medium)) +
      vPadding(0, 0, getDimension(R.dimen.padding_default_extra_small), 0) +
      tvColorResource(R.color.text_schedule_detail_secondary) +
      tvText(company)

  def speakerTwitterItemStyle(twitter: Option[String])(implicit appContext: AppContext): Tweak[TextView] =
    vWrapContent +
      tvSize(getInt(R.integer.text_small)) +
      tvColorResource(R.color.text_twitter_default) +
      twitter.map(tvText(_) + vVisible).getOrElse(vGone)

  def speakerBioItemStyle(bio: String)(implicit appContext: AppContext): Tweak[TextView] =
    vWrapContent +
      tvSize(getInt(R.integer.text_medium)) +
      vPadding(0, 0, getDimension(R.dimen.padding_default_extra_small), 0) +
      tvColorResource(R.color.text_schedule_detail_secondary) +
      tvText(bio)

  val verticalLayoutStyle: Tweak[LinearLayout] =
    llWrapWeightHorizontal +
      llVertical

}
