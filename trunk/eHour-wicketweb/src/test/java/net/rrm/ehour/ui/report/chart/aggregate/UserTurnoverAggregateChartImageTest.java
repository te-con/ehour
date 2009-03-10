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

package net.rrm.ehour.ui.report.chart.aggregate;


import net.rrm.ehour.report.reports.ReportData;
import net.rrm.ehour.ui.common.BaseUIWicketTester;
import net.rrm.ehour.ui.report.chart.aggregate.UserTurnoverAggregateChartImage;
import net.rrm.ehour.ui.report.panel.ReportTestUtil;

import org.apache.wicket.model.Model;
import org.junit.Test;

/**
 * TODO 
 **/

public class UserTurnoverAggregateChartImageTest extends BaseUIWicketTester
{
	@Test
	public void testChartImage() throws Exception
	{
		ReportData reportData = new ReportData();
		reportData.setReportElements(ReportTestUtil.getAssignmentAggregateReportElements());
		
		UserTurnoverAggregateChartImage img = new UserTurnoverAggregateChartImage("image", new Model(reportData), 200, 100);
		img.getChart(reportData);
	}
}
