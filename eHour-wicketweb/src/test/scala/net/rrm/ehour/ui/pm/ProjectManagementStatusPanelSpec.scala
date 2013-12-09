package net.rrm.ehour.ui.pm

import net.rrm.ehour.AbstractSpringWebAppSpec
import net.rrm.ehour.domain.ProjectObjectMother

class ProjectManagementStatusPanelSpec extends AbstractSpringWebAppSpec {
  "Project Management Status Panel" should {
    "render" in {
      tester.startComponentInPage(new ProjectManagementStatusPanel("id", ProjectObjectMother.createProject(1)))
      tester.assertNoErrorMessage()
    }
  }
}
