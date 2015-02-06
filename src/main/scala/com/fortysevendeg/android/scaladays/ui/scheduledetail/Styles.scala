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
import com.fortysevendeg.android.scaladays.R
import com.fortysevendeg.macroid.extras.LinearLayoutTweaks._
import com.fortysevendeg.macroid.extras.TextTweaks._
import com.fortysevendeg.macroid.extras.ViewTweaks._
import macroid.AppContext
import macroid.FullDsl._

import scala.language.postfixOps

trait Styles {

  val rootStyle = vMatchParent

  def contentStyle(implicit appContext: AppContext) = vMatchParent + llVertical + vPadding(0, 124 dp, 0, 0)

  def toolBarTitleStyle(implicit appContext: AppContext) = vMatchHeight + tvGravity(Gravity.BOTTOM) +
      tvColorResource(R.color.toolbar_title) + tvSize(24) + vPadding(0, 0, 16 dp, 16 dp)

}
