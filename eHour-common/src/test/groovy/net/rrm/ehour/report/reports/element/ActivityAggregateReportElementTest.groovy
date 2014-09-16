package net.rrm.ehour.report.reports.element


import net.rrm.ehour.domain.Activity
import org.junit.Test
import static org.junit.Assert.assertEquals

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 * Created on: 12/7/10 - 12:35 AM
 */
class ActivityAggregateReportElementTest {

    @Test
    void shouldTestOnNull() {
        def element = new ActivityAggregateReportElement()
        assertEquals 0, element.getProgressPercentage(), 0.01
        assertEquals 0, element.getAvailableHours(), 0.01
        assertEquals 0, element.getTurnOver().floatValue(), 0.01
    }

    @Test
    void shouldGetPercentageOf50ForAllottedFixed() {
        def activity = new Activity(allottedHours: 20)
        def element = new ActivityAggregateReportElement(activity: activity, hours: 10)

        assertEquals 50, element.getProgressPercentage(), 0.01
    }

    @Test
    void shouldGetAvailableHoursForAllottedFixed() {
        def activity = new Activity(allottedHours: 20)
        def element = new ActivityAggregateReportElement(activity: activity, hours: 10)

        assertEquals 10, element.getAvailableHours(), 0.01
    }
}
