package net.rrm.ehour.ui.pm

import net.rrm.ehour.AbstractSpringWebAppSpec
import net.rrm.ehour.project.service.{ProjectAssignmentService, ProjectAssignmentManagementService, ProjectService}
import org.mockito.Mockito._
import org.mockito.Matchers._
import net.rrm.ehour.domain.{ProjectObjectMother, User}
import scala.collection.convert.WrapAsJava
import scala.collection.mutable.ListBuffer
import net.rrm.ehour.user.service.UserService
import net.rrm.ehour.report.service.AggregateReportService
import net.rrm.ehour.report.reports.ProjectManagerReport

class ProjectManagementPageSpec extends AbstractSpringWebAppSpec {
  "Project Management page" should {
    val projectService = mock[ProjectService]
    springTester.getMockContext.putBean(projectService)

    val managementService = mock[ProjectAssignmentManagementService]
    springTester.getMockContext.putBean(managementService)

    val assignmentService = mock[ProjectAssignmentService]
    springTester.getMockContext.putBean(assignmentService)

    val userService = mock[UserService]
    springTester.getMockContext.putBean(userService)

    val aggregateReportService = mock[AggregateReportService]
    springTester.getMockContext.putBean(aggregateReportService)

    "render" in {
      tester.startPage(classOf[ProjectManagementPage])
      tester.assertNoErrorMessage()
    }

    "load first entry" in {
      val project = ProjectObjectMother.createProject(1)
      when(aggregateReportService.getProjectManagerDetailedReport(project)).thenReturn(new ProjectManagerReport)
      when(projectService.getProjectManagerProjects(any(classOf[User]))).thenReturn(WrapAsJava.bufferAsJavaList(ListBuffer(project)))

      tester.startPage(classOf[ProjectManagementPage])

      tester.executeAjaxEvent("entrySelectorFrame:entrySelectorFrame_body:projectSelector:entrySelectorFrame:blueBorder:blueBorder_body:itemListHolder:itemList:0", "click")

      tester.assertNoErrorMessage()
    }
  }
}
