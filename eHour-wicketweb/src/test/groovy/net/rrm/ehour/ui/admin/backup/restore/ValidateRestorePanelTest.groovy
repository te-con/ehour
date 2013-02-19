package net.rrm.ehour.ui.admin.backup.restore

import net.rrm.ehour.backup.service.ParseSession
import net.rrm.ehour.backup.service.RestoreService
import net.rrm.ehour.ui.common.AbstractSpringWebAppTester
import net.rrm.ehour.ui.common.event.AjaxEventHook
import net.rrm.ehour.ui.common.event.EventPublisher
import net.rrm.ehour.ui.common.event.PayloadAjaxEvent
import org.apache.wicket.markup.html.basic.Label
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertFalse
import static org.mockito.Mockito.when

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 * Created on: 12/7/10 - 2:12 AM
 */
class ValidateRestorePanelTest extends AbstractSpringWebAppTester {
    @Mock
    private RestoreService importService

    @Before
    void initMock() {
        MockitoAnnotations.initMocks this
        getMockContext().putBean("restoreService", importService);
    }

    @Test
    public void shouldDisplayValidateAndClickImport() {
        ParseSession session = new ParseSession(imported: false)

        when(importService.prepareImportDatabase(Mockito.anyString())).thenReturn(session)

        startPanel "fefe"

        tester.assertNoErrorMessage()
        tester.assertComponent "panel:${ValidateRestorePanel.ID_STATUS}", ParseStatusPanel.class

        def hook = new AjaxEventHook()
        EventPublisher.listenerHook = hook

        tester.executeAjaxEvent "panel:${ValidateRestorePanel.ID_IMPORT_LINK}", "onclick"

        assertEquals 1, hook.events.size()
        def event = hook.events[0] as PayloadAjaxEvent
        assertEquals(session, event.payload)

    }

    @Test
    public void shouldDisplayValidateWithFailingImport() {
        ParseSession status = new ParseSession(globalError: true, globalErrorMessage: "n/a")

        when(importService.prepareImportDatabase(Mockito.anyString())).thenReturn(status)

        startPanel "fefe"

        tester.assertNoErrorMessage()
        tester.assertComponent "panel:${ValidateRestorePanel.ID_STATUS}", ParseStatusPanel.class
        tester.assertComponent "panel:${ValidateRestorePanel.ID_STATUS}:globalError", Label.class
        tester.assertModelValue "panel:${ValidateRestorePanel.ID_STATUS}:globalError", "n/a"

        assertFalse status.importable
    }


    private void startPanel(final String constructParameter) {
        tester.startComponentInPage(new ValidateRestorePanel("panel", constructParameter))
    }
}
