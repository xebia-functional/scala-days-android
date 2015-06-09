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

import android.app.{Activity, AlertDialog}
import android.content.DialogInterface.OnClickListener
import android.content.{DialogInterface, Intent}
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.{RecyclerView, LinearLayoutManager}
import android.view._
import com.fortysevendeg.android.scaladays.R
import com.fortysevendeg.android.scaladays.model.Event
import com.fortysevendeg.android.scaladays.modules.ComponentRegistryImpl
import com.fortysevendeg.android.scaladays.modules.preferences.PreferenceRequest
import com.fortysevendeg.android.scaladays.ui.commons.{ListLayout, UiServices}
import com.fortysevendeg.android.scaladays.ui.scheduledetail.ScheduleDetailActivity
import com.fortysevendeg.macroid.extras.RecyclerViewTweaks._
import macroid.FullDsl._
import macroid.{Tweak, ContextWrapper, Contexts, Ui}
import com.fortysevendeg.android.scaladays.ui.commons.AnalyticStrings._

import scala.concurrent.ExecutionContext.Implicits.global
import com.fortysevendeg.android.scaladays.ui.commons.IntegerResults._

class ScheduleFragment
  extends Fragment
  with Contexts[Fragment]
  with ComponentRegistryImpl
  with UiServices
  with ScheduleConversion
  with ListLayout {

  var clockMenu: Option[MenuItem] = None

  var indexEventNow: Option[Int] = None

  override lazy val contextProvider: ContextWrapper = fragmentContextWrapper

  lazy val layoutManager = new LinearLayoutManager(fragmentContextWrapper.getOriginal)

  override def onCreate(savedInstanceState: Bundle): Unit = {
    super.onCreate(savedInstanceState)
    setHasOptionsMenu(true)
  }

  override def onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle): View = {
    analyticsServices.sendScreenName(analyticsScheduleListScreen)
    contentWithSwipeRefresh
  }

  override def onViewCreated(view: View, savedInstanceState: Bundle): Unit = {
    super.onViewCreated(view, savedInstanceState)
    runUi(
      (recyclerView
        <~ rvLayoutManager(layoutManager)
        <~ rvAddItemDecoration(new ScheduleItemDecorator())) ~
        loadSchedule() ~
        (refreshLayout <~ srlOnRefreshListener(loadSchedule(favorites = false, forceDownload = true))) ~
        (reloadButton <~ On.click(
          loadSchedule(favorites = false, forceDownload = true)
        )))
  }

  override def onCreateOptionsMenu(menu: Menu, inflater: MenuInflater): Unit = {
    inflater.inflate(R.menu.schedule_menu, menu)
    clockMenu = Option(menu.findItem(R.id.action_clock))
    super.onCreateOptionsMenu(menu, inflater)
  }

  override def onPrepareOptionsMenu(menu: Menu): Unit = {
    super.onPrepareOptionsMenu(menu)
    clockMenu map (_.setVisible(indexEventNow.isDefined))
  }

  override def onOptionsItemSelected(item: MenuItem): Boolean = item.getItemId match {
    case R.id.action_filter =>
      new AlertDialog.Builder(getActivity)
        .setCancelable(true)
        .setItems(R.array.filter_menu, new OnClickListener() {
        override def onClick(dialog: DialogInterface, which: Int): Unit = {
          runUi(which match {
            case 0 =>
              analyticsServices.sendEvent(
                Some(analyticsScheduleListScreen),
                analyticsCategoryFilter,
                analyticsScheduleActionFilterAll)
              loadSchedule()
            case 1 =>
              analyticsServices.sendEvent(
                Some(analyticsScheduleListScreen),
                analyticsCategoryFilter,
                analyticsScheduleActionFilterFavorites)
              loadSchedule(favorites = true)
          })
        }
      }).create().show()
      true
    case R.id.action_clock =>
      indexEventNow map {
        index =>
          runUi(recyclerView <~ Tweak[RecyclerView](_.smoothScrollToPosition(index)))
      }
      true
    case _ => super.onOptionsItemSelected(item)
  }

  override def onActivityResult(requestCode: Int, resultCode: Int, data: Intent): Unit = {
    super.onActivityResult(requestCode, resultCode, data)
    requestCode match {
      case `detailResult` =>
        resultCode match {
          case Activity.RESULT_OK =>
            runUi(
              recyclerView <~ Tweak[RecyclerView] {
                rv =>
                  rv.getAdapter match {
                    case adapter: ScheduleAdapter => rv.swapAdapter(adapter, false)
                    case _ =>
                  }
              }
            )
          case _ => ()
        }
    }
  }

  def loadSchedule(favorites: Boolean = false, forceDownload: Boolean = false, swipe: Boolean = false): Ui[_] = {
    loadSelectedConference(forceDownload) mapUi {
      conference =>
        if (swipe) runUi(refreshLayout <~ srlRefreshing(false))
        reloadList(conference.info.utcTimezoneOffset, conference.schedule, favorites)
    } recoverUi {
      case _ =>
        if (swipe) runUi(refreshLayout <~ srlRefreshing(false))
        failed()
    }
    if (swipe) Ui.nop else loading()
  }

  def reloadList(timeZone: String, events: Seq[Event], favorites: Boolean = false): Ui[_] = {
    val scheduleItems = toScheduleItem(timeZone, events,
      if (favorites) {
        event => {
          preferenceServices.fetchBooleanPreference(PreferenceRequest[Boolean](
            getNamePreferenceFavorite(event.id), false)).value
        }
      } else {
        event => true
      })
    val eventNow: Option[ScheduleItem] = scheduleItems find (_.event exists (_.isCurrentEvent()))
    indexEventNow = eventNow map scheduleItems.indexOf
    Option(getActivity) map (_.supportInvalidateOptionsMenu())
    scheduleItems.length match {
      case length if length == 0 && favorites => noFavorites()
      case 0 => empty()
      case _ =>
        val scheduleAdapter = ScheduleAdapter(timeZone, scheduleItems, new RecyclerClickListener {
          override def onClick(scheduleItem: ScheduleItem): Unit = if (!scheduleItem.isHeader) {
              scheduleItem.event map (event => clickEvent(event, timeZone))
            }
        })
        adapter(scheduleAdapter)
    }
  }

  private def clickEvent(event: Event, timeZone: String) = if (event.eventType == 1 || event.eventType == 2) {
    analyticsServices.sendEvent(
      Some(analyticsScheduleListScreen),
      analyticsCategoryNavigate,
      analyticsScheduleActionGoToDetail,
      Some(event.title))
    val intent = new Intent(fragmentContextWrapper.getOriginal, classOf[ScheduleDetailActivity])
    intent.putExtra(ScheduleDetailActivity.scheduleItemKey, event)
    intent.putExtra(ScheduleDetailActivity.timeZoneKey, timeZone)
    startActivityForResult(intent, detailResult)
  }

}
