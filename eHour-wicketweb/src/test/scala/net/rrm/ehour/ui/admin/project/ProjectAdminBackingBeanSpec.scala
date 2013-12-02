package net.rrm.ehour.ui.admin.project

import org.scalatest.mock.MockitoSugar
import org.scalatest.{Matchers, BeforeAndAfterEach, WordSpec}
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith
import net.rrm.ehour.domain._

@RunWith(classOf[JUnitRunner])
class ProjectAdminBackingBeanSpec extends WordSpec with Matchers with MockitoSugar with BeforeAndAfterEach {
  "Project Admin Backing Bean" should {
    "remove duplicate assignments on id when they have 1" in {
      val project = ProjectObjectMother.createProject(1)
      val bean = new ProjectAdminBackingBean(project)
      val projectAssignmentA = ProjectAssignmentObjectMother.createProjectAssignment(1)
      val projectAssignmentB = ProjectAssignmentObjectMother.createProjectAssignment(1)
      bean.addAssignmentToQueue(projectAssignmentA)
      bean.addAssignmentToQueue(projectAssignmentB)

      bean.getAssignmentsQueue should have size 1
    }

    "remove duplicate assignments on user and project when they don't have an id" in {
      val project = ProjectObjectMother.createProject(1)
      val bean = new ProjectAdminBackingBean(project)
      val user = UserObjectMother.createUser
      val assignmentA: ProjectAssignment = new ProjectAssignment(user, project)
      val assignmentB: ProjectAssignment = new ProjectAssignment(user, project)
      bean.addAssignmentToQueue(assignmentA)
      bean.addAssignmentToQueue(assignmentB)

      bean.getAssignmentsQueue should have size 1
    }

    "delete new assignment from queue" in {
      val project = ProjectObjectMother.createProject(1)
      val bean = new ProjectAdminBackingBean(project)
      val user = UserObjectMother.createUser
      val assignmentA = new ProjectAssignment(user, project)

      bean.addAssignmentToQueue(assignmentA)
      bean.deleteAssignment(assignmentA)

      bean.getAssignmentsQueue should be ('empty)
    }

    "add new assignment after it was deleted" in {
      val project = ProjectObjectMother.createProject(1)
      val bean = new ProjectAdminBackingBean(project)
      val user = UserObjectMother.createUser
      val assignmentA = new ProjectAssignment(user, project)

      bean.addAssignmentToQueue(assignmentA)
      bean.deleteAssignment(assignmentA)

      val assignmentB = new ProjectAssignment(user, project)
      bean.addAssignmentToQueue(assignmentB)

      bean.getAssignmentsQueue should have size 1
    }

    "delete original assignment after merging" in {
      val project = ProjectObjectMother.createProject(1)
      val bean = new ProjectAdminBackingBean(project)

      val assignmentA = ProjectAssignmentObjectMother.createProjectAssignment(1)
      bean.deleteAssignment(assignmentA)

      val assignmentB = ProjectAssignmentObjectMother.createProjectAssignment(2)

      val all = bean.mergeOriginalAssignmentsWithQueue(List(assignmentA, assignmentB))
      all should have size 1
      all(0) should be (assignmentB)
    }
  }
}
