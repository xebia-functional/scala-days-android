package com.fortysevendeg.android.scaladays.model.app

import org.joda.time.DateTime

case class Conference(
  conferenceInfo: Information,
  sponsors: Seq[Sponsor],
  speakers: Seq[Speaker],
  schedule: Seq[Event])

case class Information(
  id: Int,
  name: String,
  longName: String,
  nameAndLocation: String,
  firstDay: DateTime,
  lastDay: DateTime,
  normalSite: String,
  registrationSite: String,
  utcTimezoneOffset: String,
  utcTimezoneOffsetMillis: Long)

case class Sponsor(
  name: String,
  imageUrl: String,
  url: String)

case class Speaker(
  id: Int,
  name: String,
  title: String,
  company: String,
  twitter: Option[String],
  picture: Option[String],
  bio: String)

case class Event(
  id: Int,
  title: String,
  description: String,
  eventType: EventType,
  slot: Option[Slot],
  track: Option[Track],
  location: Option[Location],
  speakers: Seq[Speaker])

case class EventType()

case class Slot(
  id: Int,
  startTime: String,
  endTime: String)

case class Track(
  id: Int,
  name: String,
  host: String,
  shortDescription: String,
  description: String)

case class Location(
  id: Int,
  name: String,
  mapUrl: String)