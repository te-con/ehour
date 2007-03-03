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
import net.rrm.ehour.report.reports.ProjectAssignmentAggregate;

/**
 * TODO 
 **/

public class ReportAggregatedDAOTest extends BaseDAOTest
{
	private	ReportAggregatedDAO	dao;

	
	public void setReportAggregatedDAO(ReportAggregatedDAO dao)
	{
		this.dao = dao;
	}
	
	
	public void testGetMinMaxDateTimesheetEntry()
	{
		// ms accuracy rocks..
		Date endDate = new Date(2007 - 1900, 2 - 1, 2, 23, 59, 59);
		endDate = new Date(endDate.getTime() + 999);
		DateRange	range = dao.getMinMaxDateTimesheetEntry();
		assertEquals(new Date(2006 - 1900, 10 - 1, 2), range.getDateStart());
		assertEquals(endDate, range.getDateEnd());

	}	
	
	public void testGetMinMaxDateTimesheetEntryForUser()
	{
		DateRange range = dao.getMinMaxDateTimesheetEntry(2);
		Date endDate = new Date(2007 - 1900, 2 - 1, 2, 23, 59, 59);
		endDate = new Date(endDate.getTime() + 999);

		Calendar cal = new GregorianCalendar(2006, 10 - 1, 31);

		assertEquals(new Date(2006 - 1900, 10 - 1, 3), range.getDateStart());
		assertEquals(endDate, range.getDateEnd());
	}
	
	/**
	 * 
	 *
	 */
	public void testGetCumulatedHoursPerAssignmentForUserAndDate()
	{
		DateRange dateRange = new DateRange(new Date(2006 - 1900, 10 - 1, 1), // deprecated? hmm ;) 
										    new Date(2007 - 1900, 10, 30));
		
		List<ProjectAssignmentAggregate> results = dao.getCumulatedHoursPerAssignmentForUsers(new Integer[]{1}, dateRange);

		// test if collection is properly initialized
		ProjectAssignmentAggregate rep = results.get(0);
		assertEquals("eHour", rep.getProjectAssignment().getProject().getName());
		
		rep = results.get(0);
		assertEquals(3676.5f, rep.getTurnOver().floatValue(), 0.1);
		
		assertEquals(2, results.size());
	}
	
	/**
	 * 
	 *
	 */
	public void testGetCumulatedHoursPerAssignmentForUser()
	{
		List<ProjectAssignmentAggregate> results = dao.getCumulatedHoursPerAssignmentForUsers(new Integer[]{1, 2});

		// test if collection is properly initialized
		ProjectAssignmentAggregate rep = results.get(0);
		assertEquals("eHour", rep.getProjectAssignment().getProject().getName());
		
		assertEquals(38.7f, rep.getHours().floatValue(), 0.1);
		
		assertEquals(3, results.size());
	}
	
	
	/**
	 * 
	 *
	 */
	public void testGetCumulatedHoursPerAssignmentForUserProject()
	{
		List<ProjectAssignmentAggregate> results = dao.getCumulatedHoursPerAssignmentForUsers(new Integer[]{1},
																								new Integer[]{1});

		// test if collection is properly initialized
		ProjectAssignmentAggregate rep = results.get(0);
		assertEquals(38.7f, rep.getHours().floatValue(), 0.1);
		
		assertEquals(1, results.size());
	}	
	
	/**
	 * 
	 *
	 */
	public void testGetCumulatedHoursPerAssignmentForUserProjectDate()
	{
		DateRange dateRange = new DateRange(new Date(2006 - 1900, 10 - 1, 1), // deprecated? hmm ;) 
			    new Date(2006 - 1900, 10 - 1, 4));
		
		List<ProjectAssignmentAggregate> results = dao.getCumulatedHoursPerAssignmentForUsers(new Integer[]{2},
																								new Integer[]{1},
																								dateRange);

		// test if collection is properly initialized
		ProjectAssignmentAggregate rep = results.get(0);
		assertEquals(9.2f, rep.getHours().floatValue(), 0.1);
		
		assertEquals(1, results.size());
	}
	
	public void testGetCumulatedHoursPerAssignment()
	{
		DateRange dateRange = new DateRange(new Date(2006 - 1900, 10 - 1, 1), // deprecated? hmm ;) 
			    new Date(2006 - 1900, 10 - 1, 4));
		
		List<ProjectAssignmentAggregate> results = dao.getCumulatedHoursPerAssignment(dateRange);
		
		assertEquals(3, results.size());
	}
	
	public void testGetCumulatedHoursPerAssignmentForProjects()
	{
		DateRange dateRange = new DateRange(new Date(2006 - 1900, 10 - 1, 1), // deprecated? hmm ;) 
			    new Date(2006 - 1900, 10 - 1, 4));
		
		List<ProjectAssignmentAggregate> results = dao.getCumulatedHoursPerAssignmentForProjects(new Integer[]{1}, dateRange);
		
		assertEquals(2, results.size());
	}

}
