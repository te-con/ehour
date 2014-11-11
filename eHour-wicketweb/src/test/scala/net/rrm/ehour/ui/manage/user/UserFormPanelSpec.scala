package net.rrm.ehour.ui.manage.user

import com.google.common.collect.Lists
import net.rrm.ehour.AbstractSpringWebAppSpec
import net.rrm.ehour.domain.{UserDepartmentObjectMother, UserObjectMother, UserRole}
import net.rrm.ehour.user.service.UserService
import org.apache.wicket.authroles.authorization.strategies.role.Roles
import org.apache.wicket.markup.html.form.{Form, ListMultipleChoice}
import org.apache.wicket.model.CompoundPropertyModel
import org.mockito.ArgumentCaptor
import org.mockito.Matchers._
import org.mockito.Mockito._

class UserFormPanelSpec extends AbstractSpringWebAppSpec {
  val userService = mockService[UserService]

  val FormPath =  "panel:border:greySquaredFrame:border_body:userForm"

  override def beforeEach() = {
    reset(userService)

    when(userService.getUserRoles).thenReturn(Lists.newArrayList(UserRole.ADMIN, UserRole.MANAGER, UserRole.PROJECTMANAGER, UserRole.REPORT, UserRole.USER))
    when(userService.getUserDepartments).thenReturn(Lists.newArrayList(UserDepartmentObjectMother.createUserDepartment()))
  }

  "user Manage Form Panel" should {
    "render" in {
      super.startTester()

      startPanel()

      tester.assertNoErrorMessage()
      tester.assertComponent(FormPath, classOf[Form[_]])
    }

    "create new user" in {
      super.startTester()

      startPanel()

      tester.isVisible(FormPath + ":showAssignments").wasFailed() should be (false)

      val formTester = tester.newFormTester(FormPath)
      formTester.setValue("user.username", "john")
      formTester.setValue("user.firstName", "john")
      formTester.setValue("user.lastName", "john")
      formTester.select("dept:user.userDepartment", 0)
      formTester.select("user.userRoles", 0)
      formTester.setValue("password", "abc")
      formTester.setValue("confirmPassword", "abc")

      tester.executeAjaxEvent(FormPath + ":submitButton", "onclick")

      tester.assertNoErrorMessage()
      tester.assertComponent(FormPath, classOf[Form[_]])

      val captor = ArgumentCaptor.forClass(classOf[String])

      verify(userService).persistNewUser(anyObject(), captor.capture())

      captor.getValue should be("abc")
    }

    "should edit user and not have the assignments checkbox" in {
      super.startTester()

      startPanel(new UserManageBackingBean(UserObjectMother.createUser()))

      tester.isVisible(FormPath + ":showAssignments").wasFailed() should be (true)

      val formTester = tester.newFormTester(FormPath)

      formTester.setValue("user.username", "john")
      formTester.setValue("user.firstName", "john")
      formTester.setValue("user.lastName", "john")
      formTester.select("dept:user.userDepartment", 0)
      formTester.select("user.userRoles", 0)
      formTester.setValue("password", "abc")
      formTester.setValue("confirmPassword", "abc")

      tester.executeAjaxEvent(FormPath +":submitButton", "onclick")

      tester.assertNoErrorMessage()

      verify(userService).persistEditedUser(anyObject())

    }

    "should not show admin role when adding/editing as a manager" in {
      springTester.getConfig.setSplitAdminRole(true)

      super.startTester(new Roles(UserRole.ROLE_MANAGER))

      startPanel(new UserManageBackingBean(UserObjectMother.createUser()))

      val select = tester.getComponentFromLastRenderedPage(FormPath + ":user.userRoles").asInstanceOf[ListMultipleChoice[_]]

      val choices = select.getChoices
      choices should contain(UserRole.MANAGER)
      choices should contain(UserRole.REPORT)
      choices should contain(UserRole.USER)

      choices.size() should be (3)

    }

    "should not show manager role when split is disabled" in {
      springTester.getConfig.setSplitAdminRole(false)

      super.startTester(new Roles(UserRole.ROLE_ADMIN))

      startPanel(new UserManageBackingBean(UserObjectMother.createUser()))

      val select = tester.getComponentFromLastRenderedPage(FormPath + ":user.userRoles").asInstanceOf[ListMultipleChoice[_]]

      val choices = select.getChoices
      choices should contain(UserRole.ADMIN)
      choices should contain(UserRole.REPORT)
      choices should contain(UserRole.USER)

      choices.size() should be (3)

    }

    "should show manager role when split is enabled" in {
      springTester.getConfig.setSplitAdminRole(true)

      super.startTester(new Roles(UserRole.ROLE_ADMIN))

      startPanel(new UserManageBackingBean(UserObjectMother.createUser()))

      val select = tester.getComponentFromLastRenderedPage(FormPath + ":user.userRoles").asInstanceOf[ListMultipleChoice[_]]

      val choices = select.getChoices
      choices should contain(UserRole.ADMIN)
      choices should contain(UserRole.MANAGER)
      choices should contain(UserRole.REPORT)
      choices should contain(UserRole.USER)

      choices.size() should be (4)
    }
  }

  def startPanel(bean: UserManageBackingBean = new UserManageBackingBean()) {
    tester.startComponentInPage(new UserFormPanel[UserManageBackingBean]("panel", new CompoundPropertyModel[UserManageBackingBean](bean)))
  }
}
