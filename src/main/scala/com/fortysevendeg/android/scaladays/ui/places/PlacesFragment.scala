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

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import com.fortysevendeg.android.scaladays.model.Venue
import com.fortysevendeg.android.scaladays.modules.ComponentRegistryImpl
import com.fortysevendeg.android.scaladays.ui.commons.AnalyticStrings._
import com.fortysevendeg.android.scaladays.ui.commons.UiServices
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener
import com.google.android.gms.maps._
import com.google.android.gms.maps.model.{BitmapDescriptorFactory, LatLng, Marker, MarkerOptions}
import macroid.{ContextWrapper, Contexts, Ui}

import scala.concurrent.ExecutionContext.Implicits.global

class PlacesFragment
  extends SupportMapFragment
  with Contexts[Fragment]
  with ComponentRegistryImpl
  with UiServices
  with OnMapReadyCallback {

  override implicit lazy val contextProvider: ContextWrapper = fragmentContextWrapper

  val defaultZoom = 12

  private var googleMap: Option[GoogleMap] = None

  private val markersMap = scala.collection.mutable.Map[String, String]()

  override def onCreate(savedInstanceState: Bundle): Unit = {
    super.onCreate(savedInstanceState)
    analyticsServices.sendScreenName(analyticsPlacesScreen)
    getMapAsync(this)
  }

  override def onMapReady(googleMap: GoogleMap): Unit = {
    this.googleMap = Some(googleMap)
    googleMap.setInfoWindowAdapter(new PlacesInfoWindowAdapter)
    googleMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener {
      override def onInfoWindowClick(marker: Marker): Unit = {
        markersMap.get(marker.getId) foreach openLink
      }
    })
    val result = for {
      conference <- loadSelectedConference()
    } yield showVenueMarkers(conference.venues)

    result.recover {
      case _ => failed()
    }
  }

  def openLink(link: String): Unit = {
    analyticsServices.sendEvent(
      screenName = Some(analyticsPlacesScreen),
      category = analyticsCategoryNavigate,
      action = analyticsPlacesActionGoToMap)
    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(link)))
  }

  private def showVenueMarkers(venues: Seq[Venue]) = {
    val uis = (venues map createMarker) :+ (venues.headOption map { venue =>
      Ui {
        googleMap foreach (_.moveCamera(toCameraUpdate(venue)))
      }
    } getOrElse Ui.nop)
    Ui.run(Ui.sequence(uis :_*))
  }

  private def createMarker(venue: Venue): Ui[_] = {
    val markerOptions = new MarkerOptions()
      .title(venue.name)
      .snippet(venue.address)
      .icon(BitmapDescriptorFactory.fromResource(com.fortysevendeg.android.scaladays.R.drawable.map_pushpin))
      .position(new LatLng(venue.latitude, venue.longitude))
    addMarkerToMap(venue, markerOptions)
  }

  private def addMarkerToMap(venue: Venue, markerOptions: MarkerOptions): Ui[_] =
    Ui {
      googleMap map { googleMapValue =>
        markersMap.put(googleMapValue.addMarker(markerOptions).getId, venue.website)
      }
    }

  private def toCameraUpdate(venue: Venue): CameraUpdate =
    CameraUpdateFactory.newLatLngZoom(new LatLng(venue.latitude, venue.longitude), venue.zoom)

  def failed(): Unit = {}

}
