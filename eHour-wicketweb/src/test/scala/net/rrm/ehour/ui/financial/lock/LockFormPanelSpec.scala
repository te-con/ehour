package net.rrm.ehour.ui.financial.lock

import net.rrm.ehour.AbstractSpringWebAppSpec
import net.rrm.ehour.domain.TimesheetLock
import org.apache.wicket.model.Model

import net.rrm.ehour.ui.financial.lock.LockFormPanel._

class LockFormPanelSpec extends AbstractSpringWebAppSpec {

  val formPath = s"id:$OuterBorderId:greySquaredFrame:outerBorder_body:$FormId"
  def createPath(path: String) = s"$formPath:$path"

  "Lock Form Panel" should {

    def createPanel = new LockFormPanel("id", new Model(new LockAdminBackingBean(new TimesheetLock())))

    def submitForm() { tester.executeAjaxEvent(createPath("submit"), "onclick")
    }

    "render" in {
      tester.startComponentInPage(createPanel)
      tester.assertNoErrorMessage()
    }

    "create a new lock" in {
      val panel = createPanel
      tester.startComponentInPage(panel)
      tester.debugComponentTrees()

      val formTester = tester.newFormTester(formPath)

      formTester.setValue("startDate", "01/01/12")
      formTester.setValue("endDate", "01/01/13")

      submitForm()

      tester.assertNoInfoMessage()
      tester.assertNoErrorMessage()

      val confirm = tester.getComponentFromLastRenderedPage(createPath("saveConfirm"))
      confirm.getDefaultModelObject should be("Locked")

      val lockAddedEvent = tester.findEvent(classOf[LockAddedEvent])
      lockAddedEvent.isPresent should be (true)
    }

    "fail to create a new lock when start is after end date" in {
      val panel = createPanel
      tester.startComponentInPage(panel)
      tester.debugComponentTrees()

      val formTester = tester.newFormTester(formPath)

      formTester.setValue("startDate", "01/01/13")
      formTester.setValue("endDate", "01/01/12")

      submitForm()

      tester.assertErrorMessages("startDate.Date.StartDateAfterEnd")

      val lockAddedEvent = tester.findEvent(classOf[LockAddedEvent])
      lockAddedEvent.isPresent should be (false)
    }
//
//    "show details of an event when Edit Event is received" in {
//      val id = 5
//
//      when(service.find(id)).thenReturn(Some(LockedTimesheet(Some(id), new LocalDate(), new LocalDate(), Some("name"))))
//
//      val panel = tester.startComponentInPage(classOf[LockDetailsPanel])
//
//      panel.send(tester.getLastRenderedPage, Broadcast.DEPTH, new EditLockEvent(id, mock[AjaxRequestTarget]))
//
//      val model = panel.getDefaultModelObject.asInstanceOf[LockModel]
//      model.name should be ("name")
//    }
//
//    "update an existing lock" in {
//      val id = 5
//
//      val lockedTimesheet = LockedTimesheet(Some(id), new LocalDate(), new LocalDate(), Some("name"))
//
//      when(service.find(id)).thenReturn(Some(lockedTimesheet))
//
//      val panel = new LockDetailsPanel("testObject", Model(new LockModel(Some(5), "name")))
//
//      tester.startComponentInPage(panel)
//
//      submitForm()
//
//      verify(service).updateExisting(anyInt(), any(classOf[LocalDate]), any(classOf[LocalDate]), anyString)
//    }
  }

}
