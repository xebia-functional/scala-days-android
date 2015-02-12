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

import android.view.{ViewGroup, Gravity}
import android.view.ViewGroup.LayoutParams._
import android.widget.ImageView.ScaleType
import android.widget.LinearLayout
import com.fortysevendeg.android.scaladays.R
import com.fortysevendeg.macroid.extras.FrameLayoutTweaks._
import com.fortysevendeg.macroid.extras.ImageViewTweaks._
import com.fortysevendeg.macroid.extras.LinearLayoutTweaks._
import com.fortysevendeg.macroid.extras.RecyclerViewTweaks._
import com.fortysevendeg.macroid.extras.TextTweaks._
import com.fortysevendeg.macroid.extras.ViewTweaks._
import macroid.AppContext
import macroid.FullDsl._

import scala.language.postfixOps

trait Styles {

  // Styles for Fragment

  val rootStyle = vMatchParent

  val recyclerViewStyle = vMatchParent +
      rvNoFixedSize

  val progressBarStyle = vWrapContent +
      flLayoutGravity(Gravity.CENTER)

  // Styles for Schedule Adapter

  def itemContentStyle(implicit appContext: AppContext) = vMatchParent +
      llHorizontal

  def hourStyle(implicit appContext: AppContext) = lp[LinearLayout](70 dp, MATCH_PARENT) +
      tvSize(14) +
      vPadding(0, 12 dp, 0, 0) +
      vBackgroundColorResource(R.color.background_list_schedule_hour) +
      tvGravity(Gravity.CENTER_HORIZONTAL) +
      tvColorResource(R.color.text_schedule_name) +
      tvBold

  def itemInfoContentStyle(implicit appContext: AppContext) = vMatchWidth +
      llVertical +
      vPadding(16 dp, 12 dp, 16 dp, 12 dp) +
      vBackgroundColorResource(R.color.background_list_schedule_info)

  val itemSpeakersContentStyle = vMatchWidth +
      llVertical

  def itemSpeakerContentStyle(implicit appContext: AppContext) = vMatchWidth +
      llHorizontal +
      vPadding(0, 4 dp, 0, 0)

  def roomItemStyle(implicit appContext: AppContext) = vWrapContent + tvSize(12) +
      tvColorResource(R.color.text_schedule_room) +
      vPadding(0, 0, 0, 4 dp)

  def nameItemStyle(implicit appContext: AppContext) = vWrapContent +
      tvSize(14) +
      tvColorResource(R.color.text_schedule_name) +
      tvBold

  def speakerNameItemStyle(name: String)(implicit appContext: AppContext) = vWrapContent +
      tvSize(14) +
      vPadding(0, 0, 4 dp, 0) +
      tvColorResource(R.color.text_schedule_name) +
      tvText(name)

  def speakerTwitterItemStyle(twitter: Option[String])(implicit appContext: AppContext) = vWrapContent +
      tvSize(14) +
      tvColorResource(R.color.text_schedule_twitter) +
      twitter.map(tvText(_) + vVisible).getOrElse(vGone)

}
