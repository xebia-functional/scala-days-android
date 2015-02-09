package com.fortysevendeg.android.scaladays.ui.drawer

import android.app.Activity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.{LayoutInflater, View, ViewGroup}
import com.fortysevendeg.android.scaladays.R
import com.fortysevendeg.android.scaladays.model.{Root, Information}
import com.fortysevendeg.android.scaladays.modules.ComponentRegistryImpl
import com.fortysevendeg.android.scaladays.modules.json.JsonRequest
import com.fortysevendeg.android.scaladays.modules.net.NetRequest
import com.fortysevendeg.android.scaladays.ui.commons.GlideTweaks._
import com.fortysevendeg.android.scaladays.ui.main.MainActivity
import com.fortysevendeg.macroid.extras.RecyclerViewTweaks._
import com.fortysevendeg.macroid.extras.TextTweaks._
import macroid.FullDsl._
import macroid._
import scala.concurrent.ExecutionContext.Implicits.global

class MenuFragment
  extends Fragment
  with Contexts[Fragment]
  with ComponentRegistryImpl {

  override implicit lazy val appContextProvider: AppContext = fragmentAppContext

  private var fragmentLayout: Option[Layout] = None
  
  private var mainActivity: Option[MainActivity] = None

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
      recyclerView <- layout.recyclerView
    } yield {
      val adapter = new DrawerMenuAdapter(new RecyclerClickListener {
        override def onClick(info: DrawerMenuItem): Unit = {
          mainActivity map (_.itemSelected(info.section, info.name))
        }
      })
      val defaultSection = adapter.list(0)
      runUi(        
        (layout.recyclerView <~ rvAdapter(adapter)) ~
        Ui { mainActivity map (_.itemSelected(defaultSection.section, defaultSection.name)) }
      )
    }

    val conferenceSelected = 0 // TODO Use PersistentService

    val saveJsonOp = for {
      _ <- netServices.saveJsonInLocal(NetRequest(false))
      jsonResponse <- jsonServices.loadJson(JsonRequest())
    } yield {
      jsonResponse.apiResponse
    }

    saveJsonOp map {
      case Some(Root(list)) if list.length > conferenceSelected =>
        val conference = list(conferenceSelected)
        showConferenceInfo(conference.info)
      case None => failed()
    } recover {
      case _ => failed()
    }
  }

  override def onDetach(): Unit = {
    mainActivity = None
    super.onDetach()
  }

  def failed() = {}

  def showConferenceInfo(information: Information) = {
    for {
      layout <- fragmentLayout
      imageView <- layout.bigImage
      textView <- layout.conferenceTitle
    } yield runUi(
      (imageView <~ glideCenterCrop(information.pictures(0).url, R.drawable.placeholder_error)) ~
      (textView <~ tvText(information.longName))
    )
  }

}
