package net.rrm.ehour.ui.common.panel.multiselect

import com.google.common.collect.Lists
import net.rrm.ehour.AbstractSpringWebAppSpec
import net.rrm.ehour.domain.{User, UserObjectMother}
import net.rrm.ehour.user.service.UserService
import net.rrm.ehour.util._
import org.apache.wicket.model.util.ListModel
import org.mockito.Mockito._

class MultiUserSelectSpec extends AbstractSpringWebAppSpec {
  val userService = mockService[UserService]
  val user = UserObjectMother.createUser()

  override def beforeEach() {
    super.beforeEach()
    reset(userService)

    when(userService.getActiveUsers).thenReturn(toJava(List(user)))
  }

  "MultiUser Select" should {

    "render" in {
      tester.startComponentInPage(new MultiUserSelect("id"))
      tester.assertNoErrorMessage()
    }

    def clickFirstUser() { tester.executeAjaxEvent("id:allBorder:allBorder_body:users:0", "click") }
    def deselectFirstSelectedUser() { tester.executeAjaxEvent("id:selectedContainer:selectedUsers:0", "click") }

    def firstSelectedUser(subject: MultiUserSelect): User = subject.selectedUsers.getObject.get(0)

    "select user" in {
      val subject = new MultiUserSelect("id")
      tester.startComponentInPage(subject)
      clickFirstUser()

      firstSelectedUser(subject) should equal(user)

      val selectedUserItemModel = tester.getComponentFromLastRenderedPage("id:selectedContainer:selectedUsers:0")
      selectedUserItemModel.getDefaultModelObject should equal(user)

      tester.assertNoErrorMessage()
    }

    "deselect a previously selected user through the list of available users" in {
      val subject = new MultiUserSelect("id")
      tester.startComponentInPage(subject)
      clickFirstUser()
      clickFirstUser()

      subject.selectedUsers.getObject should be ('empty)

      tester.assertNoErrorMessage()
    }

    "deselect a previously selected user through the list of selected users" in {
      val subject = new MultiUserSelect("id")
      tester.startComponentInPage(subject)
      clickFirstUser()
      deselectFirstSelectedUser()

      subject.selectedUsers.getObject should be ('empty)

      tester.assertNoErrorMessage()
    }

    "deselect from a provided list" in {
      val users = Lists.newArrayList(UserObjectMother.createUser())
      val model = new ListModel(users)
      val subject = new MultiUserSelect("id", model)

      tester.startComponentInPage(subject)
      deselectFirstSelectedUser()

      users should be ('empty)

      tester.assertNoErrorMessage()

    }

  }
}
