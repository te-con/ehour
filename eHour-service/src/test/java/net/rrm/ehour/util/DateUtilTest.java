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

package net.rrm.ehour.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import junit.framework.TestCase;
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.service.config.EhourConfigStub;

import org.joda.time.DateTime;
import org.junit.Test;

@SuppressWarnings("deprecation")
public class DateUtilTest extends TestCase
{

	public void testCalendarToMonthRange()
	{
		Calendar cal = new GregorianCalendar(2006, 11 -1 , 4);
		
		DateRange dr = DateUtil.calendarToMonthRange(cal);
		
		assertEquals(106, dr.getDateStart().getYear());
		assertEquals(10, dr.getDateStart().getMonth());
		assertEquals(1, dr.getDateStart().getDate());

		assertEquals(106, dr.getDateStart().getYear());
		assertEquals(10, dr.getDateEnd().getMonth());
		assertEquals(30, dr.getDateEnd().getDate());

		assertEquals(4, cal.get(Calendar.DATE));
	}
	
	public void testGetDaysInMonth()
	{
		Calendar cal = new GregorianCalendar(2006, 11 -1 , 4);
		assertEquals(30, DateUtil.getDaysInMonth(cal));
		
		cal = new GregorianCalendar(2007, 2 - 1, 5);
		assertEquals(28, DateUtil.getDaysInMonth(cal));
	}
	
	public void testGetDaysInMonthJoda()
	{
		DateTime date = new DateTime(2006, 11 , 4, 0, 0, 0, 0);
		assertEquals(30, DateUtil.getDaysInMonth(date));
		
		date = new DateTime(2007, 2, 5, 0, 0, 0, 0);
		assertEquals(28, DateUtil.getDaysInMonth(date));
	}	

	public void testIsDateWithinRange()
	{
		boolean	inRange;
		DateRange dr = new DateRange(new GregorianCalendar(2006, 5, 5).getTime(),
						 			new GregorianCalendar(2006, 6, 5).getTime());
		
		Date testCal = new GregorianCalendar(2006, 5, 10).getTime();
		inRange = DateUtil.isDateWithinRange(testCal, dr);
		assertTrue(inRange);

		testCal = new GregorianCalendar(2006, 4, 10).getTime();
		inRange = DateUtil.isDateWithinRange(testCal, dr);
		assertFalse(inRange);

		testCal = new GregorianCalendar(2006, 5, 5).getTime();
		inRange = DateUtil.isDateWithinRange(testCal, dr);
		assertTrue(inRange);

		testCal = new GregorianCalendar(2006, 6, 5).getTime();
		inRange = DateUtil.isDateWithinRange(testCal, dr);
		assertTrue(inRange);

		testCal = new GregorianCalendar(2006, 6, 6).getTime();
		inRange = DateUtil.isDateWithinRange(testCal, dr);
		assertFalse(inRange);

		testCal = new GregorianCalendar(2006, 5, 4).getTime();
		inRange = DateUtil.isDateWithinRange(testCal, dr);
		assertFalse(inRange);
		
	}
	
