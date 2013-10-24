package net.rrm.ehour.ui.financial.lock

import net.rrm.ehour.AbstractSpringWebAppSpec

class LockDetailsPanelSpec extends AbstractSpringWebAppSpec {
  "Lock Details Panel" should {
    "render" in {
      tester.startComponentInPage(classOf[ExistingLocksPanel])
      tester.assertNoErrorMessage()
    }
  }


}
