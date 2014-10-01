package net.rrm.ehour.ui.manage.user

import java.util.Calendar

import com.google.common.collect.Lists
import com.richemont.jira.JiraService
import com.richemont.windchill.WindChillUpdateService
import net.rrm.ehour.AbstractSpringWebAppSpec
import net.rrm.ehour.domain.{User, UserObjectMother}
import net.rrm.ehour.timesheet.dto.WeekOverview
import net.rrm.ehour.timesheet.service.{IOverviewTimesheet, IPersistTimesheet}
import net.rrm.ehour.ui.common.panel.entryselector.EntrySelectedEvent
import net.rrm.ehour.ui.common.session.EhourWebSession
import net.rrm.ehour.user.service.UserService
import net.rrm.ehour.util._
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.event.Broadcast
import org.easymock.EasyMock
import org.mockito.Matchers._
import org.mockito.Mockito._
import org.scalatest.BeforeAndAfter

class ImpersonateUserPageSpec extends AbstractSpringWebAppSpec with BeforeAndAfter {
  val PathToImpersonateLink = "frame:frame_body:border:border_body:content:impersonateLink"

  "Impersonate User Page" should {
    val overviewTimesheet = mockService[IOverviewTimesheet]

    mockService[IPersistTimesheet]
    mockService[WindChillUpdateService]
    mockService[JiraService]

    before {
      reset(overviewTimesheet)

      EasyMock.reset(springTester.userService)
      EasyMock.expect(springTester.userService.getUsers).andReturn(toJava(List(UserObjectMother.createUser())))
    }

    "render" in {
      EasyMock.replay(springTester.userService)

      tester.startPage(classOf[ImpersonateUserPage])

      tester.assertNoErrorMessage()
      tester.assertNoInfoMessage()
    }

    "handle entryselectedevent by showing that user in the content panel" in {
      val user = UserObjectMother.createUser
      EasyMock.expect(springTester.userService.getUser(1)).andReturn(user)

      EasyMock.replay(springTester.userService)
      tester.startPage(classOf[ImpersonateUserPage])

      val target = mock[AjaxRequestTarget]

      val page = tester.getLastRenderedPage
      page.send(page, Broadcast.DEPTH, EntrySelectedEvent(1, target))

      tester.assertNoErrorMessage()
      tester.assertNoInfoMessage()

      EasyMock.verify(springTester.userService)
      verify(target).add(any())
    }

    "impersonate user" in {
      prepare(springTester.userService, overviewTimesheet)
      val user = UserObjectMother.createUser
      EasyMock.expect(springTester.userService.getUser(1)).andReturn(user)

      EasyMock.replay(springTester.userService)
      tester.startPage(classOf[ImpersonateUserPage])

      val target = mock[AjaxRequestTarget]

      val page = tester.getLastRenderedPage
      page.send(page, Broadcast.DEPTH, EntrySelectedEvent(1, target))

      tester.clickLink(PathToImpersonateLink)

      EhourWebSession.getSession shouldBe 'impersonating
    }

    "cannot impersonate user when user is already impersonating" in {
      prepare(springTester.userService, overviewTimesheet)
      val user = UserObjectMother.createUser
      EasyMock.expect(springTester.userService.getUser(1)).andReturn(user)
      EasyMock.expect(springTester.userService.getUsers).andReturn(toJava(List(UserObjectMother.createUser())))

      EasyMock.replay(springTester.userService)
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

  def prepare(service: UserService, overviewTimesheet: IOverviewTimesheet) {
    val user = UserObjectMother.createUser

    when(overviewTimesheet.getWeekOverview(any[User], any[Calendar])).thenReturn(new WeekOverview(Lists.newArrayList(), Lists.newArrayList()))

    EasyMock.expect(springTester.userService.getUser(1)).andReturn(user)
  }
}
