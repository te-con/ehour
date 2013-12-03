package net.rrm.ehour.ui.pm

import net.rrm.ehour.AbstractSpringWebAppSpec
import net.rrm.ehour.project.service.ProjectService

class ProjectManagementPageSpec extends AbstractSpringWebAppSpec {
  "Project Management page" should {
    val service = mock[ProjectService]
    springTester.getMockContext.putBean(service)

    "render" in {
      tester.startPage(classOf[ProjectManagementPage])
      tester.assertNoErrorMessage()
    }
  }
}
