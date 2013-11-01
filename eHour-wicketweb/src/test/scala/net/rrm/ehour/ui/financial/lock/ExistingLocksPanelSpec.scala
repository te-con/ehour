package net.rrm.ehour.ui.financial.lock

import net.rrm.ehour.AbstractSpringWebAppSpec
import net.rrm.ehour.timesheet.service.{LockedTimesheet, TimesheetLockService}
import org.mockito.Mockito._
import org.joda.time.LocalDate


class ExistingLocksPanelSpec extends AbstractSpringWebAppSpec {
  "Existing Locks Panel" should {
    val service = mock[TimesheetLockService]
    springTester.getMockContext.putBean(service)

    "render" in {
      when(service.findAll()).thenReturn(List(LockedTimesheet(new LocalDate().minusDays(5), new LocalDate())))

      tester.startComponentInPage(classOf[ExistingLocksPanel])
      tester.assertNoErrorMessage()
    }
  }
}
