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

package com.fortysevendeg.android.scaladays.ui.scheduledetail

import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v7.app.ActionBarActivity
import android.view.MenuItem
import com.fortysevendeg.android.scaladays.R
import com.fortysevendeg.android.scaladays.model.Event
import com.fortysevendeg.android.scaladays.ui.components.IconTypes
import com.fortysevendeg.macroid.extras.ViewGroupTweaks._
import macroid.Contexts
import macroid.FullDsl._
import com.fortysevendeg.macroid.extras.TextTweaks._
import com.fortysevendeg.android.scaladays.ui.commons.DateTimeTextViewTweaks._
import com.fortysevendeg.android.scaladays.ui.components.PathMorphDrawableTweaks._
import com.fortysevendeg.macroid.extras.ViewTweaks._

class ScheduleDetailActivity
    extends ActionBarActivity
    with Contexts[FragmentActivity]
    with Layout {

  // TODO We should create a PersistentService and save the favorite items in this service
  // For now is only for testing design
  var isFavorite = false

  override def onCreate(savedInstanceState: Bundle) = {
    super.onCreate(savedInstanceState)
    setContentView(layout)

    val (maybeScheduleItem, maybeTimeZone) = Option(getIntent.getExtras).map {
      extras =>
        val scheduleItem: Option[Event] = if (extras.containsKey(ScheduleDetailActivity.scheduleItemKey))
          Some(extras.getSerializable(ScheduleDetailActivity.scheduleItemKey).asInstanceOf[Event])
        else None
        val timeZone: Option[String] = if (extras.containsKey(ScheduleDetailActivity.timeZoneKey))
          Some(extras.getString(ScheduleDetailActivity.timeZoneKey))
        else None
        (scheduleItem, timeZone)
    }.getOrElse(None, None)

    toolBar map setSupportActionBar
    getSupportActionBar.setDisplayHomeAsUpEnabled(true)

    (for {
      event <- maybeScheduleItem
      timeZone <- maybeTimeZone
    } yield {
      runUi(
        (titleToolbar <~ tvText(event.title)) ~
            (date <~ tvDateDateTime(event.startTime, timeZone)) ~
            (room <~ event.track.map(track => tvText(track.name) + vVisible).getOrElse(vGone)) ~
            (description <~ tvText(event.description))
      )
      if (event.speakers.size == 0) {
        runUi(speakersContent <~ vGone)
      } else {
        runUi(speakersContent <~ vVisible <~ vgRemoveAllViews)
        event.speakers.map(
          speaker => {
            val speakerLayout = new SpeakersDetailLayout(speaker)
            runUi((speakersContent <~ vgAddView(speakerLayout.content)))
          }
        )
      }
    }).getOrElse(finish())

    runUi(fabFavorite <~ On.click{
      if (isFavorite) {
        isFavorite = false
        fabFavorite <~ pmdAnimIcon(IconTypes.ADD) <~ vBackground(R.drawable.fab_button_no_check)
      } else {
        isFavorite = true
        fabFavorite <~ pmdAnimIcon(IconTypes.CHECK) <~ vBackground(R.drawable.fab_button_check)
      }
    })

  }

  override def onOptionsItemSelected(item: MenuItem): Boolean = {
    item.getItemId match {
      case android.R.id.home =>
        finish()
        true
      case _ => super.onOptionsItemSelected(item)
    }
  }

}

object ScheduleDetailActivity {
  val scheduleItemKey = "schedule_item_key"
  val timeZoneKey = "time_zone_key"
}
