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

package com.fortysevendeg.android.scaladays.ui.schedule

import android.support.v7.widget.RecyclerView
import android.view.{ViewGroup, Gravity}
import android.view.ViewGroup.LayoutParams._
import android.widget.ImageView.ScaleType
import android.widget.{TextView, ProgressBar, FrameLayout, LinearLayout}
import com.fortysevendeg.android.scaladays.R
import com.fortysevendeg.android.scaladays.ui.commons.ResourceLoader
import com.fortysevendeg.macroid.extras.FrameLayoutTweaks._
import com.fortysevendeg.macroid.extras.ImageViewTweaks._
import com.fortysevendeg.macroid.extras.LinearLayoutTweaks._
import com.fortysevendeg.macroid.extras.RecyclerViewTweaks._
import com.fortysevendeg.macroid.extras.TextTweaks._
import com.fortysevendeg.macroid.extras.ViewTweaks._
import macroid.{Tweak, AppContext}
import macroid.FullDsl._

import scala.language.postfixOps

trait FragmentStyles extends ResourceLoader {

  // Styles for Fragment

  val rootStyle: Tweak[FrameLayout] = vMatchParent

  val recyclerViewStyle: Tweak[RecyclerView] =
    vMatchParent +
      rvNoFixedSize

  val progressBarStyle: Tweak[ProgressBar] =
    vWrapContent +
      flLayoutGravity(Gravity.CENTER)
}

trait SpeakersLayoutStyles extends ResourceLoader {

  def speakerNameItemStyle(name: String)(implicit appContext: AppContext): Tweak[TextView] =
    vWrapContent +
      tvSize(getInt(R.integer.text_medium)) +
      vPadding(0, 0, getDimension(R.dimen.padding_default_extra_small), 0) +
      tvColorResource(R.color.text_schedule_name) +
      tvText(name)

  def speakerTwitterItemStyle(twitter: Option[String])(implicit appContext: AppContext): Tweak[TextView] =
    vWrapContent +
      tvSize(getInt(R.integer.text_medium)) +
      tvColorResource(R.color.text_schedule_twitter) +
      twitter.map(tvText(_) + vVisible).getOrElse(vGone)

  def itemSpeakerContentStyle(implicit appContext: AppContext): Tweak[LinearLayout] =
    vMatchWidth +
      llHorizontal +
      vPadding(0, getDimension(R.dimen.padding_default_extra_small), 0, 0)
}

trait AdapterStyles extends ResourceLoader {

  def itemContentStyle(implicit appContext: AppContext): Tweak[LinearLayout] =
    vMatchParent +
      llHorizontal

  def hourStyle(implicit appContext: AppContext): Tweak[TextView] =
    lp[LinearLayout](getDimension(R.dimen.width_schedule_hour), MATCH_PARENT) +
      tvSize(getInt(R.integer.text_medium)) +
      vPadding(0, getDimension(R.dimen.padding_default_small), 0, 0) +
      vBackgroundColorResource(R.color.background_list_schedule_hour) +
      tvGravity(Gravity.CENTER_HORIZONTAL) +
      tvColorResource(R.color.text_schedule_name) +
      tvBold

  def itemInfoContentStyle(implicit appContext: AppContext): Tweak[LinearLayout] =
    vMatchWidth +
      llVertical +
      vPaddings(getDimension(R.dimen.padding_default), getDimension(R.dimen.padding_default_small)) +
      vBackgroundColorResource(R.color.background_list_schedule_info)

  val itemSpeakersContentStyle: Tweak[LinearLayout] =
    vMatchWidth +
      llVertical

  def roomItemStyle(implicit appContext: AppContext): Tweak[TextView] =
    vWrapContent +
      tvSize(getInt(R.integer.text_small)) +
      tvColorResource(R.color.text_schedule_room) +
      vPadding(0, 0, 0, getDimension(R.dimen.padding_default_extra_small))

  def nameItemStyle(implicit appContext: AppContext): Tweak[TextView] =
    vWrapContent +
      tvSize(getInt(R.integer.text_medium)) +
      tvColorResource(R.color.text_schedule_name) +
      tvBold

}
