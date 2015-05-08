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

package com.fortysevendeg.android.scaladays.ui.sponsors

import android.support.v7.widget.RecyclerView
import android.view.View.OnClickListener
import android.view.{View, ViewGroup}
import com.fortysevendeg.android.scaladays.R
import com.fortysevendeg.android.scaladays.ui.commons.AsyncImageTweaks._
import com.fortysevendeg.android.scaladays.ui.commons.{HeaderLayoutAdapter, ViewHolderHeaderAdapter}
import com.fortysevendeg.android.scaladays.ui.sponsors.SponsorsAdapter._
import com.fortysevendeg.macroid.extras.TextTweaks._
import com.fortysevendeg.macroid.extras.ViewTweaks._
import macroid.FullDsl._
import macroid.ActivityContextWrapper

class SponsorsAdapter(sponsorItems: Seq[SponsorItem], listener: RecyclerClickListener)
    (implicit context: ActivityContextWrapper)
    extends RecyclerView.Adapter[RecyclerView.ViewHolder] {

  val recyclerClickListener = listener

  override def onCreateViewHolder(parentViewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder = {
    viewType match {
      case `itemViewTypeSponsor` =>
        val adapter = new SponsorsLayoutAdapter()
        adapter.content.setOnClickListener(new OnClickListener {
          override def onClick(v: View): Unit = recyclerClickListener.onClick(sponsorItems(v.getTag.asInstanceOf[Int]))
        })
        new ViewHolderSponsorsAdapter(adapter)
      case `itemViewTypeHeader` =>
        val adapter = new HeaderLayoutAdapter()
        adapter.content.setOnClickListener(new OnClickListener {
          override def onClick(v: View): Unit = recyclerClickListener.onClick(sponsorItems(v.getTag.asInstanceOf[Int]))
        })
        new ViewHolderHeaderAdapter(adapter)
    }

  }

  override def getItemCount: Int = sponsorItems.size

  override def onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int): Unit = {
    val sponsorItem = sponsorItems(position)
    getItemViewType(position) match {
      case `itemViewTypeSponsor` =>
        val vh = viewHolder.asInstanceOf[ViewHolderSponsorsAdapter]
        sponsorItem.sponsor map {
          sponsor =>
            vh.content.setTag(position)
            runUi(vh.logo <~ srcImage(sponsor.logo))
        }
      case `itemViewTypeHeader` =>
        val vh = viewHolder.asInstanceOf[ViewHolderHeaderAdapter]
        runUi(
          vh.headerName <~ sponsorItem.header.map(tvText(_) + vVisible).getOrElse(vGone)
        )
    }
  }

  override def getItemViewType(position: Int): Int = if (sponsorItems(position).isHeader) itemViewTypeHeader else itemViewTypeSponsor

}

object SponsorsAdapter {
  val itemViewTypeHeader = 0
  val itemViewTypeSponsor = 1
}

trait RecyclerClickListener {
  def onClick(sponsorItem: SponsorItem)
}

