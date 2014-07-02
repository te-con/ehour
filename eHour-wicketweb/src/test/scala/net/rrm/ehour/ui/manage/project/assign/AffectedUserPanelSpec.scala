package net.rrm.ehour.ui.manage.project.assign

import net.rrm.ehour.AbstractSpringWebAppSpec
import net.rrm.ehour.domain.UserObjectMother

class AffectedUserPanelSpec extends AbstractSpringWebAppSpec {
  "Affected User Panel" should {

    "render" in {
      tester.startComponentInPage(new AffectedUserPanel("id", UserObjectMother.createUser()))
      tester.assertNoErrorMessage()
    }
  }
}
