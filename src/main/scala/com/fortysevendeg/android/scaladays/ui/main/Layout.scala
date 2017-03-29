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
import android.widget.{FrameLayout, LinearLayout}
import com.fortysevendeg.android.scaladays.ui.commons.ToolbarLayout
import macroid.FullDsl._
import macroid.{ActivityContextWrapper, IdGenerator, Ui}

trait Layout
    extends ToolbarLayout
    with Styles {

  object Id extends IdGenerator(start = 1000)

  var drawerLayout: Option[DrawerLayout] = slot[DrawerLayout]

  var fragmentMenu: Option[FrameLayout] = slot[FrameLayout]

  var fragmentContent: Option[FrameLayout] = slot[FrameLayout]

  def layout(implicit context: ActivityContextWrapper): DrawerLayout = {
    Ui.get(
      l[DrawerLayout](
        l[LinearLayout](
          toolBarLayout(),
          l[FrameLayout]() <~ wire(fragmentContent) <~ id(Id.mainFragment) <~ fragmentContentStyle
        ) <~ contentStyle,
        l[FrameLayout]() <~ wire(fragmentMenu) <~ id(Id.menuFragment) <~ drawerLayoutStyle
      ) <~ wire(drawerLayout) <~ drawerStyle
    )
  }

}
