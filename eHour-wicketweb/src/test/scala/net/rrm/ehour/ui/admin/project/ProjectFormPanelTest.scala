package net.rrm.ehour.ui.admin.project

import net.rrm.ehour.ui.common.BaseSpringWebAppTester
import org.scalatest.{Matchers, BeforeAndAfter, FunSuite}
import org.apache.wicket.model.CompoundPropertyModel
import net.rrm.ehour.domain.ProjectObjectMother
import org.easymock.EasyMock._
import net.rrm.ehour.project.service.ProjectService
import net.rrm.ehour.user.service.UserService
import net.rrm.ehour.customer.service.CustomerService
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ProjectFormPanelTest extends FunSuite with Matchers with BeforeAndAfter {

  val springTester = new BaseSpringWebAppTester()

  var projectService: ProjectService = _
  var userService: UserService = _
  var customerService: CustomerService = _

  before {
    springTester.setUp()

    projectService = createMock(classOf[ProjectService])
    springTester.getMockContext.putBean("projectService", projectService)

    userService = createMock(classOf[UserService])
    springTester.getMockContext.putBean("userService", userService)

    customerService = createMock(classOf[CustomerService])
    springTester.getMockContext.putBean("customerService", customerService)
  }

  after {
    springTester.clearMockContext()
  }

  test("should render projectFormPanel") {
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
