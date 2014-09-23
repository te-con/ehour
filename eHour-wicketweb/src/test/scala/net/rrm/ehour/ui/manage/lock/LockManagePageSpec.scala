package net.rrm.ehour.ui.manage.lock

import com.google.common.collect.Lists
import net.rrm.ehour.AbstractSpringWebAppSpec
import net.rrm.ehour.timesheet.service.TimesheetLockService
import net.rrm.ehour.user.service.UserService
import org.apache.wicket.Component
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.event.Broadcast
import org.mockito.Matchers._
import org.mockito.Mockito._

class LockManagePageSpec extends AbstractSpringWebAppSpec {
  "Lock Manage Page" should {
    val service = mockService[TimesheetLockService]

    mockService[UserService]

    val lock = LockAdminBackingBeanObjectMother.create.lock

    before {
      lock.setLockId(null)
      reset(service)
      when(service.findAll()).thenReturn(List(lock))
    }

    "render" in {
      tester.startPage(classOf[LockManagePage])

      tester.assertNoErrorMessage()
      tester.assertNoInfoMessage()
    }

    "edit first entry" in {
      lock.setLockId(5)
      tester.startPage(classOf[LockManagePage])

      tester.executeAjaxEvent("entrySelectorFrame:entrySelectorFrame_body:lockSelector:entrySelectorFrame:blueBorder:blueBorder_body", "click")
      tester.assertNoErrorMessage()
      tester.assertNoInfoMessage()
    }

    "persist lock after added event" in {
      val page = tester.startPage(classOf[LockManagePage])

      val target = mock[AjaxRequestTarget]

      page.send(page, Broadcast.DEPTH, LockAddedEvent(lock, target))

      verify(service).createNew(None, lock.getDateStart, lock.getDateEnd, Lists.newArrayList())
      verify(service, times(2)).findAll()
      verify(target, times(2)).add(any(classOf[Component]))

      tester.assertNoErrorMessage()
    }

    "update lock after edit event" in {
      val page = tester.startPage(classOf[LockManagePage])

      lock.setLockId(5)
      val target = mock[AjaxRequestTarget]

      page.send(page, Broadcast.DEPTH, LockEditedEvent(lock, target))

      verify(service).updateExisting(5, lock.getDateStart, lock.getDateEnd, lock.getName, Lists.newArrayList())
      verify(service, times(2)).findAll()
      verify(target, times(2)).add(any(classOf[Component]))

      tester.assertNoErrorMessage()
    }

    "remove lock after unlocked event" in {
      val page = tester.startPage(classOf[LockManagePage])

      lock.setLockId(5)
      val target = mock[AjaxRequestTarget]

      page.send(page, Broadcast.DEPTH, UnlockedEvent(lock, target))

      verify(service).deleteLock(5)
      verify(service, times(2)).findAll()
      verify(target, times(2)).add(any(classOf[Component]))

      tester.assertNoErrorMessage()
    }
  }
}
