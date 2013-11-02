package net.rrm.ehour.ui.financial.lock

import net.rrm.ehour.AbstractSpringWebAppSpec
import net.rrm.ehour.timesheet.service.{LockedTimesheet, TimesheetLockService}
import net.rrm.ehour.ui.financial.lock.LockDetailsPanel._
import org.mockito.Mockito._
import org.mockito.Matchers._
import org.joda.time.LocalDate
import org.apache.wicket.event.Broadcast
import org.apache.wicket.ajax.AjaxRequestTarget

class LockDetailsPanelSpec extends AbstractSpringWebAppSpec {

  def createPath(path: String) = s"$OuterBorderId:outerBorder_body:$FormId:$path"

  "Lock Details Panel" should {
    val service = mock[TimesheetLockService]
    springTester.getMockContext.putBean(service)

    "render" in {
      tester.startComponentInPage(classOf[LockDetailsPanel])
      tester.assertNoErrorMessage()
    }

    "create a new lock" in {
      tester.startComponentInPage(classOf[LockDetailsPanel])

      val formTester = tester.newFormTester(createPath(""))

      formTester.setValue("startDate", "01/01/12")
      formTester.setValue("endDate", "01/01/13")

      tester.executeAjaxEvent(createPath("submit"), "onclick")

      tester.assertNoInfoMessage()
      tester.assertNoErrorMessage()

      val confirm = tester.getComponentFromLastRenderedPage(createPath("saveConfirm"))
      assert(confirm.getDefaultModelObject == "Locked")

      verify(service).createNew(any(classOf[LocalDate]), any(classOf[LocalDate]))
    }

    "show details of an event when Edit Event is received" in {
      val id = 5

      when(service.find(id)).thenReturn(Some(LockedTimesheet(Some(id), new LocalDate(), new LocalDate(), Some("name"))))

      val panel = tester.startComponentInPage(classOf[LockDetailsPanel])

      panel.send(tester.getLastRenderedPage, Broadcast.DEPTH, new EditLockEvent(id, mock[AjaxRequestTarget]))

      val model = panel.getDefaultModelObject.asInstanceOf[LockModel]
      model.name should be ("name")
    }
  }
}
