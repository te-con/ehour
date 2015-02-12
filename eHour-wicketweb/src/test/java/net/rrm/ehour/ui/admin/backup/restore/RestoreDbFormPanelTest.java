package net.rrm.ehour.ui.admin.backup.restore;

import net.rrm.ehour.backup.domain.ParseSession;
import net.rrm.ehour.backup.service.restore.RestoreService;
import net.rrm.ehour.ui.common.BaseSpringWebAppTester;
import org.apache.wicket.extensions.ajax.markup.html.AjaxLazyLoadPanel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.util.file.File;
import org.apache.wicket.util.tester.FormTester;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RestoreDbFormPanelTest extends BaseSpringWebAppTester {

    @Mock
    private RestoreService importService;


    private RestoreDbFormPanel startPanel() {
        return tester.startComponentInPage(RestoreDbFormPanel.class);
    }

    @Test
    public void shouldUploadXML() {
        startPanel();

        FormTester formTester = tester.newFormTester("frame:frame_body:restoreBorder:restoreBorder_body:form");

        when(importService.prepareImportDatabase(any(String.class))).thenReturn(new ParseSession());

        formTester.setFile("file", new File("src/test/resources/import_ok.xml"), "text/xml");
        tester.executeAjaxEvent("frame:frame_body:restoreBorder:restoreBorder_body:form:ajaxSubmit", "onclick");
        tester.assertComponent("frame:frame_body:restoreBorder:restoreBorder_body:form:parseStatus", AjaxLazyLoadPanel.class);
    }

    @Test
    public void shouldFailForWrongContentType() {
        startPanel();

        FormTester formTester = tester.newFormTester("frame:frame_body:restoreBorder:restoreBorder_body:form");
        formTester.setFile("file", new File("src/test/resources/import_ok.xml"), "application/zip");

        tester.executeAjaxEvent("frame:frame_body:restoreBorder:restoreBorder_body:form:ajaxSubmit", "onclick");
        tester.assertComponent("frame:frame_body:restoreBorder:restoreBorder_body:form:parseStatus", Label.class);

        Mockito.verifyZeroInteractions(importService);
    }

    @Test
    public void shouldFailForNoFile() {
        startPanel();

        tester.executeAjaxEvent("frame:frame_body:restoreBorder:restoreBorder_body:form:ajaxSubmit", "onclick");
        tester.assertComponent("frame:frame_body:restoreBorder:restoreBorder_body:form:parseStatus", Label.class);

        Mockito.verifyZeroInteractions(importService);
    }
}