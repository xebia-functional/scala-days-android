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
import android.content.DialogInterface.{OnClickListener, OnShowListener}
import android.content.{DialogInterface, Intent}
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.text.InputFilter.LengthFilter
import android.view.{Gravity, View}
import android.view.ViewGroup.LayoutParams._
import android.widget.ImageView.ScaleType
import android.widget._
import com.fortysevendeg.android.scaladays.R
import com.fortysevendeg.android.scaladays.model.Event
import com.fortysevendeg.android.scaladays.modules.ComponentRegistryImpl
import com.fortysevendeg.android.scaladays.modules.net.VoteRequest
import com.fortysevendeg.android.scaladays.modules.preferences.PreferenceRequest
import com.fortysevendeg.android.scaladays.ui.commons.AnalyticStrings._
import com.fortysevendeg.android.scaladays.ui.commons.{Vote, VoteLike, VoteNeutral, VoteUnlike}
import macroid.extras.ImageViewTweaks._
import macroid.extras.LinearLayoutTweaks._
import macroid.extras.ResourcesExtras._
import macroid.extras.TextViewTweaks._
import macroid.extras.UIActionsExtras._
import macroid.extras.ViewTweaks._
import macroid._
import macroid.FullDsl._
import macroid.{ContextWrapper, Transformer, Tweak, Ui}

import scala.concurrent.ExecutionContext.Implicits.global

