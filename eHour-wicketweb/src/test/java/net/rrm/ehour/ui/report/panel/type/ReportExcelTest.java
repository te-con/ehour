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


import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.service.AggregateReportService;
import net.rrm.ehour.ui.common.AbstractSpringWebAppTester;
import net.rrm.ehour.ui.common.cache.ObjectCache;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.ui.report.TreeReport;
import net.rrm.ehour.ui.report.aggregate.CustomerAggregateReport;
import net.rrm.ehour.ui.report.aggregate.ProjectAggregateReport;
import net.rrm.ehour.ui.report.aggregate.UserAggregateReport;
import net.rrm.ehour.ui.report.panel.ReportTestUtil;
import net.rrm.ehour.ui.report.panel.aggregate.CustomerReportExcel;
import net.rrm.ehour.ui.report.panel.aggregate.EmployeeReportExcel;
import net.rrm.ehour.ui.report.panel.aggregate.ProjectReportExcel;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Excel tester
 **/
public class ReportExcelTest extends AbstractSpringWebAppTester
{
	private ObjectCache cache;
	private ReportCriteria criteria;
	private AggregateReportService aggregateReportService;
	
	
	@Before
	public void setUp() throws Exception
	{
		super.setUp();
		
		aggregateReportService = createMock(AggregateReportService.class);
		getMockContext().putBean("aggregateReportService", aggregateReportService);

		
		EhourWebSession session = this.getWebApp().getSession();
		cache = session.getObjectCache();
		
		criteria = ReportTestUtil.getReportCriteria();
		
		expect(aggregateReportService.getAggregateReportData(isA(ReportCriteria.class)))
			.andReturn(ReportTestUtil.getAssignmentReportData());
		
		replay(aggregateReportService);
	}
	
	@After
	public void tearDown()
	{
		verify(aggregateReportService);
	}

	@Test
	public void testCustomerReportExcel() throws Exception
	{
		TreeReport report = new CustomerAggregateReport(criteria);
		String reportCacheId = cache.addObjectToCache(report);

		new CustomerReportExcel().getExcelData(reportCacheId);
	}
	
	@Test
	public void testEmployeeReportExcel() throws Exception
	{
		TreeReport report = new UserAggregateReport(criteria);
		String reportCacheId = cache.addObjectToCache(report);

		new EmployeeReportExcel().getExcelData(reportCacheId);
	}
	
	@Test
	public void testProjectReportExcel() throws Exception
	{
		TreeReport report = new ProjectAggregateReport(criteria);
		String reportCacheId = cache.addObjectToCache(report);

		new ProjectReportExcel().getExcelData(reportCacheId);
	}	
}
