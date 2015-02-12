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

package com.fortysevendeg.android.scaladays.ui.components

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.{Checkable, FrameLayout}

class CheckableFrameLayout(context: Context, attrs: AttributeSet, defStyleAttr: Int)
    extends FrameLayout(context, attrs, defStyleAttr)
    with Checkable {

  var checked = false

  val CHECKED_STATE_SET = Array(android.R.attr.state_checked)

  def this(context: Context) = this(context, null, 0)

  def this(context: Context, attr: AttributeSet) = this(context, attr, 0)

  override def toggle(): Unit = setChecked(!checked)

  override def isChecked: Boolean = checked

  override def setChecked(checked: Boolean): Unit = {
    this.checked = checked
    refreshDrawableState()
    invalidate()
  }

  override def onCreateDrawableState(extraSpace: Int): Array[Int] = {
    val drawableState = super.onCreateDrawableState(extraSpace + 1)
    if (isChecked()) View.mergeDrawableStates(drawableState, CHECKED_STATE_SET)
    drawableState
  }
}
