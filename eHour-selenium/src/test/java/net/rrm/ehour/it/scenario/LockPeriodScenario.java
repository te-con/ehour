package net.rrm.ehour.it.scenario;

import net.rrm.ehour.it.AbstractScenario;
import org.joda.time.LocalDate;
import org.junit.Test;

import static net.rrm.ehour.it.driver.EhourApplicationDriver.*;
import static net.rrm.ehour.it.driver.TimesheetDriver.*;
import static net.rrm.ehour.it.driver.TimesheetLockDriver.assertServerMessage;
import static net.rrm.ehour.it.driver.TimesheetLockDriver.newLock;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class LockPeriodScenario extends AbstractScenario {
    @Test
    public void should_create_lock() {
        loginAdmin();

        newLock(new LocalDate(), new LocalDate());

        assertServerMessage("Data saved");
    }

    @Test
    public void should_not_be_able_to_enter_hours_in_locked_period() {
        loginAdmin();

        newLock(new LocalDate(2013, 12, 1), new LocalDate(2013, 12, 31));

        createUserAndAssign();

        logout();

        loginRegularUser();

        navigateToMonth("January 2014");

        clickInWeek(0);

        assertFalse(isBookingHoursPossible(1));
        assertFalse(isBookingHoursPossible(2));
        assertFalse(isBookingHoursPossible(3));
        assertTrue(isBookingHoursPossible(4));
        assertTrue(isBookingHoursPossible(5));
    }
}
