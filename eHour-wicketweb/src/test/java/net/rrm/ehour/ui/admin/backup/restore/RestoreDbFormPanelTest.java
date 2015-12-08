package net.rrm.ehour.ui.admin.backup.restore;

import net.rrm.ehour.backup.service.restore.RestoreService;
import net.rrm.ehour.ui.common.BaseSpringWebAppTester;
import org.apache.wicket.extensions.ajax.markup.html.AjaxLazyLoadPanel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.util.file.File;
import org.apache.wicket.util.tester.FormTester;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RestoreDbFormPanelTest extends BaseSpringWebAppTester {

    public static final String SUBMIT_PATH = "frame:frame_body:restoreBorder:restoreBorder_body:form:submit";
    public static final String STATUS_PATH = "frame:frame_body:restoreBorder:restoreBorder_body:form:feedback";
    @Mock
    private RestoreService restoreService;

    private RestoreDbFormPanel startPanel() {
        return tester.startComponentInPage(RestoreDbFormPanel.class);
    }

    @Before
    public void initMock() {
        MockitoAnnotations.initMocks(this);
        getMockContext().putBean(restoreService);
    }

    @Test
    public void shouldUploadXML() {
        startPanel();

        FormTester formTester = tester.newFormTester("frame:frame_body:restoreBorder:restoreBorder_body:form");

        formTester.setFile("file", new File("src/test/resources/import_ok.xml"), "text/xml");
        tester.executeAjaxEvent("frame:frame_body:restoreBorder:restoreBorder_body:form:submit", "onclick");
//        tester.assertComponent(STATUS_PATH, AjaxLazyLoadPanel.class);

//        fail();
    }

    @Test
    public void shouldFailForWrongContentType() {
        startPanel();

        FormTester formTester = tester.newFormTester("frame:frame_body:restoreBorder:restoreBorder_body:form");
        formTester.setFile("file", new File("src/test/resources/import_ok.xml"), "application/zip");

        tester.executeAjaxEvent(SUBMIT_PATH, "onclick");

        verifyZeroInteractions(restoreService);

        tester.assertErrorMessages("Invalid content type");
        tester.assertNoInfoMessage();
    }

    @Test
    public void shouldFailForNoFile() {
        startPanel();

        tester.executeAjaxEvent(SUBMIT_PATH, "onclick");
        tester.assertErrorMessages("Empty file");

        verifyZeroInteractions(restoreService);
    }
}