class VoteDialog
  extends DialogFragment
    with ComponentRegistryImpl
    with Styles { self =>

  override implicit lazy val contextProvider: ContextWrapper = ContextWrapper(self)

  def conferenceId: Int = Option(getArguments).map(_.getInt("conferenceId", 0)).getOrElse(0)
  def eventId: Int = Option(getArguments).map(_.getInt("eventId", 0)).getOrElse(0)
  def eventTitle: String = Option(getArguments).map(_.getString("eventTitle", "")).getOrElse("")

  var infoContent: Option[LinearLayout] = slot[LinearLayout]

  var votingContent: Option[LinearLayout] = slot[LinearLayout]

  var messageVote: Option[EditText] = slot[EditText]

  val defaultAndroidId = "not-found-android-id"

  val statusCodeOk = 200

  val androidId = "android_id"

  val contentGServices = "content://com.google.android.gsf.gservices"

  lazy val storedVote: Option[StoredVote] = {
    val voteValue = preferenceServices.fetchStringPreference(PreferenceRequest[String](
      ScheduleFragment.getPreferenceKeyForVote(conferenceId, eventId), null)).value

    Option(voteValue) map { v =>
      val message = Option(preferenceServices.fetchStringPreference(PreferenceRequest[String](
        ScheduleFragment.getPreferenceKeyForVoteMessage(conferenceId, eventId), null)).value)
      StoredVote(Vote.apply(v), message)
    }
  }

  var newVote: Option[StoredVote] = None

  override def onCreateDialog(savedInstanceState: Bundle): Dialog = {
    val voteRequest = VoteRequest(
      vote = VoteUnlike,
      uid = getAndroidId getOrElse defaultAndroidId,
      talkId = eventId.toString,
      conferenceId = conferenceId.toString)

    analyticsServices.sendEvent(
      Some(analyticsScheduleListScreen),
      analyticsCategoryVote,
      analyticsScheduleActionShowVotingDialog)

    newVote = storedVote

    val rootView = Ui.get(
      l[FrameLayout](
        l[LinearLayout](
          w[ProgressBar] <~ progressBarStyle,
          w[TextView] <~ votingStyle
        ) <~ votingContentStyle <~ wire(votingContent),
        l[LinearLayout](
          w[TextView] <~ titleStyle,
          w[TextView] <~ textStyle(eventTitle),
          l[LinearLayout](
            createIcon(VoteUnlike),
            createIcon(VoteNeutral),
            createIcon(VoteLike)
          ),
          w[EditText] <~ messageStyle(storedVote.flatMap(_.message)) <~ wire(messageVote)
        ) <~ contentStyle <~ wire(infoContent)
      )
    )
    val dialog = new AlertDialog.Builder(getActivity).
      setView(rootView).
      setPositiveButton(R.string.send, null).
      setNegativeButton(R.string.cancel, new OnClickListener {
        override def onClick(dialog: DialogInterface, which: Int): Unit = dialog.dismiss()
      }).
      create()
    dialog.setOnShowListener(new OnShowListener {

      def clickButtonPositive(dialog: DialogInterface): Unit = dialog match {
        case d: AlertDialog =>
          d.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener {
            override def onClick(view: View): Unit = {
              (newVote map { v =>
                val text = messageVote map (_.getText.toString) getOrElse ""
                addVote(voteRequest.copy(vote = v.vote, message = Some(text)))
              } getOrElse uiShortToast(R.string.fillFieldsVoteDialog)).run
            }
          })
        case _ =>
      }

      override def onShow(dialog: DialogInterface): Unit = {
        clickButtonPositive(dialog)
        if (storedVote.isEmpty) enableButtonPositive(dialog, enable = false)
      }
    })
    dialog
  }

  private[this] def enableButtonPositive(dialog: DialogInterface, enable: Boolean) = dialog match {
    case d: AlertDialog => d.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(enable)
    case _ =>
  }

  private[this] def hideButtons(dialog: DialogInterface) = dialog match {
    case d: AlertDialog =>
      d.getButton(DialogInterface.BUTTON_POSITIVE).setVisibility(View.GONE)
      d.getButton(DialogInterface.BUTTON_NEGATIVE).setVisibility(View.GONE)
    case _ =>
  }

  private[this] def createIcon(vote: Vote) = {
    w[ImageView] <~
      vTag(vote.value) <~
      voteStyle(vote, storedVote.exists(_.vote == vote)) <~
      On.click {
        Ui {
          enableButtonPositive(getDialog, enable = true)
          newVote = Option(newVote map (_.copy(vote = vote)) getOrElse StoredVote(vote, None))
        } ~ (infoContent <~ changeVote(vote))
      }
  }

  private[this] def addVote(voteRequest: VoteRequest): Ui[Any] =
    (infoContent <~ vGone) ~
      (votingContent <~ vVisible) ~
      Ui {
        hideButtons(getDialog)
        val responseIntent = new Intent
        netServices.addVote(voteRequest) map { response =>
          if (response.statusCode == statusCodeOk) {

            analyticsServices.sendEvent(
              Some(analyticsScheduleListScreen),
              analyticsCategoryVote,
              analyticsScheduleActionSendVote)

            val key = ScheduleFragment.getPreferenceKeyForVote(conferenceId, eventId)
            preferenceServices.saveStringPreference(PreferenceRequest[String](key, voteRequest.vote.value))
            voteRequest.message foreach { message =>
              val keyMessage = ScheduleFragment.getPreferenceKeyForVoteMessage(conferenceId, eventId)
              preferenceServices.saveStringPreference(PreferenceRequest[String](keyMessage, message))
            }
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
    val cursor = Option(contextProvider.application.getContentResolver.query(Uri.parse(contentGServices), null, null, Array(androidId), null))
    val result = cursor filter (c => c.moveToFirst && c.getColumnCount >= 2) map (_.getLong(1).toHexString.toUpperCase)
    cursor foreach (_.close())
    result
  }

}

object VoteDialog {
  def apply(conferenceId: Int, event: Event): VoteDialog = {
    val bundle = new Bundle()
    bundle.putInt("conferenceId", conferenceId)
    bundle.putInt("eventId", event.id)
    bundle.putString("eventTitle", event.title)
    val dialog = new VoteDialog()
    dialog.setArguments(bundle)
    dialog
  }
}

case class StoredVote(vote: Vote, message: Option[String])

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

  def messageStyle(message: Option[String])(implicit contextWrapper: ContextWrapper) =
    vMatchWidth +
      vPaddings(resGetDimensionPixelSize(R.dimen.padding_default)) +
      tvSizeResource(R.dimen.text_medium) +
      tvGravity(Gravity.TOP) +
      tvHint(R.string.addMessage) +
      Tweak[TextView] { view =>
        view.setHintTextColor(resGetColor(R.color.text_vote_hint_message))
        view.setFilters(Seq(new LengthFilter(500)).toArray)
      } +
      tvLines(2) +
      (message map tvText getOrElse Tweak.blank)

  def votesContentStyle =
    vMatchHeight +
      llHorizontal

  def voteStyle(vote: Vote, selected: Boolean)(implicit contextWrapper: ContextWrapper) =
    lp[LinearLayout](0, WRAP_CONTENT, 1) +
      ivScaleType(ScaleType.CENTER_INSIDE) +
      vPaddings(resGetDimensionPixelSize(R.dimen.padding_default)) +
      selectIconVote(vote, selected)

  def selectIconVote(vote: Vote, selected: Boolean): Tweak[ImageView] = {
    val res = (vote, selected) match {
      case (VoteLike, true) => R.drawable.popup_icon_vote_like
      case (VoteUnlike, true) => R.drawable.popup_icon_vote_unlike
      case (VoteNeutral, true) => R.drawable.popup_icon_vote_neutral
      case (VoteLike, false) => R.drawable.popup_icon_vote_like_disabled
      case (VoteUnlike, false) => R.drawable.popup_icon_vote_unlike_disabled
      case (VoteNeutral, false) => R.drawable.popup_icon_vote_neutral_disabled
    }
    ivSrc(res)
  }

  def changeVote(vote: Vote): Transformer = Transformer {
    case i: ImageView if Option(i.getTag).map(_.toString).contains(vote.value) => i <~ selectIconVote(vote, selected = true)
    case i: ImageView => i <~ selectIconVote(vote, selected = false)
  }

}