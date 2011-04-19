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

package net.rrm.ehour.report.criteria;
import java.util.GregorianCalendar;

import junit.framework.TestCase;
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.report.service.ReportCriteriaService;
import net.rrm.ehour.util.DateUtil;

/**
 * TODO 
 **/

public class ReportCriteriaTest extends TestCase
{
	ReportCriteriaService 	reportCriteriaService;
	
	protected void setUp() throws Exception
	{
		super.setUp();
	}


	
	public void testGetReportRange()
	{
		DateRange dr = DateUtil.calendarToMonthRange(new GregorianCalendar());
		
		AvailableCriteria availCriteria = new AvailableCriteria();
		ReportCriteria reportCriteria = new ReportCriteria(availCriteria);

		availCriteria.setReportRange(dr);
		
		assertEquals(dr, reportCriteria.getReportRange());
	}

//	
//	public void testSetUserCriteria()
//	{
//		expect(reportCriteriaService.syncUserReportCriteria(reportCriteria, ReportCriteria.UPDATE_ALL)).andReturn(reportCriteria);
//
//		replay(reportCriteriaService);
//		
//		reportCriteria.setUserCriteria(new UserCriteria());
//		reportCriteria.updateAvailableCriteria();
//		
//		verify(reportCriteriaService);
//	}

}
