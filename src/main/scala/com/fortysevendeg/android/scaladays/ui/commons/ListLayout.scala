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

package com.fortysevendeg.android.scaladays.ui.commons

import android.support.v4.widget.SwipeRefreshLayout
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener
import android.support.v7.widget.RecyclerView
import android.widget.{FrameLayout, LinearLayout, ProgressBar}
import com.fortysevendeg.macroid.extras.RecyclerViewTweaks._
import macroid.FullDsl._
import macroid.{Tweak, Ui, ActivityContextWrapper}
import com.fortysevendeg.macroid.extras.ViewTweaks._

trait ListLayout
  extends ListStyles
  with PlaceHolderLayout {

  var recyclerView = slot[RecyclerView]

  var progressBar = slot[ProgressBar]

  var refreshLayout = slot[SwipeRefreshLayout]

  var placeholderContent = slot[LinearLayout]

  def content(implicit context: ActivityContextWrapper) = getUi(
    l[FrameLayout](
      w[ProgressBar] <~ wire(progressBar) <~ progressBarStyle,
      l[SwipeRefreshLayout](
        w[RecyclerView] <~ wire(recyclerView) <~ recyclerViewStyle
      ) <~ wire(refreshLayout),
      placeholder <~ wire(placeholderContent)
    ) <~ rootStyle
  )

  def loading(): Ui[_] = (progressBar <~ vVisible) ~
    (recyclerView <~ vGone) ~
    (placeholderContent <~ vGone)

  def failed(): Ui[_] = loadFailed() ~
    (progressBar <~ vGone) ~
    (recyclerView <~ vGone) ~
    (placeholderContent <~ vVisible)

  def empty(): Ui[_] = loadEmpty() ~
    (progressBar <~ vGone) ~
    (recyclerView <~ vGone) ~
    (placeholderContent <~ vVisible)

  def noFavorites(): Ui[_] = loadNoFavorites() ~
    (progressBar <~ vGone) ~
    (recyclerView <~ vGone) ~
    (placeholderContent <~ vVisible)

  def adapter[VH <: RecyclerView.ViewHolder](adapter: RecyclerView.Adapter[VH]): Ui[_] = (progressBar <~ vGone) ~
    (placeholderContent <~ vGone) ~
    (recyclerView <~ vVisible <~ rvAdapter(adapter))

  def srlRefreshing(refreshing: Boolean) = Tweak[SwipeRefreshLayout] (_.setRefreshing(refreshing))

  def srlOnRefreshListener(f: => Ui[_]) = Tweak[SwipeRefreshLayout] (_.setOnRefreshListener(new OnRefreshListener {
    override def onRefresh(): Unit = {
      runUi(f)
    }
  }))

}