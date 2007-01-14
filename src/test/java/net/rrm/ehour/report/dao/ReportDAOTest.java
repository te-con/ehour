/**
 * Created on Nov 4, 2006
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.report.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import net.rrm.ehour.dao.BaseDAOTest;
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.report.dto.ProjectReport;

/**
 * TODO 
 **/

public class ReportDAOTest extends BaseDAOTest
{
	private	ReportDAO	dao;

	
	public void setReportDAO(ReportDAO dao)
	{
		this.dao = dao;
	}
	
	/**
	 * 
	 *
	 */
	public void testGetCumulatedHoursPerAssignmentForUser()
	{
		DateRange dateRange = new DateRange(new Date(2006 - 1900, 10 - 1, 1), // deprecated? hmm ;) 
										    new Date(2006 - 1900, 10, 30));
		
		List<ProjectReport> results = dao.getCumulatedHoursPerAssignmentForUser(new Integer(1), dateRange);

		// test if collection is properly initialized
		ProjectReport rep = results.get(0);
		assertEquals("Days off", rep.getProjectAssignment().getProject().getName());
		
		rep = results.get(1);
		assertEquals(3676.5f, rep.getTurnOver().floatValue(), 0.1);
		
		assertEquals(2, results.size());
	}
	
	public void testGetMinMaxDateTimesheetEntry()
	{
		DateRange	range = dao.getMinMaxDateTimesheetEntry();
		Calendar cal = new GregorianCalendar(2006, 10 - 1, 31);
		cal.set(Calendar.HOUR, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		assertEquals(new Date(2006 - 1900, 10 - 1, 2), range.getDateStart());
		assertEquals(cal.getTime(), range.getDateEnd());

	}
}
