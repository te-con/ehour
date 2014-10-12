package net.rrm.ehour.report.reports.element;

import net.rrm.ehour.domain.Activity;

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 *         Created on: 12/7/10 - 12:35 AM
 */
public class ActivityAggregateReportElementTest implements GroovyObject {
    @org.junit.Test
    public void shouldTestOnNull() {
        ActivityAggregateReportElement element = new ActivityAggregateReportElement();
        invokeMethod("assertEquals", new Object[]{0, element.getProgressPercentage(), 0.01});
        invokeMethod("assertEquals", new Object[]{0, element.getAvailableHours(), 0.01});
        org.junit.Assert.assertEquals(0, element.getTurnOver().floatValue(), 0.01);
    }

    @org.junit.Test
    public void shouldGetPercentageOf50ForAllottedFixed() {
        Activity activity1 = new Activity();

        Activity activity = activity1.setAllottedHours(20);
        ActivityAggregateReportElement element1 = new ActivityAggregateReportElement();


        ActivityAggregateReportElement element = element1.setActivity(activity) element1.setHours(10);

        invokeMethod("assertEquals", new Object[]{50, element.getProgressPercentage(), 0.01});
    }

    @org.junit.Test
    public void shouldGetAvailableHoursForAllottedFixed() {
        Activity activity1 = new Activity();

        Activity activity = activity1.setAllottedHours(20);
        ActivityAggregateReportElement element1 = new ActivityAggregateReportElement();


        ActivityAggregateReportElement element = element1.setActivity(activity) element1.setHours(10);

        invokeMethod("assertEquals", new Object[]{10, element.getAvailableHours(), 0.01});
    }

}
