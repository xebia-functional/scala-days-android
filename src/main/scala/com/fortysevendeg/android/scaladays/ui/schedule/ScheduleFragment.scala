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
import android.support.v7.widget.{LinearLayoutManager, RecyclerView}
import android.view._
import com.fortysevendeg.android.scaladays.R
import com.fortysevendeg.android.scaladays.model.Event
import com.fortysevendeg.android.scaladays.modules.ComponentRegistryImpl
import com.fortysevendeg.android.scaladays.modules.preferences.PreferenceRequest
import com.fortysevendeg.android.scaladays.ui.commons.AnalyticStrings._
import com.fortysevendeg.android.scaladays.ui.commons.IntegerResults._
import com.fortysevendeg.android.scaladays.ui.commons.{ListLayout, UiServices}
import com.fortysevendeg.android.scaladays.ui.scheduledetail.ScheduleDetailActivity
import macroid._
import macroid.extras.RecyclerViewTweaks._
import macroid.extras.UIActionsExtras._
import macroid.FullDsl._
import macroid.{ContextWrapper, Contexts, Tweak, Ui}

class ScheduleFragment
  extends Fragment
  with Contexts[Fragment]
  with ComponentRegistryImpl
  with UiServices
  with ScheduleConversion
  with ListLayout { self =>

  val tagDialog = "dialog"

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
    Ui.run(
      (recyclerView
        <~ rvLayoutManager(layoutManager)
        <~ rvAddItemDecoration(new ScheduleItemDecorator())) ~
        loadSchedule() ~
        (refreshLayout <~ srlOnRefreshListener(loadSchedule(favorites = false, forceDownload = true, swipe = true))) ~
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
          Ui.run(which match {
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
          Ui.run(recyclerView <~ Tweak[RecyclerView](_.smoothScrollToPosition(index)))
      }
      true
    case _ => super.onOptionsItemSelected(item)
  }

  override def onActivityResult(requestCode: Int, resultCode: Int, data: Intent): Unit = {
    super.onActivityResult(requestCode, resultCode, data)
    (requestCode, resultCode) match {
      case (`detailResult`, Activity.RESULT_OK) =>
        Ui.run(
          recyclerView <~ Tweak[RecyclerView] {
            rv =>
              rv.getAdapter match {
                case adapter: ScheduleAdapter => rv.swapAdapter(adapter, false)
                case _ =>
              }
          }
        )
      case (`voteResult`, Activity.RESULT_OK) =>
        Ui.run(
          recyclerView <~ Tweak[RecyclerView](_.getAdapter.notifyDataSetChanged()))
      case (`voteResult`, Activity.RESULT_CANCELED) =>
        Ui.run(uiShortToast(R.string.voteFailed))
      case _ =>
    }
  }

  def loadSchedule(favorites: Boolean = false, forceDownload: Boolean = false, swipe: Boolean = false): Ui[_] = {
    loadSelectedConference(forceDownload) mapUi {
      conference =>
        if (swipe) Ui.run(refreshLayout <~ srlRefreshing(false))
        reloadList(conference.info.id, conference.info.utcTimezoneOffset, conference.schedule, favorites)
    } recoverUi {
      case _ =>
        if (swipe) Ui.run(refreshLayout <~ srlRefreshing(false))
        failed()
    }
    if (swipe) Ui.nop else loading()
  }

  def reloadList(conferenceId: Int, timeZone: String, events: Seq[Event], favorites: Boolean = false): Ui[_] = {
    val scheduleItems = toScheduleItem(timeZone, events,
      if (favorites) {
        event => {
          preferenceServices.fetchBooleanPreference(PreferenceRequest[Boolean](
            getNamePreferenceFavorite(event.id), false)).value
        }
      } else {
        event => true
      })
    val eventNow: Option[ScheduleItem] = scheduleItems find (_.event exists (_.isCurrentEvent))
    indexEventNow = eventNow map scheduleItems.indexOf
    Option(getActivity) foreach (_.supportInvalidateOptionsMenu())
    scheduleItems.length match {
      case 0 if favorites => noFavorites()
      case 0 => empty()
      case _ =>
        val scheduleAdapter = ScheduleAdapter(conferenceId, timeZone, scheduleItems, new RecyclerClickListener {
          override def onClick(scheduleItem: ScheduleItem): Unit = if (!scheduleItem.isHeader) {
            scheduleItem.event foreach (event => clickEvent(event, timeZone))
          }
          override def onVoteClick(event: Event): Unit = {
            val ft = getFragmentManager.beginTransaction()
            Option(getFragmentManager.findFragmentByTag(tagDialog)) foreach ft.remove
            ft.addToBackStack(null)
            val dialog = VoteDialog(conferenceId, event)
            dialog.setTargetFragment(self, voteResult)
            dialog.show(ft, tagDialog)
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

object ScheduleFragment {

  def getPreferenceKeyForVote(conferenceId: Int, talkId: Int) = s"${conferenceId}_${talkId}_vote"

  def getPreferenceKeyForVoteMessage(conferenceId: Int, talkId: Int) = s"${conferenceId}_${talkId}_message"

}