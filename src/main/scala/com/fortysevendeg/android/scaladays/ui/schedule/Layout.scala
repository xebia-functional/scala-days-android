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
import macroid.{ActivityContextWrapper, Ui}
import macroid.FullDsl._

class SpeakersLayout(speaker: Speaker)(implicit context: ActivityContextWrapper)
  extends SpeakersLayoutStyles {

  val content: LinearLayout = layout

  var avatar: Option[ImageView] = slot[ImageView]

  var speakerName: Option[TextView] = slot[TextView]

  var speakerTwitter: Option[TextView] = slot[TextView]

  private def layout(implicit context: ActivityContextWrapper) = Ui.get(
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

  var hourContent: Option[LinearLayout] = slot[LinearLayout]

  var voteAction: Option[TextView] = slot[TextView]

  var vote: Option[ImageView] = slot[ImageView]

  var hour: Option[TextView] = slot[TextView]

  var room: Option[TextView] = slot[TextView]

  var name: Option[TextView] = slot[TextView]

  var track: Option[TextView] = slot[TextView]

  var tagFavorite: Option[ImageView] = slot[ImageView]

  var speakerContent: Option[LinearLayout] = slot[LinearLayout]

  val content: FrameLayout = layout

  private def layout(implicit context: ActivityContextWrapper) = Ui.get(
    l[FrameLayout](
      l[LinearLayout](
        l[LinearLayout](
          w[TextView] <~ wire(hour) <~ hourStyle,
          w[TextView] <~ wire(voteAction) <~ voteActionStyle,
          w[ImageView] <~ wire(vote) <~ voteStyle
        ) <~ itemHourContentStyle <~ wire(hourContent),
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

  val content: FrameLayout = adapter.content

  val hourContent: Option[LinearLayout] = adapter.hourContent

  val voteAction: Option[TextView] = adapter.voteAction

  val vote: Option[ImageView] = adapter.vote

  val hour: Option[TextView] = adapter.hour

  val room: Option[TextView] = adapter.room

  val name: Option[TextView] = adapter.name

  val track: Option[TextView] = adapter.track

  val tagFavorite: Option[ImageView] = adapter.tagFavorite

  val speakerContent: Option[LinearLayout] = adapter.speakerContent

}
