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

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.{LayoutInflater, View, ViewGroup}
import com.fortysevendeg.android.scaladays.modules.ComponentRegistryImpl
import com.fortysevendeg.android.scaladays.ui.commons.AnalyticStrings._
import com.fortysevendeg.android.scaladays.ui.commons.UiServices
import macroid.extras.TextViewTweaks._
import macroid.extras.ViewTweaks._
import macroid._
import macroid.{ContextWrapper, Contexts, Ui}

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
    loadSelectedConference() mapUi {
      conference =>
        conference.codeOfConduct map show getOrElse Ui.nop
    } recoverUi {
      case _ => failed()
    }
  }

  def show(codeOfConduct: String): Ui[_] =  description <~ tvText(codeOfConduct)

  def failed(): Ui[_] = (mainContent <~ vGone) ~ (placeholderContent <~ vVisible)

}
