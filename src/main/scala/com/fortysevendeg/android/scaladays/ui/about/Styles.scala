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

package com.fortysevendeg.android.scaladays.ui.about

import android.view.Gravity
import android.widget.{TextView, ImageView, ScrollView, LinearLayout}
import com.fortysevendeg.android.scaladays.R
import com.fortysevendeg.macroid.extras.ImageViewTweaks._
import com.fortysevendeg.macroid.extras.LinearLayoutTweaks._
import com.fortysevendeg.macroid.extras.TextTweaks._
import com.fortysevendeg.macroid.extras.ViewTweaks._
import macroid.{Tweak, AppContext}
import macroid.FullDsl._

import scala.language.postfixOps

trait FragmentStyles {

  def rootStyle(implicit appContext: AppContext): Tweak[LinearLayout] =
    vMatchParent +
      llVertical +
      vPaddings(16 dp)

  val scrollStyle: Tweak[ScrollView] = llMatchWeightVertical

  val contentStyle: Tweak[LinearLayout] =
    vMatchWidth +
      llVertical

  val about47ContentStyle: Tweak[LinearLayout] =
    vMatchWidth +
      llHorizontal +
      llGravity(Gravity.CENTER)

  def about47ImageStyle(implicit appContext: AppContext): Tweak[ImageView] =
    vWrapContent +
      ivSrc(R.drawable.logo_47deg) +
      vPadding(0, 0, 8 dp, 0)

  def about47TextStyle(implicit appContext: AppContext): Tweak[TextView] =
    vWrapContent +
      tvText(R.string.about47) +
      tvColorResource(R.color.primary) +
      tvSize(12)

  def titleStyle(implicit appContext: AppContext): Tweak[TextView] =
    vMatchParent +
      tvText(R.string.codeOfConduct) +
      tvColorResource(R.color.accent) +
      tvSize(16)

  def descriptionStyle(implicit appContext: AppContext): Tweak[TextView] =
    vWrapContent +
      tvColorResource(R.color.primary) +
      tvSize(14) +
      vPadding(0, 16 dp, 0, 0)

}
