package net.rrm.ehour.ui.financial.lock

import net.rrm.ehour.AbstractSpringWebAppSpec
import net.rrm.ehour.timesheet.service.{AffectedUser, TimesheetLockService}
import net.rrm.ehour.domain.UserObjectMother

import org.mockito.Mockito._
import net.rrm.ehour.ui.common.wicket.Model

class AffectedUsersPanelSpec extends AbstractSpringWebAppSpec {
  "Affected Users Panel" should {
    val service = mock[TimesheetLockService]
    springTester.getMockContext.putBean(service)

    "render" in {
      val bean = LockAdminBackingBeanObjectMother.create
      val lock = bean.lock

      when(service.findAffectedUsers(lock.getDateStart, lock.getDateEnd)).thenReturn(List(AffectedUser(UserObjectMother.createUser(), 12)))

      tester.startComponentInPage(new AffectedUsersPanel("id", Model(bean)))
      tester.assertNoErrorMessage()
    }
  }
}
