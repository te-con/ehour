package net.rrm.ehour.ui.pm

import net.rrm.ehour.AbstractSpringWebAppSpec
import net.rrm.ehour.project.service.{ProjectAssignmentService, ProjectAssignmentManagementService, ProjectService}
import org.mockito.Mockito._
import org.mockito.Matchers._
import net.rrm.ehour.domain.{ProjectObjectMother, User}
import scala.collection.convert.WrapAsJava
import scala.collection.mutable.ListBuffer
import net.rrm.ehour.user.service.UserService

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


    "render" in {
      tester.startPage(classOf[ProjectManagementPage])
      tester.assertNoErrorMessage()
    }

    "load first entry" in {
      when(projectService.getProjectManagerProjects(any(classOf[User]))).thenReturn(WrapAsJava.bufferAsJavaList(ListBuffer(ProjectObjectMother.createProject(1))))

      tester.startPage(classOf[ProjectManagementPage])

      tester.debugComponentTrees()
      tester.executeAjaxEvent("entrySelectorFrame:entrySelectorFrame_body:projectSelector:entrySelectorFrame:blueBorder:blueBorder_body:itemListHolder:itemList:0", "click")

      tester.assertNoErrorMessage()

    }
  }
}
