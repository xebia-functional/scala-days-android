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

import org.joda.time.format.{DateTimeFormat, DateTimeFormatter, ISODateTimeFormat}
import org.joda.time.{DateTimeZone, DateTime}

object DateTimeUtils {

  val ISODateFormatterDay = ISODateTimeFormat.date
  val ISODateFormatterDateTime = ISODateTimeFormat.dateTimeNoMillis
  val ISODateFormatterTime = ISODateTimeFormat.hourMinute

  def parseDate(
      date: String,
      dateTimeFormatter: DateTimeFormatter = ISODateFormatterDateTime,
      dateTimeZone: DateTimeZone = DateTimeZone.UTC): DateTime = 
    DateTime.parse(date, dateTimeFormatter.withZone(dateTimeZone))

  def convertTimeZone(fromDateTime: DateTime, toTimeZone: String): DateTime =
    new DateTime(fromDateTime, DateTimeZone.forID(toTimeZone))

}
