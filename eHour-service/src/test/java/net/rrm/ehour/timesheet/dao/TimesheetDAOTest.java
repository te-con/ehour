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

package net.rrm.ehour.timesheet.dao;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import net.rrm.ehour.dao.BaseDAOTest;
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.domain.TimesheetEntry;
import net.rrm.ehour.timesheet.dto.BookedDay;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SuppressWarnings({"deprecation", "unchecked"})
public class TimesheetDAOTest extends BaseDAOTest 
{
	@Autowired
	private	TimesheetDAO	timesheetDAO;
	
	/**
	 * 
	 *
	 */
	@Test
	public void testGetTimesheetEntriesInRange()
	{
		Calendar 	dateStart = new GregorianCalendar(2006, 10 - 1, 1);
		Calendar 	dateEnd = new GregorianCalendar(2006, 11 - 1, 1);
		DateRange	dateRange = new DateRange(dateStart.getTime(), dateEnd.getTime());
		List		results;
		
		results = timesheetDAO.getTimesheetEntriesInRange(new Integer(1), dateRange);
		
		assertEquals(9, results.size());
	}
	
	@Test
	public void testGetTimesheetEntriesInRangeForAssignment()
	{
		Calendar 	dateStart = new GregorianCalendar(2006, 10 - 1, 1);
		Calendar 	dateEnd = new GregorianCalendar(2006, 11 - 1, 1);
		DateRange	dateRange = new DateRange(dateStart.getTime(), dateEnd.getTime());
		List		results;
		
		results = timesheetDAO.getTimesheetEntriesInRange(new ProjectAssignment(2), dateRange);
		
		assertEquals(2, results.size());
	}	
	
	/**
	 * 
	 *
	 */
	@Test
	public void testGetBookedHoursperDayInRange()
	{
		Calendar 	dateStart = new GregorianCalendar(2006, 10 - 1, 1);
		Calendar 	dateEnd = new GregorianCalendar(2006, 11 - 1, 1);
		DateRange	dateRange = new DateRange(dateStart.getTime(), dateEnd.getTime());
		List		results;
		BookedDay	bookedDay;
		
		results = timesheetDAO.getBookedHoursperDayInRange(new Integer(1), dateRange);
		
		assertEquals(6, results.size());
		
		bookedDay = (BookedDay)results.get(3);
		assertEquals(6.5, bookedDay.getHours().doubleValue(), 0.01);

		bookedDay = (BookedDay)results.get(2);
		assertEquals(-1, bookedDay.getHours().doubleValue(), 0.01);
	}
	
	@Test
	public void testGetTimesheetEntriesBefore()
	{
		List<TimesheetEntry> res = timesheetDAO.getTimesheetEntriesBefore(new ProjectAssignment(1), new Date(2006 - 1900, 10 - 1, 3));
		assertEquals(4, res.size());
	}	

	@Test
	public void testGetTimesheetEntriesAfter()
	{
		List<TimesheetEntry> res = timesheetDAO.getTimesheetEntriesAfter(new ProjectAssignment(1), new Date(2006 - 1900, 10 - 1, 4));
		
		assertEquals(3, res.size());
	}	
	
	
	@Test
	public void testGetLatestTimesheetEntryForAssignment()
	{
		TimesheetEntry entry = timesheetDAO.getLatestTimesheetEntryForAssignment(1);
		assertEquals(9.2f, entry.getHours(), 0.01f);
	}
	
	@Test
	public void testDeleteTimesheetEntries()
	{
		List ids = new ArrayList();
		ids.add(5);
		
		int deleted = timesheetDAO.deleteTimesheetEntries(ids);
		
		assertEquals(2, deleted);
	}
}
