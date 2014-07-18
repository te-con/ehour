package net.rrm.ehour.ui.manage.lock

import com.google.common.collect.Lists
import net.rrm.ehour.AbstractSpringWebAppSpec
import net.rrm.ehour.domain.UserObjectMother
import net.rrm.ehour.timesheet.service.TimesheetLockService
import net.rrm.ehour.ui.common.wicket.Container
import net.rrm.ehour.ui.manage.lock.LockFormPanel._
import net.rrm.ehour.user.service.UserService
import org.apache.wicket.model.Model
import org.mockito.Mockito._

class LockFormPanelSpec extends AbstractSpringWebAppSpec {

  val formPath = s"id:$OuterBorderId:greySquaredFrame:outerBorder_body:$FormId"
  def createPath(path: String) = s"$formPath:$path"

  val userService = mockService[UserService]
  val timesheetLockService = mockService[TimesheetLockService]

  "Lock Form Panel" should {

    when(userService.getActiveUsers).thenReturn(Lists.newArrayList(UserObjectMother.createUser()))

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

    "exclude users in new lock" in {
      tester.startComponentInPage(createPanel)
      tester.executeAjaxEvent(createPath("excludedUsers:modify"), "click")

      val formTester = tester.newFormTester(formPath)

      formTester.setValue("startDate", "01/01/12")
      formTester.setValue("endDate", "01/01/13")

      tester.executeAjaxEvent(createPath("excludedUsers:userSelect:allBorder:allBorder_body:users:0"), "click")

      submitForm()

      val confirm = tester.getComponentFromLastRenderedPage(createPath("serverMessage"))
      confirm.getDefaultModelObject should be("Locked")

      val lockAddedEvent = tester.findEvent(classOf[LockAddedEvent])
      lockAddedEvent.isPresent should be (true)

      val payload = lockAddedEvent.get().event.getPayload.asInstanceOf[LockAddedEvent]

      payload.lock.getExcludedUsers.size() should be (1)
    }

    "show affected users" in {
      tester.startComponentInPage(createPanel)

      tester.executeAjaxEvent(s"$formPath:affected:affectedLinkToggle", "click")

      tester.assertComponent(s"$formPath:affectedContainer", classOf[LockAffectedUsersPanel])
    }

    "hide affected users" in {
      tester.startComponentInPage(createPanel)

      tester.executeAjaxEvent(s"$formPath:affected:affectedLinkToggle", "click") // show
      tester.executeAjaxEvent(s"$formPath:affected:affectedLinkToggle", "click") // hide

      tester.assertComponent(s"$formPath:affectedContainer", classOf[Container])
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
