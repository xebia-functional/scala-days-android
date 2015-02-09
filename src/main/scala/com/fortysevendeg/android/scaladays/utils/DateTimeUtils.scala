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

import com.fortysevendeg.macroid.extras.ResourcesExtras._
import macroid.AppContext
import org.joda.time.format.{DateTimeFormatterBuilder, DateTimeFormatter, ISODateTimeFormat}
import org.joda.time.{DateTimeFieldType, DateTime, DateTimeZone}

object DateTimeUtils {

  val ISODateFormatterDay = ISODateTimeFormat.date
  val ISODateFormatterDateTime = ISODateTimeFormat.dateTimeNoMillis
  val ISODateFormatterTime = ISODateTimeFormat.hourMinute
  val ISODateFormatterDayOfMonth = new DateTimeFormatterBuilder()
      .appendDecimal(DateTimeFieldType.dayOfMonth(), 1, 2)
      .toFormatter()
  val ISODateFormatterMonthOfYear = new DateTimeFormatterBuilder()
      .appendDecimal(DateTimeFieldType.monthOfYear(), 1, 2)
      .toFormatter()
  val ISODateFormatterDayOfWeek = new DateTimeFormatterBuilder()
      .appendDecimal(DateTimeFieldType.dayOfWeek(), 1, 2)
      .toFormatter()

  def parseDate(
      date: String,
      dateTimeFormatter: DateTimeFormatter = ISODateFormatterDateTime,
      dateTimeZone: DateTimeZone = DateTimeZone.UTC): DateTime =
    DateTime.parse(date, dateTimeFormatter.withZone(dateTimeZone))

  def convertTimeZone(fromDateTime: DateTime, toTimeZone: String): DateTime =
    new DateTime(fromDateTime, DateTimeZone.forID(toTimeZone))

  def parseDateSchedule(dateTime: DateTime, timeZone: String)(implicit appContext: AppContext) = {
    val dateTimeZone = convertTimeZone(dateTime, timeZone)
    val dayOfMonth = dateTimeZone.toString(ISODateFormatterDayOfMonth)
    val monthOfYear = resGetString("monthOfYear%s".format(dateTimeZone.toString(ISODateFormatterMonthOfYear))).getOrElse("")
    val dayOfWeek = resGetString("dayOfWeek%s".format(dateTimeZone.toString(ISODateFormatterDayOfWeek))).getOrElse("")
    "%s (%s %s)".format(dayOfWeek, dayOfMonth, monthOfYear)
  }

  def parseDateScheduleTime(dateTime: DateTime, timeZone: String)(implicit appContext: AppContext) = {
    val dateTimeZone = convertTimeZone(dateTime, timeZone)
    val dayOfMonth = dateTimeZone.toString(ISODateFormatterDayOfMonth)
    val monthOfYear = resGetString("monthOfYear%s".format(dateTimeZone.toString(ISODateFormatterMonthOfYear))).getOrElse("")
    val dayOfWeek = resGetString("dayOfWeek%s".format(dateTimeZone.toString(ISODateFormatterDayOfWeek))).getOrElse("")
    val hour = dateTimeZone.toString(DateTimeUtils.ISODateFormatterTime)
    "%s (%s %s) %s".format(dayOfWeek, dayOfMonth, monthOfYear, hour)
  }

}
