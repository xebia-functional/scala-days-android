package com.fortysevendeg.android.scaladays

import com.fortysevendeg.android.scaladays.commons.ContextWrapperProvider
import macroid.ContextWrapper
import org.specs2.mock.Mockito
import org.specs2.specification.Scope

trait ContextWrapperTestSupport
    extends Mockito
    with ContextWrapperProvider
    with TestConfig
    with Scope {

  implicit val contextProvider: ContextWrapper = mock[ContextWrapper]

  contextProvider.application returns mockContext

}
