/*
 * Copyright (C) 2015 47 Degrees, LLC http://47deg.com hello@47deg.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fortysevendeg.android.scaladays.ui.menu

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.{LayoutInflater, View, ViewGroup}
import com.fortysevendeg.android.scaladays.R
import com.fortysevendeg.android.scaladays.model.Information
import com.fortysevendeg.android.scaladays.modules.ComponentRegistryImpl
import com.fortysevendeg.android.scaladays.ui.commons.AsyncImageTweaks._
import com.fortysevendeg.android.scaladays.ui.commons.UiServices
import com.fortysevendeg.android.scaladays.ui.main.MainActivity
import com.fortysevendeg.macroid.extras.RecyclerViewTweaks._
import com.fortysevendeg.macroid.extras.TextTweaks._
import com.fortysevendeg.macroid.extras.ImageViewTweaks._
import macroid.FullDsl._
import macroid._

import scala.concurrent.ExecutionContext.Implicits.global

class MenuFragment
  extends Fragment
  with Contexts[Fragment]
  with ComponentRegistryImpl
  with UiServices
  with IdGeneration {

  override implicit lazy val appContextProvider: AppContext = fragmentAppContext

  val defaultItem = 0

  private var fragmentLayout: Option[Layout] = None

  private var mainActivity: Option[MainActivity] = None

  private var urlTickets: Option[String] = None

  private val previousItemSelectedKey = "previous_item_selected_key"

  lazy val mainMenuAdapter: MainMenuAdapter = new MainMenuAdapter(new MainMenuClickListener {
    override def onClick(mainMenuItem: MainMenuItem) =
      mainMenuItem.section match {
        case MenuSection.TICKETS =>
          urlTickets map {
            url =>
              val intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(url))
              fragmentActivityContext.get.startActivity(intent)
          }
        case _ => itemSelected(mainMenuItem)
      }
  })

  lazy val conferenceMenuAdapter: ConferenceMenuAdapter = new ConferenceMenuAdapter(new ConferenceMenuClickListener {
    override def onClick(conferenceMenuItem: ConferenceMenuItem) =
      conferenceSelected(conferenceMenuItem)
  })

  private var mainMenuVisible: Boolean = true

  override def onAttach(activity: Activity) = {
    super.onAttach(activity)
    mainActivity = Some(activity.asInstanceOf[MainActivity])
  }

  override def onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle): View = {
    val fLayout = new Layout
    fragmentLayout = Some(fLayout)
    runUi(
      fLayout.recyclerView <~ rvLayoutManager(new LinearLayoutManager(appContextProvider.get))
    )
    fLayout.content
  }

  override def onViewCreated(view: View, savedInstanceState: Bundle) = {
    super.onViewCreated(view, savedInstanceState)

    for {
      layout <- fragmentLayout
      bigImageLayout <- layout.bigImageLayout
    } yield {
      bigImageLayout.setOnClickListener(new View.OnClickListener {
        override def onClick(v: View) =
          toggleMenu()
      })
      runUi(
        (layout.recyclerView <~ rvAdapter(mainMenuAdapter)))

      val defaultSection = Option(savedInstanceState) map (_.getInt(previousItemSelectedKey, defaultItem)) getOrElse defaultItem
      itemSelected(mainMenuAdapter.list(defaultSection), savedInstanceState == null)

    }

    val conferenceId = loadSelectedConferenceId
    val result = for {
      conferences <- loadConferences()
      selectedConference <- findConference(conferences, conferenceId)
    } yield {
      conferenceMenuAdapter.loadConferences(conferences map (_.info))
      showConference(selectedConference.info)
      urlTickets = Some(selectedConference.info.registrationSite)
    }

    result.recover {
      case _ => failed()
    }
  }

  def failed() = {}


  override def onSaveInstanceState(outState: Bundle): Unit = {
    outState.putInt(previousItemSelectedKey, mainMenuAdapter.selectedItem map (mainMenuAdapter.list.indexOf(_)) getOrElse defaultItem)
    super.onSaveInstanceState(outState)
  }

  override def onDetach(): Unit = {
    mainActivity = None
    super.onDetach()
  }

  def toggleMenu() =
    for {
      layout <- fragmentLayout
      recyclerView <- layout.recyclerView
      selectorImageView <- layout.conferenceSelector
    } yield {
      mainMenuVisible = !mainMenuVisible
      if (mainMenuVisible) runUi(
        (selectorImageView <~ ivSrc(R.drawable.menu_header_select_arrow)) ~
          (recyclerView <~ rvAdapter(mainMenuAdapter)))
      else runUi(
        (selectorImageView <~ ivSrc(R.drawable.menu_header_select_arrow_up)) ~
          (recyclerView <~ rvAdapter(conferenceMenuAdapter)))
    }

  def showMainMenu =
    if (!mainMenuVisible) toggleMenu()

  def itemSelected(menuItem: MainMenuItem, callCallback: Boolean = true) = {
    mainMenuAdapter.selectItem(Some(menuItem))
    if (callCallback) mainActivity map (_.itemSelected(menuItem.section, menuItem.name))
  }

  def conferenceSelected(menuItem: ConferenceMenuItem) = {
    showConference(menuItem.information)
    saveSelectedConferenceId(menuItem.id)
    mainMenuAdapter.selectedItem map (itemSelected(_))
  }

  def showConference(information: Information) =
    for {
      layout <- fragmentLayout
      imageView <- layout.bigImage
      textView <- layout.conferenceTitle
    } yield {
      runUi(
        (imageView <~ srcImage(information.pictures(0).url, R.drawable.placeholder_square)) ~
          (textView <~ tvText(information.longName)))
    }

}
