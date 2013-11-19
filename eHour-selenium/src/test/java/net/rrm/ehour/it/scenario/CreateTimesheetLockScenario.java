package net.rrm.ehour.it.scenario;

import net.rrm.ehour.it.AbstractScenario;
import net.rrm.ehour.it.driver.EhourApplicationDriver;
import org.joda.time.LocalDate;
import org.junit.Test;

import static net.rrm.ehour.it.driver.EhourApplicationDriver.loginAdmin;
import static net.rrm.ehour.it.driver.TimesheetLockApplicationDriver.*;

public class CreateTimesheetLockScenario extends AbstractScenario {
    @Test
    public void should_lock_timesheets() {
        loginAdmin();

        navigateToAdminLocks();

        newLock(new LocalDate(), new LocalDate());

        EhourApplicationDriver.sleep(500);

        assertServerMessage("Locked");
    }
}
