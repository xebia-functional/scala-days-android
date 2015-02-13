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

package com.fortysevendeg.android.scaladays.ui.main

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v7.app.{ActionBarActivity, ActionBarDrawerToggle}
import android.view.{MenuItem, View}
import com.fortysevendeg.android.scaladays.R
import com.fortysevendeg.android.scaladays.ui.qrcode.QrCodeFragment
import com.fortysevendeg.android.scaladays.ui.menu.MenuSection._
import com.fortysevendeg.android.scaladays.ui.menu._
import com.fortysevendeg.android.scaladays.ui.schedule.ScheduleFragment
import com.fortysevendeg.android.scaladays.ui.social.SocialFragment
import com.fortysevendeg.android.scaladays.ui.speakers.SpeakersFragment
import com.fortysevendeg.android.scaladays.ui.sponsors.SponsorsFragment
import com.fortysevendeg.macroid.extras.DrawerLayoutTweaks._
import com.fortysevendeg.macroid.extras.FragmentExtras._
import com.fortysevendeg.macroid.extras.ToolbarTweaks._
import macroid.FullDsl._
import macroid._
import com.crashlytics.android.Crashlytics


class MainActivity
    extends ActionBarActivity
    with Contexts[FragmentActivity]
    with Layout
    with IdGeneration {

  var actionBarDrawerToggle: Option[ActionBarDrawerToggle] = None

  override def onCreate(savedInstanceState: Bundle) = {
    super.onCreate(savedInstanceState)
    Crashlytics.start(this);
    setContentView(layout)

    toolBar map setSupportActionBar

    getSupportActionBar.setDisplayHomeAsUpEnabled(true)
    getSupportActionBar.setHomeButtonEnabled(true)

    drawerLayout map { drawerLayout =>
      val drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.openMenu, R.string.clodeMenu) {
        override def onDrawerClosed(drawerView: View): Unit = {
          super.onDrawerClosed(drawerView)
          invalidateOptionsMenu()
          findFragmentById[MenuFragment](Id.menuFragment) map (_.showMainMenu)
        }

        override def onDrawerOpened(drawerView: View): Unit = {
          super.onDrawerOpened(drawerView)
          invalidateOptionsMenu()
        }
      }
      actionBarDrawerToggle = Some(drawerToggle)
      drawerLayout.setDrawerListener(drawerToggle)
    }

    if (savedInstanceState == null) {
      runUi(
        replaceFragment(
          builder = f[MenuFragment],
          id = Id.menuFragment,
          tag = Some(Tag.menuFragment)))
    }
  }

  override def onActivityResult(requestCode: Int, resultCode: Int, data: Intent): Unit =
    super.onActivityResult(requestCode, resultCode, data)

  def itemSelected(section: MenuSection.Value, title: String) {
    val builder = section match {
      case SPEAKERS => f[SpeakersFragment]
      case SCHEDULE => f[ScheduleFragment]
      case SOCIAL => f[SocialFragment]
      case CONTACTS => f[QrCodeFragment]
      case SPONSORS => f[SponsorsFragment]
      case _ => f[SampleFragment].pass(SampleFragment.titleArg → title)
    }
    runUi(
      (toolBar <~ tbTitle(title)) ~
          (drawerLayout <~ dlCloseDrawer(fragmentMenu)) ~
          replaceFragment(
            builder = builder,
            id = Id.mainFragment,
            tag = Some(Tag.mainFragment))
    )
  }

  override def onPostCreate(savedInstanceState: Bundle): Unit = {
    super.onPostCreate(savedInstanceState)
    actionBarDrawerToggle map (_.syncState)

  }

  override def onConfigurationChanged(newConfig: Configuration): Unit = {
    super.onConfigurationChanged(newConfig)
    actionBarDrawerToggle map (_.onConfigurationChanged(newConfig))
  }

  override def onOptionsItemSelected(item: MenuItem): Boolean = {
    if (actionBarDrawerToggle.isDefined && actionBarDrawerToggle.get.onOptionsItemSelected(item)) true
    else super.onOptionsItemSelected(item)
  }
}
