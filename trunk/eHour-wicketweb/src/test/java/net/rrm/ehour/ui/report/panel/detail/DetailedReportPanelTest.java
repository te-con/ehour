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

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.Locale;

import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.service.DetailedReportService;
import net.rrm.ehour.ui.common.BaseUIWicketTester;
import net.rrm.ehour.ui.report.panel.ReportTestUtil;
import net.rrm.ehour.ui.report.trend.DetailedReport;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.util.tester.TestPanelSource;
import org.junit.Before;
import org.junit.Test;

/**
 * Detailed report panel test
 **/

public class DetailedReportPanelTest extends BaseUIWicketTester
{
	private DetailedReportService detailedReportService;
	
	@Before
	public void setup()
	{
		detailedReportService = createMock(DetailedReportService.class);
		mockContext.putBean("detailedReportService", detailedReportService);

	}
	
	/**
	 * Test method for {@link net.rrm.ehour.ui.report.panel.detail.DetailedReportPanel#DetailedReportPanel(java.lang.String, net.rrm.ehour.ui.report.TreeReport, net.rrm.ehour.report.reports.ReportData)}.
	 */
	@Test
	@SuppressWarnings("serial")
	public void testDetailedReportPanel()
	{
		expect(detailedReportService.getDetailedReportData(isA(ReportCriteria.class)))
			.andReturn(ReportTestUtil.getFlatReportData());
		
		replay(detailedReportService);
		
		final DetailedReport detailedReport = new DetailedReport(ReportTestUtil.getReportCriteria(), Locale.ENGLISH);
		
		tester.startPanel(new TestPanelSource(){

			public Panel getTestPanel(String panelId)
			{
				return new DetailedReportPanel(panelId, 
												detailedReport);
			}
		});
		
		tester.assertNoErrorMessage();
		
		verify(detailedReportService);
	}

}
