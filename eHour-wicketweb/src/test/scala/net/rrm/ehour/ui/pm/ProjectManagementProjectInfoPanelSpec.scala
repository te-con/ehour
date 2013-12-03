package net.rrm.ehour.ui.pm

import net.rrm.ehour.AbstractSpringWebAppSpec
import org.scalatest.BeforeAndAfterAll
import net.rrm.ehour.domain.ProjectObjectMother
import net.rrm.ehour.project.service.ProjectAssignmentService
import org.mockito.Mockito._
import net.rrm.ehour.user.service.UserService

class ProjectManagementProjectInfoPanelSpec extends AbstractSpringWebAppSpec with BeforeAndAfterAll {
  val assignmentService = mock[ProjectAssignmentService]
  val userService = mock[UserService]

  override def beforeAll() {
    springTester.getMockContext.putBean(assignmentService)
    springTester.getMockContext.putBean(userService)
    springTester.setUp()
  }

  override def beforeEach() {
    reset(assignmentService, userService)
  }

  "Project Management Project Info Panel" should {
    "render" in {
        tester.startComponentInPage(new ProjectManagementProjectInfoPanel("id", ProjectObjectMother.createProject(1)))
        tester.assertNoErrorMessage()
    }
  }
}
