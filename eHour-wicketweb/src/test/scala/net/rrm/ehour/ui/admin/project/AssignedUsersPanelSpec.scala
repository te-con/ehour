package net.rrm.ehour.ui.admin.project

import net.rrm.ehour.AbstractSpringWebAppSpec
import net.rrm.ehour.domain.{UserObjectMother, ProjectAssignmentObjectMother, ProjectObjectMother}
import org.apache.wicket.model.Model
import net.rrm.ehour.project.service.ProjectAssignmentService
import net.rrm.ehour.user.service.UserService
import org.mockito.Mockito._
import org.mockito.Matchers._
import net.rrm.ehour.util._
import java.util

class AssignedUsersPanelSpec extends AbstractSpringWebAppSpec {
  "Assigned Users panel" should {
    val assignmentService = mock[ProjectAssignmentService]
    springTester.getMockContext.putBean(assignmentService)

    val userService = mock[UserService]
    springTester.getMockContext.putBean(userService)

    "render" in {
      val project = ProjectObjectMother.createProject(1)
      when(assignmentService.getProjectAssignments(project, true)).thenReturn(toJava(List(ProjectAssignmentObjectMother.createProjectAssignment(1))))

      tester.startComponentInPage(new AssignedUsersPanel("id", new Model(new ProjectAdminBackingBean(project))))
      tester.assertNoErrorMessage()
    }

    "render with (new) project without id" in {
      val project = ProjectObjectMother.createProject(1)
      project.setProjectId(null)

      tester.startComponentInPage(new AssignedUsersPanel("id", new Model(new ProjectAdminBackingBean(project))))
      tester.assertNoErrorMessage()

      verify(assignmentService, never).getProjectAssignment(anyInt())
    }

    "add unassigned users to the list when toggle box is clicked" in {
      when(userService.getActiveUsers).thenReturn(util.Arrays.asList(UserObjectMother.createUser()))

      val project = ProjectObjectMother.createProject(1)
      when(assignmentService.getProjectAssignments(project, true)).thenReturn(toJava(List(ProjectAssignmentObjectMother.createProjectAssignment(1))))

      tester.startComponentInPage(new AssignedUsersPanel("id", new Model(new ProjectAdminBackingBean(project))))

      tester.clickAjaxCheckBoxToEnable("id:border:border_body:toggleAll")

      verify(userService).getActiveUsers
    }
  }
}
