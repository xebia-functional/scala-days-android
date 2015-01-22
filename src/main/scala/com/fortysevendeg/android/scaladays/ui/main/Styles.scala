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

import android.graphics.Color
import android.support.v4.widget.DrawerLayout
import android.support.v7.widget.RecyclerView
import android.view.{Gravity, View}
import android.view.ViewGroup.LayoutParams._
import android.widget.{LinearLayout, TextView, AbsListView}
import com.fortysevendeg.android.scaladays.R
import com.fortysevendeg.macroid.extras.LinearLayoutTweaks._
import com.fortysevendeg.macroid.extras.TextTweaks._
import com.fortysevendeg.macroid.extras.ViewTweaks._
import macroid.{Tweak, AppContext}
import macroid.FullDsl._

import scala.language.postfixOps

object Styles {

  val drawerStyle = vMatchParent

  val contentStyle = vMatchParent + llVertical

  val fragmentContentStyle = vMatchParent

  def drawerMenuStyle(implicit appContext: AppContext) = 
    lp[AbsListView](280 dp, MATCH_PARENT) +
    DrawerLayoutTweaks.dlLayoutGravity(Gravity.START) +
    vBackgroundColor(appContext.app.getResources.getColor(R.color.menuBackground))

  def menuItemStyle(implicit appContext: AppContext) = 
    lp[AbsListView](MATCH_PARENT, 50 dp) + 
    tvSize(18) + 
    tvColor(Color.WHITE) +
    tvGravity(Gravity.CENTER_VERTICAL) + 
    vPaddings(10 dp) +
    TextTweaksExtra.tvDrawablePadding(10 dp)

}

object RecyclerViewTweaksExtras {
  type W = RecyclerView
  
  def rvFitsSystemWindows(fitSystemWindows: Boolean) = Tweak[View] { view =>
    Tweak[W](_.setFitsSystemWindows(fitSystemWindows))
  }
}

object LinearLayoutTweaksExtra {
  type W = LinearLayout

  def llFitsSystemWindows(fitSystemWindows: Boolean) = Tweak[View] { view =>
    Tweak[W](_.setFitsSystemWindows(fitSystemWindows))
  }

  def llPadding(left: Int, top: Int, right: Int, bottom: Int) = Tweak[View] { view =>
    Tweak[W](_.setPadding(left, top, right, bottom))
  }
}

object DrawerLayoutTweaks {
  type W = DrawerLayout

  def dlLayoutGravity(gravity: Int) = Tweak[View] { view =>
    val param = new DrawerLayout.LayoutParams(view.getLayoutParams.width, view.getLayoutParams.height)
    param.gravity = gravity
    view.setLayoutParams(param)
  }

  def dlFitsSystemWindows(fitSystemWindows: Boolean) = Tweak[View] { view =>
    Tweak[W](_.setFitsSystemWindows(fitSystemWindows))
  }
}

object TextTweaksExtra {
  type W = TextView
  
  def tvDrawablePadding(padding: Int) = Tweak[W](_.setCompoundDrawablePadding(padding))
}