package net.rrm.ehour.ui.financial.lock

import net.rrm.ehour.AbstractSpringWebAppSpec
import net.rrm.ehour.timesheet.service.TimesheetLockService
import org.mockito.Mockito._


class ExistingLocksPanelSpec extends AbstractSpringWebAppSpec {
  "Existing Locks Panel" should {
    val service = mock[TimesheetLockService]
    springTester.getMockContext.putBean(service)

    "render" in {
      when(service.findAll()).thenReturn(List())

      tester.startComponentInPage(classOf[ExistingLocksPanel])
      tester.assertNoErrorMessage()
    }
  }
}
