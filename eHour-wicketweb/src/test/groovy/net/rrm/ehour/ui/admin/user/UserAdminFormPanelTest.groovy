package net.rrm.ehour.ui.admin.user

import net.rrm.ehour.domain.User
import net.rrm.ehour.domain.UserDepartmentObjectMother
import net.rrm.ehour.domain.UserObjectMother
import net.rrm.ehour.domain.UserRole
import net.rrm.ehour.ui.common.BaseSpringWebAppTester
import net.rrm.ehour.user.service.UserService
import org.apache.wicket.markup.html.form.Form
import org.apache.wicket.model.CompoundPropertyModel
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mock
import org.mockito.MockitoAnnotations

import static org.junit.Assert.*
import static org.mockito.Matchers.anyObject
import static org.mockito.Mockito.verify

public class UserAdminFormPanelTest extends BaseSpringWebAppTester {
    @Mock
    private UserService userService
    def formPath =  "panel:border:greySquaredFrame:border_body:userForm"

    @Before
    void "set up"() {
        MockitoAnnotations.initMocks this
        getMockContext().putBean("userService", userService);
    }

    @Test
    void "should render"() {
        startPanel(new UserAdminBackingBean())

        tester.assertNoErrorMessage()
        tester.assertComponent(formPath, Form.class)
    }

    @Test
    void "should create new user"() {
        startPanel(new UserAdminBackingBean())

        assertFalse(tester.isVisible(formPath + ":showAssignments").wasFailed())

        def formTester = tester.newFormTester(formPath)

        formTester.setValue("user.username", "john")
        formTester.setValue("user.firstName", "john")
        formTester.setValue("user.lastName", "john")
        formTester.select("user.userDepartment", 0)
        formTester.select("user.userRoles", 0)
        formTester.setValue("password", "abc")
        formTester.setValue("confirmPassword", "abc")

        tester.executeAjaxEvent(formPath +":submitButton", "onclick")

        tester.assertNoErrorMessage()
        tester.assertComponent(formPath, Form.class)

        def captor = ArgumentCaptor.forClass(String.class);

        verify(userService).newUser(anyObject() as User, captor.capture())

        assertEquals("abc", captor.getValue())
    }

    @Test
    void "should edit user and not have the assignments checkbox"() {
        startPanel(new UserAdminBackingBean(UserObjectMother.createUser()))

        assertTrue(tester.isVisible(formPath + ":showAssignments").wasFailed())

        def formTester = tester.newFormTester(formPath)

        formTester.setValue("user.username", "john")
        formTester.setValue("user.firstName", "john")
        formTester.setValue("user.lastName", "john")
        formTester.select("user.userDepartment", 0)
        formTester.select("user.userRoles", 0)
        formTester.setValue("password", "abc")
        formTester.setValue("confirmPassword", "abc")

        tester.executeAjaxEvent(formPath +":submitButton", "onclick")

        tester.assertNoErrorMessage()
        tester.assertComponent(formPath, Form.class)

        verify(userService).editUser(anyObject() as User)
    }


    void startPanel(UserAdminBackingBean bean) {
        tester.startComponentInPage(new UserAdminFormPanel("panel",
                        new CompoundPropertyModel<UserAdminBackingBean>(bean),
                        Arrays.asList(UserRole.ADMIN),
                        Arrays.asList(UserDepartmentObjectMother.createUserDepartment())))
    }
}
