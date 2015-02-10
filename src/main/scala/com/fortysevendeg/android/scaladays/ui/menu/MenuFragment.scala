package com.fortysevendeg.android.scaladays.ui.menu

import android.app.Activity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.{RecyclerView, LinearLayoutManager}
import android.view.{LayoutInflater, View, ViewGroup}
import com.fortysevendeg.android.scaladays.R
import com.fortysevendeg.android.scaladays.model.{Conference, Information}
import com.fortysevendeg.android.scaladays.modules.ComponentRegistryImpl
import com.fortysevendeg.android.scaladays.ui.commons.GlideTweaks._
import com.fortysevendeg.android.scaladays.ui.commons.{EmptyAdapter, UiServices}
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
    override def onClick(menuItem: MainMenuItem) =
      itemSelected(menuItem)
  })
  
  private var conferenceMenuAdapter: Option[ConferenceMenuAdapter] = None

  private var mainMenuVisible: Boolean = true
  
  private val emptyAdapter: RecyclerView.Adapter[RecyclerView.ViewHolder] = new EmptyAdapter

  override def onAttach(activity: Activity): Unit = {
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

  override def onViewCreated(view: View, savedInstanceState: Bundle): Unit = {
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

    val conferenceSelected = 0 // TODO Use PersistentService
    
    loadConferences(
      success = { root =>        
        conferencesLoaded(root.conferences, if (root.conferences.length > conferenceSelected) conferenceSelected else 0)
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
        (recyclerView <~ vBackgroundTransition(200, true)) ~
          (recyclerView <~ rvAdapter(mainMenuAdapter)))
      else runUi(
        (recyclerView <~ vBackgroundTransition(200, false)) ~
          (recyclerView <~ rvAdapter(conferenceMenuAdapter getOrElse emptyAdapter)))
    }
  }

  def getColor: (Int) => Int = {
    appContextProvider.get.getResources.getColor
  }

  def itemSelected(menuItem: MainMenuItem) = {
    mainMenuAdapter.selectItem(Some(menuItem.id))
    mainActivity map (_.itemSelected(menuItem.section, menuItem.name))
  }
  
  def conferenceSelected(menuItem: ConferenceMenuItem) = {}
  
  def conferencesLoaded(conferences: Seq[Conference], selected: Int) = {
    for {
      layout <- fragmentLayout
      imageView <- layout.bigImage
      textView <- layout.conferenceTitle
    } yield {
      conferenceMenuAdapter = Some(new ConferenceMenuAdapter(conferences map (_.info), new ConferenceMenuClickListener {
        override def onClick(info: ConferenceMenuItem) = 
          conferenceSelected(info)
      }))
      runUi(
        (imageView <~ glideCenterCrop(conferences(selected).info.pictures(0).url, R.drawable.placeholder_error)) ~
          (textView <~ tvText(conferences(selected).info.longName))
      )
    }
  }

}
