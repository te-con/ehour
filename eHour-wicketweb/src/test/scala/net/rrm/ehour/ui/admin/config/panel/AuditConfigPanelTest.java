package net.rrm.ehour.ui.admin.config.panel;

import net.rrm.ehour.domain.AuditType;
import net.rrm.ehour.domain.UserRole;
import net.rrm.ehour.ui.admin.config.AbstractMainConfigTest;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.util.tester.FormTester;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

public class AuditConfigPanelTest extends AbstractMainConfigTest {

    @Test
    public void shouldSubmit() {
        startPage();

        tester.assertComponent(AbstractMainConfigTest.FORM_PATH, Form.class);

        tester.clickLink("configTabs:tabs-container:tabs:" + ConfigTab.AUDIT.getTabIndex() + ":link", true);

        FormTester formTester = tester.newFormTester(AbstractMainConfigTest.FORM_PATH);

        formTester.select("config.auditType", ConfigTab.AUDIT.getTabIndex());

        tester.executeAjaxEvent(AbstractMainConfigTest.FORM_PATH + ":submitButton", "onclick");

        assertEquals(AuditType.ALL, config.getAuditType());

        verify(iPersistConfiguration).persistAndCleanUp(config, UserRole.ADMIN);
    }
}
