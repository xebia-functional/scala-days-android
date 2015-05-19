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

package com.fortysevendeg.android.scaladays.ui.sponsors

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.{LayoutInflater, View, ViewGroup}
import com.fortysevendeg.android.scaladays.model.SponsorType
import com.fortysevendeg.android.scaladays.modules.ComponentRegistryImpl
import com.fortysevendeg.android.scaladays.ui.commons.AnalyticStrings._
import com.fortysevendeg.android.scaladays.ui.commons.{ListLayout, UiServices}
import com.fortysevendeg.macroid.extras.RecyclerViewTweaks._
import macroid.FullDsl._
import macroid.{ContextWrapper, Contexts, Ui}

import scala.concurrent.ExecutionContext.Implicits.global

class SponsorsFragment
  extends Fragment
  with Contexts[Fragment]
  with ComponentRegistryImpl
  with UiServices
  with ListLayout {

  override lazy val contextProvider: ContextWrapper = fragmentContextWrapper

  override def onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle): View = {
    analyticsServices.sendScreenName(analyticsSponsorsScreen)
    content
  }

  override def onViewCreated(view: View, savedInstanceState: Bundle): Unit = {
    super.onViewCreated(view, savedInstanceState)
    runUi(
      (recyclerView
        <~ rvLayoutManager(new LinearLayoutManager(fragmentContextWrapper.application))) ~
        loadSponsors() ~
        (reloadButton <~ On.click(loadSponsors(forceDownload = true))))
  }

  def loadSponsors(forceDownload: Boolean = false): Ui[_] = {
    loadSelectedConference(forceDownload) mapUi {
      conference =>
        reloadList(conference.sponsors)
    } recoverUi {
      case _ => failed()
    }
    loading()
  }

  def reloadList(sponsors: Seq[SponsorType]): Ui[_] = {
    sponsors.length match {
      case 0 => empty()
      case _ =>
        val sponsorItems = SponsorConversion.toSponsorItem(sponsors)
        val sponsorsAdapter = new SponsorsAdapter(sponsorItems, new RecyclerClickListener {
          override def onClick(sponsorItem: SponsorItem): Unit = {
            sponsorItem.sponsor map {
              sponsor =>
                analyticsServices.sendEvent(
                  screenName = Some(analyticsSponsorsScreen),
                  category = analyticsCategoryNavigate,
                  action = analyticsSponsorsActionGoToSponsor)
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(sponsor.url)))
            }
          }
        })
        adapter(sponsorsAdapter)
    }
  }

}

