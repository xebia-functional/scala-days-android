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

import android.support.v7.widget.RecyclerView
import android.view.View.OnClickListener
import android.view.{View, ViewGroup}
import com.fortysevendeg.android.scaladays.R
import com.fortysevendeg.android.scaladays.utils.MenuSection._
import com.fortysevendeg.android.scaladays.utils.MenuSection.MenuSection
import macroid.{ActivityContext, AppContext}

class DrawerMenuAdapter(listener: RecyclerClickListener)
    (implicit context: ActivityContext, appContext: AppContext)
    extends RecyclerView.Adapter[ViewHolderMenuAdapter] {

  val recyclerClickListener = listener

  val list = List(
    DrawerMenuItem(appContext.app.getString(R.string.schedule), R.drawable.menu_icon_schedule, SCHEDULE),
    DrawerMenuItem(appContext.app.getString(R.string.social), R.drawable.menu_icon_social, SOCIAL),
    DrawerMenuItem(appContext.app.getString(R.string.speakers), R.drawable.menu_icon_speakers, SPEAKERS),
    DrawerMenuItem(appContext.app.getString(R.string.tickets), R.drawable.menu_icon_tickets, SAMPLE),
    DrawerMenuItem(appContext.app.getString(R.string.contacts), R.drawable.menu_icon_contact, SAMPLE),
    DrawerMenuItem(appContext.app.getString(R.string.sponsors), R.drawable.menu_icon_sponsors, SAMPLE),
    DrawerMenuItem(appContext.app.getString(R.string.places), R.drawable.menu_icon_places, SAMPLE),
    DrawerMenuItem(appContext.app.getString(R.string.about), R.drawable.menu_icon_about, SAMPLE))

  override def onCreateViewHolder(parentViewGroup: ViewGroup, i: Int): ViewHolderMenuAdapter = {
    val adapter = new MenuAdapter()
    adapter.content.setOnClickListener(new OnClickListener {
      override def onClick(v: View): Unit = recyclerClickListener.onClick(list(v.getTag.asInstanceOf[Int]))
    })
    new ViewHolderMenuAdapter(adapter)
  }

  override def getItemCount: Int = list.size

  override def onBindViewHolder(viewHolder: ViewHolderMenuAdapter, position: Int): Unit = {
    val demoInfo = list(position)
    viewHolder.content.setTag(position)
    viewHolder.title.map { textView =>
      textView.setText(demoInfo.name)
      textView.setCompoundDrawablesWithIntrinsicBounds(demoInfo.icon, 0, 0, 0)
    }
  }
}

trait RecyclerClickListener {
  def onClick(info: DrawerMenuItem)
}

case class DrawerMenuItem(name: String, icon: Int, section: MenuSection)
