package net.rrm.ehour.it.scenario;

import net.rrm.ehour.it.AbstractScenario;
import net.rrm.ehour.it.WicketBy;
import org.junit.Test;

import static net.rrm.ehour.it.EhourApplicationDriver.*;
import static org.junit.Assert.assertTrue;

public class BookHourAndExportScenario extends AbstractScenario {
    @Test
    public void should_book_hours() {
        loginAdmin();
        createUser("thies", "a");
        createCustomer("KLM", "KLM");
        createProject("ET", "ET", "KLM", "KLM");
        createProjectAssignment(0, "KLM", "KLM", "ET", "ET");
        logout();

        login("thies", "a");

        assertTrue(Driver.findElement(WicketBy.wicketPath("contentContainer_projectOverview_greyBorder_title")).getText().contains("Aggregated hours"));
    }
}
