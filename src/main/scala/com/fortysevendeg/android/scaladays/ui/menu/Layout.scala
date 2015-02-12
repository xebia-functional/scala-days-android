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

package com.fortysevendeg.android.scaladays.ui.menu

import android.support.v7.widget.RecyclerView
import android.widget._
import macroid.FullDsl._
import macroid.{ActivityContext, AppContext}

class Layout(implicit appContext: AppContext, context: ActivityContext)
  extends Styles {

  var drawerMenuLayout = slot[LinearLayout]
  
  var bigImageLayout = slot[FrameLayout]
  
  var bigImage = slot[ImageView]
  
  var conferenceTitle = slot[TextView]
  
  var conferenceSelector = slot[ImageView]

  var recyclerView = slot[RecyclerView]
  
  val content = getUi(
    l[LinearLayout](
      l[FrameLayout](
        w[ImageView] <~ wire(bigImage) <~ bigImageStyle,
        l[LinearLayout](
          w[TextView] <~ wire(conferenceTitle) <~ conferenceTitleStyle,
          w[ImageView] <~ wire(conferenceSelector) <~ conferenceSelectorStyle
        ) <~ bigImageActionLayout
      ) <~ wire(bigImageLayout) <~ bigImageLayoutStyle,
      w[RecyclerView] <~ wire(recyclerView) <~ drawerMenuStyle
    ) <~ menuStyle
  )

  def layout = content
}

class MainMenuAdapterLayout(implicit context: ActivityContext, appContext: AppContext) extends Styles {

  var menuItem = slot[CheckedTextView]

  val content = layout

  private def layout(implicit appContext: AppContext, context: ActivityContext) = getUi(
    w[CheckedTextView] <~ wire(menuItem) <~ mainMenuItemStyle)
}

class ViewHolderMainMenuAdapter(adapterLayout: MainMenuAdapterLayout)(implicit context: ActivityContext, appContext: AppContext)
  extends RecyclerView.ViewHolder(adapterLayout.content) {

  var content = adapterLayout.content

  var title = adapterLayout.menuItem

}

class ConferenceMenuAdapterLayout(implicit context: ActivityContext, appContext: AppContext) extends Styles {

  var menuItem = slot[TextView]
  
  var menuIcon = slot[ImageView]

  val content = layout

  private def layout(implicit appContext: AppContext, context: ActivityContext) = getUi(
    l[LinearLayout](
      w[ImageView] <~ wire(menuIcon) <~ conferenceMenuItemIconStyle,
      w[TextView] <~ wire(menuItem) <~ conferenceMenuItemStyle
    ) <~ conferenceMenuItemLayoutStyle
  )
}

class ViewHolderConferenceMenuAdapter(adapterLayout: ConferenceMenuAdapterLayout)(implicit context: ActivityContext, appContext: AppContext)
  extends RecyclerView.ViewHolder(adapterLayout.content) {

  var content = adapterLayout.content

  var title = adapterLayout.menuItem

  var icon = adapterLayout.menuIcon

}
