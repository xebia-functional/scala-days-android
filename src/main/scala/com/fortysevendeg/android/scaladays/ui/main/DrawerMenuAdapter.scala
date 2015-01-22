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
import macroid.{ActivityContext, AppContext}

class DrawerMenuAdapter(listener: RecyclerClickListener)
    (implicit context: ActivityContext, appContext: AppContext)
    extends RecyclerView.Adapter[ViewHolder] {

  val recyclerClickListener = listener

  val list = List(
    DrawerMenuItem(appContext.app.getString(R.string.menuSchedule), R.drawable.ic_calendar),
    DrawerMenuItem(appContext.app.getString(R.string.menuSocial), R.drawable.ic_message_reply),
    DrawerMenuItem(appContext.app.getString(R.string.menuTickets), R.drawable.ic_tag),
    DrawerMenuItem(appContext.app.getString(R.string.menuSponsors), R.drawable.ic_flag),
    DrawerMenuItem(appContext.app.getString(R.string.menuPlaces), R.drawable.ic_map_marker),
    DrawerMenuItem(appContext.app.getString(R.string.menuAbout), R.drawable.ic_information_outline))

  override def onCreateViewHolder(parentViewGroup: ViewGroup, i: Int): ViewHolder = {
    val adapter = new Adapter
    adapter.layout.setOnClickListener(new OnClickListener {
      override def onClick(v: View): Unit = recyclerClickListener.onClick(list(v.getTag.asInstanceOf[Int]))
    })
    new ViewHolder(adapter)
  }

  override def getItemCount: Int = list.size

  override def onBindViewHolder(viewHolder: ViewHolder, position: Int): Unit = {
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

class ViewHolder(adapter: Adapter)(implicit context: ActivityContext, appContext: AppContext) extends RecyclerView.ViewHolder(adapter.layout) {

  var content = adapter.layout

  var title = adapter.menuItem

}

case class DrawerMenuItem(name: String, icon: Int)
