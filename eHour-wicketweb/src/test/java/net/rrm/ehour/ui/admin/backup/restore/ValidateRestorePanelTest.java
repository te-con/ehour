package net.rrm.ehour.ui.admin.backup.restore;

import net.rrm.ehour.backup.domain.ParseSession;
import net.rrm.ehour.backup.service.RestoreService;
import net.rrm.ehour.ui.common.BaseSpringWebAppTester;
import net.rrm.ehour.ui.common.event.AjaxEventHook;
import net.rrm.ehour.ui.common.event.EventPublisher;
import net.rrm.ehour.ui.common.event.PayloadAjaxEvent;
import org.apache.wicket.markup.html.basic.Label;
import org.codehaus.groovy.runtime.DefaultGroovyMethods;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 *         Created on: 12/7/10 - 2:12 AM
 */
public class ValidateRestorePanelTest extends BaseSpringWebAppTester {
    @Before
    public void initMock() {
        MockitoAnnotations.initMocks(this);
        getMockContext().putBean("restoreService", importService);
    }

    @Test
    public void shouldDisplayValidateAndClickImport() {
        ParseSession session = new ParseSession();
        session.setImported(false);

        Mockito.when(importService.prepareImportDatabase(Mockito.anyString())).thenReturn(session);

        startPanel("fefe");

        getTester().assertNoErrorMessage();
        getTester().assertComponent("panel:" + ValidateRestorePanel.ID_STATUS, ParseStatusPanel.class);

        AjaxEventHook hook = new AjaxEventHook();
        EventPublisher.listenerHook = hook;

        getTester().executeAjaxEvent("panel:" + ValidateRestorePanel.ID_IMPORT_LINK, "onclick");

        Assert.assertEquals(1, hook.events.size());
        PayloadAjaxEvent event = DefaultGroovyMethods.asType(hook.events.get(0), PayloadAjaxEvent.class);
        Assert.assertEquals(session, event.getPayload());

    }

    @Test
    public void shouldDisplayValidateWithFailingImport() {
        ParseSession session = new ParseSession();

        session.setGlobalError(true);
        session.setGlobalErrorMessage("n/a");

        Mockito.when(importService.prepareImportDatabase(Mockito.anyString())).thenReturn(session);

        startPanel("fefe");

        getTester().assertNoErrorMessage();
        getTester().assertComponent("panel:" + ValidateRestorePanel.ID_STATUS, ParseStatusPanel.class);
        getTester().assertComponent("panel:" + ValidateRestorePanel.ID_STATUS + ":globalError", Label.class);
        getTester().assertModelValue("panel:" + ValidateRestorePanel.ID_STATUS + ":globalError", "n/a");

        Assert.assertFalse(session.isImportable());
    }

    private void startPanel(final String constructParameter) {
        getTester().startComponentInPage(new ValidateRestorePanel("panel", constructParameter));
    }

    @Mock
    private RestoreService importService;
}
