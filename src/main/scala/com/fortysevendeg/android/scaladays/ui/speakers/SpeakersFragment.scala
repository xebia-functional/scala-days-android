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

package com.fortysevendeg.android.scaladays.ui.speakers

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.{LayoutInflater, View, ViewGroup}
import com.fortysevendeg.android.scaladays.R
import com.fortysevendeg.android.scaladays.model.Speaker
import com.fortysevendeg.android.scaladays.modules.ComponentRegistryImpl
import com.fortysevendeg.android.scaladays.ui.commons.AnalyticStrings._
import com.fortysevendeg.android.scaladays.ui.commons.{ListLayout, UiServices, LineItemDecorator}
import macroid.{Ui, ContextWrapper, Contexts}
import scala.concurrent.ExecutionContext.Implicits.global
import macroid._
import macroid.FullDsl._
import macroid.extras.ResourcesExtras._
import macroid.extras.RecyclerViewTweaks._

class SpeakersFragment
  extends Fragment
  with Contexts[Fragment]
  with ComponentRegistryImpl
  with UiServices
  with ListLayout {

  override lazy val contextProvider: ContextWrapper = fragmentContextWrapper

  override def onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle): View = {
    analyticsServices.sendScreenName(analyticsSpeakersScreen)
    content
  }

  override def onViewCreated(view: View, savedInstanceState: Bundle): Unit = {
    super.onViewCreated(view, savedInstanceState)
    Ui.run(
      (recyclerView
        <~ rvLayoutManager(new LinearLayoutManager(fragmentContextWrapper.application))
        <~ rvAddItemDecoration(new LineItemDecorator())) ~
        loadSpeakers() ~
        (reloadButton <~ On.click(
          loadSpeakers(forceDownload = true)
        )))
  }

  def loadSpeakers(forceDownload: Boolean = false): Ui[_] = {
    loadSelectedConference(forceDownload) mapUi {
      conference =>
        reloadList(conference.speakers)
    } recoverUi {
      case _ => failed()
    }
    loading()
  }

  def reloadList(speakers: Seq[Speaker]): Ui[_] = {
    speakers.length match {
      case 0 => empty()
      case _ =>
        val speakersAdapter = new SpeakersAdapter(speakers, new RecyclerClickListener {
          override def onClick(speaker: Speaker): Unit = {
            speaker.twitter foreach {
              twitterName =>
                val twitterUser = if (twitterName.startsWith("@")) twitterName.substring(1) else twitterName
                analyticsServices.sendEvent(
                  screenName = Some(analyticsSpeakersScreen),
                  category = analyticsCategoryNavigate,
                  action = analyticsSpeakersActionGoToUser)
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(resGetString(R.string.url_twitter_user, twitterUser))))
            }
          }
        })
        adapter(speakersAdapter)
    }
  }

}
