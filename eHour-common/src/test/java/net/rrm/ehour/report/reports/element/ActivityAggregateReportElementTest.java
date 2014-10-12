package net.rrm.ehour.report.reports.element;


import net.rrm.ehour.domain.Activity;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 *         Created on: 12/7/10 - 12:35 AM
 */
public class ActivityAggregateReportElementTest {
    @org.junit.Test
    public void shouldGetPercentageOf50ForAllottedFixed() {
        Activity activity = new Activity();
        activity.setAllottedHours(20f);

        ActivityAggregateReportElement element = new ActivityAggregateReportElement();
        element.setActivity(activity);
        element.setHours(10);

        assertEquals(50, element.getProgressPercentage().get(), 0.01);
    }

    @org.junit.Test
    public void shouldGetAvailableHoursForAllottedFixed() {
        Activity activity = new Activity();

        activity.setAllottedHours(20f);

        ActivityAggregateReportElement element= new ActivityAggregateReportElement();
        element.setActivity(activity);
        element.setHours(10);

        assertEquals(10, element.getAvailableHours().get(), 0.01);
    }

}
