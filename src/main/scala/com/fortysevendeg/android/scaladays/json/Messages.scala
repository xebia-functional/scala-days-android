package com.fortysevendeg.android.scaladays.json

import com.fortysevendeg.android.scaladays.model.api.ApiConference

case class JsonRequest(jsonPath: String, fromCache: Boolean = false)

case class JsonResponse(apiResponse: Option[ApiConference])