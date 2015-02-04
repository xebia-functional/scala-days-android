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

package com.fortysevendeg.android.scaladays.ui.speakers

import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.State
import com.fortysevendeg.android.scaladays.R
import macroid.AppContext
import macroid.FullDsl._

import scala.language.postfixOps

class SpeakerItemDecorator(implicit appContext: AppContext) extends RecyclerView.ItemDecoration {

  val divider = new ColorDrawable(appContext.get.getResources.getColor(R.color.list_line_default))

  override def onDrawOver(c: Canvas, parent: RecyclerView, state: State): Unit = {
    val left = parent.getPaddingLeft() + (72 dp)
    val right = parent.getWidth() - parent.getPaddingRight()

    val childCount = parent.getChildCount()
    for (i <- 0 to childCount - 1) {
      val child = parent.getChildAt(i)
      val params = child.getLayoutParams().asInstanceOf[RecyclerView.LayoutParams]
      val top = child.getBottom() + params.bottomMargin
      val bottom = top + 1
      divider.setBounds(left, top, right, bottom)
      divider.draw(c)
    }
  }
}
