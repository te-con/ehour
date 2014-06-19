package net.rrm.ehour.ui.admin.user

import java.util
import java.util.Calendar

import net.rrm.ehour.AbstractSpringWebAppSpec
import net.rrm.ehour.domain.{User, UserObjectMother}
import net.rrm.ehour.timesheet.dto.TimesheetOverview
import net.rrm.ehour.timesheet.service.IOverviewTimesheet
import net.rrm.ehour.ui.common.panel.entryselector.EntrySelectedEvent
import net.rrm.ehour.ui.common.session.EhourWebSession
import net.rrm.ehour.user.service.UserService
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.event.Broadcast
import org.mockito.Matchers._
import org.mockito.Mockito._
import org.scalatest.BeforeAndAfter

class ImpersonateUserPageSpec extends AbstractSpringWebAppSpec with BeforeAndAfter {
  val PathToImpersonateLink = "frame:frame_body:border:border_body:content:impersonateLink"

  "Impersonate User Page" should {
    val service = mockService[UserService]
    val timesheetService = mockService[IOverviewTimesheet]

    before {
      reset(service, timesheetService)
      when(service.getActiveUsers).thenReturn(util.Arrays.asList(new User("thies", "thies")))
    }

    "render" in {
      tester.startPage(classOf[ImpersonateUserPage])

      tester.assertNoErrorMessage()
      tester.assertNoInfoMessage()
    }

    "handle entryselectedevent by showing that user in the content panel" in {
      val user = UserObjectMother.createUser
      when(service.getUser(1)).thenReturn(user)

      tester.startPage(classOf[ImpersonateUserPage])

      val target = mock[AjaxRequestTarget]

      val page = tester.getLastRenderedPage
      page.send(page, Broadcast.DEPTH, EntrySelectedEvent(1, target))

      tester.assertNoErrorMessage()
      tester.assertNoInfoMessage()

      verify(service).getUser(1)
      verify(target).add(any())
    }

    "impersonate user" in {
      val user = UserObjectMother.createUser

      when(timesheetService.getTimesheetOverview(any[User], any[Calendar])).thenReturn(new TimesheetOverview)

      when(service.getUser(1)).thenReturn(user)

      tester.startPage(classOf[ImpersonateUserPage])

      val target = mock[AjaxRequestTarget]

      val page = tester.getLastRenderedPage
      page.send(page, Broadcast.DEPTH, EntrySelectedEvent(1, target))

      tester.clickLink(PathToImpersonateLink)

      EhourWebSession.getSession shouldBe 'impersonating
    }

    "cannot impersonate user when user is already impersonating" in {
      val user = UserObjectMother.createUser

      when(timesheetService.getTimesheetOverview(any[User], any[Calendar])).thenReturn(new TimesheetOverview)

      when(service.getUser(1)).thenReturn(user)

      tester.startPage(classOf[ImpersonateUserPage])

      val target = mock[AjaxRequestTarget]

      val page = tester.getLastRenderedPage
      page.send(page, Broadcast.DEPTH, EntrySelectedEvent(1, target))

      tester.clickLink(PathToImpersonateLink)

      tester.startPage(classOf[ImpersonateUserPage])
      val page2 = tester.getLastRenderedPage
      page2.send(page2, Broadcast.DEPTH, EntrySelectedEvent(1, target))

      tester.getLastRenderedPage.get(PathToImpersonateLink) should be(null)
    }
  }
}
