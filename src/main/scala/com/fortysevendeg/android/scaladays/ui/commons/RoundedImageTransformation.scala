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

import android.graphics.Bitmap
import android.widget.ImageView
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.fortysevendeg.android.scaladays.components.RoundedDrawable
import macroid.AppContext
import macroid.FullDsl._
import scala.language.postfixOps

class RoundedImageTransformation(implicit appContext: AppContext)
    extends BitmapTransformation(appContext.get) {

  override def transform(pool: BitmapPool, source: Bitmap, outWidth: Int, outHeight: Int): Bitmap = {
    val res = appContext.get.getResources()
    val transformed = RoundedDrawable.fromBitmap(source)
        .setScaleType(ImageView.ScaleType.FIT_CENTER)
        .setCornerRadius(30 dp)
        .setOval(false)
        .toBitmap()
    if (!source.equals(transformed)) source.recycle()
    return transformed
  }

  override def getId: String = "roundedimage"

}
