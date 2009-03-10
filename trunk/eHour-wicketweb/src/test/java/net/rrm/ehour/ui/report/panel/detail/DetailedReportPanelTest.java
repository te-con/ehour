/**
 * Created on Jan 20, 2008
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

package net.rrm.ehour.ui.report.panel.detail;

import java.util.Locale;

import net.rrm.ehour.report.reports.ReportData;
import net.rrm.ehour.report.reports.element.FlatReportElement;
import net.rrm.ehour.ui.common.BaseUIWicketTester;
import net.rrm.ehour.ui.report.panel.ReportTestUtil;
import net.rrm.ehour.ui.report.panel.detail.DetailedReportPanel;
import net.rrm.ehour.ui.report.trend.DetailedReport;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.util.tester.TestPanelSource;
import org.junit.Test;

/**
 * Detailed report panel test
 **/

public class DetailedReportPanelTest extends BaseUIWicketTester
{
	/**
	 * Test method for {@link net.rrm.ehour.ui.report.panel.detail.DetailedReportPanel#DetailedReportPanel(java.lang.String, net.rrm.ehour.ui.report.TreeReport, net.rrm.ehour.report.reports.ReportData)}.
	 */
	@Test
	@SuppressWarnings("serial")
	public void testDetailedReportPanel()
	{
		final ReportData<FlatReportElement> reportData = ReportTestUtil.getFlatReportData();
		final DetailedReport detailedReport = new DetailedReport(reportData, Locale.ENGLISH);
		
		tester.startPanel(new TestPanelSource(){

			public Panel getTestPanel(String panelId)
			{
				return new DetailedReportPanel(panelId, 
												detailedReport,
												reportData);
			}
		});
		
		tester.assertNoErrorMessage();
	}

}