	public void testIsDateRangeOverlaps()
	{
		DateRange rangeA = new DateRange(new GregorianCalendar(2006, 5, 5).getTime(),
	 									new GregorianCalendar(2006, 6, 5).getTime());
		
		DateRange rangeB = new DateRange(new GregorianCalendar(2007, 5, 5).getTime(),
	 									new GregorianCalendar(2007, 6, 5).getTime());
		
		// rangeB after rangeA
		assertFalse(DateUtil.isDateRangeOverlaps(rangeA, rangeB));
		
		// rangeB overlaps after rangeA
		rangeB = new DateRange(new GregorianCalendar(2006, 5, 20).getTime(),
								new GregorianCalendar(2007, 6, 5).getTime());
		assertTrue(DateUtil.isDateRangeOverlaps(rangeA, rangeB));
		assertTrue(DateUtil.isDateRangeOverlaps(rangeB, rangeA));

		// rangeB in between rangeA
		rangeB = new DateRange(new GregorianCalendar(2006, 5, 20).getTime(),
								new GregorianCalendar(2006, 5, 25).getTime());
		assertTrue(DateUtil.isDateRangeOverlaps(rangeA, rangeB));
		assertTrue(DateUtil.isDateRangeOverlaps(rangeB, rangeA));
		
		// rangeB overlaps before rangeA
		rangeB = new DateRange(new GregorianCalendar(2006, 4, 20).getTime(),
								new GregorianCalendar(2006, 5, 25).getTime());
		assertTrue(DateUtil.isDateRangeOverlaps(rangeA, rangeB));
		assertTrue(DateUtil.isDateRangeOverlaps(rangeB, rangeA));

		// rangeB before rangeA
		rangeB = new DateRange(new GregorianCalendar(2006, 4, 20).getTime(),
								new GregorianCalendar(2006, 4, 30).getTime());
		assertFalse(DateUtil.isDateRangeOverlaps(rangeA, rangeB));
		assertFalse(DateUtil.isDateRangeOverlaps(rangeB, rangeA));

		// rangeB overlaps on date
		rangeB = new DateRange(new GregorianCalendar(2006, 4, 20).getTime(),
								new GregorianCalendar(2006, 5, 5).getTime());
		assertTrue(DateUtil.isDateRangeOverlaps(rangeA, rangeB));
		assertTrue(DateUtil.isDateRangeOverlaps(rangeB, rangeA));
	}
	
	public void testIsDateRangeOverlapsNull()
	{
		DateRange rangeA = new DateRange(null,
	 									new GregorianCalendar(2006, 6, 5).getTime());
		
		
		// rangeB after rangeA
		DateRange rangeB = new DateRange(new GregorianCalendar(2007, 5, 5).getTime(),
											new GregorianCalendar(2007, 6, 5).getTime());
		assertFalse(DateUtil.isDateRangeOverlaps(rangeA, rangeB));
		
		// rangeB overlaps after rangeA
		rangeB = new DateRange(new GregorianCalendar(2006, 5, 20).getTime(),
								new GregorianCalendar(2007, 6, 5).getTime());
		assertTrue(DateUtil.isDateRangeOverlaps(rangeA, rangeB));

		// rangeB in between rangeA
		rangeB = new DateRange(new GregorianCalendar(2006, 5, 20).getTime(),
								new GregorianCalendar(2006, 5, 25).getTime());
		assertTrue(DateUtil.isDateRangeOverlaps(rangeA, rangeB));
		
		// rangeB overlaps before rangeA
		rangeB = new DateRange(new GregorianCalendar(2006, 4, 20).getTime(),
								new GregorianCalendar(2006, 5, 25).getTime());
		assertTrue(DateUtil.isDateRangeOverlaps(rangeA, rangeB));

		// rangeB before rangeA
		rangeB = new DateRange(new GregorianCalendar(2006, 4, 20).getTime(),
								new GregorianCalendar(2006, 4, 30).getTime());
		assertTrue(DateUtil.isDateRangeOverlaps(rangeA, rangeB));

		// rangeB overlaps on date
		rangeB = new DateRange(new GregorianCalendar(2006, 4, 20).getTime(),
								new GregorianCalendar(2006, 5, 5).getTime());
		assertTrue(DateUtil.isDateRangeOverlaps(rangeA, rangeB));
	}	
	
	/**
	 * 
	 *
	 */
	public void testGetDateRangeForWeek()
	{
		Calendar cal = new GregorianCalendar(2007, 1 - 1, 1);
		DateRange range = DateUtil.getDateRangeForWeek(cal);
		
		assertEquals(2006 - 1900, range.getDateStart().getYear());
		assertEquals(12 - 1, range.getDateStart().getMonth());
		assertEquals(31, range.getDateStart().getDate());

		assertEquals(2007 - 1900, range.getDateEnd().getYear());
		assertEquals(1 - 1, range.getDateEnd().getMonth());
		assertEquals(6, range.getDateEnd().getDate());
	}

