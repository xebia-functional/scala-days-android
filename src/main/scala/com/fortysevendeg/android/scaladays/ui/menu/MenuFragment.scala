package com.fortysevendeg.android.scaladays.ui.menu

import android.app.Activity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.{LayoutInflater, View, ViewGroup}
import com.fortysevendeg.android.scaladays.R
import com.fortysevendeg.android.scaladays.model.{Information, Conference}
import com.fortysevendeg.android.scaladays.modules.ComponentRegistryImpl
import com.fortysevendeg.android.scaladays.ui.commons.GlideTweaks._
import com.fortysevendeg.android.scaladays.ui.commons.UiServices
import com.fortysevendeg.android.scaladays.ui.main.MainActivity
import com.fortysevendeg.macroid.extras.RecyclerViewTweaks._
import com.fortysevendeg.macroid.extras.TextTweaks._
import com.fortysevendeg.macroid.extras.ViewTweaks._
import macroid.FullDsl._
import macroid._

class MenuFragment
  extends Fragment
  with Contexts[Fragment]
  with ComponentRegistryImpl
  with UiServices {

  override implicit lazy val appContextProvider: AppContext = fragmentAppContext

  private var fragmentLayout: Option[Layout] = None
  
  private var mainActivity: Option[MainActivity] = None
  
  lazy val mainMenuAdapter: MainMenuAdapter = new MainMenuAdapter(new MainMenuClickListener {
    override def onClick(mainMenuItem: MainMenuItem) =
      itemSelected(mainMenuItem)
  })
  
  lazy val conferenceMenuAdapter: ConferenceMenuAdapter = new ConferenceMenuAdapter(new ConferenceMenuClickListener {
    override def onClick(conferenceMenuItem: ConferenceMenuItem) =
      conferenceSelected(conferenceMenuItem)
  })

  private var mainMenuVisible: Boolean = true
  
  val colorTransitionTime = 400

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
      recyclerView <- layout.recyclerView
    } yield {
      bigImageLayout.setOnClickListener(new View.OnClickListener {
        override def onClick(v: View) =
          toggleMenu()
      })
      val defaultSection = 0
      runUi(        
        (layout.recyclerView <~ rvAdapter(mainMenuAdapter)) ~
        Ui { itemSelected(mainMenuAdapter.list(defaultSection)) }
      )
    }

    val conferenceId = 0 // TODO Use PersistentService
    
    loadConferences(
      success = { root =>
        conferenceMenuAdapter.loadConferences(root.conferences map (_.info))
        root.conferences find (_.info.id == conferenceId || conferenceId == 0) map { conf =>
          showConference(conf.info)
        }
      }
    )
  }

  override def onDetach(): Unit = {
    mainActivity = None
    super.onDetach()
  }
  
  def toggleMenu() = {
    for {
      layout <- fragmentLayout
      recyclerView <- layout.recyclerView
    } yield {
      mainMenuVisible = !mainMenuVisible
      if (mainMenuVisible) runUi(
        (recyclerView <~ vBackgroundTransition(durationMilis = colorTransitionTime, reverse = true)) ~
          (recyclerView <~ rvAdapter(mainMenuAdapter)))
      else runUi(
        (recyclerView <~ vBackgroundTransition(durationMilis = colorTransitionTime, reverse = false)) ~
          (recyclerView <~ rvAdapter(conferenceMenuAdapter)))
    }
  }

  def itemSelected(menuItem: MainMenuItem) = {
    mainMenuAdapter.selectItem(Some(menuItem.id))
    mainActivity map (_.itemSelected(menuItem.section, menuItem.name))
  }
  
  def conferenceSelected(menuItem: ConferenceMenuItem) = {
    showConference(menuItem.information)
  }
  
  def showConference(information: Information) = {
    for {
      layout <- fragmentLayout
      imageView <- layout.bigImage
      textView <- layout.conferenceTitle
    } yield {
      runUi(
        (imageView <~ glideCenterCrop(information.pictures(0).url, R.drawable.placeholder_error)) ~
          (textView <~ tvText(information.longName))
      )
    }
  }

}
