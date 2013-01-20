package net.rrm.ehour.ui.admin.project.panel

import net.rrm.ehour.ui.common.AbstractSpringWebAppTester
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{BeforeAndAfter, FunSuite}
import org.apache.wicket.model.CompoundPropertyModel
import net.rrm.ehour.ui.admin.project.dto.ProjectAdminBackingBean
import net.rrm.ehour.domain.ProjectObjectMother
import org.easymock.EasyMock._
import net.rrm.ehour.project.service.ProjectService
import net.rrm.ehour.user.service.UserService
import net.rrm.ehour.customer.service.CustomerService
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ProjectFormPanelTest extends AbstractSpringWebAppTester with FunSuite with ShouldMatchers with BeforeAndAfter {
  var projectService: ProjectService = _
  var userService: UserService = _
  var customerService: CustomerService = _


  before {
    super.setUp()

    projectService = createMock(classOf[ProjectService])
    getMockContext.putBean("projectService", projectService)

    userService = createMock(classOf[UserService])
    getMockContext.putBean("userService", userService)

    customerService = createMock(classOf[CustomerService])
    getMockContext.putBean("customerService", customerService)
  }

  after {
    super.clearMockContext()
  }

  test("should render projectFormPanel") {
    startPanel(createModel())

    assertOkay()
  }

  def assertOkay() {
    tester.assertNoInfoMessage()
    tester.assertNoErrorMessage()
  }

  def createModel() = {
    val project = ProjectObjectMother.createProject(1)
    val backingBean = new ProjectAdminBackingBean(project)
    new CompoundPropertyModel[ProjectAdminBackingBean](backingBean)
  }

  def startPanel(model: CompoundPropertyModel[ProjectAdminBackingBean]) {
    tester.startComponentInPage(new ProjectFormPanel("id", model))
  }
}
