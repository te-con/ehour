package net.rrm.ehour.ui.manage.user

import com.google.common.collect.Lists
import net.rrm.ehour.domain.User
import net.rrm.ehour.domain.UserObjectMother
import net.rrm.ehour.domain.UserRole
import net.rrm.ehour.ui.common.BaseSpringWebAppTester
import net.rrm.ehour.user.service.LdapUser
import net.rrm.ehour.user.service.UserService
import org.apache.wicket.markup.html.form.Form
import org.apache.wicket.model.CompoundPropertyModel
import org.junit.Test

import static org.easymock.EasyMock.*

public class ManageUserFormPanelTest extends BaseSpringWebAppTester {
    public static final User user = UserObjectMother.createUser()
    def formPath = "panel:border:greySquaredFrame:border_body:userForm"


    @Override
    protected void afterSetup() {

    }

    @Test
    void "should render"() {
        def bean = getMockContext().getBean(UserService.class)
        expect(bean.getUserRoles()).andReturn(Lists.newArrayList(UserRole.ADMIN, UserRole.MANAGER, UserRole.PROJECTMANAGER, UserRole.REPORT, UserRole.USER))
        replay(bean)

        super.startTester()

        startPanel()

        tester.assertNoErrorMessage()
        tester.assertComponent(formPath, Form.class)
    }

    @Test
    void "should update new user"() {
        def userService = getMockContext().getBean(UserService.class)
        expect(userService.getUserRoles()).andReturn(Lists.newArrayList(UserRole.ADMIN, UserRole.MANAGER, UserRole.PROJECTMANAGER, UserRole.REPORT, UserRole.USER))

        userService.editUser(user)

        replay(userService)

        super.startTester()

        startPanel()

        def formTester = tester.newFormTester(formPath)

        formTester.select("user.user.userRoles", 0)

        tester.executeAjaxEvent(formPath + ":submitButton", "onclick")

        tester.assertNoErrorMessage()
        tester.assertComponent(formPath, Form.class)

        verify(userService)
    }


    void startPanel() {
        def ldapUser = new LdapUser("thies", "thies", "thies@rrm.net", "o=ptc")
        ldapUser.setUser(user)
        tester.startComponentInPage(new ManageUserFormPanel("panel",
                new CompoundPropertyModel<LdapUserBackingBean>(new LdapUserBackingBean(ldapUser))))
    }
}
