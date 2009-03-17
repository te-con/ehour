/**
 * Created on Dec 31, 2007
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

package net.rrm.ehour.ui.report.trend;


import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.util.Locale;

import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.service.DetailedReportService;
import net.rrm.ehour.ui.common.AbstractSpringInjectorTester;
import net.rrm.ehour.ui.report.panel.ReportTestUtil;

import org.junit.Before;
import org.junit.Test;

/**
 * Test of detailed report 
 **/
public class DetailedReportTest extends AbstractSpringInjectorTester
{
	private DetailedReportService detailedReportService;
	
	@Before
	public void setup() throws Exception
	{
		super.springLocatorSetup();
		
		detailedReportService = createMock(DetailedReportService.class);
		mockContext.putBean("detailedReportService", detailedReportService);
	}
	
	/**
	 * @throws java.lang.Exception
	 */
	@Test
	public void testCreateDetailedReport()
	{
		expect(detailedReportService.getDetailedReportData(isA(ReportCriteria.class)))
			.andReturn(ReportTestUtil.getFlatReportData());
		
		replay(detailedReportService);
		
		DetailedReport detailedReport = new DetailedReport(ReportTestUtil.getReportCriteria(), Locale.ENGLISH);
		assertEquals(6, detailedReport.getReportData().getReportElements().size());

		verify(detailedReportService);
	}
}
