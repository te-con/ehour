package net.rrm.ehour.ui.admin.user

import java.util

import net.rrm.ehour.AbstractSpringWebAppSpec
import net.rrm.ehour.domain.User
import net.rrm.ehour.ui.common.panel.entryselector.EntryListUpdatedEvent
import net.rrm.ehour.user.service.UserService
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.markup.html.list.ListItem
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

      tester.startComponentInPage(classOf[UserSelectionPanel])
      tester.assertNoErrorMessage()

      tester.assertComponent("testObject:border:border_body:entrySelectorFrame:entrySelectorFrame:blueBorder:blueBorder_body:itemListHolder:itemList:0", classOf[ListItem[_]])
    }

    "handle updated list event" in {
      when(service.getActiveUsers).thenReturn(util.Arrays.asList(new User("thies", "thies")))

      val component = tester.startComponentInPage(classOf[UserSelectionPanel])

      tester.debugComponentTrees()

      val target = mock[AjaxRequestTarget]
      val event = mockEvent(EntryListUpdatedEvent(target))

      component.onEvent(event)

      tester.assertNoErrorMessage()
    }
  }
}
