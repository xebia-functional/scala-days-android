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

import android.widget.{Button, ImageView, LinearLayout, TextView}
import com.fortysevendeg.android.scaladays.R
import com.fortysevendeg.macroid.extras.TextTweaks._
import com.fortysevendeg.macroid.extras.ImageViewTweaks._
import com.fortysevendeg.macroid.extras.ViewTweaks._
import macroid.{Ui, ActivityContextWrapper}
import macroid.FullDsl._

trait PlaceHolderLayout
  extends PlaceHolderStyles {

  var reloadButton = slot[Button]

  var image = slot[ImageView]

  var text = slot[TextView]

  def placeholder(implicit context: ActivityContextWrapper) = {
    l[LinearLayout](
      w[ImageView] <~ placeholderImageStyle <~ wire(image),
      w[TextView] <~ placeholderMessageStyle <~ wire(text),
      w[Button] <~ placeholderButtonStyle <~ wire(reloadButton)
    ) <~ placeholderContentStyle
  }

  def loadFailed(): Ui[_] = load(R.string.generalMessageError, R.drawable.placeholder_error)

  def loadEmpty(): Ui[_] = load(R.string.generalMessageEmpty, R.drawable.placeholder_general)

  def loadNoFavorites(): Ui[_] = load(R.string.noFavoritesMessage, R.drawable.placeholder_favorite, false)

  private def load(messageRes: Int, imageRes: Int, showButton: Boolean = true): Ui[_] =
    (text <~ tvText(messageRes)) ~
      (image <~ ivSrc(imageRes)) ~
      (reloadButton <~ (if (showButton) vVisible else vGone))

}
