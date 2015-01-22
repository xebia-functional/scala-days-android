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
import android.support.v7.app.{ActionBarDrawerToggle, ActionBarActivity}
import android.support.v7.widget.LinearLayoutManager
import android.view.{MenuItem, View}
import com.fortysevendeg.android.scaladays.R
import com.fortysevendeg.macroid.extras.ExtraActions._
import macroid.Contexts


class MainActivity
  extends ActionBarActivity
  with Contexts[FragmentActivity]
  with Layout {

  var actionBarDrawerToggle: Option[ActionBarDrawerToggle] = None

  override def onCreate(savedInstanceState: Bundle) = {
    super.onCreate(savedInstanceState)
    setContentView(layout)

    toolBar map setSupportActionBar

    getSupportActionBar.setDisplayHomeAsUpEnabled(true)
    getSupportActionBar.setHomeButtonEnabled(true)

    drawerLayout.map(view => {
      val drawerToggle = new ActionBarDrawerToggle(this, view, R.string.openMenu, R.string.clodeMenu) {
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
      view.setDrawerListener(drawerToggle)
      view.setStatusBarBackgroundColor(getResources.getColor(R.color.primary))
    })

    val adapter = new DrawerMenuAdapter(new RecyclerClickListener {
      override def onClick(info: DrawerMenuItem): Unit = {
        toolBar map {
          _.setTitle(info.name)
        }
        aShortToast("Element " + info.name + " clicked")
      }
    })

    recyclerView.map(view => {
      view.setLayoutManager(new LinearLayoutManager(this))
      view.setAdapter(adapter)
    })
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
