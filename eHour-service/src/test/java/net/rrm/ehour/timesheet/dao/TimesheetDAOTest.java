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

package net.rrm.ehour.timesheet.dao;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import net.rrm.ehour.dao.AbstractDaoTest;
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
public class TimesheetDAOTest extends AbstractDaoTest 
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
