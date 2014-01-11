package net.rrm.ehour.ui.admin.project.assign

import net.rrm.ehour.AbstractSpringWebAppSpec
import net.rrm.ehour.project.service.ProjectAssignmentService
import net.rrm.ehour.domain.{ProjectAssignmentObjectMother, ProjectObjectMother}
import org.mockito.Mockito._
import net.rrm.ehour.util._
import org.apache.wicket.model.Model
import net.rrm.ehour.ui.admin.project.ProjectAdminBackingBean

class CurrentAssignmentsListViewSpec extends AbstractSpringWebAppSpec {
  val assignmentService = mockService[ProjectAssignmentService]

  override def beforeEach() {
    super.beforeEach()
    reset(assignmentService)
  }

  "Current Assignments List View Panel" should {

    "render for existing project" in {
      val project = ProjectObjectMother.createProject(1)
      when(assignmentService.getProjectAssignmentsAndCheckDeletability(project)).thenReturn(toJava(List(ProjectAssignmentObjectMother.createProjectAssignment(1))))

      tester.startComponentInPage(new CurrentAssignmentsListView("id", new Model(new ProjectAdminBackingBean(project))))
      tester.assertNoErrorMessage()
      verify(assignmentService).getProjectAssignmentsAndCheckDeletability(project)
    }

    "render for new project" in {
      val project = ProjectObjectMother.createProject(1)
      project.setProjectId(null)

      tester.startComponentInPage(new CurrentAssignmentsListView("id", new Model(new ProjectAdminBackingBean(project))))
      tester.assertNoErrorMessage()

      verifyNoMoreInteractions(assignmentService)
    }
  }
}
