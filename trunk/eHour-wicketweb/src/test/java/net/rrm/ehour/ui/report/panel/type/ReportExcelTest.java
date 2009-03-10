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

package net.rrm.ehour.ui.report.panel.type;


import net.rrm.ehour.report.reports.ReportData;
import net.rrm.ehour.ui.common.BaseUIWicketTester;
import net.rrm.ehour.ui.common.report.ReportCache;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.ui.report.TreeReport;
import net.rrm.ehour.ui.report.aggregate.CustomerAggregateReport;
import net.rrm.ehour.ui.report.aggregate.ProjectAggregateReport;
import net.rrm.ehour.ui.report.aggregate.UserAggregateReport;
import net.rrm.ehour.ui.report.panel.ReportTestUtil;
import net.rrm.ehour.ui.report.panel.aggregate.CustomerReportExcel;
import net.rrm.ehour.ui.report.panel.aggregate.EmployeeReportExcel;
import net.rrm.ehour.ui.report.panel.aggregate.ProjectReportExcel;

import org.junit.Before;
import org.junit.Test;

/**
 * Excel tester
 **/
@SuppressWarnings({"unchecked"})
public class ReportExcelTest extends BaseUIWicketTester
{
	private ReportCache cache;
	private ReportData reportData;
	
	@Before
	public void setUp() throws Exception
	{
		super.setUp();
		
		EhourWebSession session = this.webapp.getSession();
		cache = session.getReportCache();
		reportData = ReportTestUtil.getAssignmentReportData();
	}

	@Test
	public void testCustomerReportExcel() throws Exception
	{
		TreeReport report = new CustomerAggregateReport(reportData);
		String reportCacheId = cache.addReportToCache(report, reportData);

		new CustomerReportExcel().getExcelData(reportCacheId);
	}
	
	@Test
	public void testEmployeeReportExcel() throws Exception
	{
		TreeReport report = new UserAggregateReport(reportData);
		String reportCacheId = cache.addReportToCache(report, reportData);

		new EmployeeReportExcel().getExcelData(reportCacheId);
	}
	
	@Test
	public void testProjectReportExcel() throws Exception
	{
		TreeReport report = new ProjectAggregateReport(reportData);
		String reportCacheId = cache.addReportToCache(report, reportData);

		new ProjectReportExcel().getExcelData(reportCacheId);
	}	
}
