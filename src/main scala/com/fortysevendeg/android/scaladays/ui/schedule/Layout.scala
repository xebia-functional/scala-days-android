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
import com.fortysevendeg.android.scaladays.model.Speaker
import macroid.FullDsl._
import macroid.ActivityContextWrapper

class SpeakersLayout(speaker: Speaker)(implicit context: ActivityContextWrapper)
  extends SpeakersLayoutStyles {

  val content = layout

  var avatar = slot[ImageView]

  var speakerName = slot[TextView]

  var speakerTwitter = slot[TextView]

  private def layout(implicit context: ActivityContextWrapper) = getUi(
    l[LinearLayout](
      l[FrameLayout](
        w[ImageView] <~ wire(avatar) <~ avatarStyle(speaker.picture)
      ) <~ backgroundAvatar,
      l[LinearLayout](
        w[TextView] <~ wire(speakerName) <~ speakerNameItemStyle(speaker.name),
        w[TextView] <~ wire(speakerTwitter) <~ speakerTwitterItemStyle(speaker.twitter)
      ) <~ itemSpeakerContentNamesStyle
    ) <~ itemSpeakerContentStyle
  )

}

class ScheduleLayoutAdapter(implicit context: ActivityContextWrapper)
  extends AdapterStyles {

  var hour = slot[TextView]

  var room = slot[TextView]

  var name = slot[TextView]

  var track = slot[TextView]

  var tagFavorite = slot[ImageView]

  var speakerContent = slot[LinearLayout]

  val content = layout

  private def layout(implicit context: ActivityContextWrapper) = getUi(
    l[FrameLayout](
      l[LinearLayout](
        w[TextView] <~ wire(hour) <~ hourStyle,
        l[LinearLayout](
          w[TextView] <~ wire(track) <~ trackItemStyle,
          w[TextView] <~ wire(room) <~ roomItemStyle,
          w[TextView] <~ wire(name) <~ nameItemStyle,
          l[LinearLayout]() <~ itemSpeakersContentStyle <~ wire(speakerContent)
        ) <~ itemInfoContentStyle
      ) <~ itemContentStyle,
      w[ImageView] <~ wire(tagFavorite) <~ tagFavoriteStyle
    ) <~ itemRootContentStyle
  )
}

class ViewHolderScheduleAdapter(adapter: ScheduleLayoutAdapter)(implicit context: ActivityContextWrapper)
  extends RecyclerView.ViewHolder(adapter.content) {

  val content = adapter.content

  val hour = adapter.hour

  val room = adapter.room

  val name = adapter.name

  val track = adapter.track

  val tagFavorite = adapter.tagFavorite

  val speakerContent = adapter.speakerContent

}
