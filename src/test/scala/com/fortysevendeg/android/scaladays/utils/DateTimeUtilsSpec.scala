package com.fortysevendeg.android.scaladays.utils

import org.joda.time.{DateTimeZone, DateTime}
import org.specs2.mutable.Specification
import DateTimeUtils._

class DateTimeUtilsSpec
  extends Specification
  with DateTimeUtilsTestSupport {

  "method parseDate" should {

    "return a valid DateTime with valid date string" in {

      val dateTime = parseDate(validDateTime, DateTimeUtils.ISODateFormatterDateTime)
      
      dateTime.year.get shouldEqual validYear
      dateTime.monthOfYear.get shouldEqual validMonth
      dateTime.dayOfMonth.get shouldEqual validDay
    }

    "return a valid Date with valid date string" in {

      val dateTime = parseDate(validDate, DateTimeUtils.ISODateFormatterDay)

      dateTime.year.get shouldEqual validYear
      dateTime.monthOfYear.get shouldEqual validMonth
      dateTime.dayOfMonth.get shouldEqual validDay
    }

  }
  
  "method convertTimeZone" should {
    
    "convert between different time zones" in {

      def toDate(date: String, zone: String) =
        convertTimeZone(parseDate(date, ISODateFormatterDateTime), zone).toString(dateFormat)

      val datesParsed: Seq[String] = validConversions map { tuple => toDate(tuple._1, tuple._3) }
      val datesZone: Seq[String] = validConversions map (_._2)

      datesParsed shouldEqual datesZone
    }

    "load correctly in time zones" in {

      def toDate(date: String, zone: String) = {
        parseDate(date, ISODateFormatterDateTime, DateTimeZone.forID(zone)).toString(dateFormat)
      }

      val datesParsed: Seq[String] = validConversions map { tuple => toDate(tuple._1, tuple._3) }
      val datesZone: Seq[String] = validConversions map (_._2)

      datesParsed shouldEqual datesZone
    }
    
  }

}
