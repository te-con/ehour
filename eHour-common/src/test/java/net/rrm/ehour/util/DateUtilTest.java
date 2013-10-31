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

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.data.DateRange;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@SuppressWarnings("deprecation")
public class DateUtilTest {
    @Test
    public void testCalendarToMonthRange() {
        Calendar cal = new GregorianCalendar(2006, 11 - 1, 4);

        DateRange dr = DateUtil.calendarToMonthRange(cal);

        assertEquals(106, dr.getDateStart().getYear());
        assertEquals(10, dr.getDateStart().getMonth());
        assertEquals(1, dr.getDateStart().getDate());

        assertEquals(106, dr.getDateStart().getYear());
        assertEquals(10, dr.getDateEnd().getMonth());
        assertEquals(30, dr.getDateEnd().getDate());

        assertEquals(23, dr.getDateEnd().getHours());
        assertEquals(0, dr.getDateStart().getHours());

        assertEquals(4, cal.get(Calendar.DATE));
    }

    @Test
    public void testGetDaysInMonth() {
        Calendar cal = new GregorianCalendar(2006, 11 - 1, 4);
        assertEquals(30, DateUtil.getDaysInMonth(cal));

        cal = new GregorianCalendar(2007, 2 - 1, 5);
        assertEquals(28, DateUtil.getDaysInMonth(cal));
    }

    @Test
    public void testGetDaysInMonthJoda() {
        DateTime date = new DateTime(2006, 11, 4, 0, 0, 0, 0);
        assertEquals(30, DateUtil.getDaysInMonth(date));

        date = new DateTime(2007, 2, 5, 0, 0, 0, 0);
        assertEquals(28, DateUtil.getDaysInMonth(date));
    }

    @Test
    public void testIsDateWithinRange() {
        DateRange dr = new DateRange(new GregorianCalendar(2006, 5, 5).getTime(),
                new GregorianCalendar(2006, 6, 5).getTime());

        Date testCal = new GregorianCalendar(2006, 5, 10).getTime();
        assertTrue(DateUtil.isDateWithinRange(testCal, dr));

        testCal = new GregorianCalendar(2006, 4, 10).getTime();
        assertFalse(DateUtil.isDateWithinRange(testCal, dr));

        testCal = new GregorianCalendar(2006, 5, 5).getTime();
        assertTrue(DateUtil.isDateWithinRange(testCal, dr));

        testCal = new GregorianCalendar(2006, 6, 5).getTime();
        assertTrue(DateUtil.isDateWithinRange(testCal, dr));

        testCal = new GregorianCalendar(2006, 6, 6).getTime();
        assertFalse(DateUtil.isDateWithinRange(testCal, dr));

        testCal = new GregorianCalendar(2006, 5, 4).getTime();
        assertFalse(DateUtil.isDateWithinRange(testCal, dr));

    }

