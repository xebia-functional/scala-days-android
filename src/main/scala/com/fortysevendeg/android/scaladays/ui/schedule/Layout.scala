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
import android.widget._
import macroid.FullDsl._
import macroid.{ActivityContext, AppContext}

class Layout(implicit appContext: AppContext, context: ActivityContext)
    extends Styles {

  var recyclerView = slot[RecyclerView]

  var progressBar = slot[ProgressBar]

  val content = getUi(
    l[FrameLayout](
      w[ProgressBar] <~ wire(progressBar) <~ progressBarStyle,
      w[RecyclerView] <~ wire(recyclerView) <~ recyclerViewStyle
    ) <~ rootStyle
  )

  def layout = content

}

class ScheduleLayoutAdapter(implicit context: ActivityContext, appContext: AppContext)
    extends Styles {

  var hour = slot[TextView]

  var room = slot[TextView]

  var name = slot[TextView]

  var speakerContent = slot[LinearLayout]

  var speakerName = slot[TextView]

  var speakerTwitter = slot[TextView]

  val content = layout

  private def layout(implicit appContext: AppContext, context: ActivityContext) = getUi(
    l[LinearLayout](
      w[TextView] <~ wire(hour) <~ hourStyle,
      l[LinearLayout](
        w[TextView] <~ wire(room) <~ roomItemStyle,
        w[TextView] <~ wire(name) <~ nameItemStyle,
        l[LinearLayout](
          w[TextView] <~ wire(speakerName) <~ speakerNameItemStyle,
          w[TextView] <~ wire(speakerTwitter) <~ speakerTwitterItemStyle
        ) <~ itemSpeakerContentStyle <~ wire(speakerContent)
      ) <~ itemInfoContentStyle
    ) <~ itemContentStyle
  )
}

class ViewHolderSpeakersAdapter(adapter: ScheduleLayoutAdapter)(implicit context: ActivityContext, appContext: AppContext)
    extends RecyclerView.ViewHolder(adapter.content) {

  var content = adapter.content

  var hour = adapter.hour

  var room = adapter.room

  var name = adapter.name

  var speakerContent = adapter.speakerContent

  var speakerName = adapter.speakerName

  var speakerTwitter = adapter.speakerTwitter

}
