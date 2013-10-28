package net.rrm.ehour.ui.financial.lock

import net.rrm.ehour.AbstractSpringWebAppSpec

class ExistingLocksPanelSpec extends AbstractSpringWebAppSpec {
  "Existing Locks Panel" should {
    "render" in {
      tester.startComponentInPage(classOf[ExistingLocksPanel])
      tester.assertNoErrorMessage()
    }
  }
}
