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
import com.fortysevendeg.android.scaladays.modules.preferences.{PreferenceRequest, PreferenceServicesComponent}
import com.fortysevendeg.android.scaladays.ui.commons.UiServices
import com.fortysevendeg.android.scaladays.utils.DateTimeUtils
import macroid.ContextWrapper

import scala.annotation.tailrec

case class ScheduleItem(
  isHeader: Boolean,
  header: Option[String],
  event: Option[Event])

trait ScheduleConversion {

  self: PreferenceServicesComponent with UiServices =>

  def toScheduleItem(
    timeZone: String,
    events: Seq[Event],
    func: (Event) => Boolean)(implicit context: ContextWrapper): Seq[ScheduleItem] = {

    @tailrec
    def loop(events: Seq[Event], date: String = "", acc: Seq[ScheduleItem] = Nil): Seq[ScheduleItem] =
      events match {
        case Nil => acc
        case h :: t =>
          val dayStr = DateTimeUtils.parseDateSchedule(h.startTime, timeZone)
          val addEvent = func(h)
          val newAcc = if (date != dayStr && addEvent) {
            acc :+ new ScheduleItem(isHeader = true, header = Some(dayStr), event = None)
          } else {
            acc
          }
          if (addEvent) {
            loop(t, dayStr, newAcc :+ ScheduleItem(isHeader = false, None, Some(h)))
          } else {
            loop(t, date, newAcc)
          }
      }

    loop(events)

  }

}