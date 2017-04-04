package com.fortysevendeg.android.scaladays.ui

import android.app.Application
import com.localytics.android._

class ScalaDaysApplication extends Application {
  override def onCreate(): Unit = {
    super.onCreate()
    Localytics.autoIntegrate(this)
  }
}
