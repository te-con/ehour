package net.rrm.ehour.ui.admin.project

import net.rrm.ehour.ui.common.BaseSpringWebAppTester
import org.scalatest.{Matchers, BeforeAndAfter, FunSuite}
import org.apache.wicket.model.CompoundPropertyModel
import net.rrm.ehour.domain.{ProjectAssignment, Project, ProjectObjectMother}
import org.easymock.EasyMock._
import net.rrm.ehour.project.service.{ProjectAssignmentService, ProjectService}
import net.rrm.ehour.user.service.UserService
import net.rrm.ehour.customer.service.CustomerService
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.mock.MockitoSugar
import org.mockito.Matchers._
import org.mockito.Mockito._
import com.google.common.collect.Lists

// should be rewritten to Spec style
@RunWith(classOf[JUnitRunner])
class ProjectFormPanelTest extends FunSuite with Matchers with BeforeAndAfter with MockitoSugar {

  val springTester = new BaseSpringWebAppTester()

  var projectService: ProjectService = _
  var userService: UserService = _
  var customerService: CustomerService = _
  var assignmentService: ProjectAssignmentService = _

  before {
    springTester.setUp()

    projectService = mock[ProjectService]
    springTester.getMockContext.putBean("projectService", projectService)

    userService = mock[UserService]
    springTester.getMockContext.putBean("userService", userService)

    customerService = mock[CustomerService]
    springTester.getMockContext.putBean("customerService", customerService)

    assignmentService = mock[ProjectAssignmentService]
    springTester.getMockContext.putBean(assignmentService)
  }

  after {
    springTester.clearMockContext()
  }

  test("should render projectFormPanel") {
    when(assignmentService.getProjectAssignments(any(classOf[Project]))).thenReturn(Lists.newArrayList[ProjectAssignment]())

    startPanel(createModel())

    assertOkay()
  }

  def assertOkay() {
    springTester.tester.assertNoInfoMessage()
    springTester.tester.assertNoErrorMessage()
  }

  def createModel() = {
    val project = ProjectObjectMother.createProject(1)
    val backingBean = new ProjectAdminBackingBean(project)
    new CompoundPropertyModel[ProjectAdminBackingBean](backingBean)
  }

  def startPanel(model: CompoundPropertyModel[ProjectAdminBackingBean]) {
    springTester.tester.startComponentInPage(new ProjectFormPanel("id", model))
  }
}
