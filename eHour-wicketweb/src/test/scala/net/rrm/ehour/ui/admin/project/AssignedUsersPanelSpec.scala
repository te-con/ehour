package net.rrm.ehour.ui.admin.project

import net.rrm.ehour.AbstractSpringWebAppSpec
import net.rrm.ehour.domain.{ProjectAssignmentObjectMother, ProjectObjectMother}
import org.apache.wicket.model.Model
import net.rrm.ehour.project.service.ProjectAssignmentService
import net.rrm.ehour.user.service.UserService
import org.mockito.Mockito._
import net.rrm.ehour.util._

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

    "add unassigned users when toggle box is clicked" in {
      val project = ProjectObjectMother.createProject(1)
      when(assignmentService.getProjectAssignments(project, true)).thenReturn(toJava(List(ProjectAssignmentObjectMother.createProjectAssignment(1))))

      tester.startComponentInPage(new AssignedUsersPanel("id", new Model(new ProjectAdminBackingBean(project))))

      tester.executeAjaxEvent("id:border:border_body:toggleAll", "click")
    }
  }
}
