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
import net.rrm.ehour.ui.common.AbstractSpringWebAppTester;
import net.rrm.ehour.ui.report.panel.DetailedReportDataObjectMother;

import org.junit.Before;
import org.junit.Test;

/**
 * Test of detailed report 
 **/
public class DetailedReportTest extends AbstractSpringWebAppTester
{
	private DetailedReportService detailedReportService;
	
	@Before
	public void setup() throws Exception
	{
		detailedReportService = createMock(DetailedReportService.class);
		getMockContext().putBean("detailedReportService", detailedReportService);
	}
	
	/**
	 * @throws java.lang.Exception
	 */
	@Test
	public void testCreateDetailedReport()
	{
		expect(detailedReportService.getDetailedReportData(isA(ReportCriteria.class)))
			.andReturn(DetailedReportDataObjectMother.getFlatReportData());
		
		replay(detailedReportService);
		
		DetailedReportModel detailedReport = new DetailedReportModel(DetailedReportDataObjectMother.getReportCriteria());
		assertEquals(5, detailedReport.getReportData().getReportElements().size());

		verify(detailedReportService);
	}
}
