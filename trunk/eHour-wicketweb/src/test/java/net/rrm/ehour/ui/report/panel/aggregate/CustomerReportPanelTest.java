/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 * eHour is sponsored by TE-CON  - http://www.te-con.nl/
 */

package net.rrm.ehour.ui.report.panel.aggregate;


import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.service.AggregateReportService;
import net.rrm.ehour.ui.common.AbstractSpringWebAppTester;
import net.rrm.ehour.ui.report.aggregate.CustomerAggregateReport;
import net.rrm.ehour.ui.report.panel.ReportTestUtil;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.util.tester.TestPanelSource;
import org.junit.Before;
import org.junit.Test;

/**
 * Created on Mar 17, 2009, 6:33:02 AM
 * @author Thies Edeling (thies@te-con.nl) 
 *
 */
public class CustomerReportPanelTest extends AbstractSpringWebAppTester
{
	private AggregateReportService aggregateReportService;
	
	@Before
	public void setup()
	{
		aggregateReportService = createMock(AggregateReportService.class);
		getMockContext().putBean("aggregateReportService", aggregateReportService);
	}
	
	/**
	 * Test method for {@link net.rrm.ehour.ui.report.panel.detail.DetailedReportPanel#DetailedReportPanel(java.lang.String, net.rrm.ehour.ui.report.TreeReport, net.rrm.ehour.report.reports.ReportData)}.
	 */
	@Test
	@SuppressWarnings("serial")
	public void testDetailedReportPanel()
	{
		expect(aggregateReportService.getAggregateReportData(isA(ReportCriteria.class)))
			.andReturn(ReportTestUtil.getAssignmentReportData())
			.anyTimes();
		
		replay(aggregateReportService);
		
		final CustomerAggregateReport customerAggregateReport = new CustomerAggregateReport(ReportTestUtil.getReportCriteria());
		
		getTester().startPanel(new TestPanelSource(){

			public Panel getTestPanel(String panelId)
			{
				return new CustomerReportPanel(panelId, 
						customerAggregateReport);
			}
		});
		
		getTester().assertNoErrorMessage();
		
		verify(aggregateReportService);
	}

}