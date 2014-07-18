package net.rrm.ehour.ui.manage.lock

import net.rrm.ehour.AbstractSpringWebAppSpec
import net.rrm.ehour.domain.UserObjectMother
import net.rrm.ehour.timesheet.service.{AffectedUser, TimesheetLockService}
import net.rrm.ehour.ui.common.wicket.Model
import org.mockito.Mockito._

class LockAffectedUsersPanelSpec extends AbstractSpringWebAppSpec {
  "Affected Users Panel" should {
    val service = mock[TimesheetLockService]
    springTester.getMockContext.putBean(service)

    "render" in {
      val bean = LockAdminBackingBeanObjectMother.create
      val lock = bean.lock

      when(service.findAffectedUsers(lock.getDateStart, lock.getDateEnd, Nil)).thenReturn(List(AffectedUser(UserObjectMother.createUser(), Map())))

      tester.startComponentInPage(new LockAffectedUsersPanel("id", Model(bean)))
      tester.assertNoErrorMessage()
    }
  }
}
