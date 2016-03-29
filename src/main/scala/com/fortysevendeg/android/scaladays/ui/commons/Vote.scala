/*
 *  Copyright (C) 2015 47 Degrees, LLC http://47deg.com hello@47deg.com
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

package com.fortysevendeg.android.scaladays.ui.commons

sealed trait Vote {
  def value: String
}

case object VoteUnlike
  extends Vote{
  override def value: String = Vote.voteUnlike
}

case object VoteLike
  extends Vote{
  override def value: String = Vote.voteLike
}

case object VoteNeutral
  extends Vote{
  override def value: String = Vote.voteNeutral
}

object Vote {
  val voteUnlike = "0"
  val voteNeutral = "1"
  val voteLike = "2"

  def apply(vote: String): Vote = vote match {
    case `voteUnlike` => VoteUnlike
    case `voteLike` => VoteLike
    case _ => VoteNeutral
  }

}