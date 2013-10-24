package net.rrm.ehour.ui.financial.lock

import net.rrm.ehour.AbstractSpringWebAppSpec

class LockAdminPageSpec extends AbstractSpringWebAppSpec {
  "Lock Admin Page" should {
    "render" in {
      tester.startPage(classOf[LockAdminPage])
    }
  }
}
