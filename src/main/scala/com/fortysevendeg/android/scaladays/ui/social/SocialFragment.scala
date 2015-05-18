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

import java.net.URLEncoder

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view._
import com.fortysevendeg.android.scaladays.R
import com.fortysevendeg.android.scaladays.model.TwitterMessage
import com.fortysevendeg.android.scaladays.modules.ComponentRegistryImpl
import com.fortysevendeg.android.scaladays.modules.twitter.SearchRequest
import com.fortysevendeg.android.scaladays.ui.commons.AnalyticStrings._
import com.fortysevendeg.android.scaladays.ui.commons.{ListLayout, LineItemDecorator, UiServices}
import com.fortysevendeg.macroid.extras.RecyclerViewTweaks._
import com.fortysevendeg.macroid.extras.ResourcesExtras._
import macroid.FullDsl._
import macroid.{ContextWrapper, Contexts, Ui}

import scala.concurrent.ExecutionContext.Implicits.global
import com.fortysevendeg.android.scaladays.ui.commons.IntegerResults._

class SocialFragment
  extends Fragment
  with Contexts[Fragment]
  with ComponentRegistryImpl
  with UiServices
  with ListLayout {

  override lazy val contextProvider: ContextWrapper = fragmentContextWrapper

  private var hashtag: Option[String] = None

  lazy val socialAdapter = SocialAdapter(Seq.empty, new RecyclerClickListener {
    override def onClick(message: TwitterMessage): Unit = {
      analyticsServices.sendEvent(
        screenName = Some(analyticsSocialScreen),
        category = analyticsCategoryNavigate,
        action = analyticsSocialActionGoToTweet)
      startActivity(new Intent(Intent.ACTION_VIEW,
        Uri.parse(resGetString(R.string.url_twitter_status, message.screenName, message.id.toString))))
    }
  })

  override def onCreate(savedInstanceState: Bundle): Unit = {
    super.onCreate(savedInstanceState)
    setHasOptionsMenu(true)
  }

  override def onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle): View = {
    analyticsServices.sendScreenName(analyticsSocialScreen)
    content
  }

  override def onViewCreated(view: View, savedInstanceState: Bundle): Unit = {
    super.onViewCreated(view, savedInstanceState)
    runUi(
      (recyclerView
        <~ rvLayoutManager(new LinearLayoutManager(fragmentContextWrapper.application))
        <~ rvAddItemDecoration(new LineItemDecorator())) ~
        (reloadButton <~ On.click(search())) ~
        search())
    if (!twitterServices.isConnected()) {
      val intent = new Intent(getActivity, classOf[AuthorizationActivity])
      startActivityForResult(intent, authResult)
    }
  }

  override def onCreateOptionsMenu(menu: Menu, inflater: MenuInflater): Unit = {
    inflater.inflate(R.menu.social_menu, menu)
    super.onCreateOptionsMenu(menu, inflater)
  }

  override def onOptionsItemSelected(item: MenuItem): Boolean = item.getItemId match {
    case R.id.action_new_tweet =>
      hashtag map {
        ht =>
          analyticsServices.sendEvent(
            screenName = Some(analyticsSocialScreen),
            category = analyticsCategoryNavigate,
            action = analyticsSocialActionPostTweet)
          val encode = URLEncoder.encode(ht, "UTF-8")
          val i = new Intent(Intent.ACTION_VIEW)
          i.setData(Uri.parse(getString(R.string.url_twitter_new_status, encode)))
          startActivity(i)
      }
      true
    case _ => super.onOptionsItemSelected(item)
  }

  override def onActivityResult(requestCode: Int, resultCode: Int, data: Intent): Unit = {
    super.onActivityResult(requestCode, resultCode, data)
    requestCode match {
      case request if request == authResult =>
        resultCode match {
          case Activity.RESULT_OK => runUi(search())
          case Activity.RESULT_CANCELED => runUi(failed())
        }
    }
  }

  def search(): Ui[_] = {
    val result = for {
      conference <- loadSelectedConference()
      searchResponse <- twitterServices.search(SearchRequest(conference.info.query))
    } yield {
      hashtag = Some(conference.info.hashTag)
      reloadList(searchResponse.messages)
    }
    result mapUi (ui => ui) recoverUi {
      case _ => failed()
    }
    loading()
  }

  def reloadList(messages: Seq[TwitterMessage]): Ui[_] = {
    messages.length match {
      case 0 => empty()
      case _ => adapter(socialAdapter.copy(messages = messages))
    }
  }

}
