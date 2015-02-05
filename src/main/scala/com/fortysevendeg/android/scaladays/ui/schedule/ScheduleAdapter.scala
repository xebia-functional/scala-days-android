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
import android.view.View.OnClickListener
import android.view.{View, ViewGroup}
import com.fortysevendeg.android.scaladays.model.Event
import com.fortysevendeg.android.scaladays.ui.commons.DateTimeTextViewTweaks._
import com.fortysevendeg.macroid.extras.TextTweaks._
import com.fortysevendeg.macroid.extras.ViewTweaks._
import macroid.FullDsl._
import macroid.{ActivityContext, AppContext}

class ScheduleAdapter(events: Seq[Event], listener: RecyclerClickListener)
    (implicit context: ActivityContext, appContext: AppContext)
    extends RecyclerView.Adapter[ViewHolderSpeakersAdapter] {

  val recyclerClickListener = listener

  override def onCreateViewHolder(parentViewGroup: ViewGroup, i: Int): ViewHolderSpeakersAdapter = {
    val adapter = new ScheduleLayoutAdapter()
    adapter.content.setOnClickListener(new OnClickListener {
      override def onClick(v: View): Unit = recyclerClickListener.onClick(events(v.getTag.asInstanceOf[Int]))
    })
    new ViewHolderSpeakersAdapter(adapter)
  }

  override def getItemCount: Int = events.size

  override def onBindViewHolder(viewHolder: ViewHolderSpeakersAdapter, position: Int): Unit = {
    val event = events(position)
    viewHolder.content.setTag(position)
    runUi(
      (viewHolder.hour <~ tvDateTimeText(event.startTime)) ~
          (viewHolder.name <~ tvText(event.title)) ~
          (viewHolder.speakerContent <~ vGone) ~
          (viewHolder.room <~ event.track.map(track => tvText(track.name) + vVisible).getOrElse(vGone))
    )
  }
  override def getItemViewType(position: Int): Int = {
    super.getItemViewType(position)
  }
}

object ScheduleAdapter {
  val itemViewTypeHeader = 0
  val itemViewTypeTalk = 0
}

trait RecyclerClickListener {
  def onClick(event: Event)
}

