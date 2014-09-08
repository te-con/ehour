package net.rrm.ehour.ui.manage.user

import net.rrm.ehour.AbstractSpringWebAppSpec
import net.rrm.ehour.domain.UserObjectMother
import org.apache.wicket.model.CompoundPropertyModel

class UserManageReadOnlyPanelSpec extends AbstractSpringWebAppSpec {
  "Manage User Read Only Panel" should {

    "render" in {
      val user = UserObjectMother.createUser()
      val bean = new UserManageBackingBean(user)
      val model = new CompoundPropertyModel(bean)

      tester.startComponentInPage(new UserManageReadOnlyPanel("id", model))
      tester.assertNoErrorMessage()
    }
  }
}
