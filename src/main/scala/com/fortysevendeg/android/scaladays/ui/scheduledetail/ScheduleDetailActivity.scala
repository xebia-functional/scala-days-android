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

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AppCompatActivity
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.MenuItem
import com.fortysevendeg.android.scaladays.R
import com.fortysevendeg.android.scaladays.model.Event
import com.fortysevendeg.android.scaladays.modules.ComponentRegistryImpl
import com.fortysevendeg.android.scaladays.modules.preferences.PreferenceRequest
import com.fortysevendeg.android.scaladays.ui.commons.AnalyticStrings._
import com.fortysevendeg.android.scaladays.ui.commons.DateTimeTextViewTweaks._
import com.fortysevendeg.android.scaladays.ui.commons.UiServices
import com.fortysevendeg.android.scaladays.ui.components.IconTypes
import com.fortysevendeg.android.scaladays.ui.components.PathMorphDrawableTweaks._
import com.fortysevendeg.macroid.extras.ResourcesExtras._
import com.fortysevendeg.macroid.extras.TextTweaks._
import com.fortysevendeg.macroid.extras.ViewGroupTweaks._
import com.fortysevendeg.macroid.extras.ViewTweaks._
import macroid.FullDsl._
import macroid.{Ui, ContextWrapper, Contexts}

class ScheduleDetailActivity
  extends AppCompatActivity
  with Contexts[FragmentActivity]
  with ComponentRegistryImpl
  with UiServices
  with Layout {

  override lazy val contextProvider: ContextWrapper = activityContextWrapper

  private var favoriteChanged = false

  private var idEvent: Option[Int] = None

  private var isFavoriteEvent: Boolean = false

  override def onCreate(savedInstanceState: Bundle) = {
    super.onCreate(savedInstanceState)

    val (maybeScheduleItem, maybeTimeZone) = Option(getIntent.getExtras) map {
      extras =>
        val scheduleItem: Option[Event] = if (extras.containsKey(ScheduleDetailActivity.scheduleItemKey))
          Some(extras.getSerializable(ScheduleDetailActivity.scheduleItemKey).asInstanceOf[Event])
        else None
        val timeZone: Option[String] = if (extras.containsKey(ScheduleDetailActivity.timeZoneKey))
          Some(extras.getString(ScheduleDetailActivity.timeZoneKey))
        else None
        (scheduleItem, timeZone)
    } getOrElse ((None, None))

    (for {
      event <- maybeScheduleItem
      timeZone <- maybeTimeZone
    } yield {
      idEvent = Option(event.id)
      analyticsServices.sendScreenName(analyticsScheduleDetailScreen)
      val namePreferenceFavorite = getNamePreferenceFavorite(event.id)
      val isFavorite = preferenceServices.fetchBooleanPreference(PreferenceRequest[Boolean](
        namePreferenceFavorite, false)).value

      setContentView(layout(isFavorite))
      toolBar map setSupportActionBar
      getSupportActionBar.setDisplayHomeAsUpEnabled(true)

      runUi(
        (fabFavorite <~ On.click {
          favoriteClick(event.title, namePreferenceFavorite)
        }) ~
          (titleToolbar <~ tvText(event.title)) ~
          (date <~ tvDateDateTime(event.startTime, timeZone)) ~
          (track <~ (event.track map (track => tvText(track.name) + vVisible) getOrElse vGone)) ~
          (room <~ (event.location map (
            location =>
                vVisible +
                (if (location.mapUrl == "") {
                  tvText(getString(R.string.roomName, location.name))
                } else {
                  val content = new SpannableString(getString(R.string.roomName, location.name))
                  content.setSpan(new UnderlineSpan(), 0, content.length(), 0)
                  tvText(content) + On.click {
                    Ui {
                      val intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(location.mapUrl))
                     startActivity(intent)
                    }
                  }
                })
            )
            getOrElse vGone)) ~
          (description <~ tvText(event.description))
      )
      if (event.speakers.size == 0) {
        runUi(
          (speakersContent <~ vGone) ~
            (speakerTitle <~ vGone))
      } else {
        runUi(speakersContent <~ vVisible <~ vgRemoveAllViews)
        event.speakers.map(
          speaker => {
            val speakerLayout = new SpeakersDetailLayout(speaker)
            runUi(speakersContent <~ vgAddView(speakerLayout.content))
          }
        )
      }
    }) getOrElse finish()

  }

  private def favoriteClick(eventTitle: String, name: String) = {
    favoriteChanged = true
    val isFavorite = preferenceServices.fetchBooleanPreference(PreferenceRequest[Boolean](name, false)).value
    isFavoriteEvent = !isFavorite
    if (isFavorite) {
      analyticsServices.sendEvent(
        screenName = Some(analyticsScheduleDetailScreen),
        category = analyticsCategoryFavorites,
        action = analyticsScheduleActionRemoveToFavorites,
        label = Some(eventTitle))
      preferenceServices.saveBooleanPreference(PreferenceRequest[Boolean](name, false))
      fabFavorite <~ pmdAnimIcon(IconTypes.ADD) <~ vBackground(R.drawable.fab_button_no_check) <~ vPaddings(resGetDimensionPixelSize(R.dimen.padding_schedule_detail_fab))
    } else {
      analyticsServices.sendEvent(
        screenName = Some(analyticsScheduleDetailScreen),
        category = analyticsCategoryFavorites,
        action = analyticsScheduleActionAddToFavorites,
        label = Some(eventTitle))
      preferenceServices.saveBooleanPreference(PreferenceRequest[Boolean](name, true))
      fabFavorite <~ pmdAnimIcon(IconTypes.CHECK) <~ vBackground(R.drawable.fab_button_check) <~ vPaddings(resGetDimensionPixelSize(R.dimen.padding_schedule_detail_fab))
    }
  }

  override def onOptionsItemSelected(item: MenuItem): Boolean = {
    item.getItemId match {
      case android.R.id.home =>
        if (favoriteChanged) setResult(Activity.RESULT_OK)
        finish()
        true
      case _ => super.onOptionsItemSelected(item)
    }
  }

  override def onBackPressed(): Unit = {
    if (favoriteChanged) {
      setResult(Activity.RESULT_OK)
      finish()
    } else {
      super.onBackPressed()
    }
  }
}

object ScheduleDetailActivity {
  val scheduleItemKey = "schedule_item_key"
  val timeZoneKey = "time_zone_key"
}
