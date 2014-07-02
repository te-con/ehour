package net.rrm.ehour.ui.manage.config.panel;

import net.rrm.ehour.domain.AuditType;
import net.rrm.ehour.ui.manage.config.page.AbstractMainConfigTest;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.util.tester.FormTester;
import org.junit.Test;

import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertEquals;

public class AuditConfigPanelTest extends AbstractMainConfigTest {

    @Test
    public void shouldSubmit() {
        getConfigService().persistConfiguration(getConfigStub());

        replay(getConfigService());

        startPage();

        tester.assertComponent(FORM_PATH, Form.class);

        tester.clickLink("configTabs:tabs-container:tabs:4:link", true);

        FormTester formTester = tester.newFormTester(FORM_PATH);

        formTester.select("config.auditType", 3);

        tester.executeAjaxEvent(FORM_PATH + ":submitButton", "onclick");

        assertEquals(AuditType.ALL, getConfigStub().getAuditType());
    }
}
