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

import java.util.Calendar

import org.joda.time.format.{DateTimeFormatter, ISODateTimeFormat}
import org.joda.time.{DateTime, DateTimeZone, LocalDateTime}

object DateTimeUtils {

  val ISODateFormatterDayPrecission = ISODateTimeFormat.date.withZoneUTC
  val ISODateFormatterMillisPrecission = ISODateTimeFormat.dateTime
  val DateWithoutZoneStringLength = 23
  
  def parseDate(
      date: String,
      fmt: DateTimeFormatter = ISODateFormatterMillisPrecission): DateTime = DateTime.parse(date, fmt)

  def asLocalDateTime(date: String): DateTime =
    new LocalDateTime(date.take(DateWithoutZoneStringLength)).toDateTime(DateTimeZone.UTC)

  def getDateFromCalendar(calendar: Calendar): DateTime = new DateTime(calendar.getTime)
}
