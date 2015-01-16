package com.fortysevendeg.android.scaladays.ui

import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.util.Log
import macroid.{AppContext, Contexts}

class MainActivity
  extends FragmentActivity
  with Contexts[FragmentActivity] {

  override def onCreate(savedInstanceState: Bundle) = {
    super.onCreate(savedInstanceState)
    Log.d("Test", "On Create")
    
  }

}
