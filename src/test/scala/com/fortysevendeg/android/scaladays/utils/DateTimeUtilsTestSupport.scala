package com.fortysevendeg.android.scaladays.utils

import com.fortysevendeg.android.scaladays.BaseTestSupport

trait DateTimeUtilsTestSupport
  extends BaseTestSupport {

  val validDateTime = "2015-03-16T23:00:00Z"

  val validDate = "2015-03-16"
  
  val validYear = 2015
  val validMonth = 3
  val validDay = 16
  
  val dateFormat = "yyyy-MM-dd HH:mm:ss"

  val validConversions = Seq(
    ("2015-05-08T19:00:00Z", "2015-05-08 20:00:00", "Europe/London" ),
    ("2015-05-08T19:00:00Z", "2015-05-08 21:00:00", "Europe/Brussels" ),
    ("2015-05-08T19:00:00Z", "2015-05-08 12:00:00", "America/Tijuana" ),
    ("2015-05-08T19:00:00Z", "2015-05-08 23:00:00", "Asia/Dubai" ),
    ("2015-05-08T19:00:00Z", "2015-05-09 04:00:00", "Asia/Tokyo" ),
    ("2015-05-08T19:00:00Z", "2015-05-08 21:00:00", "Europe/Madrid" ))
  
}
