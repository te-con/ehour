package net.rrm.ehour.ui.userprefs.panel

import net.rrm.ehour.ui.common.AbstractSpringWebAppTester
import net.rrm.ehour.user.service.UserService
import org.apache.wicket.markup.html.form.Form
import org.apache.wicket.markup.html.panel.Panel
import org.apache.wicket.util.tester.ITestPanelSource
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

import static org.mockito.Mockito.when
import org.springframework.security.authentication.BadCredentialsException

import static org.mockito.Mockito.verify
import static org.mockito.Mockito.doThrow

class ChangePasswordPanelTest extends AbstractSpringWebAppTester {
    @Mock
    private UserService userService

    @Before
    void "set up"() {
        MockitoAnnotations.initMocks this
        getMockContext().putBean("userService", userService);
    }

    @Test
    void "should render"() {
        startPanel()

        tester.assertNoErrorMessage()
        def path = makePanelPath(ChangePasswordPanel.BORDER, ChangePasswordPanel.CHANGE_PASSWORD_FORM)
        tester.assertComponent(path, Form.class)
    }

    @Test
    void "should halt with invalid credentials"() {
        doThrow(new BadCredentialsException("Failed")).when(userService).changePassword("thies", "b", "a")

        startPanel()

        def formPath = makePanelPath(ChangePasswordPanel.BORDER, ChangePasswordPanel.CHANGE_PASSWORD_FORM)

        def formTester = tester.newFormTester(formPath)

        formTester.setValue("password", "a")
        formTester.setValue("confirmPassword", "a")
        formTester.setValue("currentPassword", "b")

        tester.executeAjaxEvent(formPath + ":submitButton", "onclick")

        tester.assertErrorMessages(["currentPassword.user.invalidCurrentPassword"] as String[])
        tester.assertComponent(formPath, Form.class)
    }

    @Test
    void "should change password"() {
        startPanel()

        def formPath = makePanelPath(ChangePasswordPanel.BORDER, ChangePasswordPanel.CHANGE_PASSWORD_FORM)

        def formTester = tester.newFormTester(formPath)

        formTester.setValue("password", "a")
        formTester.setValue("confirmPassword", "a")
        formTester.setValue("currentPassword", "b")

        tester.executeAjaxEvent(formPath + ":submitButton", "onclick")

        tester.assertNoErrorMessage()
        tester.assertComponent(formPath, Form.class)

        verify(userService).changePassword("thies", "b", "a")
    }

    void startPanel() {
        tester.startPanel(new ITestPanelSource() {
            @Override
            Panel getTestPanel(String panelId) {
                return new ChangePasswordPanel(panelId, new ChangePasswordBackingBean())


            }
        })
    }
}
