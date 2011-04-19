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

package net.rrm.ehour.ui.report.panel.detail;

import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.service.DetailedReportService;
import net.rrm.ehour.ui.common.AbstractSpringWebAppTester;
import net.rrm.ehour.ui.report.panel.ReportTestUtil;
import net.rrm.ehour.ui.report.trend.DetailedReport;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.util.tester.ITestPanelSource;
import org.junit.Before;
import org.junit.Test;

import java.util.Locale;

import static org.easymock.EasyMock.*;

/**
 * Detailed report panel test
 **/

public class DetailedReportPanelTest extends AbstractSpringWebAppTester
{
	private DetailedReportService detailedReportService;
	
	@Before
	public void setup()
	{
		detailedReportService = createMock(DetailedReportService.class);
		getMockContext().putBean("detailedReportService", detailedReportService);
	}
	
	/**
	 * Test method for {@link net.rrm.ehour.persistence.persistence.ui.report.panel.detail.DetailedReportPanel#DetailedReportPanel(java.lang.String, net.rrm.ehour.persistence.persistence.ui.report.TreeReport, net.rrm.ehour.persistence.persistence.report.reports.ReportData)}.
	 */
	@Test
	@SuppressWarnings("serial")
	public void testDetailedReportPanel()
	{
		expect(detailedReportService.getDetailedReportData(isA(ReportCriteria.class)))
			.andReturn(ReportTestUtil.getFlatReportData());
		
		replay(detailedReportService);
		
		final DetailedReport detailedReport = new DetailedReport(ReportTestUtil.getReportCriteria(), Locale.ENGLISH);
		
		getTester().startPanel(new ITestPanelSource(){

			public Panel getTestPanel(String panelId)
			{
				return new DetailedReportPanel(panelId, 
												detailedReport);
			}
		});
		
		getTester().assertNoErrorMessage();
		
		verify(detailedReportService);
	}

}
