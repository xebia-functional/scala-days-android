package com.fortysevendeg.android.scaladays.model.api

case class ApiConference(
  slotList: Seq[ApiSlot],
//  topicList: Seq[Topic],
  htmlContents: Map[String, String],
  locationList: Seq[ApiLocation],
  conference: ApiInformation,
  speakerList: Seq[ApiSpeaker],
  trackList: Seq[ApiTrack],
  eventList: Seq[ApiEvent])

case class ApiSlot(
  id: Int, 
  startTime: String, 
  endTime: String)

case class Topic()

case class ApiLocation(
  id: Int, 
  name: String, 
  mapUrl: String)

case class ApiSpeaker(
  id: Int, 
  name: String, 
  title: String, 
  picture: Option[String],
  company: String,
  twitter: Option[String],
  bio: String)

case class ApiInformation(
  id: Int,
  name: String,
  longName: String,
  nameAndLocation: String,
  firstDay: String,
  lastDay: String,
  normalSite: String,
  registrationSite: String,
  utcTimezoneOffset: String,
  utcTimezoneOffsetMillis: Long)

case class ApiTrack(
  id: Int,
  name: String,
  host: String,
  shortdescription: String,
  description: String)

case class ApiEvent(
  id: Int, 
  title: String,
  `type`: Int,
  slotIds: Seq[Int],
  trackId: Option[Int],
  locationId: Option[Int],
  speakerIds: Option[Seq[Int]],
  aabstract: String)