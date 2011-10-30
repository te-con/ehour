package net.rrm.ehour.ui.admin.user.panel

import net.rrm.ehour.domain.UserDepartmentMother
import net.rrm.ehour.domain.UserMother
import net.rrm.ehour.domain.UserRole
import net.rrm.ehour.ui.admin.user.dto.UserBackingBean
import net.rrm.ehour.ui.common.AbstractSpringWebAppTester
import net.rrm.ehour.user.service.UserService
import org.apache.wicket.markup.html.form.Form
import org.apache.wicket.markup.html.panel.Panel
import org.apache.wicket.model.CompoundPropertyModel
import org.apache.wicket.util.tester.ITestPanelSource
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

public class UserAdminFormPanelTest extends AbstractSpringWebAppTester {
    @Mock
    private UserService userService
    def formPath =  makePanelPath(UserAdminFormPanel.BORDER, UserAdminFormPanel.FORM)

    @Before
    void "set up"() {
        MockitoAnnotations.initMocks this
        getMockContext().putBean("userService", userService);
    }

    @Test
    void "should render"() {
        startPanel()

        tester.assertNoErrorMessage()
        tester.assertComponent(formPath, Form.class)
    }

    @Test
    void "should submit"() {
        startPanel()
        def formTester = tester.newFormTester(formPath)

        formTester.setValue("user.username", "john")
        formTester.setValue("user.firstName", "john")
        formTester.setValue("user.lastName", "john")
        formTester.select("user.userDepartment", 0)
        formTester.select("user.userRoles", 0)

        tester.executeAjaxEvent(formPath +":submitButton", "onclick")

        tester.assertNoErrorMessage()
        tester.assertComponent(formPath, Form.class)
    }


    void startPanel() {
        tester.startPanel(new ITestPanelSource() {
            @Override
            Panel getTestPanel(String panelId) {
                return new UserAdminFormPanel(panelId, new CompoundPropertyModel<UserBackingBean>(new UserBackingBean(UserMother.createUser())), Arrays.asList(UserRole.ADMIN), Arrays.asList(UserDepartmentMother.createUserDepartment()))



            }
        })
    }
}
