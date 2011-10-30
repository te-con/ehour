package net.rrm.ehour.ui.userprefs.panel

import net.rrm.ehour.ui.common.AbstractSpringWebAppTester
import net.rrm.ehour.user.service.UserService
import org.apache.wicket.markup.html.form.Form
import org.apache.wicket.markup.html.panel.Panel
import org.apache.wicket.util.tester.ITestPanelSource
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class ChangePasswordPanelTest extends AbstractSpringWebAppTester {
      @Mock
  private UserService userService

    @Test
    void "should render"() {
        MockitoAnnotations.initMocks this
        getMockContext().putBean("userService", userService);


        startPanel()

        tester.assertNoErrorMessage()
        def path = makePanelPath(ChangePasswordPanel.BORDER, ChangePasswordPanel.CHANGE_PASSWORD_FORM)
        tester.assertComponent(path, Form.class)

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
