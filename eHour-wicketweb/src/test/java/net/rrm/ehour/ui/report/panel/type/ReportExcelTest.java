/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package net.rrm.ehour.ui.report.panel.type;


import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.reports.AggregateReportDataObjectMother;
import net.rrm.ehour.report.service.AggregateReportService;
import net.rrm.ehour.ui.common.AbstractSpringWebAppTester;
import net.rrm.ehour.ui.report.TreeReportModel;
import net.rrm.ehour.ui.report.aggregate.CustomerAggregateReportModel;
import net.rrm.ehour.ui.report.aggregate.ProjectAggregateReportModel;
import net.rrm.ehour.ui.report.aggregate.UserAggregateReportModel;
import net.rrm.ehour.ui.report.panel.DetailedReportDataObjectMother;
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
	private ReportCriteria criteria;
	private AggregateReportService aggregateReportService;
	
	
	@Before
	public void before() throws Exception
	{
		aggregateReportService = createMock(AggregateReportService.class);
		getMockContext().putBean("aggregateReportService", aggregateReportService);
		
		criteria = DetailedReportDataObjectMother.getReportCriteria();
		
		expect(aggregateReportService.getAggregateReportData(isA(ReportCriteria.class)))
			.andReturn(AggregateReportDataObjectMother.getAssignmentReportData());
		
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
		TreeReportModel reportModel = new CustomerAggregateReportModel(criteria);
		new CustomerReportExcel().getExcelData(reportModel);
	}
	
	@Test
	public void testEmployeeReportExcel() throws Exception
	{
		TreeReportModel reportModel = new UserAggregateReportModel(criteria);
		new EmployeeReportExcel().getExcelData(reportModel);
	}
	
	@Test
	public void testProjectReportExcel() throws Exception
	{
		TreeReportModel reportModel = new ProjectAggregateReportModel(criteria);
		new ProjectReportExcel().getExcelData(reportModel);
	}	
}
