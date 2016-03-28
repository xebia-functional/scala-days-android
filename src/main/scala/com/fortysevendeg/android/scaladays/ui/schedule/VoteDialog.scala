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

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.view.ViewGroup.LayoutParams._
import android.view.{Gravity, ViewGroup}
import android.widget.ImageView.ScaleType
import android.widget.{ImageView, LinearLayout, TextView}
import com.fortysevendeg.android.scaladays.R
import com.fortysevendeg.android.scaladays.model.Event
import com.fortysevendeg.macroid.extras.ResourcesExtras._
import com.fortysevendeg.macroid.extras.ImageViewTweaks._
import com.fortysevendeg.macroid.extras.LinearLayoutTweaks._
import com.fortysevendeg.macroid.extras.TextTweaks._
import com.fortysevendeg.macroid.extras.ViewTweaks._
import macroid.ContextWrapper
import macroid.FullDsl._

class VoteDialog(event: Event)(implicit contextWrapper: ContextWrapper)
  extends DialogFragment
  with Styles {

  override def onCreateDialog(savedInstanceState: Bundle): Dialog = {

    val rootView = getUi(
      l[LinearLayout](
        w[TextView] <~ titleStyle,
        w[TextView] <~ textStyle(event.title),
        l[LinearLayout](
          w[ImageView] <~ voteStyle(R.drawable.popup_icon_vote_like),
          w[ImageView] <~ voteStyle(R.drawable.popup_icon_vote_neutral),
          w[ImageView] <~ voteStyle(R.drawable.popup_icon_vote_unlike)
        )
      ) <~ contentStyle
    )

    new AlertDialog.Builder(getActivity).setView(rootView).create()
  }

}

trait Styles {

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