    @Test
    public void testIsDateRangeOverlaps() {
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

    @Test
    public void testIsDateRangeOverlapsNull() {
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

    @Test
    public void testGetDateRangeForWeek() {
        Calendar cal = new GregorianCalendar(2007, 1 - 1, 1);
        cal.setFirstDayOfWeek(Calendar.SUNDAY);
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
    @Test
    public void testNullifyTime() {
        Calendar cal = new GregorianCalendar();
        cal.set(Calendar.MINUTE, 10);
        DateUtil.nullifyTime(cal);

        assertEquals(0, cal.get(Calendar.MINUTE));
        assertEquals(0, cal.get(Calendar.HOUR_OF_DAY));
    }

    @Test
    public void testMaximizeTime() {
        Calendar cal = new GregorianCalendar();
        cal.set(Calendar.MINUTE, 10);
        DateUtil.maximizeTime(cal);

        assertEquals(59, cal.get(Calendar.MINUTE));
        assertEquals(23, cal.get(Calendar.HOUR_OF_DAY));
    }

    @Test
    public void testGetDateRangeForMonth() {
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
    public void testCreateCalendarSequence() {
        EhourConfig config = mock(EhourConfig.class);

        when(config.getFirstDayOfWeek()).thenReturn(1);
        when(config.getTzAsTimeZone()).thenReturn(TimeZone.getDefault());

        List<Date> res = DateUtil.createDateSequence(new DateRange(new Date(2007 - 1900, 5 - 1, 2), new Date(2007 - 1900, 5 - 1, 8)), config);

        assertEquals(7, res.size());
    }

    @Test
    public void testGetDateRangeForQuarter() {
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
    public void testAddQuarter() {
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
    public void should_get_proper_US_date_formatting_pattern() {
        assertEquals("M/d/yy", DateUtil.getPatternForDateLocale(Locale.US));
    }

    @Test
    public void should_get_proper_German_date_formatting_pattern() {
        assertEquals("dd.MM.yy", DateUtil.getPatternForDateLocale(Locale.GERMANY));
    }

    @Test
    public void should_get_proper_Chinese_date_formatting_pattern() {
        assertEquals("yy-M-d", DateUtil.getPatternForDateLocale(Locale.CHINA));
    }

    @Test
    public void should_get_proper_French_date_formatting_pattern() {
        assertEquals("dd/MM/yy", DateUtil.getPatternForDateLocale(Locale.FRANCE));
    }

    @Test
    public void should_get_proper_Japanese_date_formatting_pattern() {
        assertEquals("yy/MM/dd", DateUtil.getPatternForDateLocale(Locale.JAPAN));
    }


    @Test
    public void should_get_proper_Dutch_date_formatting_pattern() {
        assertEquals("d-M-yy", DateUtil.getPatternForDateLocale(new Locale("nl")));
    }

    @Test
    public void should_return_week_35_for_august_28_with_Sunday_as_first_day_of_week() {
        assertEquals(35, DateUtil.getWeekNumberForDate(new GregorianCalendar(2011, 7, 28).getTime(), Calendar.SUNDAY));
    }

    @Test
    public void should_return_week_35_for_august_29_with_Monday_as_first_day_of_week() {
        assertEquals(35, DateUtil.getWeekNumberForDate(new GregorianCalendar(2011, 7, 29).getTime(), Calendar.MONDAY));
    }

    @Test
    public void should_return_week_1_for_january_1_2012_with_Sunday_as_first_day_of_week() {
        assertEquals(1, DateUtil.getWeekNumberForDate(new GregorianCalendar(2012, Calendar.JANUARY, 1).getTime(), Calendar.SUNDAY));
    }

    @Test
    public void should_return_31_days_as_amount_of_days_in_January() {
        assertEquals(31, DateUtil.getDaysInMonth(new GregorianCalendar(2012, 0, 15)));
    }

    @Test
    public void should_return_29_days_as_amount_of_days_in_February_2012() {
        assertEquals(29, DateUtil.getDaysInMonth(new GregorianCalendar(2012, 1, 15)));
    }

    @Test
    public void should_convert_calendar_sunday_to_jodatime_sunday() {
        assertEquals(DateTimeConstants.SUNDAY, DateUtil.fromCalendarToJodaTimeDayInWeek(Calendar.SUNDAY));
    }

    @Test
    public void should_convert_calendar_thursday_to_jodatime_thursday() {
        assertEquals(DateTimeConstants.THURSDAY, DateUtil.fromCalendarToJodaTimeDayInWeek(Calendar.THURSDAY));
    }

    @Test
    public void should_have_date_in_range_with_open_end() {
        DateTime now = new DateTime();

        DateRange range = new DateRange(now.toDate(), null);

        assertTrue(DateUtil.isDateWithinRange(now.plusDays(1).toDate(), range));
    }

    @Test
    public void should_have_date_in_range_with_open_start() {
        DateTime now = new DateTime();

        DateRange range = new DateRange(null, now.plusDays(15).toDate());

        assertTrue(DateUtil.isDateWithinRange(now.plusDays(1).toDate(), range));
    }

    @Test
    public void should_format_date_with_local_pattern() {
        LocalDate date = new LocalDate(2013, 1, 2);

        String formatted = DateUtil.formatDate(date, Locale.ENGLISH);
        assertEquals("1/2/13", formatted);
    }
}
