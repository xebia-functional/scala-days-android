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

package com.fortysevendeg.android.scaladays.ui.sponsors

import com.fortysevendeg.android.scaladays.model.{SponsorType, Sponsor}
import macroid.AppContext

import scala.annotation.tailrec

case class SponsorItem(
    isHeader: Boolean,
    header: Option[String],
    sponsor: Option[Sponsor])

object SponsorConversion {

  def toSponsorItem(sponsors: Seq[SponsorType]): Seq[SponsorItem] = {

    @tailrec
    def loop(sponsors: Seq[SponsorType], acc: Seq[SponsorItem] = Nil): Seq[SponsorItem] =
      sponsors match {
        case Nil => acc
        case h :: t =>
          loop(t, (acc :+ SponsorItem(isHeader = true, header = Some(h.name), sponsor = None)) ++
              h.sponsors.map(sponsor => SponsorItem(isHeader = false, header = None, sponsor = Some(sponsor))))
      }

    loop(sponsors)

  }

}