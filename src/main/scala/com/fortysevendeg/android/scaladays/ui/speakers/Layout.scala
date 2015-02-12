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
import com.fortysevendeg.android.scaladays.R
import com.fortysevendeg.android.scaladays.ui.commons.PlaceHolderFailedLayout
import macroid.{ActivityContext, AppContext}
import macroid.FullDsl._

class Layout(implicit appContext: AppContext, context: ActivityContext)
    extends FragmentStyles
    with PlaceHolderFailedLayout {

  var recyclerView = slot[RecyclerView]

  var progressBar = slot[ProgressBar]

  var failedContent = slot[LinearLayout]

  val content = getUi(
    l[FrameLayout](
      w[ProgressBar] <~ wire(progressBar) <~ progressBarStyle,
      w[RecyclerView] <~ wire(recyclerView) <~ recyclerViewStyle,
      placeholderFailed(R.string.generalMessageError) <~ wire(failedContent)
    ) <~ rootStyle
  )

  def layout = content

}

class SpeakersLayoutAdapter(implicit context: ActivityContext, appContext: AppContext)
    extends AdapterStyles {

  var avatar = slot[ImageView]

  var name = slot[TextView]

  var twitter = slot[TextView]

  var bio = slot[TextView]

  val content = layout

  private def layout(implicit appContext: AppContext, context: ActivityContext) = getUi(
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

class ViewHolderSpeakersAdapter(adapter: SpeakersLayoutAdapter)(implicit context: ActivityContext, appContext: AppContext)
    extends RecyclerView.ViewHolder(adapter.content) {

  val content = adapter.content

  val avatar = adapter.avatar

  val name = adapter.name

  val twitter = adapter.twitter

  val bio = adapter.bio

}
