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

import android.widget.{TextView, ImageView}
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.fortysevendeg.android.scaladays.utils.DateTimeUtils
import macroid.{AppContext, Tweak}
import org.joda.time.DateTime

object GlideTweaks {
  type W = ImageView

  def glideRoundedImage(
      url: String,
      placeHolder: Int
      )(implicit appContext: AppContext): Tweak[W] = Tweak[W](
    imageView => {
      Glide.`with`(appContext.get)
          .load(url)
          .transform(new CenterCrop(appContext.get), new RoundedImageTransformation)
          .placeholder(placeHolder)
          .into(imageView)
    }
  )

  def glideCenterCrop(
      url: String,
      placeHolder: Int
      )(implicit appContext: AppContext): Tweak[W] = Tweak[W](
    imageView => {
      Glide.`with`(appContext.get)
          .load(url)
          .centerCrop()
          .placeholder(placeHolder)
          .into(imageView)
    }
  )
}

object DateTimeTextViewTweaks {
  type W = TextView

  def tvDateTimeHourMinute(dateTime: DateTime, timeZone: String = "UTC"): Tweak[W] =
    Tweak[W](_.setText(DateTimeUtils.convertTimeZone(dateTime, timeZone).toString(DateTimeUtils.ISODateFormatterTime)))

}
