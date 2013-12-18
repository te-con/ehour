package net.rrm.ehour.ui.financial.lock

import net.rrm.ehour.AbstractSpringWebAppSpec
import net.rrm.ehour.timesheet.service.{LockedTimesheet, TimesheetLockService}
import net.rrm.ehour.ui.financial.lock.LockDetailsPanel._
import org.mockito.Mockito._
import org.mockito.Matchers._
import org.joda.time.LocalDate
import org.apache.wicket.event.Broadcast
import org.apache.wicket.ajax.AjaxRequestTarget
import org.scalatest.BeforeAndAfterAll
import net.rrm.ehour.ui.common.wicket._
import net.rrm.ehour.ui.common.wicket.WicketDSL._

class LockDetailsPanelSpec extends AbstractSpringWebAppSpec with BeforeAndAfterAll {

  def createPath(path: String) = s"$OuterBorderId:outerBorder_body:$FormId:$path"

  val service = mock[TimesheetLockService]

  override def beforeAll() {
    springTester.getMockContext.putBean(service)
    springTester.setUp()
  }

  override def beforeEach() {
    reset(service)
  }

  "Lock Details Panel" should {

    "render" in {
      tester.startComponentInPage(classOf[LockDetailsPanel])
      tester.assertNoErrorMessage()
    }

    "create a new lock" in {
      tester.startComponentInPage(classOf[LockDetailsPanel])

      val formTester = tester.newFormTester(createPath(""))

      formTester.setValue("startDate", "01/01/12")
      formTester.setValue("endDate", "01/01/13")

      submitForm()

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

    "update an existing lock" in {
      val id = 5

      val lockedTimesheet = LockedTimesheet(Some(id), new LocalDate(), new LocalDate(), Some("name"))

      when(service.find(id)).thenReturn(Some(lockedTimesheet))

      val panel = new LockDetailsPanel("testObject", Model(new LockModel(Some(5), "name")))

      tester.startComponentInPage(panel)

      submitForm()

      verify(service).updateExisting(anyInt(), any(classOf[LocalDate]), any(classOf[LocalDate]), anyString)
    }
  }

  def submitForm() {
    tester.executeAjaxEvent(createPath("submit"), "onclick")
  }
}
