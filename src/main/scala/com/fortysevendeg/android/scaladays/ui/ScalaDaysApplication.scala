package com.fortysevendeg.android.scaladays.ui

import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex

class ScalaDaysApplication extends Application {

  override def attachBaseContext(base: Context): Unit = {
    super.attachBaseContext(base)
    MultiDex.install(this)
  }

}
