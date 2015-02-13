package com.fortysevendeg.android.scaladays.ui.commons

import android.graphics.drawable.Drawable
import macroid.AppContext

trait ResourceLoader {
  
  def getDrawable(resource: Int)(implicit appContext: AppContext): Drawable = {
    appContext.get.getResources.getDrawable(resource)
  }
  
  def getDimension(resource: Int)(implicit appContext: AppContext): Int = {
    appContext.get.getResources.getDimensionPixelSize(resource)  
  }
  
  def getInt(resource: Int)(implicit appContext: AppContext): Int = {
    appContext.get.getResources.getInteger(resource)
  }

}
