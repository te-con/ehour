package net.rrm.ehour.ui.financial.lock

import net.rrm.ehour.AbstractSpringWebAppSpec
import net.rrm.ehour.timesheet.service.TimesheetLockService

class LockAdminPageSpec extends AbstractSpringWebAppSpec {
  "Lock Admin Page" should {
    val service = mock[TimesheetLockService]
    springTester.getMockContext.putBean(service)

    "render" in {
      tester.startPage(classOf[LockAdminPage])

      tester.assertNoErrorMessage()
      tester.assertNoInfoMessage()
    }
  }
}
