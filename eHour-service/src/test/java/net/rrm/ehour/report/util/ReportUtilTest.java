package net.rrm.ehour.report.util;

import com.google.common.collect.Lists;
import net.rrm.ehour.report.reports.element.AssignmentAggregateReportElement;
import net.rrm.ehour.report.reports.util.ReportUtil;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 *         Created on: 12/1/10 - 11:24 PM
 */
public class ReportUtilTest {
    @Test
    public void shouldReportEmptyList() {
        AssignmentAggregateReportElement element = new AssignmentAggregateReportElement();
        element.setHours(0);
        List<AssignmentAggregateReportElement> aggs = Lists.newArrayList(element);

        assertTrue(ReportUtil.isEmptyAggregateList(aggs));
    }
}
