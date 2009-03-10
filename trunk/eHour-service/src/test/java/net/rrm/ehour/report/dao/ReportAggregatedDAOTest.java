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

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.rrm.ehour.dao.BaseDAOTest;
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.report.reports.element.AssignmentAggregateReportElement;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 
 **/

@SuppressWarnings({"unchecked", "deprecation"})
@RunWith(SpringJUnit4ClassRunner.class)
public class ReportAggregatedDAOTest extends BaseDAOTest
{
	@Autowired
	private	ReportAggregatedDAO	reportAggregatedDAO;

	@Test
	public void testGetMinMaxDateTimesheetEntry()
	{
		// ms accuracy rocks..
		Date endDate = new Date(2007 - 1900, 2 - 1, 2, 23, 59, 59);
		endDate = new Date(endDate.getTime() + 999);
		DateRange	range = reportAggregatedDAO.getMinMaxDateTimesheetEntry();
		assertEquals(new Date(2006 - 1900, 10 - 1, 2), range.getDateStart());
		assertEquals(endDate, range.getDateEnd());

	}	

	@Test
	public void testGetMinMaxDateTimesheetEntryForUser()
	{
		DateRange range = reportAggregatedDAO.getMinMaxDateTimesheetEntry(new User(1));
		Date endDate = new Date(2007 - 1900, 2 - 1, 2, 23, 59, 59);
		endDate = new Date(endDate.getTime() + 999);

		assertEquals(new Date(2006 - 1900, 10 - 1, 2), range.getDateStart());
		assertEquals(endDate, range.getDateEnd());
	}
	
	/**
	 * 
	 *
	 */
	@Test
	public void testGetCumulatedHoursPerAssignmentForUserAndDate()
	{
		DateRange dateRange = new DateRange(new Date(2006 - 1900, 10 - 1, 1), // deprecated? hmm ;) 
										    new Date(2007 - 1900, 10, 30));
		
		List ids = new ArrayList();
		ids.add(new User(1));

		List<AssignmentAggregateReportElement> results = reportAggregatedDAO.getCumulatedHoursPerAssignmentForUsers(ids, dateRange);

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
	@Test
	public void testGetCumulatedHoursPerAssignmentForUser()
	{
		List ids = new ArrayList();
		ids.add(new User(1));
		ids.add(new User(2));
		
		List<AssignmentAggregateReportElement> results = reportAggregatedDAO.getCumulatedHoursPerAssignmentForUsers(ids);

		// test if collection is properly initialized
		AssignmentAggregateReportElement rep = results.get(0);
		assertEquals("eHour", rep.getProjectAssignment().getProject().getName());
		
		assertEquals(38.7f, rep.getHours().floatValue(), 0.1);
		
		assertEquals(5, results.size());
	}
	
	
	/**
	 * 
	 *
	 */
	@Test
	public void testGetCumulatedHoursPerAssignmentForUserProject()
	{
		List<User> ids = new ArrayList<User>();
		ids.add(new User(1));
		List<Project> pids = new ArrayList<Project>();
		pids.add(new Project(1));		
		
		List<AssignmentAggregateReportElement> results = reportAggregatedDAO.getCumulatedHoursPerAssignmentForUsers(ids, pids);

		// test if collection is properly initialized
		AssignmentAggregateReportElement rep = results.get(0);
		assertEquals(38.7f, rep.getHours().floatValue(), 0.1);
		
		assertEquals(2, results.size());
	}	
	
	/**
	 * 
	 *
	 */
	@Test
	public void testGetCumulatedHoursPerAssignmentForUserProjectDate()
	{
		DateRange dateRange = new DateRange(new Date(2006 - 1900, 10 - 1, 1), // deprecated? hmm ;) 
			    new Date(2006 - 1900, 10 - 1, 4));
		List<User> ids = new ArrayList<User>();
		ids.add(new User(1));
		List<Project> pids = new ArrayList<Project>();
		pids.add(new Project(1));
		List<AssignmentAggregateReportElement> results = reportAggregatedDAO.getCumulatedHoursPerAssignmentForUsers(ids, pids, dateRange);

		// test if collection is properly initialized
		AssignmentAggregateReportElement rep = results.get(0);
		assertEquals(14f, rep.getHours().floatValue(), 0.1);
		
		assertEquals(2, results.size());
	}
	
	@Test
	public void testGetCumulatedHoursPerAssignment()
	{
		DateRange dateRange = new DateRange(new Date(2006 - 1900, 10 - 1, 1), // deprecated? hmm ;) 
			    new Date(2006 - 1900, 10 - 1, 4));
		
		List<AssignmentAggregateReportElement> results = reportAggregatedDAO.getCumulatedHoursPerAssignment(dateRange);
		
		assertEquals(3, results.size());
	}
	
	@Test
	public void testGetCumulatedHoursPerAssignmentForProjects()
	{
		DateRange dateRange = new DateRange(new Date(2006 - 1900, 10 - 1, 1), // deprecated? hmm ;) 
			    new Date(2006 - 1900, 10 - 1, 4));
		List<Project> pids = new ArrayList<Project>();
		pids.add(new Project(1));

		List<AssignmentAggregateReportElement> results = reportAggregatedDAO.getCumulatedHoursPerAssignmentForProjects(pids, dateRange);
		
		assertEquals(2, results.size());
	}
	
}
