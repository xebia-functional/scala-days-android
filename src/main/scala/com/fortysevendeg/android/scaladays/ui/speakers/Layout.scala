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

package com.fortysevendeg.android.scaladays.ui.speakers

import android.support.v7.widget.RecyclerView
import android.widget._
import macroid.FullDsl._
import macroid.{ActivityContextWrapper, Ui}

class SpeakersLayoutAdapter(implicit context: ActivityContextWrapper)
    extends AdapterStyles {

  var avatar: Option[ImageView] = slot[ImageView]

  var name: Option[TextView] = slot[TextView]

  var twitter: Option[TextView] = slot[TextView]

  var bio: Option[TextView] = slot[TextView]

  val content: LinearLayout = layout

  private def layout(implicit context: ActivityContextWrapper) = Ui.get(
    l[LinearLayout](
      w[ImageView] <~ wire(avatar) <~ avatarStyle,
      l[LinearLayout](
        w[TextView] <~ wire(name) <~ nameItemStyle,
        w[TextView] <~ wire(twitter) <~ twitterItemStyle,
        w[TextView] <~ wire(bio) <~ bioItemStyle
      ) <~ itemNoAvatarContentStyle
    ) <~ itemContentStyle
  )
}

class ViewHolderSpeakersAdapter(adapter: SpeakersLayoutAdapter)(implicit context: ActivityContextWrapper)
    extends RecyclerView.ViewHolder(adapter.content) {

  val content: LinearLayout = adapter.content

  val avatar: Option[ImageView] = adapter.avatar

  val name: Option[TextView] = adapter.name

  val twitter: Option[TextView] = adapter.twitter

  val bio: Option[TextView] = adapter.bio

}
