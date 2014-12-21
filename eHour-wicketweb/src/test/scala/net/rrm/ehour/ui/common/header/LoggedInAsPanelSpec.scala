package net.rrm.ehour.ui.common.header

import java.util.Calendar

import com.google.common.collect.Lists
import net.rrm.ehour.AbstractSpringWebAppSpec
import net.rrm.ehour.domain.{User, UserObjectMother}
import net.rrm.ehour.timesheet.dto.{TimesheetOverview, WeekOverview}
import net.rrm.ehour.timesheet.service.{IOverviewTimesheet, IPersistTimesheet, TimesheetLockService}
import net.rrm.ehour.ui.common.session.EhourWebSession
import net.rrm.ehour.ui.timesheet.model.TimesheetModel
import net.rrm.ehour.ui.timesheet.panel.renderer.SectionRenderFactoryCollection
import org.apache.wicket.markup.html.panel.Fragment
import org.mockito.Matchers._
import org.mockito.Mockito._

class LoggedInAsPanelSpec extends AbstractSpringWebAppSpec {
  "Logged In As Panel" should {
    "render" in {
      startPanel()

      tester.assertNoErrorMessage()
    }

    "show impersonate notification" in {
      impersonate()

      tester.assertComponent("impersonatingNotification", classOf[Fragment])
    }

    "stop impersonating" in {
      springTester.getMockContext.putBean(new TimesheetModel())
      mockService[TimesheetLockService]
      mockService[IPersistTimesheet]
      mockService[SectionRenderFactoryCollection]
      val overviewTimesheet = mockService[IOverviewTimesheet]

      when(overviewTimesheet.getWeekOverview(any[User], any[Calendar])).thenReturn(new WeekOverview(Lists.newArrayList(), Lists.newArrayList()))
      when(overviewTimesheet.getTimesheetOverview(any[User], any[Calendar])).thenReturn(new TimesheetOverview)

      impersonate()

      tester.clickLink("impersonatingNotification:stop")

      EhourWebSession.getSession.isImpersonating should be(false)
    }


    def impersonate() {
      startPanel()
      EhourWebSession.getSession.impersonateUser(UserObjectMother.createUser())
      startPanel()
    }
  }

  def startPanel() {
    tester.startComponentInPage(classOf[LoggedInAsPanel])
  }
}
