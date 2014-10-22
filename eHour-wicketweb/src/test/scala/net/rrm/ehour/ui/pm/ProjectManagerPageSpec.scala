package net.rrm.ehour.ui.pm

import net.rrm.ehour.AbstractSpringWebAppSpec
import net.rrm.ehour.domain.{ProjectObjectMother, User}
import net.rrm.ehour.project.service.{ProjectAssignmentManagementService, ProjectAssignmentService, ProjectService}
import net.rrm.ehour.report.reports.ProjectManagerReport
import net.rrm.ehour.report.service.AggregateReportService
import net.rrm.ehour.user.service.UserService
import org.mockito.Matchers._
import org.mockito.Mockito._

import scala.collection.convert.WrapAsJava
import scala.collection.mutable.ListBuffer

class ProjectManagerPageSpec extends AbstractSpringWebAppSpec {
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
      tester.startPage(classOf[ProjectManagerPage])
      tester.assertNoErrorMessage()
    }

    "load first entry" in {
      val project = ProjectObjectMother.createProject(1)
      when(aggregateReportService.getProjectManagerDetailedReport(project)).thenReturn(new ProjectManagerReport)
      when(projectService.getProjectManagerProjects(any(classOf[User]))).thenReturn(WrapAsJava.bufferAsJavaList(ListBuffer(project)))
      when(projectService.getProject(1)).thenReturn(project)

      tester.startPage(classOf[ProjectManagerPage])

      tester.executeAjaxEvent("entrySelectorFrame:entrySelectorFrame_body:projectSelector:entrySelectorFrame:blueBorder:blueBorder_body:listScroll:itemList:0", "click")

      tester.assertNoErrorMessage()
    }
  }
}
