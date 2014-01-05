package net.rrm.ehour.ui.admin.project.assign

import net.rrm.ehour.AbstractSpringWebAppSpec
import org.mockito.Mockito._
import net.rrm.ehour.domain.UserObjectMother
import net.rrm.ehour.util._
import net.rrm.ehour.user.service.UserService

class NewAssignmentUserListViewSpec extends AbstractSpringWebAppSpec {
  val userService = mockService[UserService]

  override def beforeEach() {
    super.beforeEach()
    reset(userService)
  }

  "New Assignment User List View Panel" should {

    "render" in {
      when(userService.getActiveUsers).thenReturn(toJava(List(UserObjectMother.createUser())))

      tester.startComponentInPage(new NewAssignmentUserListView("id"))
      tester.assertNoErrorMessage()
    }
  }
}
