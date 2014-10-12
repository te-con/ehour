package net.rrm.ehour.ui.manage.user

import com.google.common.collect.Lists
import net.rrm.ehour.AbstractSpringWebAppSpec
import net.rrm.ehour.domain.{UserObjectMother, User}
import net.rrm.ehour.ui.common.border.GreyBlueRoundedBorder
import net.rrm.ehour.ui.common.panel.entryselector.{EntryListUpdatedEvent, HideInactiveFilter, InactiveFilterChangedEvent}
import net.rrm.ehour.user.service.UserService
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.markup.html.list.ListItem
import org.apache.wicket.markup.html.panel.Fragment
import org.mockito.Matchers._
import org.mockito.Mockito._

class UserSelectionPanelSpec extends AbstractSpringWebAppSpec  {
  "User Selection Panel" should {
    val service = mockService[UserService]

    before {
      reset(service)
    }

    "render" in {
      when(service.getUsers).thenReturn(Lists.newArrayList(UserObjectMother.createUser()))

      startPanel()
      tester.assertNoErrorMessage()

      tester.assertComponent("id:border:border_body:entrySelectorFrame:entrySelectorFrame:blueBorder:blueBorder_body:itemList:0", classOf[ListItem[_]])
    }

    "handle updated list event" in {
      when(service.getUsers).thenReturn(Lists.newArrayList(UserObjectMother.createUser()))

      val component = startPanel()

      val target = mock[AjaxRequestTarget]
      val event = mockEvent(EntryListUpdatedEvent(target))

      component.onEvent(event)

      tester.assertNoErrorMessage()

      verify(target).add(isA(classOf[GreyBlueRoundedBorder]))
    }

    "handle inactive filter event" in {
      when(service.getUsers).thenReturn(Lists.newArrayList(UserObjectMother.createUser()))

      val component = startPanel()

      val target = mock[AjaxRequestTarget]
      val event = mockEvent(InactiveFilterChangedEvent(new HideInactiveFilter(), target))

      component.onEvent(event)

      tester.assertNoErrorMessage()

      verify(target).add(isA(classOf[GreyBlueRoundedBorder]))
    }
  }

  def startPanel() = tester.startComponentInPage(new UserSelectionPanel("id", None))
}
