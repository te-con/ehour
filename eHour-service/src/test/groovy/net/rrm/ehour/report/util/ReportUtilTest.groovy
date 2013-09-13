package net.rrm.ehour.report.util

import net.rrm.ehour.report.reports.element.AssignmentAggregateReportElement
import net.rrm.ehour.report.reports.util.ReportUtil
import org.junit.Test

import static org.junit.Assert.assertTrue

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 * Created on: 12/1/10 - 11:24 PM
 */
class ReportUtilTest
{
  @Test
  public void shouldReportEmptyList()
  {
    def aggs = [new AssignmentAggregateReportElement(hours: 0), new AssignmentAggregateReportElement()]

    assertTrue ReportUtil.isEmptyAggregateList(aggs)

  }
}
