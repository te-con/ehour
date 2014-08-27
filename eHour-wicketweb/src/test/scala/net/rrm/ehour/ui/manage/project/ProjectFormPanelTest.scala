package net.rrm.ehour.ui.manage.project

import net.rrm.ehour.activity.service.ActivityService
import net.rrm.ehour.customer.service.CustomerService
import net.rrm.ehour.domain.ProjectObjectMother
import net.rrm.ehour.project.service.ProjectService
import net.rrm.ehour.ui.common.BaseSpringWebAppTester
import net.rrm.ehour.user.service.UserService
import org.apache.wicket.model.CompoundPropertyModel
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.mock.MockitoSugar
import org.scalatest.{BeforeAndAfter, FunSuite, Matchers}

// should be rewritten to Spec style
@RunWith(classOf[JUnitRunner])
class ProjectFormPanelTest extends FunSuite with Matchers with BeforeAndAfter with MockitoSugar {

  val springTester = new BaseSpringWebAppTester()

  var projectService: ProjectService = _
  var userService: UserService = _
  var customerService: CustomerService = _
  var activityService: ActivityService = _

  before {
    springTester.setUp()

    projectService = mock[ProjectService]
    springTester.getMockContext.putBean("projectService", projectService)

    userService = mock[UserService]
    springTester.getMockContext.putBean("userService", userService)

    customerService = mock[CustomerService]
    springTester.getMockContext.putBean("customerService", customerService)

    activityService = mock[ActivityService]
    springTester.getMockContext.putBean(activityService)
  }

  after {
    springTester.clearMockContext()
  }

  test("should render projectFormPanel") {
//    when(activityService.getProjectAssignmentsAndCheckDeletability(any(classOf[Project]))).thenReturn(Lists.newArrayList[ProjectAssignment]())

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
