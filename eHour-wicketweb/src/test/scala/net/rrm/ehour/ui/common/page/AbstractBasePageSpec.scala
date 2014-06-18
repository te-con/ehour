package net.rrm.ehour.ui.common.page

import java.util.Calendar

import net.rrm.ehour.AbstractSpringWebAppSpec
import net.rrm.ehour.domain.{User, UserObjectMother}
import net.rrm.ehour.timesheet.dto.TimesheetOverview
import net.rrm.ehour.timesheet.service.IOverviewTimesheet
import net.rrm.ehour.ui.common.session.EhourWebSession
import org.apache.wicket.markup.html.panel.Fragment
import org.apache.wicket.model.ResourceModel
import org.mockito.Matchers._
import org.mockito.Mockito._

class AbstractBasePageSpec extends AbstractSpringWebAppSpec {
  "render" in {
    tester.startPage(classOf[DummyPage])

    tester.assertNoErrorMessage()
    tester.assertNoInfoMessage()
    tester.assertRenderedPage(classOf[DummyPage])
  }

  "show impersonate notification" in {
    impersonate()

    tester.assertComponent("impersonate", classOf[Fragment])
  }

  "stop impersonating" in {
    val timesheetService = mockService[IOverviewTimesheet]
    when(timesheetService.getTimesheetOverview(any[User], any[Calendar])).thenReturn(new TimesheetOverview)

    impersonate()

    tester.clickLink("impersonate:stop")

    EhourWebSession.getSession.isImpersonating should be(false)
  }


  def impersonate() {
    tester.startPage(classOf[DummyPage])
    EhourWebSession.getSession.impersonateUser(UserObjectMother.createUser())
    tester.startPage(classOf[DummyPage])
  }
}


class DummyPage extends AbstractBasePage[Void](new ResourceModel("test"))