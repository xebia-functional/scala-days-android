package com.fortysevendeg.android.scaladays.modules.preferences

case class PreferenceRequest[T](name: String, value: T)

case class PreferenceResponse[T](value: T)
