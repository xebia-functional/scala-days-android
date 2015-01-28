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
import android.support.v7.widget.RecyclerView
import android.widget.{FrameLayout, LinearLayout, TextView}
import com.fortysevendeg.android.scaladays.ui.commons.ToolbarLayout
import com.fortysevendeg.android.scaladays.ui.main.Styles._
import macroid.FullDsl._
import macroid.{IdGeneration, ActivityContext, AppContext}

trait Layout 
  extends ToolbarLayout
  with IdGeneration {
  
  var drawerLayout = slot[DrawerLayout]

  var drawerMenuLayout = slot[LinearLayout]

  var fragmentContent = slot[FrameLayout]

  var recyclerView = slot[RecyclerView]

  def layout(implicit appContext: AppContext, context: ActivityContext) = {
    getUi(
      l[DrawerLayout](
        l[LinearLayout](
          toolBarLayout,
          l[FrameLayout]() <~ wire(fragmentContent) <~ id(Id.mainFragment) <~ fragmentContentStyle
        ) <~ contentStyle,
        l[LinearLayout](
          l[FrameLayout]() <~ imageMenuStyle,
          w[RecyclerView] <~ wire(recyclerView) <~ drawerMenuStyle
        ) <~ wire(drawerMenuLayout) <~ drawerLayoutStyle
      ) <~ wire(drawerLayout) <~ drawerStyle
    )
  }

}

class Adapter(implicit appContext: AppContext, context: ActivityContext) {

  var menuItem = slot[TextView]

  val content = {
    getUi(
      w[TextView] <~ wire(menuItem) <~ menuItemStyle
    )
  }

  def layout = content
}

class ScheduleLayout(implicit appContext: AppContext, context: ActivityContext) {

  var textView = slot[TextView]

  val content = getUi(
    l[LinearLayout](
      w[TextView] <~ wire(textView) <~ sampleTextStyle
    ) <~ sampleStyle
  )

  def layout = content

}
