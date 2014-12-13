package net.rrm.ehour.it.scenario;

import net.rrm.ehour.it.AbstractScenario;
import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;
import org.junit.Test;

import static net.rrm.ehour.it.driver.EhourApplicationDriver.*;
import static net.rrm.ehour.it.driver.TimesheetDriver.*;
import static net.rrm.ehour.it.driver.TimesheetLockDriver.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LockPeriodScenario extends AbstractScenario {
    @Test
    public void should_create_lock() {
        loginAdmin();

        newLock(new LocalDate(2013, DateTimeConstants.DECEMBER, 1), new LocalDate(2013, DateTimeConstants.DECEMBER, 31));

        submit();
        assertDataSaved();
    }

    @Test
    public void should_create_lock_with_excluded_users() {
        loginAdmin();

        newLock(new LocalDate(2013, DateTimeConstants.DECEMBER, 1), new LocalDate(2013, DateTimeConstants.DECEMBER, 31));

        excludeUser(0);
        submit();
        assertDataSaved();

        navigateToAdminLocks();
        editLock(0, "December, 2013");
// TODO fix me later
//        sleep();
//
//        WebElement element = findElement(By.className("maxScroll"));
//        assertTrue(element.getText().contains("Admin, eHour"));
    }

    @Test
    public void should_not_be_able_to_enter_hours_in_locked_period() {
        loginAdmin();

        newLock(new LocalDate(2013, DateTimeConstants.DECEMBER, 1), new LocalDate(2013, DateTimeConstants.DECEMBER, 31));
        submit();
        assertDataSaved();

        createUserAndAssign();

        logout();

        loginRegularUser();

        navigateToMonth("January 2014");

        clickInWeek(0);

        assertFalse(isBookingHoursPossible(1));
        assertFalse(isBookingHoursPossible(2));
        assertTrue(isBookingHoursPossible(3));
        assertTrue(isBookingHoursPossible(4));
        assertTrue(isBookingHoursPossible(5));
    }
}
