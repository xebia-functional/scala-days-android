package com.fortysevendeg.android.scaladays.ui.drawer

import android.support.v7.widget.RecyclerView
import android.widget.{ImageView, TextView, FrameLayout, LinearLayout}
import macroid.FullDsl._
import macroid.{ActivityContext, AppContext}

class Layout(implicit appContext: AppContext, context: ActivityContext)
  extends Styles {

  var drawerMenuLayout = slot[LinearLayout]
  
  var bigImage = slot[ImageView]
  
  var conferenceTitle = slot[TextView]
  
  var conferenceSelector = slot[ImageView]

  var recyclerView = slot[RecyclerView]
  
  val content = getUi(
    l[LinearLayout](
      l[FrameLayout](
        w[ImageView] <~ wire(bigImage) <~ bigImageStyle,
        l[LinearLayout](
          w[TextView] <~ wire(conferenceTitle) <~ conferenceTitleStyle,
          w[ImageView] <~ wire(conferenceSelector) <~ conferenceSelectorStyle
        ) <~ bigImageActionLayout
      ) <~ bigImageLayoutStyle,
      w[RecyclerView] <~ wire(recyclerView) <~ drawerMenuStyle
    ) <~ menuStyle
  )

  def layout = content
}

class MenuAdapter(implicit context: ActivityContext, appContext: AppContext)
  extends Styles {

  var menuItem = slot[TextView]

  val content = layout

  private def layout(implicit appContext: AppContext, context: ActivityContext) = getUi(
    w[TextView] <~ wire(menuItem) <~ menuItemStyle)
}

class ViewHolderMenuAdapter(adapter: MenuAdapter)(implicit context: ActivityContext, appContext: AppContext)
  extends RecyclerView.ViewHolder(adapter.content) {

  var content = adapter.content

  var title = adapter.menuItem

}
