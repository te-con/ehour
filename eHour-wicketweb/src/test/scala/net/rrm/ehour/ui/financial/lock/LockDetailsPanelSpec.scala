package net.rrm.ehour.ui.financial.lock

import net.rrm.ehour.AbstractSpringWebAppSpec
import net.rrm.ehour.timesheet.service.TimesheetLockService
import net.rrm.ehour.ui.financial.lock.LockDetailsPanel._
import org.mockito.Mockito._
import org.mockito.Matchers._
import org.joda.time.LocalDate

class LockDetailsPanelSpec extends AbstractSpringWebAppSpec {

  "Lock Details Panel" should {
    val service = mock[TimesheetLockService]
    springTester.getMockContext.putBean(service)

    "render" in {
      tester.startComponentInPage(classOf[LockDetailsPanel])
      tester.assertNoErrorMessage()
    }

    "create a new lock" in {
      def createPath(path: String) = s"$OuterBorderId:outerBorder_body:$FormId:$path"

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
  }
}
