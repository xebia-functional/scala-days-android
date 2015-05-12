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
import com.fortysevendeg.android.scaladays.model.Speaker
import com.fortysevendeg.android.scaladays.modules.ComponentRegistryImpl
import com.fortysevendeg.android.scaladays.modules.analytics.AnalyticsServicesComponent
import com.fortysevendeg.android.scaladays.ui.commons.AnalyticStrings._
import com.fortysevendeg.android.scaladays.ui.commons.ToolbarLayout
import com.fortysevendeg.macroid.extras.ResourcesExtras._
import macroid.FullDsl._
import macroid.{ContextWrapper, Ui, ActivityContextWrapper}

import scala.language.postfixOps

trait Layout
    extends ToolbarLayout
    with ActivityStyles {

  var titleToolbar = slot[TextView]

  var date = slot[TextView]

  var track = slot[TextView]

  var room = slot[TextView]

  var description = slot[TextView]

  var speakersContent = slot[LinearLayout]

  var speakerTitle = slot[TextView]

  var fabFavorite = slot[ImageView]

  def layout(favorite: Boolean)(implicit context: ActivityContextWrapper) = {
    getUi(
      l[FrameLayout](
        l[ScrollView](
          l[LinearLayout](
            l[LinearLayout](
              w[ImageView] <~ iconCalendarStyle,
              l[LinearLayout](
                w[TextView] <~ wire(date) <~ dateStyle,
                w[TextView] <~ wire(track) <~ trackStyle,
                w[TextView] <~ wire(room) <~ roomStyle,
                w[TextView] <~ wire(description) <~ descriptionStyle,
                w[ImageView] <~ lineStyle,
                w[TextView] <~ speakerTitleStyle <~ wire(speakerTitle)
              ) <~ verticalLayoutStyle
            ) <~ descriptionContentLayoutStyle,
            l[LinearLayout]() <~ wire(speakersContent) <~ speakersContentLayoutStyle
          ) <~ contentStyle
        ) <~ scrollContentStyle,
        expandedToolBarLayout(
          w[TextView] <~ wire(titleToolbar) <~ toolBarTitleStyle
        )(resGetDimensionPixelSize(R.dimen.height_toolbar_expanded)),
        w[ImageView] <~ fabStyle(favorite) <~ wire(fabFavorite)
      ) <~ rootStyle
    )
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
