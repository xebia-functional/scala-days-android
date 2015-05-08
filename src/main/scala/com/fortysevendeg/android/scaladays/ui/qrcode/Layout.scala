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

package com.fortysevendeg.android.scaladays.ui.qrcode

import android.widget._
import macroid.FullDsl._
import macroid.ActivityContextWrapper

trait Layout
  extends Styles {

  var scanButton = slot[Button]

  def content(implicit context: ActivityContextWrapper) = getUi(
    l[LinearLayout](
      w[ImageView] <~ qrImageStyle,
      w[TextView] <~ qrMessageStyle,
      w[Button] <~ qrButtonStyle <~ wire(scanButton)
    ) <~ qrContentStyle
  )

}
