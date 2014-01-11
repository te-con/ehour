package net.rrm.ehour.ui.admin.project.assign

import net.rrm.ehour.AbstractSpringWebAppSpec
import org.mockito.Mockito._
import net.rrm.ehour.domain.{User, UserObjectMother}
import net.rrm.ehour.util._
import net.rrm.ehour.user.service.UserService

class NewAssignmentUserListViewSpec extends AbstractSpringWebAppSpec {
  val userService = mockService[UserService]
  val user = UserObjectMother.createUser()

  override def beforeEach() {
    super.beforeEach()
    reset(userService)

    when(userService.getActiveUsers).thenReturn(toJava(List(user)))
  }

  "New Assignment User List View Panel" should {

    "render" in {
      tester.startComponentInPage(new NewAssignmentUserListView("id"))
      tester.assertNoErrorMessage()
    }

    def clickFirstUser() { tester.executeAjaxEvent("id:allBorder:allBorder_body:users:0", "click") }
    def deselectFirstAffectedUser() { tester.executeAjaxEvent("id:affectedContainer:affectedUsers:0", "click") }

    def firstSelectedUser(subject: NewAssignmentUserListView): User = subject.selectedAffectedUsers.getObject.get(0)

    "select user" in {
      val subject = new NewAssignmentUserListView("id")
      tester.startComponentInPage(subject)
      clickFirstUser()

      firstSelectedUser(subject) should equal(user)

      val affectedUserItemModel = tester.getComponentFromLastRenderedPage("id:affectedContainer:affectedUsers:0")
      affectedUserItemModel.getDefaultModelObject should equal(user)

      tester.assertNoErrorMessage()
    }

    "deselect a previously selected user through the list of available users" in {
      val subject = new NewAssignmentUserListView("id")
      tester.startComponentInPage(subject)
      clickFirstUser()
      clickFirstUser()

      subject.selectedAffectedUsers.getObject should be ('empty)

      tester.assertNoErrorMessage()
    }

    "deselect a previously selected user through the list of affected users" in {
      val subject = new NewAssignmentUserListView("id")
      tester.startComponentInPage(subject)
      clickFirstUser()
      deselectFirstAffectedUser()

      subject.selectedAffectedUsers.getObject should be ('empty)

      tester.assertNoErrorMessage()
    }

  }
}
