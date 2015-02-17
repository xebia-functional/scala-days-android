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

package com.fortysevendeg.android.scaladays.ui.places

import android.view.View
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter
import com.google.android.gms.maps.model.Marker
import macroid.{ActivityContext, AppContext}
import macroid.FullDsl._

class PlacesInfoWindowAdapter(implicit appContext: AppContext, context: ActivityContext) 
  extends InfoWindowAdapter {

  lazy val layout = new PlacesInfoWindowLayout
  
  override def getInfoWindow(marker: Marker): View = {
    renderInfo(marker)
  }

  override def getInfoContents(marker: Marker): View = {
    renderInfo(marker)
  }
  
  def renderInfo(marker: Marker): View = {
    runUi(
      (layout.title <~ text(marker.getTitle)) ~
        (layout.snippet <~ text(marker.getSnippet.replaceAll("\n\r\n", "\n")))
    )
    layout.content
  }
  
}
