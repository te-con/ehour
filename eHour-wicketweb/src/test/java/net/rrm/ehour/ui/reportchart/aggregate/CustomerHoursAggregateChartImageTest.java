/**
 * Created on Dec 30, 2007
 * Author: Thies
 *
 * Copyright (C) 2007 TE-CON, All Rights Reserved.
 *
 * This Software is copyright TE-CON 2007. This Software is not open source by definition. The source of the Software is available for educational purposes.
 * TE-CON holds all the ownership rights on the Software.
 * TE-CON freely grants the right to use the Software. Any reproduction or modification of this Software, whether for commercial use or open source,
 * is subject to obtaining the prior express authorization of TE-CON.
 * 
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.ui.reportchart.aggregate;

import net.rrm.ehour.report.reports.ReportData;
import net.rrm.ehour.ui.common.BaseUIWicketTester;
import net.rrm.ehour.ui.panel.report.ReportTestUtil;

import org.apache.wicket.model.Model;
import org.junit.Test;

/**
 * 
 **/
@SuppressWarnings("unchecked")

public class CustomerHoursAggregateChartImageTest extends BaseUIWicketTester
{

	/**
	 * Test method for {@link net.rrm.ehour.ui.reportchart.aggregate.CustomerHoursAggregateChartImage#CustomerHoursAggregateChartImage(java.lang.String, org.apache.wicket.model.Model, int, int)}.
	 * @throws Exception 
	 */
	@Test
	public void testCustomerHoursAggregateChartImage() throws Exception
	{
		ReportData reportData = new ReportData();
		reportData.setReportElements(ReportTestUtil.getAssignmentAggregateReportElements());
		
		CustomerHoursAggregateChartImage img = new CustomerHoursAggregateChartImage("image", new Model(reportData), 200, 100);
		img.getChart(reportData);
	}
}
