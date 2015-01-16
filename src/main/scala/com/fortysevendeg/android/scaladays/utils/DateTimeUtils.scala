package com.fortysevendeg.android.scaladays.utils

import java.util.Calendar

import org.joda.time.format.{DateTimeFormatter, ISODateTimeFormat}
import org.joda.time.{DateTime, DateTimeZone, LocalDateTime}

object DateTimeUtils {

  val ISODateFormatterDayPrecission = ISODateTimeFormat.date.withZoneUTC
  val ISODateFormatterMillisPrecission = ISODateTimeFormat.dateTime
  val MillisPerHour = 3600000
  val MillisPerMinute = MillisPerHour / 60
  val DateWithoutZoneStringLength = 23

  def fromDateTime2JavaDate(d: DateTime) = new java.util.Date(d.getMillis)

  def parseDate(
      date: String,
      fmt: DateTimeFormatter = ISODateFormatterMillisPrecission): DateTime = DateTime.parse(date, fmt)

  def getHourOffset(date: DateTime): Int =
    date.getZone.getOffset(date) / MillisPerHour

  def getMillisOffset(date: String): Int =
    date.split("\\.").reverse.head.drop(3).split("\\:").toList match {
      case (hh :: mm :: Nil) => (hh.toInt * MillisPerHour) + (mm.toInt * MillisPerMinute)
      case _ => 0
    }

  def asLocalDateTime(date: String): DateTime =
    new LocalDateTime(date.take(DateWithoutZoneStringLength)).toDateTime(DateTimeZone.UTC)

  def getDateFromCalendar(calendar: Calendar): DateTime = new DateTime(calendar.getTime)

  implicit class DateTimeOps(dt : DateTime) {

    def isFutureDate: Boolean = dt.isAfter(DateTime.now())
  }
}
