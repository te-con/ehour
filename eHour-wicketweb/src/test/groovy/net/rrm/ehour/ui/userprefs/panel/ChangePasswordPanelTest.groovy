package net.rrm.ehour.ui.userprefs.panel

import net.rrm.ehour.ui.common.BaseSpringWebAppTester
import net.rrm.ehour.user.service.UserService
import org.apache.wicket.markup.html.form.Form
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.springframework.security.authentication.BadCredentialsException

import static org.mockito.Mockito.doThrow
import static org.mockito.Mockito.verify

class ChangePasswordPanelTest extends BaseSpringWebAppTester {
    @Mock
    private UserService userService

    private static FORM_PATH = "id:border:border_body:changePasswordForm"


    @Before
    void "set up"() {
        MockitoAnnotations.initMocks this
        getMockContext().putBean("userService", userService);
    }

    @Test
    void "should render"() {
        startPanel()

        tester.assertNoErrorMessage()
        tester.assertComponent(FORM_PATH, Form.class)
    }

    @Test
    void "should halt with invalid credentials"() {
        doThrow(new BadCredentialsException("Failed")).when(userService).changePassword("thies", "b", "a")

        startPanel()

        def formTester = tester.newFormTester(FORM_PATH)

        formTester.setValue("password", "a")
        formTester.setValue("confirmPassword", "a")
        formTester.setValue("currentPassword", "b")

        tester.executeAjaxEvent(FORM_PATH + ":submitButton", "onclick")

        tester.assertErrorMessages(["currentPassword.user.invalidCurrentPassword"] as String[])
        tester.assertComponent(FORM_PATH, Form.class)
    }

    @Test
    void "should change password"() {
        startPanel()

        def formTester = tester.newFormTester(FORM_PATH)

        formTester.setValue("password", "a")
        formTester.setValue("confirmPassword", "a")
        formTester.setValue("currentPassword", "b")

        tester.executeAjaxEvent(FORM_PATH + ":submitButton", "onclick")

        tester.assertNoErrorMessage()
        tester.assertComponent(FORM_PATH, Form.class)

        verify(userService).changePassword("thies", "b", "a")
    }

    void startPanel() {
        tester.startComponentInPage(new ChangePasswordPanel("id", new ChangePasswordBackingBean()))
    }
}
