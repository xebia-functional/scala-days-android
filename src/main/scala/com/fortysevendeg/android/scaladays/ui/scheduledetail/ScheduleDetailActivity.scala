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
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.fortysevendeg.android.scaladays.model.Event
import com.fortysevendeg.android.scaladays.modules.ComponentRegistryImpl
import com.fortysevendeg.android.scaladays.ui.commons.AnalyticStrings._
import com.fortysevendeg.android.scaladays.ui.commons.UiServices
import macroid.{ContextWrapper, Contexts}

class ScheduleDetailActivity
  extends AppCompatActivity
  with Contexts[FragmentActivity]
  with ComponentRegistryImpl
  with UiServices
  with Layout {

  override lazy val contextProvider: ContextWrapper = activityContextWrapper

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
        analyticsServices.sendScreenName(analyticsScheduleDetailScreen)
        setContentView(layout(event, timeZone))
        toolBar foreach setSupportActionBar
        getSupportActionBar.setDisplayHomeAsUpEnabled(true)
      }) getOrElse finish()
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
