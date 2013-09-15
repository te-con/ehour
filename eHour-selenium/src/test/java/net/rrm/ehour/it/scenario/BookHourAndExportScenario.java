package net.rrm.ehour.it.scenario;

import net.rrm.ehour.it.AbstractScenario;
import org.junit.Test;

import static net.rrm.ehour.it.driver.EhourApplicationDriver.*;
import static net.rrm.ehour.it.driver.TimesheetApplicationDriver.*;
import static net.rrm.ehour.it.driver.UserManagementApplicationDriver.assertUserDataSaved;
import static net.rrm.ehour.it.driver.UserManagementApplicationDriver.createUser;
import static org.junit.Assert.assertTrue;

public class BookHourAndExportScenario extends AbstractScenario {
    @Test
    public void should_book_hours() {
        loginAdmin();
        createUser("thies", "a", "Edeling");
        assertUserDataSaved();
        createCustomer("KLM", "KLM");
        createProject("ET", "ET", "KLM", "KLM");
        createProjectAssignment(0, "KLM", "KLM", "ET", "ET");
        logout();

        login("thies", "a");

        assertInOverviewPage();

        clickInWeek(1);

        bookHours(2, 8f);

        addDayComment(2, "some comment");

        submitTimesheet();

        assertTrue(getServerMessage().startsWith("8 hours booked"));
    }
}
