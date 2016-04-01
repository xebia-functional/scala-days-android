/*
 *  Copyright (C) 2015 47 Degrees, LLC http://47deg.com hello@47deg.com
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

package com.fortysevendeg.android.scaladays.ui.schedule

import android.app.{Activity, Dialog}
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.view.Gravity
import android.view.ViewGroup.LayoutParams._
import android.widget.ImageView.ScaleType
import android.widget._
import com.fortysevendeg.android.scaladays.R
import com.fortysevendeg.android.scaladays.model.Event
import com.fortysevendeg.android.scaladays.modules.ComponentRegistryImpl
import com.fortysevendeg.android.scaladays.modules.net.VoteRequest
import com.fortysevendeg.android.scaladays.modules.preferences.PreferenceRequest
import com.fortysevendeg.android.scaladays.ui.commons.{VoteNeutral, VoteLike, VoteUnlike}
import com.fortysevendeg.macroid.extras.ImageViewTweaks._
import com.fortysevendeg.macroid.extras.LinearLayoutTweaks._
import com.fortysevendeg.macroid.extras.ResourcesExtras._
import com.fortysevendeg.macroid.extras.TextTweaks._
import com.fortysevendeg.macroid.extras.ViewTweaks._
import macroid.{Ui, ContextWrapper}
import macroid.FullDsl._

import scala.concurrent.ExecutionContext.Implicits.global

class VoteDialog(conferenceId: Int, event: Event)(implicit contextWrapper: ContextWrapper)
  extends DialogFragment
    with ComponentRegistryImpl
    with Styles {

  var infoContent = slot[LinearLayout]

  var votingContent = slot[LinearLayout]

  val defaultAndroidId = "not-found-android-id"

  val statusCodeOk = 200

  val androidId = "android_id"

  val contentGServices = "content://com.google.android.gsf.gservices"

  override val contextProvider: ContextWrapper = contextWrapper

  override def onCreateDialog(savedInstanceState: Bundle): Dialog = {
    val voteRequest = VoteRequest(
      vote = VoteUnlike,
      uid = getAndroidId getOrElse defaultAndroidId,
      talkId = event.id.toString,
      conferenceId = conferenceId.toString)

    val rootView = getUi(
      l[FrameLayout](
        l[LinearLayout](
          w[ProgressBar] <~ progressBarStyle,
          w[TextView] <~ votingStyle
        ) <~ votingContentStyle <~ wire(votingContent),
        l[LinearLayout](
          w[TextView] <~ titleStyle,
          w[TextView] <~ textStyle(event.title),
          l[LinearLayout](
            w[ImageView] <~ voteStyle(R.drawable.popup_icon_vote_like) <~ On.click {
              addVote(voteRequest.copy(vote = VoteLike))
            },
            w[ImageView] <~ voteStyle(R.drawable.popup_icon_vote_neutral) <~ On.click {
              addVote(voteRequest.copy(vote = VoteNeutral))
            },
            w[ImageView] <~ voteStyle(R.drawable.popup_icon_vote_unlike) <~ On.click {
              addVote(voteRequest.copy(vote = VoteUnlike))
            }
          )
        ) <~ contentStyle <~ wire(infoContent)
      )
    )

    new AlertDialog.Builder(getActivity).setView(rootView).create()
  }

  private[this] def addVote(voteRequest: VoteRequest) =
    (infoContent <~ vGone) ~
      (votingContent <~ vVisible) ~
      Ui {
        val responseIntent = new Intent
        netServices.addVote(voteRequest) map { response =>
          if (response.statusCode == statusCodeOk) {
            val key = ScheduleFragment.getPreferenceKeyForVote(conferenceId, event.id)
            preferenceServices.saveStringPreference(PreferenceRequest[String](key, voteRequest.vote.value))
            getTargetFragment.onActivityResult(getTargetRequestCode, Activity.RESULT_OK, responseIntent)
            dismiss()
          } else {
            getTargetFragment.onActivityResult(getTargetRequestCode, Activity.RESULT_CANCELED, responseIntent)
            dismiss()
          }
        } recover {
          case _ =>
            getTargetFragment.onActivityResult(getTargetRequestCode, Activity.RESULT_CANCELED, responseIntent)
            dismiss()
        }
      }

  private[this] def getAndroidId: Option[String] = {
    val cursor = Option(contextWrapper.application.getContentResolver.query(Uri.parse(contentGServices), null, null, Array(androidId), null))
    val result = cursor filter (c => c.moveToFirst && c.getColumnCount >= 2) map (_.getLong(1).toHexString.toUpperCase)
    cursor foreach (_.close())
    result
  }

}

trait Styles {

  def votingContentStyle(implicit contextWrapper: ContextWrapper) =
    vMatchParent +
      llVertical +
      vPaddings(resGetDimensionPixelSize(R.dimen.padding_default)) +
      llGravity(Gravity.CENTER) +
      vGone

  def progressBarStyle(implicit contextWrapper: ContextWrapper) =
    vWrapContent

  def votingStyle(implicit contextWrapper: ContextWrapper) =
    vWrapContent +
      vPaddings(resGetDimensionPixelSize(R.dimen.padding_default)) +
      tvSizeResource(R.dimen.text_big) +
      tvGravity(Gravity.CENTER) +
      tvText(R.string.sendingVote) +
      tvColorResource(R.color.text_vote_title)

  def contentStyle(implicit contextWrapper: ContextWrapper) =
    vMatchParent +
      llVertical +
      vPaddings(resGetDimensionPixelSize(R.dimen.padding_default)) +
      llGravity(Gravity.CENTER_HORIZONTAL)

  def titleStyle(implicit contextWrapper: ContextWrapper) =
    vWrapContent +
      vPaddings(resGetDimensionPixelSize(R.dimen.padding_default)) +
      tvSizeResource(R.dimen.text_big) +
      tvGravity(Gravity.CENTER) +
      tvText(R.string.titleVoteConference) +
      tvColorResource(R.color.text_vote_title) +
      tvBoldCondensed

  def textStyle(title: String)(implicit contextWrapper: ContextWrapper) =
    vWrapContent +
      vPaddings(resGetDimensionPixelSize(R.dimen.padding_default)) +
      tvGravity(Gravity.CENTER) +
      tvSizeResource(R.dimen.text_medium) +
      tvColorResource(R.color.text_vote_text) +
      tvText(title)

  def votesContentStyle =
    vMatchHeight +
      llHorizontal

  def voteStyle(res: Int)(implicit contextWrapper: ContextWrapper) =
    lp[LinearLayout](0, WRAP_CONTENT, 1) +
      ivScaleType(ScaleType.CENTER_INSIDE) +
      vPaddings(resGetDimensionPixelSize(R.dimen.padding_default)) +
      ivSrc(res)

}