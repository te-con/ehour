package net.rrm.ehour.ui.userprefs.panel;

import net.rrm.ehour.exception.ObjectNotFoundException;
import net.rrm.ehour.ui.common.BaseSpringWebAppTester;
import net.rrm.ehour.user.service.UserService;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.util.tester.FormTester;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.BadCredentialsException;

import static org.mockito.Mockito.doThrow;

public class ChangePasswordPanelTest extends BaseSpringWebAppTester {
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        getMockContext().putBean("userService", userService);
    }

    @Test
    public void shouldRender() throws ObjectNotFoundException {
        startPanel();

        tester.assertNoErrorMessage();
        tester.assertComponent(FORM_PATH, Form.class);
    }

    @Test
    public void shouldHaltWithInvalidCredentials() throws ObjectNotFoundException {
        doThrow(new BadCredentialsException("Failed")).when(userService).changePassword("thies", "b", "a");

        startPanel();

        FormTester formTester = tester.newFormTester(FORM_PATH);

        formTester.setValue("password", "a");
        formTester.setValue("confirmPassword", "a");
        formTester.setValue("currentPassword", "b");

        tester.executeAjaxEvent(FORM_PATH + ":submitButton", "onclick");

        tester.assertErrorMessages("currentPassword.user.invalidCurrentPassword");
        tester.assertComponent(FORM_PATH, Form.class);
    }

    @Test
    public void shouldChangePassword() throws ObjectNotFoundException {
        startPanel();

        FormTester formTester = tester.newFormTester(FORM_PATH);

        formTester.setValue("password", "a");
        formTester.setValue("confirmPassword", "a");
        formTester.setValue("currentPassword", "b");

        tester.executeAjaxEvent(FORM_PATH + ":submitButton", "onclick");

        tester.assertNoErrorMessage();
        tester.assertComponent(FORM_PATH, Form.class);

        Mockito.verify(userService).changePassword("thies", "b", "a");
    }

    public void startPanel() throws ObjectNotFoundException {
        tester.startComponentInPage(new ChangePasswordPanel("id", new ChangePasswordBackingBean()));
    }

    @Mock
    private UserService userService;
    private static String FORM_PATH = "id:border:border_body:changePasswordForm";
}
