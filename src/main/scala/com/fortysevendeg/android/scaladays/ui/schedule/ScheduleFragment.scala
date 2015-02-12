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

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.{LayoutInflater, View, ViewGroup}
import com.fortysevendeg.android.scaladays.model.{Root, Event}
import com.fortysevendeg.android.scaladays.modules.ComponentRegistryImpl
import com.fortysevendeg.android.scaladays.modules.json.JsonRequest
import com.fortysevendeg.android.scaladays.modules.json.models.ApiRoot
import com.fortysevendeg.android.scaladays.modules.net.NetRequest
import com.fortysevendeg.android.scaladays.ui.commons.UiServices
import com.fortysevendeg.android.scaladays.ui.scheduledetail.ScheduleDetailActivity
import com.fortysevendeg.macroid.extras.ActionsExtras._
import com.fortysevendeg.macroid.extras.RecyclerViewTweaks._
import com.fortysevendeg.macroid.extras.ViewTweaks._
import macroid.FullDsl._
import macroid.{Ui, AppContext, Contexts}

import scala.concurrent.ExecutionContext.Implicits.global

class ScheduleFragment
    extends Fragment
    with Contexts[Fragment]
    with ComponentRegistryImpl
    with UiServices {

  override implicit lazy val appContextProvider: AppContext = fragmentAppContext

  private var fragmentLayout: Option[Layout] = None

  override def onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle): View = {
    val fLayout = new Layout
    fragmentLayout = Some(fLayout)
    runUi(
      (fLayout.recyclerView
          <~ rvLayoutManager(new LinearLayoutManager(appContextProvider.get))
          <~ rvAddItemDecoration(new ScheduleItemDecorator())) ~
          (fLayout.reloadButton <~ On.click(Ui {
            // TODO reload
          })))
    fLayout.content
  }

  override def onViewCreated(view: View, savedInstanceState: Bundle): Unit = {
    super.onViewCreated(view, savedInstanceState)
    val result = for {
      conference <- loadSelectedConference()
    } yield reloadList(conference.info.utcTimezoneOffset, conference.schedule)

    result.recover {
      case _ => failed()
    }
  }

  def failed() = {
    fragmentLayout map {
      layout =>
        runUi(
          (layout.progressBar <~ vGone) ~
              (layout.recyclerView <~ vGone) ~
              (layout.failedContent <~ vVisible))
    }
  }

  def reloadList(timeZone: String, events: Seq[Event]) = {
    val scheduleItems = ScheduleConversion.toScheduleItem(timeZone, events)
    for {
      layout <- fragmentLayout
      recyclerView <- layout.recyclerView
    } yield {
      val adapter = new ScheduleAdapter(timeZone, scheduleItems, new RecyclerClickListener {
        override def onClick(scheduleItem: ScheduleItem): Unit = {
          if (!scheduleItem.isHeader) {
            scheduleItem.event map {
              event =>
                val intent = new Intent(fragmentActivityContext.get, classOf[ScheduleDetailActivity])
                intent.putExtra(ScheduleDetailActivity.scheduleItemKey, event)
                intent.putExtra(ScheduleDetailActivity.timeZoneKey, timeZone)
                fragmentActivityContext.get.startActivity(intent)
            }
          }
        }
      })
      runUi(
        (layout.progressBar <~ vGone) ~
            (layout.recyclerView <~ rvAdapter(adapter))
      )
    }
  }

}
