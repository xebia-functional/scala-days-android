package com.fortysevendeg.android.scaladays.ui

import android.support.multidex.MultiDexApplication
import com.localytics.android._

class ScalaDaysApplication extends MultiDexApplication {
  override def onCreate(): Unit = {
    super.onCreate()
    Localytics.autoIntegrate(this)
  }
}
