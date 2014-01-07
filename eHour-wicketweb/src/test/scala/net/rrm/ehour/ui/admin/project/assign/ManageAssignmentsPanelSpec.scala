package net.rrm.ehour.ui.admin.project.assign

import net.rrm.ehour.AbstractSpringWebAppSpec
import net.rrm.ehour.project.service.{ProjectAssignmentManagementService, ProjectAssignmentService}
import net.rrm.ehour.user.service.UserService
import net.rrm.ehour.domain.{Project, ProjectAssignmentObjectMother, ProjectObjectMother}
import org.mockito.Mockito._
import net.rrm.ehour.util._
import org.apache.wicket.model.Model
import net.rrm.ehour.ui.admin.project.ProjectAdminBackingBean
import org.apache.wicket.ajax.AjaxRequestTarget

class ManageAssignmentsPanelSpec extends AbstractSpringWebAppSpec {
  "Manage Assignments Panel" should {
    val assignmentService = mockService[ProjectAssignmentService]
    mockService[UserService]

    mockService[ProjectAssignmentManagementService]

    "render" in {
      tester.startComponentInPage(new ManageAssignmentsPanel("id", new Model(new ProjectAdminBackingBean(mockAssignment))))
      tester.assertNoErrorMessage()
    }

    "initialize for a new assignment" in {
      val subject = new ManageAssignmentsPanel("id", new Model(new ProjectAdminBackingBean(mockAssignment)))
      tester.startComponentInPage(subject)

      val target = mock[AjaxRequestTarget]

      val event = mockEvent(NewAssignmentEvent(target))

      subject.onEvent(event)

      tester.assertComponent("%s:%s" format (subject.getBorderContainer.getPageRelativePath, subject.LIST_ID), classOf[NewAssignmentUserListView])
      tester.assertComponent("%s:%s" format (subject.getBorderContainer.getPageRelativePath, subject.AFFECTED_USER_ID), classOf[AffectedUsersPanel])

      tester.assertNoErrorMessage()
    }

    def mockAssignment:Project = {
      val project = ProjectObjectMother.createProject(1)
      val assignment = ProjectAssignmentObjectMother.createProjectAssignment(1)
      when(assignmentService.getProjectAssignmentsAndCheckDeletability(project)).thenReturn(toJava(List(assignment)))
      project
    }
  }
}
