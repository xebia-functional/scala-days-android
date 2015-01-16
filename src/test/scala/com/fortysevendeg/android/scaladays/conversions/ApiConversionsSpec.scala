package com.fortysevendeg.android.scaladays.conversions

import com.fortysevendeg.android.scaladays.model.api._
import com.fortysevendeg.android.scaladays.model.app._
import org.specs2.mutable._

class ApiConversionsSpec 
  extends Specification 
  with ApiConversions
  with TestConfig
  with ApiConversionsTestSupport {
  
  "toSlot method" should {

    "return a Slot with the right fields" in {
      toSlot(ApiSlot(
        slot.id, 
        slot.startTime, 
        slot.endTime)) shouldEqual slot
    }

  }

  "toTrack method" should {

    "return a Track with the right fields" in {
      toTrack(ApiTrack(
        track.id, 
        track.name, 
        track.host, 
        track.shortDescription, 
        track.description)) shouldEqual track
    }

  }

  "toLocation method" should {

    "return a Location with the right fields" in {
      toLocation(ApiLocation(
        location.id, 
        location.name, 
        location.mapUrl)) shouldEqual location
    }
    
  }

  "toSpeaker method" should {

    "return a Speaker with the right fields" in {
      toSpeaker(ApiSpeaker(
        speaker.id, 
        speaker.name, 
        speaker.title, 
        speaker.picture, 
        speaker.company, 
        speaker.twitter, 
        speaker.bio)) shouldEqual speaker
    }

  }

  "toInformation method" should {

    "return a Information with the right fields" in {
      toInformation(ApiInformation(
        information.id,
        information.name,
        information.longName,
        information.nameAndLocation,
        firstDayString,
        lastDayString,
        information.normalSite,
        information.registrationSite,
        information.utcTimezoneOffset,
        information.utcTimezoneOffsetMillis)) shouldEqual information
    }
    
    "throw IllegalArgumentException with wrong string date" in {
      toInformation(ApiInformation(
        information.id,
        information.name,
        information.longName,
        information.nameAndLocation,
        firstDayStringError,
        lastDayStringError,
        information.normalSite,
        information.registrationSite,
        information.utcTimezoneOffset,
        information.utcTimezoneOffsetMillis)) must throwA[IllegalArgumentException]
    }
    
  }
  
  "toSeqEvent method" should {
    
    "return a sequence with one event when the param has only one right slot id" in {
      implicit val slotList = Seq(slot)
      implicit val trackList: Seq[Track] = Nil
      implicit val locationList: Seq[Location] = Nil
      implicit val speakerList: Seq[Speaker] = Nil
      toSeqEvent(ApiEvent(
        eventId, 
        name, 
        typeInt, 
        Seq(slot.id), 
        None, 
        None, 
        None, 
        aabstract)) shouldEqual Seq(simpleEvent)
    }
    
    "return a sequence with two events when the param has two slots id" in {
      implicit val slotList = Seq(slot)
      implicit val trackList: Seq[Track] = Nil
      implicit val locationList: Seq[Location] = Nil
      implicit val speakerList: Seq[Speaker] = Nil
      toSeqEvent(ApiEvent(
        eventId, 
        name, 
        typeInt, 
        Seq(slot.id, slot.id), 
        None, 
        None, 
        None, 
        aabstract)) shouldEqual Seq(simpleEvent, simpleEvent)
    }

    "return a sequence with one event and all the fields (track, location and speaker)" in {
      implicit val slotList = Seq(slot)
      implicit val trackList: Seq[Track] = Seq(track)
      implicit val locationList: Seq[Location] = Seq(location)
      implicit val speakerList: Seq[Speaker] = Seq(speaker)
      toSeqEvent(ApiEvent(
        eventId, 
        name, 
        typeInt, 
        Seq(slot.id), 
        Some(trackId), 
        Some(locationId), 
        Some(Seq(speakerId)), 
        aabstract)) shouldEqual Seq(event)
    }

    "return a empty sequence when the param has wrong slots id" in {
      implicit val slotList = Seq(slot)
      implicit val trackList: Seq[Track] = Nil
      implicit val locationList: Seq[Location] = Nil
      implicit val speakerList: Seq[Speaker] = Nil
      toSeqEvent(ApiEvent(
        eventId, 
        name, 
        typeInt, 
        Seq(wrongId), 
        None, 
        None, 
        None, 
        aabstract)) must be empty
    }
    
    
  }

}
