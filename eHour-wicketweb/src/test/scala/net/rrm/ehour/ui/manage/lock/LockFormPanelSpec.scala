package net.rrm.ehour.ui.manage.lock

import net.rrm.ehour.AbstractSpringWebAppSpec
import net.rrm.ehour.ui.manage.lock.LockFormPanel._
import org.apache.wicket.model.Model

class LockFormPanelSpec extends AbstractSpringWebAppSpec {

  val formPath = s"id:$OuterBorderId:greySquaredFrame:outerBorder_body:$FormId"
  def createPath(path: String) = s"$formPath:$path"

  "Lock Form Panel" should {

    def createPanel = new LockFormPanel("id", new Model(LockAdminBackingBeanObjectMother.create))

    def submitForm() { tester.executeAjaxEvent(createPath(LockFormPanel.SubmitId), "onclick") }

    "render" in {
      tester.startComponentInPage(createPanel)
      tester.assertNoErrorMessage()
    }

    "create a new lock" in {
      val panel = createPanel
      tester.startComponentInPage(panel)

      val formTester = tester.newFormTester(formPath)

      formTester.setValue("startDate", "01/01/12")
      formTester.setValue("endDate", "01/01/13")

      submitForm()

      tester.assertNoInfoMessage()
      tester.assertNoErrorMessage()

      val confirm = tester.getComponentFromLastRenderedPage(createPath("serverMessage"))
      confirm.getDefaultModelObject should be("Locked")

      val lockAddedEvent = tester.findEvent(classOf[LockAddedEvent])
      lockAddedEvent.isPresent should be (true)
    }

    "fail to create a new lock when start is after end date" in {
      val panel = createPanel
      tester.startComponentInPage(panel)

      val formTester = tester.newFormTester(formPath)

      formTester.setValue("startDate", "01/01/13")
      formTester.setValue("endDate", "01/01/12")

      submitForm()

      tester.assertErrorMessages("startDate.Date.StartDateAfterEnd")

      val lockAddedEvent = tester.findEvent(classOf[LockAddedEvent])
      lockAddedEvent.isPresent should be (false)
    }

    "update an existing lock" in {
      val id = 5

      val panel = createPanel
      val lockAdminBackingBean = panel.getPanelModelObject
      lockAdminBackingBean.getLock.setLockId(id)

      tester.startComponentInPage(panel)

      submitForm()

      tester.assertNoErrorMessage()
      tester.assertNoInfoMessage()
      val lockModifiedEvent = tester.findEvent(classOf[LockEditedEvent])
      lockModifiedEvent.isPresent should be (true)
    }

    "unlock an existing lock" in {
      val id = 5

      val panel = createPanel
      val lockAdminBackingBean = panel.getPanelModelObject
      lockAdminBackingBean.getLock.setLockId(id)

      tester.startComponentInPage(panel)

      tester.executeAjaxEvent(createPath(LockFormPanel.UnlockId), "onclick")

      val expectedEvent = tester.findEvent(classOf[UnlockedEvent])
      expectedEvent.isPresent should be (true)
    }
  }
}
