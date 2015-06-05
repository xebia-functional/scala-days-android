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

package com.fortysevendeg.android.scaladays.ui.scheduledetail

import android.content.Intent
import android.net.Uri
import android.widget._
import com.fortysevendeg.android.scaladays.R
import com.fortysevendeg.android.scaladays.model.{Event, Speaker}
import com.fortysevendeg.android.scaladays.modules.ComponentRegistryImpl
import com.fortysevendeg.android.scaladays.modules.analytics.AnalyticsServicesComponent
import com.fortysevendeg.android.scaladays.modules.preferences.{PreferenceRequest, PreferenceServicesComponent}
import com.fortysevendeg.android.scaladays.ui.commons.AnalyticStrings._
import com.fortysevendeg.android.scaladays.ui.commons.{ToolbarLayout, UiServices}
import com.fortysevendeg.android.scaladays.ui.components.IconTypes
import com.fortysevendeg.android.scaladays.ui.components.PathMorphDrawableTweaks._
import com.fortysevendeg.macroid.extras.ResourcesExtras._
import com.fortysevendeg.macroid.extras.ViewTweaks._
import macroid.FullDsl._
import macroid.{ActivityContextWrapper, ContextWrapper, Ui}

import scala.language.postfixOps

trait Layout
    extends ToolbarLayout
    with ActivityStyles {

  self: UiServices with PreferenceServicesComponent with AnalyticsServicesComponent =>

  protected var favoriteChanged = false

  var titleToolbar = slot[TextView]

  var fabFavorite = slot[ImageView]

  def layout(event: Event, timeZone: String)(implicit context: ActivityContextWrapper) = {
    val namePreferenceFavorite = getNamePreferenceFavorite(event.id)
    val isFavorite = preferenceServices.fetchBooleanPreference(PreferenceRequest[Boolean](
      namePreferenceFavorite, false)).value

    val showSpeakers = event.speakers.nonEmpty

    getUi(
      l[FrameLayout](
        l[ScrollView](
          l[LinearLayout](
            l[LinearLayout](
              w[ImageView] <~ iconCalendarStyle,
              l[LinearLayout](
                w[TextView] <~ dateStyle(event.startTime, timeZone),
                w[TextView] <~ trackStyle(event.track),
                w[TextView] <~ roomStyle(event.location),
                w[TextView] <~ descriptionStyle(event.description),
                w[ImageView] <~ lineStyle,
                w[TextView] <~ speakerTitleStyle(showSpeakers)
              ) <~ verticalLayoutStyle
            ) <~ descriptionContentLayoutStyle,
            l[LinearLayout]() <~ speakersContentLayoutStyle(event.speakers)
          ) <~ contentStyle
        ) <~ scrollContentStyle,
        expandedToolBarLayout(
          w[TextView] <~ wire(titleToolbar) <~ toolBarTitleStyle(event.title)
        )(resGetDimensionPixelSize(R.dimen.height_toolbar_expanded)),
        w[ImageView] <~ wire(fabFavorite) <~ fabStyle(isFavorite) <~ On.click {
          favoriteClick(event.title, namePreferenceFavorite)
        }
      ) <~ rootStyle
    )
  }

  private def favoriteClick(eventTitle: String, name: String)(implicit context: ActivityContextWrapper) = {
    favoriteChanged = true
    val isFavorite = preferenceServices.fetchBooleanPreference(PreferenceRequest[Boolean](name, false)).value
    if (isFavorite) {
      analyticsServices.sendEvent(
        screenName = Some(analyticsScheduleDetailScreen),
        category = analyticsCategoryFavorites,
        action = analyticsScheduleActionRemoveToFavorites,
        label = Some(eventTitle))
      preferenceServices.saveBooleanPreference(PreferenceRequest[Boolean](name, false))
      fabFavorite <~ pmdAnimIcon(IconTypes.ADD) <~ vBackground(R.drawable.fab_button_no_check) <~ vPaddings(resGetDimensionPixelSize(R.dimen.padding_schedule_detail_fab))
    } else {
      analyticsServices.sendEvent(
        screenName = Some(analyticsScheduleDetailScreen),
        category = analyticsCategoryFavorites,
        action = analyticsScheduleActionAddToFavorites,
        label = Some(eventTitle))
      preferenceServices.saveBooleanPreference(PreferenceRequest[Boolean](name, true))
      fabFavorite <~ pmdAnimIcon(IconTypes.CHECK) <~ vBackground(R.drawable.fab_button_check) <~ vPaddings(resGetDimensionPixelSize(R.dimen.padding_schedule_detail_fab))
    }
  }

}

class SpeakersDetailLayout(speaker: Speaker)(implicit context: ActivityContextWrapper)
    extends SpeakersDetailStyles
    with ComponentRegistryImpl {

  val content = layout

  private def layout(implicit context: ActivityContextWrapper) = getUi(
    l[LinearLayout](
      w[ImageView] <~ speakerAvatarStyle(speaker.picture),
      l[LinearLayout](
        w[TextView] <~ speakerNameItemStyle(speaker.name),
        w[TextView] <~ speakerCompanyItemStyle(speaker.company),
        w[TextView] <~ speakerTwitterItemStyle(speaker.twitter),
        w[TextView] <~ speakerBioItemStyle(speaker.bio)
      ) <~ verticalLayoutStyle
    ) <~ itemSpeakerContentStyle <~ On.click {
      Ui {
        speaker.twitter map {
          twitterName =>
            val twitterUser = if (twitterName.startsWith("@")) twitterName.substring(1) else twitterName
            analyticsServices.sendEvent(
              screenName = Some(analyticsScheduleDetailScreen),
              category = analyticsCategoryNavigate,
              action = analyticsSpeakersActionGoToUser)
            context.getOriginal.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(resGetString(R.string.url_twitter_user, twitterUser))))
        }
      }
    }
  )

  override val contextProvider: ContextWrapper = context
}
