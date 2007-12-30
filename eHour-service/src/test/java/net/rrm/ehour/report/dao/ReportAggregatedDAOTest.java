/**
 * Created on Nov 4, 2006
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
 *
 * This Software is copyright TE-CON 2007. This Software is not open source by definition. The source of the Software is available for educational purposes.
 * TE-CON holds all the ownership rights on the Software.
 * TE-CON freely grants the right to use the Software. Any reproduction or modification of this Software, whether for commercial use or open source,
 * is subject to obtaining the prior express authorization of TE-CON.
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.report.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.rrm.ehour.dao.BaseDAOTest;
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.project.domain.Project;
import net.rrm.ehour.report.reports.element.AssignmentAggregateReportElement;
import net.rrm.ehour.user.domain.User;

/**
 * 
 **/

@SuppressWarnings("unchecked")
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
		DateRange range = dao.getMinMaxDateTimesheetEntry(new User(1));
		Date endDate = new Date(2007 - 1900, 2 - 1, 2, 23, 59, 59);
		endDate = new Date(endDate.getTime() + 999);

		assertEquals(new Date(2006 - 1900, 10 - 1, 2), range.getDateStart());
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
		
		List ids = new ArrayList();
		ids.add(new User(1));

		List<AssignmentAggregateReportElement> results = dao.getCumulatedHoursPerAssignmentForUsers(ids, dateRange);

		// test if collection is properly initialized
		AssignmentAggregateReportElement rep = results.get(0);
		assertEquals("eHour", rep.getProjectAssignment().getProject().getName());
		
		rep = results.get(0);
		assertEquals(3676.5f, rep.getTurnOver().floatValue(), 0.1);
		
		assertEquals(3, results.size());
	}
	
	/**
	 * 
	 *
	 */
	public void testGetCumulatedHoursPerAssignmentForUser()
	{
		List ids = new ArrayList();
		ids.add(new User(1));
		ids.add(new User(2));
		
		List<AssignmentAggregateReportElement> results = dao.getCumulatedHoursPerAssignmentForUsers(ids);

		// test if collection is properly initialized
		AssignmentAggregateReportElement rep = results.get(0);
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
		List<User> ids = new ArrayList<User>();
		ids.add(new User(1));
		List<Project> pids = new ArrayList<Project>();
		pids.add(new Project(1));		
		
		List<AssignmentAggregateReportElement> results = dao.getCumulatedHoursPerAssignmentForUsers(ids, pids);

		// test if collection is properly initialized
		AssignmentAggregateReportElement rep = results.get(0);
		assertEquals(38.7f, rep.getHours().floatValue(), 0.1);
		
		assertEquals(2, results.size());
	}	
	
	/**
	 * 
	 *
	 */
	public void testGetCumulatedHoursPerAssignmentForUserProjectDate()
	{
		DateRange dateRange = new DateRange(new Date(2006 - 1900, 10 - 1, 1), // deprecated? hmm ;) 
			    new Date(2006 - 1900, 10 - 1, 4));
		List<User> ids = new ArrayList<User>();
		ids.add(new User(1));
		List<Project> pids = new ArrayList<Project>();
		pids.add(new Project(1));
		List<AssignmentAggregateReportElement> results = dao.getCumulatedHoursPerAssignmentForUsers(ids, pids, dateRange);

		// test if collection is properly initialized
		AssignmentAggregateReportElement rep = results.get(0);
		assertEquals(14f, rep.getHours().floatValue(), 0.1);
		
		assertEquals(2, results.size());
	}
	
	public void testGetCumulatedHoursPerAssignment()
	{
		DateRange dateRange = new DateRange(new Date(2006 - 1900, 10 - 1, 1), // deprecated? hmm ;) 
			    new Date(2006 - 1900, 10 - 1, 4));
		
		List<AssignmentAggregateReportElement> results = dao.getCumulatedHoursPerAssignment(dateRange);
		
		assertEquals(3, results.size());
	}
	
	public void testGetCumulatedHoursPerAssignmentForProjects()
	{
		DateRange dateRange = new DateRange(new Date(2006 - 1900, 10 - 1, 1), // deprecated? hmm ;) 
			    new Date(2006 - 1900, 10 - 1, 4));
		List<Project> pids = new ArrayList<Project>();
		pids.add(new Project(1));

		List<AssignmentAggregateReportElement> results = dao.getCumulatedHoursPerAssignmentForProjects(pids, dateRange);
		
		assertEquals(2, results.size());
	}
	
	private List<Integer> getAsList(Integer id)
	{
		List ids = new ArrayList();
		ids.add(id);
		
		return ids;
		
	}

}
