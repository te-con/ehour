package net.rrm.ehour.ui.manage.project

import net.rrm.ehour.AbstractSpringWebAppSpec
import net.rrm.ehour.customer.service.CustomerService
import net.rrm.ehour.domain.{ProjectAssignmentObjectMother, ProjectObjectMother}
import net.rrm.ehour.project.service.{ProjectAssignmentManagementService, ProjectAssignmentService, ProjectService}
import net.rrm.ehour.user.service.UserService
import net.rrm.ehour.util._
import org.apache.wicket.model.Model
import org.mockito.Mockito._

class ProjectFormContainerSpec extends AbstractSpringWebAppSpec {
  "Project Form Container" should {
    val assignmentService = mockService[ProjectAssignmentService]
    mockService[UserService]
    mockService[CustomerService]
    mockService[ProjectService]
    mockService[ProjectAssignmentManagementService]

    "render" in {
      val project = ProjectObjectMother.createProject(1)
      when(assignmentService.getProjectAssignmentsAndCheckDeletability(project)).thenReturn(toJava(List(ProjectAssignmentObjectMother.createProjectAssignment(1))))

      tester.startComponentInPage(new ProjectFormContainer("id", new Model(new ProjectAdminBackingBean(project))))
      tester.assertNoErrorMessage()
    }
  }
}
