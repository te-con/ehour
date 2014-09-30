package net.rrm.ehour.ui.manage.user

import com.google.common.collect.Lists
import net.rrm.ehour.domain.User
import net.rrm.ehour.domain.UserRole
import net.rrm.ehour.ui.common.BaseSpringWebAppTester
import net.rrm.ehour.user.service.LdapUser
import net.rrm.ehour.user.service.UserService
import org.apache.wicket.markup.html.form.Form
import org.apache.wicket.model.CompoundPropertyModel
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

import static org.junit.Assert.assertFalse
import static org.mockito.Matchers.anyObject
import static org.mockito.Mockito.verify
import static org.mockito.Mockito.when

public class ManageUserFormPanelTest extends BaseSpringWebAppTester {
    @Mock
    private UserService userService
    def formPath =  "panel:border:greySquaredFrame:border_body:userForm"

    @Before
    void "set up"() {
        MockitoAnnotations.initMocks this
        when(userService.getUserRoles()).thenReturn(Lists.newArrayList(UserRole.ADMIN, UserRole.MANAGER, UserRole.PROJECTMANAGER, UserRole.REPORT, UserRole.USER))
    }

    @Override
    protected void afterSetup() {
        // don't start in tester
    }

    @Test
    void "should render"() {
        super.startTester()

        startPanel()

        tester.assertNoErrorMessage()
        tester.assertComponent(formPath, Form.class)
    }

    @Test
    void "should create new user"() {
        super.startTester()

        startPanel()

        assertFalse(tester.isVisible(formPath + ":showAssignments").wasFailed())

        def formTester = tester.newFormTester(formPath)

        formTester.setValue("user.username", "john")
        formTester.setValue("user.firstName", "john")
        formTester.setValue("user.lastName", "john")
        formTester.select("user.userDepartment", 0)
        formTester.select("user.userRoles", 0)
        formTester.setValue("password", "abc")
        formTester.setValue("confirmPassword", "abc")

        tester.executeAjaxEvent(formPath + ":submitButton", "onclick")

        tester.assertNoErrorMessage()
        tester.assertComponent(formPath, Form.class)

        verify(userService).editUser(anyObject() as User)
    }


    void startPanel() {
        tester.startComponentInPage(new ManageUserFormPanel("panel",
                new CompoundPropertyModel<LdapUserBackingBean>(new LdapUserBackingBean(new LdapUser("thies", "thies", "thies@rrm.net", "o=ptc")))))
    }
}
