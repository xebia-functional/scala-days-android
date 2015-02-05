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

import android.view.Gravity
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

  val recyclerViewStyle = vMatchParent + rvNoFixedSize

  val progressBarStyle = vWrapContent + flLayoutGravity(Gravity.CENTER)

  // Styles for Adapter

  def itemContentStyle(implicit appContext: AppContext) =
    vMatchParent + llHorizontal

  def hourStyle(implicit appContext: AppContext) = lp[LinearLayout](70 dp, MATCH_PARENT) + tvSize(14) +
      vPadding(0, 8 dp, 0, 0) + vBackgroundColorResource(R.color.background_list_schedule_hour) +
      tvGravity(Gravity.CENTER) + tvColorResource(R.color.text_schedule_name)

  def itemInfoContentStyle(implicit appContext: AppContext) = vMatchWidth + llVertical + vPaddings(8 dp) +
      vBackgroundColorResource(R.color.background_list_schedule_info)

  val itemSpeakerContentStyle = vMatchWidth + llHorizontal

  def roomItemStyle(implicit appContext: AppContext) = vWrapContent + tvSize(12) +
      tvColorResource(R.color.text_schedule_room)

  def nameItemStyle(implicit appContext: AppContext) = vWrapContent + tvSize(14) +
      tvColorResource(R.color.text_schedule_name)

  def speakerNameItemStyle(implicit appContext: AppContext) = vWrapContent + tvSize(14) +
      vPadding(0, 0, 4 dp, 0) + tvColorResource(R.color.text_schedule_name)

  def speakerTwitterItemStyle(implicit appContext: AppContext) = vWrapContent + tvSize(14) +
      tvColorResource(R.color.text_schedule_twitter)

}
