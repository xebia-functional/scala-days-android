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

import android.app.AlertDialog
import android.content.DialogInterface.OnClickListener
import android.content.{DialogInterface, Intent}
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view._
import com.fortysevendeg.android.scaladays.R
import com.fortysevendeg.android.scaladays.model.Event
import com.fortysevendeg.android.scaladays.modules.ComponentRegistryImpl
import com.fortysevendeg.android.scaladays.modules.preferences.PreferenceRequest
import com.fortysevendeg.android.scaladays.ui.commons.UiServices
import com.fortysevendeg.android.scaladays.ui.scheduledetail.ScheduleDetailActivity
import com.fortysevendeg.macroid.extras.RecyclerViewTweaks._
import com.fortysevendeg.macroid.extras.ViewTweaks._
import macroid.FullDsl._
import macroid.{AppContext, Contexts, Ui}

import scala.concurrent.ExecutionContext.Implicits.global

class ScheduleFragment
  extends Fragment
  with Contexts[Fragment]
  with ComponentRegistryImpl
  with UiServices
  with ScheduleConversion {

  override implicit lazy val appContextProvider: AppContext = fragmentAppContext

  private var fragmentLayout: Option[Layout] = None

  override def onCreate(savedInstanceState: Bundle): Unit = {
    super.onCreate(savedInstanceState)
    setHasOptionsMenu(true)
  }

  override def onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle): View = {
    val fLayout = new Layout
    fragmentLayout = Some(fLayout)
    runUi(
      (fLayout.recyclerView
        <~ rvLayoutManager(new LinearLayoutManager(appContextProvider.get))
        <~ rvAddItemDecoration(new ScheduleItemDecorator())) ~
        (fLayout.reloadButton <~ On.click(Ui {
          loadSchedule()
        })))
    fLayout.content
  }

  override def onViewCreated(view: View, savedInstanceState: Bundle): Unit = {
    super.onViewCreated(view, savedInstanceState)
    loadSchedule()
  }

  override def onCreateOptionsMenu(menu: Menu, inflater: MenuInflater): Unit = {
    inflater.inflate(R.menu.schedule_menu, menu)
    super.onCreateOptionsMenu(menu, inflater)
  }
  
  override def onOptionsItemSelected(item: MenuItem): Boolean = {
    item.getItemId match {
      case R.id.action_filter =>
        new AlertDialog.Builder(getActivity)
          .setCancelable(true)
          .setItems(R.array.filter_menu, new OnClickListener() {
          override def onClick(dialog: DialogInterface, which: Int): Unit = {
            which match {
              case 0 => loadSchedule()
              case 1 => loadSchedule(true)
            }
          }
        }).create().show()
        true
      case _ => super.onOptionsItemSelected(item)
    }
  }
  
  def loadSchedule(favorites: Boolean = false): Unit = {
    loading()
    val result = for {
      conference <- loadSelectedConference()
    } yield reloadList(conference.info.utcTimezoneOffset, conference.schedule, favorites)

    result recover {
      case _ => failed()
    }
  }

  def reloadList(timeZone: String, events: Seq[Event], favorites: Boolean = false) = {
    events.length match {
      case 0 => empty()
      case _ =>
        val scheduleItems = toScheduleItem(timeZone, events,
          if (favorites) {
            event => {
              val namePreferenceFavorite = "%d_%d".format(loadSelectedConferenceId, event.id)
              preferenceServices.fetchBooleanPreference(PreferenceRequest[Boolean](namePreferenceFavorite, false)).value
            }
          } else {
            event => true
          })
        fragmentLayout map {
          layout =>
            val adapter = new ScheduleAdapter(timeZone, scheduleItems, new RecyclerClickListener {
              override def onClick(scheduleItem: ScheduleItem): Unit = {
                if (!scheduleItem.isHeader) {
                  scheduleItem.event map {
                    event =>
                      if (event.eventType == 1 || event.eventType == 2) {
                        val intent = new Intent(fragmentActivityContext.get, classOf[ScheduleDetailActivity])
                        intent.putExtra(ScheduleDetailActivity.scheduleItemKey, event)
                        intent.putExtra(ScheduleDetailActivity.timeZoneKey, timeZone)
                        fragmentActivityContext.get.startActivity(intent)
                      }
                  }
                }
              }
            })
            runUi(
              (layout.progressBar <~ vGone) ~
                (layout.placeholderContent <~ vGone) ~
                (layout.recyclerView <~ vVisible) ~
                (layout.recyclerView <~ rvAdapter(adapter))
            )
        }
    }
  }

  def loading() = {
    fragmentLayout map {
      layout =>
        runUi(
          (layout.progressBar <~ vVisible) ~
            (layout.recyclerView <~ vGone) ~
            (layout.placeholderContent <~ vGone))
    }
  }

  def failed() = {
    fragmentLayout map {
      layout =>
        layout.loadFailed()
        runUi(
          (layout.progressBar <~ vGone) ~
            (layout.recyclerView <~ vGone) ~
            (layout.placeholderContent <~ vVisible))
    }
  }

  def empty() = {
    fragmentLayout map {
      layout =>
        layout.loadEmpty()
        runUi(
          (layout.progressBar <~ vGone) ~
            (layout.recyclerView <~ vGone) ~
            (layout.placeholderContent <~ vVisible))
    }
  }

}
