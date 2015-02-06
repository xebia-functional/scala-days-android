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
import com.fortysevendeg.android.scaladays.model.Event
import macroid.Contexts
import macroid.FullDsl._
import com.fortysevendeg.macroid.extras.TextTweaks._
import com.fortysevendeg.macroid.extras.ViewTweaks._

class ScheduleDetailActivity
    extends ActionBarActivity
    with Contexts[FragmentActivity]
    with Layout {

  override def onCreate(savedInstanceState: Bundle) = {
    super.onCreate(savedInstanceState)
    setContentView(layout)

    val scheduleItem = Option(getIntent.getExtras).flatMap {
      extras =>
        if (extras.containsKey(ScheduleDetailActivity.scheduleItem))
          Some(extras.getSerializable(ScheduleDetailActivity.scheduleItem).asInstanceOf[Event]) else None
    }

    toolBar map setSupportActionBar
    getSupportActionBar.setDisplayHomeAsUpEnabled(true)

    runUi(titleToolbar <~ scheduleItem.map(t => tvText(t.title)).getOrElse(vInvisible))

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
  val scheduleItem = "schedule_item"
}
