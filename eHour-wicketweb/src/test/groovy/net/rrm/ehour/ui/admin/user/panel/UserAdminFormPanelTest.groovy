package net.rrm.ehour.ui.admin.user.panel

import net.rrm.ehour.domain.UserDepartmentMother
import net.rrm.ehour.domain.UserObjectMother
import net.rrm.ehour.domain.UserRole
import net.rrm.ehour.ui.admin.user.dto.UserBackingBean
import net.rrm.ehour.ui.common.AbstractSpringWebAppTester
import net.rrm.ehour.ui.common.AdminAction
import net.rrm.ehour.user.service.UserService
import org.apache.wicket.markup.html.form.Form
import org.apache.wicket.model.CompoundPropertyModel
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mock
import org.mockito.MockitoAnnotations

import static org.junit.Assert.assertEquals
import static org.mockito.Matchers.anyObject
import static org.mockito.Mockito.verify

public class UserAdminFormPanelTest extends AbstractSpringWebAppTester {
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
        startPanel()

        tester.assertNoErrorMessage()
        tester.debugComponentTrees()
        tester.assertComponent(formPath, Form.class)
    }

    @SuppressWarnings("GroovyAssignabilityCheck")
    @Test
    void "should submit"() {
        startPanel()
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

        verify(userService).newUser(anyObject(), captor.capture())

        assertEquals("abc", captor.getValue())
    }


    void startPanel() {
        tester.startComponentInPage(new UserAdminFormPanel("panel",
                        new CompoundPropertyModel<UserBackingBean>(new UserBackingBean(UserObjectMother.createUser(), AdminAction.NEW)),
                        Arrays.asList(UserRole.ADMIN),
                        Arrays.asList(UserDepartmentMother.createUserDepartment())))
    }
}
