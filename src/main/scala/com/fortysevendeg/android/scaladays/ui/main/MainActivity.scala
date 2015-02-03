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

import android.content.res.Configuration
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v7.app.{ActionBarActivity, ActionBarDrawerToggle}
import android.support.v7.widget.LinearLayoutManager
import android.view.{MenuItem, View}
import com.fortysevendeg.android.scaladays.R
import com.fortysevendeg.android.scaladays.ui.speakers.SpeakersFragment
import com.fortysevendeg.android.scaladays.utils.MenuSection._
import com.fortysevendeg.macroid.extras.DrawerLayoutTweaks._
import com.fortysevendeg.macroid.extras.FragmentExtras._
import com.fortysevendeg.macroid.extras.ToolbarTweaks._
import macroid.FullDsl._
import macroid._


class MainActivity
  extends ActionBarActivity
  with Contexts[FragmentActivity]
  with Layout
  with IdGeneration {

  var actionBarDrawerToggle: Option[ActionBarDrawerToggle] = None

  override def onCreate(savedInstanceState: Bundle) = {
    super.onCreate(savedInstanceState)
    setContentView(layout)

    toolBar map setSupportActionBar

    getSupportActionBar.setDisplayHomeAsUpEnabled(true)
    getSupportActionBar.setHomeButtonEnabled(true)

    drawerLayout map { drawerLayout =>
      val drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.openMenu, R.string.clodeMenu) {
        override def onDrawerClosed(drawerView: View): Unit = {
          super.onDrawerClosed(drawerView)
          invalidateOptionsMenu()
        }

        override def onDrawerOpened(drawerView: View): Unit = {
          super.onDrawerOpened(drawerView)
          invalidateOptionsMenu()
        }
      }
      actionBarDrawerToggle = Some(drawerToggle)
      drawerLayout.setDrawerListener(drawerToggle)
    }

    val adapter = new DrawerMenuAdapter(new RecyclerClickListener {
      override def onClick(info: DrawerMenuItem): Unit = {
        runUi((toolBar <~ tbTitle(info.name)) ~ (drawerLayout <~ dlCloseDrawer(drawerMenuLayout)))
        itemSelected(info)
      }
    })

    recyclerView map {
      view => {
        view.setLayoutManager(new LinearLayoutManager(this))
        view.setAdapter(adapter)
      }
    }

    if (savedInstanceState == null) {
      itemSelected(adapter.list.head)
    }
  }

  private def itemSelected(info: DrawerMenuItem) {
    val builder = info.section match {
      case SPEAKERS => f[SpeakersFragment]
      case _ => f[SampleFragment].pass(SampleFragment.titleArg → info.name)
    }
    runUi(replaceFragment(
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
