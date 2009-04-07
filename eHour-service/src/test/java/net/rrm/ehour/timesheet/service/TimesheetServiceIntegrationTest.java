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

package net.rrm.ehour.timesheet.service;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import net.rrm.ehour.AbstractServiceTest;
import net.rrm.ehour.exception.ObjectNotFoundException;
import net.rrm.ehour.timesheet.dto.BookedDay;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * TODO 
 **/

@RunWith(SpringJUnit4ClassRunner.class)
public class TimesheetServiceIntegrationTest extends AbstractServiceTest
{
	@Autowired
	private TimesheetService	timesheetService;
	

	/**
	 * Test method for {@link net.rrm.ehour.timesheet.service.TimesheetServiceImpl#getBookedDaysMonthOverview(java.lang.Integer, java.util.Calendar)}.
	 */
	@Test
	public void testGetBookedDaysMonthOverview() throws ObjectNotFoundException
	{
		Calendar	cal = new GregorianCalendar(2006, 10 - 1, 1);
		List<BookedDay> res = timesheetService.getBookedDaysMonthOverview(1, cal);
		
		assertEquals(8.0, res.get(0).getHours());
		assertEquals(9.0, res.get(2).getHours());
		assertEquals(9.2f, res.get(3).getHours().floatValue(), 0.1f);
	}

}
