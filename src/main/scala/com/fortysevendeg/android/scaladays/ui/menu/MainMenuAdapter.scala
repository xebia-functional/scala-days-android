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

package com.fortysevendeg.android.scaladays.ui.menu

import android.support.v7.widget.RecyclerView
import android.view.View.OnClickListener
import android.view.{View, ViewGroup}
import com.fortysevendeg.android.scaladays.R
import MenuSection._
import macroid.{IdGeneration, ActivityContextWrapper}
import com.fortysevendeg.macroid.extras.TextTweaks._
import macroid.FullDsl._

class MainMenuAdapter(listener: MainMenuClickListener)
    (implicit context: ActivityContextWrapper)
    extends RecyclerView.Adapter[ViewHolderMainMenuAdapter]
    with IdGeneration {
  
  var selectedItem: Option[MainMenuItem] = None

  val recyclerClickListener = listener

  val list = List(
    MainMenuItem(Id.schedule,
      context.application.getString(R.string.schedule), 
      R.drawable.menu_icon_schedule, SCHEDULE),
    MainMenuItem(Id.social,
      context.application.getString(R.string.social), 
      R.drawable.menu_icon_social, SOCIAL),
    MainMenuItem(Id.speaker,
      context.application.getString(R.string.speakers), 
      R.drawable.menu_icon_speakers, SPEAKERS),
    MainMenuItem(Id.tickets,
      context.application.getString(R.string.tickets), 
      R.drawable.menu_icon_tickets, TICKETS),
    MainMenuItem(Id.contacts,
      context.application.getString(R.string.contacts), 
      R.drawable.menu_icon_contact, CONTACTS),
    MainMenuItem(Id.sponsors,
      context.application.getString(R.string.sponsors), 
      R.drawable.menu_icon_sponsors, SPONSORS),
    MainMenuItem(Id.places,
      context.application.getString(R.string.places), 
      R.drawable.menu_icon_places, PLACES),
    MainMenuItem(Id.about,
      context.application.getString(R.string.about), 
      R.drawable.menu_icon_about, ABOUT))

  override def onCreateViewHolder(parentViewGroup: ViewGroup, i: Int): ViewHolderMainMenuAdapter = {
    val adapter = new MainMenuAdapterLayout()
    adapter.content.setOnClickListener(new OnClickListener {
      override def onClick(v: View): Unit = recyclerClickListener.onClick(list(v.getTag.asInstanceOf[Int]))
    })
    new ViewHolderMainMenuAdapter(adapter)
  }

  override def getItemCount: Int = list.size

  override def onBindViewHolder(viewHolder: ViewHolderMainMenuAdapter, position: Int): Unit = {
    val mainMenuItem = list(position)
    viewHolder.content.setTag(position)
    runUi(
      viewHolder.title <~ tvText(mainMenuItem.name) <~ tvCompoundDrawablesWithIntrinsicBounds(mainMenuItem.icon, 0, 0, 0)
    )
    
    selectedItem match {
      case Some(menuItem) if menuItem.id == mainMenuItem.id => viewHolder.content.setChecked(true)
      case _ => viewHolder.content.setChecked(false)
    }
  }
  
  def selectItem(menuItem: Option[MainMenuItem]) {
    selectedItem = menuItem
    notifyDataSetChanged()
  }
}

trait MainMenuClickListener {
  def onClick(mainMenuItem: MainMenuItem)
}

case class MainMenuItem(id: Int, name: String, icon: Int, section: MenuSection)
