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

package net.rrm.ehour.ui.report.page;

import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.criteria.ReportCriteriaUpdateType;

import org.junit.Test;



/**
 * TODO 
 **/

public class GlobalReportPageTest extends BaseTestReport
{
	@Test
	public void testReportPageRender()
	{
		expect(reportCriteriaService.syncUserReportCriteria(isA(ReportCriteria.class), eq(ReportCriteriaUpdateType.UPDATE_ALL)))
			.andReturn(reportCriteria);
		
		replay(reportCriteriaService);
		
		getTester().startPage(GlobalReportPage.class);
		getTester().assertRenderedPage(GlobalReportPage.class);
		getTester().assertNoErrorMessage();
		
		verify(reportCriteriaService);
	}
}
