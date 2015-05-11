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

package com.fortysevendeg.android.scaladays.ui.about

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.{LayoutInflater, View, ViewGroup}
import com.fortysevendeg.android.scaladays.R
import com.fortysevendeg.android.scaladays.modules.ComponentRegistryImpl
import com.fortysevendeg.android.scaladays.ui.commons.AnalyticStrings._
import com.fortysevendeg.android.scaladays.ui.commons.UiServices
import com.fortysevendeg.macroid.extras.TextTweaks._
import com.fortysevendeg.macroid.extras.ViewTweaks._
import com.fortysevendeg.macroid.extras.ResourcesExtras._
import macroid.FullDsl._
import macroid.{Ui, ContextWrapper, Contexts}

import scala.concurrent.ExecutionContext.Implicits.global

class AboutFragment
  extends Fragment
  with Contexts[Fragment]
  with ComponentRegistryImpl
  with UiServices
  with Layout {

  override lazy val contextProvider: ContextWrapper = fragmentContextWrapper

  override def onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle): View = {
    analyticsServices.sendScreenName(analyticsAboutScreen)
    content
  }

  override def onViewCreated(view: View, savedInstanceState: Bundle): Unit = {
    super.onViewCreated(view, savedInstanceState)
    runUi(
      aboutContent <~ On.click {
        Ui {
          analyticsServices.sendEvent(
            screenName = Some(analyticsAboutScreen),
            category = analyticsCategoryNavigate,
            action = analyticsAboutActionGoTo47Deg)
          startActivity(new Intent(Intent.ACTION_VIEW,
            Uri.parse(resGetString(R.string.url_47deg))))
        }
      }
    )
    val result = for {
      conference <- loadSelectedConference()
    } yield show(conference.codeOfConduct)
    result.recover {
      case _ => failed()
    }
  }

  def show(codeOfConduct: Option[String]) =  runUi(description <~ (codeOfConduct map (tvText(_) + vVisible) getOrElse vGone))

  def failed() = runUi((mainContent <~ vGone) ~ (placeholderContent <~ vVisible))

}
