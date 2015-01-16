package com.fortysevendeg.android.scaladays

import scala.concurrent.Future

package object scaladays {

  type Service[Req, Res] = Req => Future[Res]

}
