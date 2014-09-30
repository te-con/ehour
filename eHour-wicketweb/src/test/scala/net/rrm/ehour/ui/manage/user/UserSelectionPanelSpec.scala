package net.rrm.ehour.ui.manage.user

import com.google.common.collect.Lists
import net.rrm.ehour.AbstractSpringWebAppSpec
import net.rrm.ehour.domain.UserObjectMother
import net.rrm.ehour.ui.common.panel.entryselector.{EntryListUpdatedEvent, HideInactiveFilter, InactiveFilterChangedEvent}
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.markup.html.list.ListItem
import org.apache.wicket.markup.html.panel.Fragment
import org.easymock.EasyMock
import org.mockito.Matchers._
import org.mockito.Mockito._

class UserSelectionPanelSpec extends AbstractSpringWebAppSpec  {
  "User Selection Panel" should {

    "render" in {
      EasyMock.reset(springTester.userService)
      EasyMock.expect(springTester.userService.getUsers).andReturn(Lists.newArrayList(UserObjectMother.createUser()))
      EasyMock.replay(springTester.userService)

      startPanel()
      tester.assertNoErrorMessage()

      tester.assertComponent("id:border:border_body:entrySelectorFrame:entrySelectorFrame:blueBorder:blueBorder_body:itemListHolder:itemList:0", classOf[ListItem[_]])
    }

    "handle updated list event" in {
      EasyMock.reset(springTester.userService)
      EasyMock.expect(springTester.userService.getUsers).andReturn(Lists.newArrayList(UserObjectMother.createUser())).times(2)
      EasyMock.replay(springTester.userService)

      val component = startPanel()

      val target = mock[AjaxRequestTarget]
      val event = mockEvent(EntryListUpdatedEvent(target))

      component.onEvent(event)

      tester.assertNoErrorMessage()

      verify(target).add(isA(classOf[Fragment]))
    }

    "handle inactive filter event" in {
      EasyMock.reset(springTester.userService)
      EasyMock.expect(springTester.userService.getUsers).andReturn(Lists.newArrayList(UserObjectMother.createUser())).times(2)
      EasyMock.replay(springTester.userService)

      val component = startPanel()

      val target = mock[AjaxRequestTarget]
      val event = mockEvent(InactiveFilterChangedEvent(new HideInactiveFilter(), target))

      component.onEvent(event)

      tester.assertNoErrorMessage()

      verify(target).add(isA(classOf[Fragment]))
    }
  }

  def startPanel() = tester.startComponentInPage(new UserSelectionPanel("id", None))
}
