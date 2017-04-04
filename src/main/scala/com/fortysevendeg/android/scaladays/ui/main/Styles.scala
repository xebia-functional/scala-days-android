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

package com.fortysevendeg.android.scaladays.ui.main

import android.support.v4.widget.DrawerLayout
import android.view.Gravity
import android.view.ViewGroup.LayoutParams._
import android.widget.{FrameLayout, LinearLayout}
import com.fortysevendeg.android.scaladays.R
import macroid.extras.DrawerLayoutTweaks._
import macroid.extras.LinearLayoutTweaks._
import macroid.extras.ResourcesExtras._
import macroid.extras.ViewTweaks._
import macroid.FullDsl._
import macroid.{ContextWrapper, Tweak}

import scala.language.postfixOps

trait Styles {

  val drawerStyle: Tweak[DrawerLayout] = vMatchParent

  def drawerLayoutStyle(implicit context: ContextWrapper): Tweak[FrameLayout] =
    lp[FrameLayout](resGetDimensionPixelSize(R.dimen.width_drawer), MATCH_PARENT) +
      dlLayoutGravity(Gravity.START)

  val contentStyle: Tweak[LinearLayout] =
    vMatchParent +
      llVertical

  val fragmentContentStyle: Tweak[FrameLayout] =
    vMatchParent
  
}