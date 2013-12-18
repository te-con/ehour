package net.rrm.ehour.ui.financial.lock

import net.rrm.ehour.AbstractSpringWebAppSpec
import net.rrm.ehour.timesheet.service.{AffectedUser, TimesheetLockService}
import org.mockito.Mockito._
import net.rrm.ehour.domain.UserObjectMother
import net.rrm.ehour.ui.common.wicket._
import net.rrm.ehour.ui.common.wicket.WicketDSL._


class AffectedUsersPanelSpec extends AbstractSpringWebAppSpec {
  "Affected Users Panel" should {
    val service = mock[TimesheetLockService]
    springTester.getMockContext.putBean(service)

    "render" in {
      val lockModel = new LockModel()

      when(service.findAffectedUsers(lockModel.startDate, lockModel.endDate)).thenReturn(List(AffectedUser(UserObjectMother.createUser(), 12)))

      tester.startComponentInPage(new AffectedUsersPanel("id", Model(lockModel)))
      tester.assertNoErrorMessage()
    }
  }
}
