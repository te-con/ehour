package net.rrm.ehour.ui.timesheet.dto;

import org.junit.Test;

import java.util.Arrays;
import java.util.Date;

import static org.junit.Assert.assertTrue;

public class TimesheetTest {
    @Test
    public void should_mark_date_in_sequence_as_locked() {
        Timesheet timesheet = new Timesheet();

        Date date = new Date();
        timesheet.setLockedDays(Arrays.asList(date));
        timesheet.setDateSequence(new Date[]{date});
        assertTrue(timesheet.isLocked(0));
    }
}
