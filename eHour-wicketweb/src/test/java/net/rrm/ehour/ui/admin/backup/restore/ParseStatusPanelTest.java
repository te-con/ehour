package net.rrm.ehour.ui.admin.backup.restore;

import net.rrm.ehour.backup.common.BackupEntityType;
import net.rrm.ehour.backup.domain.ParseSession;
import net.rrm.ehour.ui.common.BaseSpringWebAppTester;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 *         Created on: 12/5/10 - 12:32 AM
 */
public class ParseStatusPanelTest extends BaseSpringWebAppTester {

    private static final BackupEntityType USERS = new BackupEntityType("USERS", 1);

    @Test
    public void shouldDisplayErrors() {
        ParseSession status = new ParseSession();
        status.addError(USERS, "failed");
        status.addError(USERS, "failed again");

        startPanel(status);

        tester.assertNoErrorMessage();
        tester.assertComponent("panel:errors", ListView.class);
        tester.assertComponent("panel:errors:0:msgs", ListView.class);
        Component msg = tester.getComponentFromLastRenderedPage("panel:errors:0:msgs:0:msg");
        assertEquals("failed", msg.getDefaultModelObject());
    }

    @Test
    public void shouldDisplayInsertions() {
        ParseSession status = new ParseSession();

        status.addInsertion(USERS);
        status.addInsertion(USERS);

        startPanel(status);

        tester.assertNoErrorMessage();
        tester.assertNoInfoMessage();
        tester.assertComponent("panel:insertions", ListView.class);
        tester.assertModelValue("panel:insertions:0:insertions", "2");
        tester.assertModelValue("panel:insertions:0:key", "USERS");

    }

    private void startPanel(final ParseSession status) {
        tester.startComponentInPage(new ParseStatusPanel("panel", new Model<>(status)));
    }
}