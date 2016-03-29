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
import com.fortysevendeg.android.scaladays.R
import com.fortysevendeg.android.scaladays.model.Event
import com.fortysevendeg.android.scaladays.modules.ComponentRegistryImpl
import com.fortysevendeg.android.scaladays.modules.preferences.PreferenceRequest
import com.fortysevendeg.android.scaladays.ui.commons.DateTimeTextViewTweaks._
import com.fortysevendeg.android.scaladays.ui.commons._
import com.fortysevendeg.android.scaladays.ui.schedule.ScheduleAdapter._
import com.fortysevendeg.macroid.extras.TextTweaks._
import com.fortysevendeg.macroid.extras.ViewGroupTweaks._
import com.fortysevendeg.macroid.extras.ViewTweaks._
import com.fortysevendeg.macroid.extras.ImageViewTweaks._
import macroid.FullDsl._
import macroid.{Ui, ActivityContextWrapper, ContextWrapper}
import org.joda.time.{DateTimeZone, DateTime}

case class ScheduleAdapter(conferenceId: Int, timeZone: String, scheduleItems: Seq[ScheduleItem], listener: RecyclerClickListener)
    (implicit context: ActivityContextWrapper)
    extends RecyclerView.Adapter[RecyclerView.ViewHolder]
    with ComponentRegistryImpl
    with UiServices {

  override val contextProvider: ContextWrapper = context

  val recyclerClickListener = listener

  val dateTimeZone = DateTimeZone.forID(timeZone)

  override def onCreateViewHolder(parentViewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder = {
    viewType match {
      case `itemViewTypeTalk` =>
        val adapter = new ScheduleLayoutAdapter()
        adapter.content.setOnClickListener(new OnClickListener {
          override def onClick(v: View): Unit =
            recyclerClickListener.onClick(scheduleItems(v.getTag.asInstanceOf[Int]))
        })
        new ViewHolderScheduleAdapter(adapter)
      case `itemViewTypeHeader` =>
        val adapter = new HeaderLayoutAdapter()
        adapter.content.setOnClickListener(new OnClickListener {
          override def onClick(v: View): Unit =
            recyclerClickListener.onClick(scheduleItems(v.getTag.asInstanceOf[Int]))
        })
        new ViewHolderHeaderAdapter(adapter)
    }
  }

  override def getItemCount: Int = scheduleItems.size

  override def onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int): Unit = {
    val scheduleItem = scheduleItems(position)
    getItemViewType(position) match {
      case `itemViewTypeTalk` =>
        val vh = viewHolder.asInstanceOf[ViewHolderScheduleAdapter]
        scheduleItem.event map {
          event =>
            vh.content.setTag(position)
            if (event.speakers.isEmpty) {
              runUi(vh.speakerContent <~ vGone)
            } else {
              runUi(vh.speakerContent <~ vVisible <~ vgRemoveAllViews)
              event.speakers.map(
                speaker => {
                  val speakerLayout = new SpeakersLayout(speaker)
                  runUi(vh.speakerContent <~ vgAddView(speakerLayout.content))
                }
              )
            }
            val isFavorite = preferenceServices.fetchBooleanPreference(PreferenceRequest[Boolean](
              getNamePreferenceFavorite(event.id), false)).value

            val voteValue = preferenceServices.fetchStringPreference(PreferenceRequest[String](
              ScheduleFragment.getPreferenceKeyForVote(conferenceId, event.id), null)).value

            val vote = Option(voteValue) map Vote.apply

            val currentEvent = event.isCurrentEvent

            val canVote = event.canVote(dateTimeZone) && event.eventType == 2

            val voteUi = if (canVote) {
              vote map { v =>
                (vh.vote <~ vVisible <~ vAlpha(1f) <~ (v match {
                  case VoteLike => ivSrc(R.drawable.list_icon_vote_like)
                  case VoteUnlike => ivSrc(R.drawable.list_icon_vote_unlike)
                  case VoteNeutral => ivSrc(R.drawable.list_icon_vote_neutral)
                })) ~
                  (vh.voteAction <~ vGone) ~
                  (vh.hourContent <~ On.click(Ui(listener.onVoteClick(event))))
              } getOrElse {
                (vh.vote <~ vGone) ~
                  (vh.voteAction <~ vVisible) ~
                  (vh.hourContent <~ On.click(Ui(listener.onVoteClick(event))))
              }
            } else {
              vote map { v =>
                (vh.vote <~ vVisible <~ vAlpha(.5f) <~ (v match {
                  case VoteLike => ivSrc(R.drawable.list_icon_vote_like)
                  case VoteUnlike => ivSrc(R.drawable.list_icon_vote_unlike)
                  case VoteNeutral => ivSrc(R.drawable.list_icon_vote_neutral)
                })) ~
                  (vh.voteAction <~ vGone) ~
                  (vh.hourContent <~ On.click(Ui.nop))
              } getOrElse {
                (vh.vote <~ vGone) ~ (vh.voteAction <~ vGone) ~ (vh.hourContent <~ On.click(Ui.nop))
              }
            }

            runUi(
              (vh.hourContent <~
                vBackgroundColorResource(if (currentEvent) R.color.background_list_schedule_hour_current_event else R.color.background_list_schedule_hour)) ~
                (vh.hour <~
                  tvDateTimeHourMinute(event.startTime, timeZone)) ~
                (vh.name <~ tvText(event.title)) ~
                (vh.track <~ (event.track map (
                  track => tvText(track.name) + vVisible)
                  getOrElse vGone)) ~
                (vh.room <~ (event.location map (
                  location => tvText(context.application.getString(R.string.roomName, location.name)) + vVisible)
                  getOrElse vGone)) ~
                (vh.tagFavorite <~ (if (isFavorite) vVisible else vGone)) ~
                voteUi
            )
        }
      case `itemViewTypeHeader` =>
        val vh = viewHolder.asInstanceOf[ViewHolderHeaderAdapter]
        runUi(
          vh.headerName <~ (scheduleItem.header map (tvText(_) + vVisible) getOrElse vGone)
        )
    }
  }

  override def getItemViewType(position: Int): Int = if (scheduleItems(position).isHeader) itemViewTypeHeader else itemViewTypeTalk

}

object ScheduleAdapter {
  val itemViewTypeHeader = 0
  val itemViewTypeTalk = 1
}

trait RecyclerClickListener {
  def onClick(scheduleItem: ScheduleItem): Unit
  def onVoteClick(event: Event): Unit
}

