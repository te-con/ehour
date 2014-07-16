package net.rrm.ehour.ui.report.panel.criteria

import net.rrm.ehour.AbstractSpringWebAppSpec
import net.rrm.ehour.report.service.ReportCriteriaService
import org.scalatest.BeforeAndAfterAll
import org.mockito.Mockito._
import org.mockito.Matchers._
import net.rrm.ehour.report.criteria.{ReportCriteria, AvailableCriteria, UserSelectedCriteria}
import org.apache.wicket.model.CompoundPropertyModel
import net.rrm.ehour.domain._
import java.{util => ju}
import scala.collection.convert.WrapAsJava
import scala.collection.mutable
import com.google.common.collect.Lists
import java.util.Date


class ReportCriteriaPanelSpec extends AbstractSpringWebAppSpec with BeforeAndAfterAll {
  val service = mock[ReportCriteriaService]

  override def beforeAll() {
    springTester.getMockContext.putBean(service)
    springTester.setUp()
  }

  override def beforeEach() {
    reset(service)
  }

  "Report Criteria Panel" should {
    def toList[T](xs: mutable.Buffer[T]): ju.List[T] = WrapAsJava.bufferAsJavaList(xs)

    val availableCriteria = new AvailableCriteria(toList(mutable.Buffer(CustomerObjectMother.createCustomer())),
      toList(mutable.Buffer(ProjectObjectMother.createProject(1))),
      toList(mutable.Buffer(UserObjectMother.createUser())),
      toList(mutable.Buffer(UserDepartmentObjectMother.createUserDepartment())))
    val criteria = new ReportCriteria(availableCriteria, new UserSelectedCriteria)
    val model = new CompoundPropertyModel[ReportCriteriaBackingBean](new ReportCriteriaBackingBean(criteria))

    "render without locks available" in {
      tester.startComponentInPage(new ReportCriteriaPanel("testObject", model))
      tester.assertNoErrorMessage()
    }

    "render with locks available" in {
      availableCriteria.setTimesheetLocks(Lists.newArrayList(new TimesheetLock(new Date(), new Date(), "period", Lists.newArrayList())))

      tester.startComponentInPage(new ReportCriteriaPanel("testObject", model))
      tester.assertNoErrorMessage()
    }

    "EHO-352: tick only Billable projects should not throw an exception" in {
      when(service.syncUserReportCriteria(anyObject(), anyObject())).thenReturn(criteria)

      tester.startComponentInPage(new ReportCriteriaPanel("testObject", model))
      val checkbox = tester.getComponentFromLastRenderedPage("testObject:border:greySquaredFrame:border_body:criteriaForm:customerProjectsBorder:customerProjectsBorder_body:reportCriteria.userSelectedCriteria.onlyBillableProjects")
      tester.executeAjaxEvent(checkbox, "onclick")
      tester.assertNoErrorMessage()
    }
  }
}
