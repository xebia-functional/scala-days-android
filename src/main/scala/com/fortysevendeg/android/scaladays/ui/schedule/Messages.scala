/*
 * Copyright (C) 2015 47 Degrees, LLC http://47deg.com hello@47deg.com
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may
 *  not use this file except in compliance with the License. You may obtain
 *  a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.fortysevendeg.android.scaladays.ui.schedule

import com.fortysevendeg.android.scaladays.model.Event
import com.fortysevendeg.android.scaladays.utils.DateTimeUtils
import macroid.AppContext
import com.fortysevendeg.macroid.extras.ResourcesExtras._

case class ScheduleItem(
    isHeader: Boolean,
    header: Option[String],
    event: Option[Event]
    )

object ScheduleConversion {

  def toScheduleItem(timeZone: String, events: Seq[Event])(implicit appContext: AppContext): Seq[ScheduleItem] = {
    var list: Seq[ScheduleItem] = Seq.empty
    var date = ""
    for (event <- events) {
      val dateTimeZone = DateTimeUtils.convertTimeZone(event.startTime, timeZone)
      val dayOfMonth = dateTimeZone.toString(DateTimeUtils.ISODateFormatterDayOfMonth)
      val monthOfYear = resGetString("monthOfYear%s".format(dateTimeZone.toString(DateTimeUtils.ISODateFormatterMonthOfYear))).getOrElse("")
      val dayOfWeek = resGetString("dayOfWeek%s".format(dateTimeZone.toString(DateTimeUtils.ISODateFormatterDayOfWeek))).getOrElse("")
      val dayStr = dayOfWeek + " (" + dayOfMonth + " " + monthOfYear + ")"
      if (date != dayStr) {
        date = dayStr
        list = list :+ new ScheduleItem(true, Some(dayStr), None)
      }
      list = list :+ new ScheduleItem(false, None, Some(event))
    }
    list
  }

}