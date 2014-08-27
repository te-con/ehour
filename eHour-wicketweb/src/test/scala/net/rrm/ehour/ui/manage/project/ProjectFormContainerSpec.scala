package net.rrm.ehour.ui.manage.project

import net.rrm.ehour.AbstractSpringWebAppSpec
import net.rrm.ehour.activity.service.ActivityService
import net.rrm.ehour.customer.service.CustomerService
import net.rrm.ehour.domain.ProjectObjectMother
import net.rrm.ehour.project.service.ProjectService
import net.rrm.ehour.user.service.UserService
import org.apache.wicket.model.Model

class ProjectFormContainerSpec extends AbstractSpringWebAppSpec {
  "Project Form Container" should {
    mockService[ActivityService]
    mockService[UserService]
    mockService[CustomerService]
    mockService[ProjectService]

    "render" in {
      val project = ProjectObjectMother.createProject(1)

      tester.startComponentInPage(new ProjectFormContainer("id", new Model(new ProjectAdminBackingBean(project))))
      tester.assertNoErrorMessage()
    }
  }
}
