/**
 * Created on Dec 17, 2006
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