	/**
	 * 
	 *
	 */
	public void testNullifyTime()
	{
		Calendar cal = new GregorianCalendar();
		cal.set(Calendar.MINUTE, 10);
		DateUtil.nullifyTime(cal);
		
		assertEquals(0, cal.get(Calendar.MINUTE));
		assertEquals(0, cal.get(Calendar.HOUR_OF_DAY));
	}
	
	public void testMaximizeTime()
	{
		Calendar cal = new GregorianCalendar();
		cal.set(Calendar.MINUTE, 10);
		DateUtil.maximizeTime(cal);
		
		assertEquals(59, cal.get(Calendar.MINUTE));
		assertEquals(23, cal.get(Calendar.HOUR_OF_DAY));
	}	
	
	/**
	 * 
	 *
	 */
	public void testGetDateRangeForMonth()
	{
		Calendar cal = new GregorianCalendar(2006, 12 - 1, 5);
		DateRange range = DateUtil.getDateRangeForMonth(cal);
		
		assertEquals(2006 - 1900, range.getDateStart().getYear());
		assertEquals(12 - 1, range.getDateStart().getMonth());
		assertEquals(1, range.getDateStart().getDate());

		assertEquals(2006 - 1900, range.getDateEnd().getYear());
		assertEquals(12 - 1, range.getDateEnd().getMonth());
		assertEquals(31, range.getDateEnd().getDate());
	}

	
	@Test
	public void testCreateCalendarSequence()
	{
		List<Date> res = DateUtil.createDateSequence(new DateRange(new Date(2007 - 1900, 5 - 1, 2),new Date(2007 - 1900, 5 - 1, 8)), new EhourConfigStub());
		
		assertEquals(7, res.size());
	}	
	
	@Test
	public void testGetDateRangeForQuarter()
	{
		Calendar cal = new GregorianCalendar(2007, 12 - 1, 5);
		DateRange range = DateUtil.getDateRangeForQuarter(cal);
		
		assertEquals(2007 - 1900, range.getDateStart().getYear());
		assertEquals(10 - 1, range.getDateStart().getMonth());
		assertEquals(1, range.getDateStart().getDate());

		assertEquals(2007 - 1900, range.getDateEnd().getYear());
		assertEquals(12 - 1, range.getDateEnd().getMonth());
		assertEquals(31, range.getDateEnd().getDate());

		cal = new GregorianCalendar(2007, 5 - 1, 5);
		range = DateUtil.getDateRangeForQuarter(cal);
		
		assertEquals(2007 - 1900, range.getDateStart().getYear());
		assertEquals(4 - 1, range.getDateStart().getMonth());
		assertEquals(1, range.getDateStart().getDate());

		assertEquals(2007 - 1900, range.getDateEnd().getYear());
		assertEquals(6 - 1, range.getDateEnd().getMonth());
		assertEquals(30, range.getDateEnd().getDate());
	}
	
	@Test
	public void testAddQuarter()
	{
		Calendar cal = new GregorianCalendar(2007, 12 - 1, 5);

		Calendar q = DateUtil.addQuarter(cal, 1);
		
		assertEquals(2008, q.get(Calendar.YEAR));
		assertEquals(0, q.get(Calendar.MONTH));
		assertEquals(1, q.get(Calendar.DATE));

		cal = new GregorianCalendar(2007, 12 - 1, 5);
		q = DateUtil.addQuarter(cal, -2);

		assertEquals(2007, q.get(Calendar.YEAR));
		assertEquals(4 - 1, q.get(Calendar.MONTH));
		assertEquals(1, q.get(Calendar.DATE));
	}
	
	@Test
	public void testGetPatternForDateLocale()
	{
		assertEquals("MM/dd/yyyy", DateUtil.getPatternForDateLocale(Locale.US));
		assertEquals("dd.MM.yyyy", DateUtil.getPatternForDateLocale(Locale.GERMANY));
	}
}
