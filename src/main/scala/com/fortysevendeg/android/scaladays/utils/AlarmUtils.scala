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

package com.fortysevendeg.android.scaladays.utils

import android.app.{PendingIntent, AlarmManager}
import android.content.{Intent, Context}
import com.fortysevendeg.android.scaladays.services.ReloadJsonService
import macroid.AppContext

object AlarmUtils {

  val fourHours = 1000 * 60 * 60 * 4

  def setReloadJsonService(implicit appContext: AppContext): Unit = {
    val am = appContext.get.getSystemService(Context.ALARM_SERVICE).asInstanceOf[AlarmManager]
    val i = new Intent(appContext.get, classOf[ReloadJsonService])
    val pendingIntent = PendingIntent.getService(appContext.get, 0, i, 0)
    am.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + fourHours, fourHours, pendingIntent)
  }

}
