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

package com.fortysevendeg.android.scaladays.ui.qrcode

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.support.v4.app.Fragment
import android.view.{LayoutInflater, View, ViewGroup}
import com.fortysevendeg.android.scaladays.R
import com.fortysevendeg.android.scaladays.modules.ComponentRegistryImpl
import com.fortysevendeg.android.scaladays.ui.commons.AnalyticStrings._
import com.google.zxing.Result
import com.google.zxing.client.result.VCardResultParser
import macroid.{ContextWrapper, Contexts, Ui}
import macroid.extras.UIActionsExtras._
import com.fortysevendeg.android.scaladays.ui.commons.IntegerResults._

import scala.concurrent.Future

class QrCodeFragment
  extends Fragment
  with Contexts[Fragment]
  with ComponentRegistryImpl
  with Layout {

  override lazy val contextProvider: ContextWrapper = fragmentContextWrapper

  override def onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle): View = {
    analyticsServices.sendScreenName(analyticsContactsScreen)
    content
  }

  override def onActivityResult(requestCode: Int, resultCode: Int, data: Intent): Unit = {
    super.onActivityResult(requestCode, resultCode, data)
    requestCode match {
      case request if request == scanResult =>
        resultCode match {
          case Activity.RESULT_OK =>
            loadVCard(requestCode, resultCode, data)
          case Activity.RESULT_CANCELED =>
            failed()
        }
    }
  }

  def loadVCard(requestCode: Int, resultCode: Int, data: Intent) = {
    Option(data.getStringExtra("SCAN_RESULT")) map {
      case contents if contents.startsWith("BEGIN:VCARD") =>
        val result = new Result(contents, null, null, null)
        val vCardResultParser = new VCardResultParser()
        val vcard = vCardResultParser.parse(result)
        val addContactIntent = new Intent(ContactsContract.Intents.Insert.ACTION, ContactsContract.Contacts.CONTENT_URI)
        addContactIntent.setType(ContactsContract.Contacts.CONTENT_TYPE)
        Option(vcard.getNames) map {
          case names if names.nonEmpty => addContactIntent.putExtra(ContactsContract.Intents.Insert.NAME, names(0).replace(";", " "))
        }
        Option(vcard.getTitle) map (addContactIntent.putExtra(ContactsContract.Intents.Insert.JOB_TITLE, _))
        Option(vcard.getOrg) map (addContactIntent.putExtra(ContactsContract.Intents.Insert.COMPANY, _))
        Option(vcard.getNote) map (addContactIntent.putExtra(ContactsContract.Intents.Insert.NOTES, _))
        val phoneNumbers = Option(vcard.getPhoneNumbers) map (_.toList) getOrElse List.empty
        val phoneNumberCounter = (1 until phoneNumbers.size + 1).zip(phoneNumbers)
        phoneNumberCounter map {
          phone =>
            phone._1 match {
              case 1 => addContactIntent.putExtra(ContactsContract.Intents.Insert.PHONE, phone._2)
              case 2 => addContactIntent.putExtra(ContactsContract.Intents.Insert.SECONDARY_PHONE, phone._2)
              case 3 => addContactIntent.putExtra(ContactsContract.Intents.Insert.TERTIARY_PHONE, phone._2)
            }
        }
        val emails = Option(vcard.getEmails) map (_.toList) getOrElse List.empty
        val emailsCounter = (1 until emails.size + 1).zip(emails)
        emailsCounter map {
          email =>
            email._1 match {
              case 1 => addContactIntent.putExtra(ContactsContract.Intents.Insert.EMAIL, email._2)
              case 2 => addContactIntent.putExtra(ContactsContract.Intents.Insert.SECONDARY_EMAIL, email._2)
              case 3 => addContactIntent.putExtra(ContactsContract.Intents.Insert.TERTIARY_EMAIL, email._2)
            }
        }
        startActivity(addContactIntent)
      case _ => failed()
    }
  }

  def failed(): Future[Unit] = Ui.run(uiShortToast(R.string.scanError))

}
