/*
 * Copyright (C) 2015 47 Degrees, LLC http://47deg.com hello@47deg.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fortysevendeg.android.scaladays.utils

import android.text.format.DateFormat
import macroid.ContextWrapper
import macroid.extras.ResourcesExtras._
import org.joda.time.format.{DateTimeFormat, DateTimeFormatterBuilder, DateTimeFormatter, ISODateTimeFormat}
import org.joda.time.{DateTimeFieldType, DateTime, DateTimeZone}
import org.ocpsoft.prettytime.PrettyTime

object DateTimeUtils {

  val ordinalPrefix = List("th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th")

  val ISODateFormatterDay: DateTimeFormatter = ISODateTimeFormat.date
  val ISODateFormatterDateTime: DateTimeFormatter = ISODateTimeFormat.dateTimeNoMillis
  val ISODateFormatter24hTime: DateTimeFormatter = ISODateTimeFormat.hourMinute
  val ISODateFormatter12hTime: DateTimeFormatter = DateTimeFormat.forPattern("h:mm aa")
  val ISODateFormatterDayOfMonth: DateTimeFormatter = new DateTimeFormatterBuilder()
    .appendDecimal(DateTimeFieldType.dayOfMonth(), 1, 2)
    .toFormatter
  val ISODateFormatterMonthOfYear: DateTimeFormatter = new DateTimeFormatterBuilder()
    .appendDecimal(DateTimeFieldType.monthOfYear(), 1, 2)
    .toFormatter
  val ISODateFormatterDayOfWeek: DateTimeFormatter = new DateTimeFormatterBuilder()
    .appendDecimal(DateTimeFieldType.dayOfWeek(), 1, 2)
    .toFormatter

  def parseDate(
      date: String,
      dateTimeFormatter: DateTimeFormatter = ISODateFormatterDateTime,
      dateTimeZone: DateTimeZone = DateTimeZone.UTC): DateTime =
    DateTime.parse(date, dateTimeFormatter.withZone(dateTimeZone))

  def convertTimeZone(fromDateTime: DateTime, toTimeZone: String): DateTime =
    new DateTime(fromDateTime, DateTimeZone.forID(toTimeZone))

  def parseTime(dateTime: DateTime, timeZone: String)(implicit context: ContextWrapper): String = {
    if (DateFormat.is24HourFormat(context.application)) {
      convertTimeZone(dateTime, timeZone).toString(ISODateFormatter24hTime)
    } else {
      convertTimeZone(dateTime, timeZone).toString(ISODateFormatter12hTime)
    }
  }

  def parseDateSchedule(dateTime: DateTime, timeZone: String)(implicit context: ContextWrapper): String = {
    val dateTimeZone = convertTimeZone(dateTime, timeZone)
    val dayOfMonth = dateTimeZone.toString(ISODateFormatterDayOfMonth).toInt
    val monthOfYear = resGetString("monthOfYear%s".format(dateTimeZone.toString(ISODateFormatterMonthOfYear))).getOrElse("")
    val dayOfWeek = resGetString("dayOfWeek%s".format(dateTimeZone.toString(ISODateFormatterDayOfWeek))).getOrElse("")
    "%s (%s %s)".format(dayOfWeek, ordinal(dayOfMonth), monthOfYear)
  }

  def parseDateScheduleTime(dateTime: DateTime, timeZone: String)(implicit context: ContextWrapper): String = {
    val dateTimeZone = convertTimeZone(dateTime, timeZone)
    val dayOfMonth = dateTimeZone.toString(ISODateFormatterDayOfMonth).toInt
    val monthOfYear = resGetString("monthOfYear%s".format(dateTimeZone.toString(ISODateFormatterMonthOfYear))).getOrElse("")
    val dayOfWeek = resGetString("dayOfWeek%s".format(dateTimeZone.toString(ISODateFormatterDayOfWeek))).getOrElse("")
    val hour = parseTime(dateTime, timeZone)
    "%s (%s %s) %s".format(dayOfWeek, ordinal(dayOfMonth), monthOfYear, hour)
  }

  def parsePrettyTime(dateTime: DateTime): String = new PrettyTime().format(dateTime.toDate)

  private def ordinal(number: Int): String = number match {
    case n if n % 100 == 11 || n % 100 == 12 || n % 100 == 13 => n + "th"
    case _ => number + ordinalPrefix(number % 10)
  }

}
