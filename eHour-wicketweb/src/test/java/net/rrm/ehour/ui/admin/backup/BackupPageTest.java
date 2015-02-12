package net.rrm.ehour.ui.admin.backup;

import net.rrm.ehour.backup.domain.ParseSession;
import net.rrm.ehour.backup.service.backup.DatabaseBackupService;
import net.rrm.ehour.backup.service.restore.RestoreService;
import net.rrm.ehour.ui.common.BaseSpringWebAppTester;
import org.apache.wicket.Page;
import org.apache.wicket.extensions.ajax.markup.html.AjaxLazyLoadPanel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.protocol.http.mock.MockHttpServletRequest;
import org.apache.wicket.util.file.File;
import org.apache.wicket.util.tester.FormTester;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 *         Created on: Nov 12, 2010 - 11:42:00 PM
 */
public class BackupPageTest extends BaseSpringWebAppTester {
    @Before
    public void initMock() {
        MockitoAnnotations.initMocks(this);
        getMockContext().putBean("databaseBackupService", exportService);
        getMockContext().putBean("restoreService", importService);
    }

    @Test
    public void shouldRenderPage() {
        startPage();
        tester.assertRenderedPage(BackupDbPage.class);
        tester.assertNoErrorMessage();
    }

    private Page startPage() {
        BackupDbPage page = tester.startPage(BackupDbPage.class);

        MockHttpServletRequest request = tester.getRequest();
        request.setUseMultiPartContentType(true);

        return page;
    }

    @Test
    public void shouldClickBackupLink() {
        when(exportService.exportDatabase()).thenReturn("this should be xml".getBytes());

        startPage();

        tester.clickLink("frame:frame_body:backupBorder:backupBorder_body:backupLink");
        tester.assertNoErrorMessage();
    }

    @Test
    public void shouldUploadXML() {
        startPage();

        FormTester formTester = tester.newFormTester("frame:frame_body:restoreBorder:restoreBorder_body:form");

        when(importService.prepareImportDatabase(any(String.class))).thenReturn(new ParseSession());

        formTester.setFile("file", new File("src/test/resources/import_ok.xml"), "text/xml");
        tester.executeAjaxEvent("frame:frame_body:restoreBorder:restoreBorder_body:form:ajaxSubmit", "onclick");
        tester.assertComponent("frame:frame_body:restoreBorder:restoreBorder_body:form:parseStatus", AjaxLazyLoadPanel.class);
    }

    @Test
    public void shouldFailForWrongContentType() {
        startPage();

        FormTester formTester = tester.newFormTester("frame:frame_body:restoreBorder:restoreBorder_body:form");
        formTester.setFile("file", new File("src/test/resources/import_ok.xml"), "application/zip");

        tester.executeAjaxEvent("frame:frame_body:restoreBorder:restoreBorder_body:form:ajaxSubmit", "onclick");
        tester.assertComponent("frame:frame_body:restoreBorder:restoreBorder_body:form:parseStatus", Label.class);

        Mockito.verifyZeroInteractions(importService);
    }

    @Test
    public void shouldFailForNoFile() {
        startPage();

        tester.executeAjaxEvent("frame:frame_body:restoreBorder:restoreBorder_body:form:ajaxSubmit", "onclick");
        tester.assertComponent("frame:frame_body:restoreBorder:restoreBorder_body:form:parseStatus", Label.class);

        Mockito.verifyZeroInteractions(importService);
    }

    @Mock
    private DatabaseBackupService exportService;
    @Mock
    private RestoreService importService;
}
