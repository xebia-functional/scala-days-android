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
import com.fortysevendeg.android.scaladays.model.{TwitterMessage, Speaker}
import com.fortysevendeg.android.scaladays.ui.commons.GlideTweaks._
import com.fortysevendeg.macroid.extras.ImageViewTweaks._
import com.fortysevendeg.macroid.extras.TextTweaks._
import com.fortysevendeg.macroid.extras.ViewTweaks._
import macroid.FullDsl._
import macroid.{ActivityContext, AppContext}

class SocialAdapter(messages: Seq[TwitterMessage], listener: RecyclerClickListener)
    (implicit context: ActivityContext, appContext: AppContext)
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
    viewHolder.content.setTag(position)
    runUi(
      (viewHolder.avatar <~ glideRoundedImage(message.avatar, R.drawable.placeholder_circle)) ~
          (viewHolder.name <~ tvText(message.fullName)) ~
          (viewHolder.twitter <~ tvText("@%s".format(message.screenName))) ~
          (viewHolder.message <~ tvText(Html.fromHtml(message.message)))
    )
  }
}

trait RecyclerClickListener {
  def onClick(message: TwitterMessage)
}

