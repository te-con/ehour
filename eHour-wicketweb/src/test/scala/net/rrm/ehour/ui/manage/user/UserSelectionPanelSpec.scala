package net.rrm.ehour.ui.manage.user

import java.util

import net.rrm.ehour.AbstractSpringWebAppSpec
import net.rrm.ehour.domain.User
import net.rrm.ehour.ui.common.panel.entryselector.{EntryListUpdatedEvent, HideInactiveFilter, InactiveFilterChangedEvent}
import net.rrm.ehour.ui.common.wicket.Container
import net.rrm.ehour.user.service.UserService
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.markup.html.list.ListItem
import org.mockito.Matchers._
import org.mockito.Mockito._
import org.scalatest.BeforeAndAfter

class UserSelectionPanelSpec extends AbstractSpringWebAppSpec with BeforeAndAfter {
  "User Selection Panel" should {
    val service = mockService[UserService]

    before {
      reset(service)
    }

    "render" in {
      when(service.getActiveUsers).thenReturn(util.Arrays.asList(new User("thies", "thies")))

      startPanel()
      tester.assertNoErrorMessage()

      tester.assertComponent("id:border:border_body:entrySelectorFrame:entrySelectorFrame:blueBorder:blueBorder_body:listScroll:itemList:0", classOf[ListItem[_]])
    }

    "handle updated list event" in {
      when(service.getActiveUsers).thenReturn(util.Arrays.asList(new User("thies", "thies")))

      val component = startPanel()

      val target = mock[AjaxRequestTarget]
      val event = mockEvent(EntryListUpdatedEvent(target))

      component.onEvent(event)

      tester.assertNoErrorMessage()

      verify(target).add(isA(classOf[Container]))
    }

    "handle inactive filter event" in {
      when(service.getActiveUsers).thenReturn(util.Arrays.asList(new User("thies", "thies")))

      val component = startPanel()

      val target = mock[AjaxRequestTarget]
      val event = mockEvent(InactiveFilterChangedEvent(new HideInactiveFilter(), target))

      component.onEvent(event)

      tester.assertNoErrorMessage()

      verify(target).add(isA(classOf[Container]))
    }
  }

  def startPanel() = tester.startComponentInPage(new UserSelectionPanel("id", None))
}
