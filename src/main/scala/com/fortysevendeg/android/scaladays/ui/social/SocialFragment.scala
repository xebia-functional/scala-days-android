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

package com.fortysevendeg.android.scaladays.ui.social

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.{LayoutInflater, View, ViewGroup}
import com.fortysevendeg.android.scaladays.model.Speaker
import com.fortysevendeg.android.scaladays.modules.ComponentRegistryImpl
import com.fortysevendeg.android.scaladays.modules.json.JsonRequest
import com.fortysevendeg.android.scaladays.modules.net.NetRequest
import com.fortysevendeg.android.scaladays.ui.commons.LineItemDecorator
import com.fortysevendeg.macroid.extras.ActionsExtras._
import com.fortysevendeg.macroid.extras.RecyclerViewTweaks._
import com.fortysevendeg.macroid.extras.ViewTweaks._
import macroid.FullDsl._
import macroid.{Ui, AppContext, Contexts}

import scala.concurrent.ExecutionContext.Implicits.global

class SocialFragment
    extends Fragment
    with Contexts[Fragment]
    with ComponentRegistryImpl {

  val authResult = 1001

  override implicit lazy val appContextProvider: AppContext = fragmentAppContext

  private var fragmentLayout: Option[Layout] = None

  override def onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle): View = {
    val fLayout = new Layout
    fragmentLayout = Some(fLayout)
    runUi(
      fLayout.recyclerView
          <~ rvLayoutManager(new LinearLayoutManager(appContextProvider.get))
          <~ rvAddItemDecoration(new LineItemDecorator())
    )
    fLayout.content
  }

  override def onViewCreated(view: View, savedInstanceState: Bundle): Unit = {
    super.onViewCreated(view, savedInstanceState)
    if (twitterServices.isConnected()) {
      search()
    } else {
      val intent = new Intent(getActivity, classOf[AuthorizationActivity])
      startActivityForResult(intent, authResult)
    }
  }

  override def onActivityResult(requestCode: Int, resultCode: Int, data: Intent): Unit = {
    super.onActivityResult(requestCode, resultCode, data)
    requestCode match {
      case request if request == authResult =>
        resultCode match {
          case result if result == Activity.RESULT_OK =>
            search()
          case result if result == Activity.RESULT_CANCELED =>
            failed()
        }
    }
  }

  def search() = {
    runUi(Ui {
      aShortToast("Vamos!!")
    })
    // TODO
  }

  def failed() = {
    runUi(Ui {
      aShortToast("failed :<((")
    })
    // TODO
  }

  def reloadList(speakers: Seq[Speaker]) = {
    for {
      layout <- fragmentLayout
      recyclerView <- layout.recyclerView
    } yield {
      val adapter = new SocialAdapter(speakers, new RecyclerClickListener {
        override def onClick(speaker: Speaker): Unit = {
          speaker.twitter map {
            twitterName =>
              val intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/%s".format(twitterName)))
              startActivity(intent)
          }

        }
      })
      runUi(
        (layout.progressBar <~ vInvisible) ~
            (layout.recyclerView <~ rvAdapter(adapter))
      )
    }
  }

}
