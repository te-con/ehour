package net.rrm.ehour.ui.financial.lock

import net.rrm.ehour.AbstractSpringWebAppSpec
import net.rrm.ehour.timesheet.service.TimesheetLockService
import org.mockito.Mockito._
import org.mockito.Matchers._
import net.rrm.ehour.domain.TimesheetLock
import java.util.Date
import org.apache.wicket.event.Broadcast
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.Component
import org.scalatest.BeforeAndAfter

class LockAdminPageSpec extends AbstractSpringWebAppSpec with BeforeAndAfter  {
  "Lock Admin Page" should {
    val service = mockService[TimesheetLockService]

    val lock = new TimesheetLock()
    lock.setDateStart(new Date())
    lock.setDateEnd(new Date)

    when(service.findAll()).thenReturn(List(lock))

    before {
      reset(service)
    }

    "render" in {
      tester.startPage(classOf[LockAdminPage])

      tester.assertNoErrorMessage()
      tester.assertNoInfoMessage()
    }

    "handle lock added event" in {
      val page = tester.startPage(classOf[LockAdminPage])

      val bean = new LockAdminBackingBean(lock)
      val target = mock[AjaxRequestTarget]

      page.send(page, Broadcast.DEPTH, LockAddedEvent(bean, target))

      verify(service).createNew(None, lock.getDateStart, lock.getDateEnd)
      verify(service, times(2)).findAll()
      verify(target, times(2)).add(any(classOf[Component]))

      tester.assertNoErrorMessage()
    }

    "handle lock edit event" in {
      val page = tester.startPage(classOf[LockAdminPage])

      val bean = new LockAdminBackingBean(lock)
      lock.setLockId(5)
      val target = mock[AjaxRequestTarget]

      page.send(page, Broadcast.DEPTH, LockEditedEvent(bean, target))

      verify(service).updateExisting(5, lock.getDateStart, lock.getDateEnd, lock.getName)
      verify(service, times(2)).findAll()
      verify(target, times(2)).add(any(classOf[Component]))

      tester.assertNoErrorMessage()
    }

    "handle lock unlocked event" in {
      val page = tester.startPage(classOf[LockAdminPage])

      val bean = new LockAdminBackingBean(lock)
      lock.setLockId(5)
      val target = mock[AjaxRequestTarget]

      page.send(page, Broadcast.DEPTH, UnlockedEvent(bean, target))

      verify(service).deleteLock(5)
      verify(service, times(2)).findAll()
      verify(target, times(2)).add(any(classOf[Component]))

      tester.assertNoErrorMessage()
    }
  }
}
