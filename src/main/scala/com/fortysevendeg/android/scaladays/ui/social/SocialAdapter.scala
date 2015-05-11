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

package com.fortysevendeg.android.scaladays.ui.social

import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.View.OnClickListener
import android.view.{View, ViewGroup}
import com.fortysevendeg.android.scaladays.R
import com.fortysevendeg.android.scaladays.model.TwitterMessage
import com.fortysevendeg.android.scaladays.ui.commons.DateTimeTextViewTweaks._
import com.fortysevendeg.android.scaladays.ui.commons.AsyncImageTweaks._
import com.fortysevendeg.macroid.extras.TextTweaks._
import macroid.FullDsl._
import macroid.ActivityContextWrapper

class SocialAdapter(messages: Seq[TwitterMessage], listener: RecyclerClickListener)
    (implicit context: ActivityContextWrapper)
    extends RecyclerView.Adapter[ViewHolderSocialAdapter] {

  val recyclerClickListener = listener

  override def onCreateViewHolder(parentViewGroup: ViewGroup, i: Int): ViewHolderSocialAdapter = {
    val adapter = new SocialLayoutAdapter()
    adapter.content.setOnClickListener(new OnClickListener {
      override def onClick(v: View): Unit = recyclerClickListener.onClick(messages(v.getTag.asInstanceOf[Int]))
    })
    new ViewHolderSocialAdapter(adapter)
  }

  override def getItemCount: Int = messages.size

  override def onBindViewHolder(viewHolder: ViewHolderSocialAdapter, position: Int): Unit = {
    val message = messages(position)
    val avatarSize = context.application.getResources.getDimensionPixelSize(R.dimen.size_avatar)
    viewHolder.content.setTag(position)
    runUi(
      (viewHolder.avatar <~ roundedImage(message.avatar, R.drawable.placeholder_circle, avatarSize)) ~
          (viewHolder.name <~ tvText(message.fullName)) ~
          (viewHolder.date <~ tvPrettyTime(message.date)) ~
          (viewHolder.twitter <~ tvText("@%s".format(message.screenName))) ~
          (viewHolder.message <~ tvText(Html.fromHtml(message.message)))
    )
  }
}

trait RecyclerClickListener {
  def onClick(message: TwitterMessage)
}

