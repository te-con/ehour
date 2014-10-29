package net.rrm.ehour.ui.manage.project.assign

import net.rrm.ehour.AbstractSpringWebAppSpec
import net.rrm.ehour.domain.{ProjectAssignment, ProjectAssignmentObjectMother, ProjectObjectMother}
import net.rrm.ehour.project.service.{ProjectAssignmentManagementService, ProjectAssignmentService}
import net.rrm.ehour.ui.common.panel.multiselect.MultiUserSelect
import net.rrm.ehour.ui.common.wicket.Container
import net.rrm.ehour.ui.manage.assignment.AssignmentFormPanel
import net.rrm.ehour.ui.manage.project.ProjectAdminBackingBean
import net.rrm.ehour.user.service.UserService
import net.rrm.ehour.util._
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.model.Model
import org.mockito.Mockito._

class ManageAssignmentsPanelSpec extends AbstractSpringWebAppSpec {
  "Manage Assignments Panel" should {
    val assignmentService = mockService[ProjectAssignmentService]
    mockService[UserService]

    mockService[ProjectAssignmentManagementService]

    "render" in {
      tester.startComponentInPage(new ManageAssignmentsPanel("id", new Model(new ProjectAdminBackingBean(mockAssignment.getProject))))
      tester.assertNoErrorMessage()
    }

    "initialize for a new assignment" in {
      val subject = new ManageAssignmentsPanel("id", new Model(new ProjectAdminBackingBean(mockAssignment.getProject)))
      tester.startComponentInPage(subject)

      val target = mock[AjaxRequestTarget]

      val event = mockEvent(NewAssignmentEvent(target))

      subject.onEvent(event)

      tester.assertComponent("%s:%s" format (subject.getBorderContainer.getPageRelativePath, subject.ListId), classOf[MultiUserSelect])
      tester.assertComponent("%s:%s" format (subject.getBorderContainer.getPageRelativePath, subject.AffectedUserId), classOf[Container])

      tester.assertNoErrorMessage()
    }

    "edit an existing assignment" in {
      val subject = new ManageAssignmentsPanel("id", new Model(new ProjectAdminBackingBean(mockAssignment.getProject)))
      tester.startComponentInPage(subject)

      val target = mock[AjaxRequestTarget]

      val event = mockEvent(EditAssignmentEvent(mockAssignment, target))

      subject.onEvent(event)

      tester.assertComponent("%s:%s" format (subject.getBorderContainer.getPageRelativePath, subject.FormId), classOf[AssignmentFormPanel])
      tester.assertComponent("%s:%s" format (subject.getBorderContainer.getPageRelativePath, subject.AffectedUserId), classOf[AffectedUserPanel])

      tester.assertNoErrorMessage()
    }


    def mockAssignment:ProjectAssignment = {
      val project = ProjectObjectMother.createProject(1)
      val assignment = ProjectAssignmentObjectMother.createProjectAssignment(1)
      when(assignmentService.getProjectAssignmentsAndCheckDeletability(project)).thenReturn(toJava(List(assignment)))
      assignment
    }
  }
}
