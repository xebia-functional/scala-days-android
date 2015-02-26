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
import android.view.View.OnClickListener
import android.view.{View, ViewGroup}
import com.fortysevendeg.android.scaladays.R
import com.fortysevendeg.android.scaladays.model.{Picture, Information}
import com.fortysevendeg.android.scaladays.ui.commons.AsyncImageTweaks._
import com.fortysevendeg.macroid.extras.ImageViewTweaks._
import com.fortysevendeg.macroid.extras.TextTweaks._
import macroid.{ActivityContext, AppContext}
import macroid.FullDsl._

class ConferenceMenuAdapter(listener: ConferenceMenuClickListener)
    (implicit context: ActivityContext, appContext: AppContext)
    extends RecyclerView.Adapter[ViewHolderConferenceMenuAdapter] {

  val recyclerClickListener = listener

  var list = Seq.empty[ConferenceMenuItem]
  
  def findIconImage(pictures: Seq[Picture]): Option[String] =
    pictures.headOption map (_.url)

  override def onCreateViewHolder(parentViewGroup: ViewGroup, i: Int): ViewHolderConferenceMenuAdapter = {
    val adapter = new ConferenceMenuAdapterLayout()
    adapter.content.setOnClickListener(new OnClickListener {
      override def onClick(v: View): Unit = recyclerClickListener.onClick(list(v.getTag.asInstanceOf[Int]))
    })
    new ViewHolderConferenceMenuAdapter(adapter)
  }

  override def getItemCount: Int = list.size

  override def onBindViewHolder(viewHolder: ViewHolderConferenceMenuAdapter, position: Int): Unit = {
    val conferenceMenuItem = list(position)
    val iconSize = appContext.get.getResources.getDimensionPixelSize(R.dimen.size_menu_conference_icon)
    viewHolder.content.setTag(position)
    runUi(
      (viewHolder.title <~ tvText(conferenceMenuItem.name)) ~
        (viewHolder.icon <~ conferenceMenuItem.icon.map(roundedImage(_, R.drawable.placeholder_circle, iconSize)).getOrElse(ivSrc(R.drawable.placeholder_circle)))
    )
  }
  
  def loadConferences(conferences: Seq[Information]) = {
    list = conferences map { conference =>
      ConferenceMenuItem(conference.id, conference.longName, findIconImage(conference.pictures), conference)
    }
    notifyDataSetChanged()
  }
  
}

trait ConferenceMenuClickListener {
  def onClick(conferenceMenuItem: ConferenceMenuItem)
}

case class ConferenceMenuItem(id: Int, name: String, icon: Option[String], information: Information)
