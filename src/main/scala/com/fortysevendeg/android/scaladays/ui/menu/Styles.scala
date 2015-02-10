package com.fortysevendeg.android.scaladays.ui.menu

import android.graphics.Color
import android.view.Gravity
import android.view.ViewGroup.LayoutParams._
import android.widget.ImageView.ScaleType
import android.widget.{LinearLayout, FrameLayout, AbsListView}
import com.fortysevendeg.android.scaladays.R
import com.fortysevendeg.macroid.extras.FrameLayoutTweaks._
import com.fortysevendeg.macroid.extras.LinearLayoutTweaks._
import com.fortysevendeg.macroid.extras.TextTweaks._
import com.fortysevendeg.macroid.extras.ImageViewTweaks._
import com.fortysevendeg.macroid.extras.ViewTweaks._
import macroid.AppContext
import macroid.FullDsl._

trait Styles {

  val menuStyle = vMatchParent + 
    llVertical

  def drawerMenuStyle(implicit appContext: AppContext) = lp[AbsListView](MATCH_PARENT, MATCH_PARENT) +
    vBackground(R.drawable.background_menu_transition)

  def bigImageLayoutStyle(implicit appContext: AppContext) = lp[FrameLayout](MATCH_PARENT, 169 dp) +
    flForeground(appContext.get.getResources.getDrawable(R.drawable.background_header_menu_default))
  
  val bigImageStyle = vMatchParent
  
  def bigImageActionLayout(implicit appContext: AppContext) = vMatchWidth +
    llGravity(Gravity.CENTER_VERTICAL) +
    flLayoutGravity(Gravity.BOTTOM) +
    vPadding(16 dp, 15 dp, 16 dp, 15 dp)
  
  def conferenceTitleStyle(implicit appContext: AppContext) = llWrapWeightHorizontal +
    tvSize(14) +
    tvColor(Color.WHITE) + 
    tvBoldLight

  val conferenceSelectorStyle = vWrapContent +
    ivSrc(R.drawable.menu_header_select_arrow)

  def mainMenuItemStyle(implicit appContext: AppContext) = lp[AbsListView](MATCH_PARENT, 48 dp) +
    tvSize(14) +
    tvColor(Color.WHITE) +
    tvGravity(Gravity.CENTER_VERTICAL) +
    vPadding(18 dp, 14 dp, 18 dp, 14 dp) +
    tvDrawablePadding(34 dp) +
    tvBoldLight +
    vBackground(R.drawable.background_list_menu)

  def conferenceMenuItemLayoutStyle(implicit appContext: AppContext) = lp[AbsListView](MATCH_PARENT, 74 dp) +
    llHorizontal +
    llGravity(Gravity.CENTER_VERTICAL) +
    vPadding(18 dp, 14 dp, 18 dp, 14 dp) +
    vBackground(R.drawable.background_list_default)
  
  def conferenceMenuItemIconStyle(implicit appContext: AppContext) = lp[LinearLayout](42 dp, 42 dp) +
    ivScaleType(ScaleType.CENTER_CROP)
    

  def conferenceMenuItemStyle(implicit appContext: AppContext) = vWrapContent +
    tvSize(14) +
    tvColor(Color.DKGRAY) +
    vPadding(paddingLeft = 34 dp) +
    tvBoldLight

}
