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

package net.rrm.ehour.ui.report.user.page;

import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.criteria.ReportCriteriaUpdateType;
import net.rrm.ehour.ui.report.page.BaseTestReport;
import org.junit.Test;

import static org.easymock.EasyMock.*;

@SuppressWarnings("serial")
public class UserReportPageTest extends BaseTestReport
{
	@Test
	public void shouldRenderPage()
	{
		expect(reportCriteriaService.syncUserReportCriteria(isA(ReportCriteria.class), eq(ReportCriteriaUpdateType.UPDATE_ALL)))
		.andReturn(reportCriteria);	

		expect(aggregateReportService.getAggregateReportData(reportCriteria))
				.andReturn(data);
		
		replay(reportCriteriaService);
		replay(aggregateReportService);
		
		tester.startPage(UserReportPage.class);
		tester.assertRenderedPage(UserReportPage.class);
		tester.assertNoErrorMessage();
		
		verify(reportCriteriaService);
		verify(aggregateReportService);
	}
}
