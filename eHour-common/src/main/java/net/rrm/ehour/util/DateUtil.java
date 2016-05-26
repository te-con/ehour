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
import org.joda.time.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Various static methods for date manipulation
 */

public class DateUtil {
    /**
     * Do not instantiate
     */
    private DateUtil() {
    }

    /**
     * Get week number for date but compensate with configured first day of week.
     * Week officially starts on monday but when it's configured to start on sunday we have to compensate
     * because the day falls in the previous week.
     */
    public static int getWeekNumberForDate(Date date, int configuredFirstDayOfWeek) {
        DateTime dateTime = new DateTime(date);

        return configuredFirstDayOfWeek == Calendar.SUNDAY ? dateTime.plusDays(1).getWeekOfWeekyear() : dateTime.getWeekOfWeekyear();
    }

    /**
     * wtf is this??
     */
    public static void dayOfWeekFix(Calendar calendar) {
        calendar.add(Calendar.DATE, 1);
        calendar.add(Calendar.DATE, -1);
    }

    /**
     * Is date a weekend day?
     *
     * @param calendar
     * @return
     */
    public static boolean isWeekend(Calendar calendar) {
        int dayInWeek = calendar.get(Calendar.DAY_OF_WEEK);

        return dayInWeek == Calendar.SATURDAY || dayInWeek == Calendar.SUNDAY;
    }

    /**
     * Get days in month
     *
     * @param calendar
     * @return
     */
    public static int getDaysInMonth(Calendar calendar) {
        return getDaysInMonth(new DateTime(calendar));
    }

    /**
     * Get days in month
     *
     * @param calendar
     * @return
     */
    public static int getDaysInMonth(DateTime calendar) {
        return calendar.dayOfMonth().getMaximumValue();
    }


    /**
     * Converts a calendar to a range covering that month
     *
     * @return first Calendar object is start of the month, last Calendar object is end of the month
     */
    public static DateRange calendarToMonthRange(Calendar calendar) {

        DateTime date = new DateTime(calendar);

        return new DateRange(new Interval(date.withDayOfMonth(1), date.withDayOfMonth(getDaysInMonth(date))));
    }

    /**
     * Check whether a date is within range
     *
     * @return
     */

    public static boolean isDateWithinRange(Date date, DateRange dateRange) {
        return dateRange.isOpenEnded() || isDateWithinRange(new DateTime(date), dateRange);
    }

    public static boolean isDateWithinRange(DateTime date, DateRange dateRange) {
        return dateRange.isOpenEnded() || dateRange.toInterval().contains(date);
    }

    /**
     * Is calendar within date range ?
     *
     * @param calendar
     * @param dateRange
     * @return
     */
    public static boolean isDateWithinRange(Calendar calendar, DateRange dateRange) {
        return DateUtil.isDateWithinRange(calendar.getTime(), dateRange);
    }

    /**
     * Check whether two dateranges overlap eachother
     */
    public static boolean isDateRangeOverlaps(DateRange rangeA, DateRange rangeB) {
        boolean overlaps;

        boolean includeStart;
        boolean includeEnd;

        includeStart = rangeA.getDateStart() != null;
        includeEnd = rangeA.getDateEnd() != null;

        if (includeStart && includeEnd) {
            overlaps = rangeA.toInterval().overlaps(rangeB.toInterval());
        } else if (!includeStart && includeEnd) {
            overlaps = rangeB.getDateStart().before(rangeA.getDateEnd());
        } else if (includeStart && !includeEnd) {
            overlaps = rangeB.getDateEnd().after(rangeA.getDateStart());
        } else {
            overlaps = true;
        }

        return overlaps;
    }

    /**
     * Get a date range covering the week the supplied calendar is in
     *
     * @param calendar
     * @return
     */
    public static DateRange getDateRangeForWeek(Calendar calendar) {
        DateRange weekRange = new DateRange();
        Calendar calClone = (Calendar) calendar.clone();

        DateUtil.dayOfWeekFix(calClone);
        calClone.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        weekRange.setDateStart(calClone.getTime());

        calClone.add(Calendar.DAY_OF_MONTH, +6);
        weekRange.setDateEnd(calClone.getTime());

        return weekRange;
    }

    public static DateRange getDateRangeForMonth(Date date) {
        return getDateRangeForMonth(getCalendar(date));
    }

    /**
     * Get a date range covering the month the supplied calendar is in
     *
     * @param calendar
     * @return
     */
    public static DateRange getDateRangeForMonth(Calendar calendar) {
        DateRange monthRange = new DateRange();

        Calendar calClone = (Calendar) calendar.clone();

        calClone.set(Calendar.DAY_OF_MONTH, 1);
        monthRange.setDateStart(calClone.getTime());

        calClone.add(Calendar.MONTH, 1);
        calClone.add(Calendar.DATE, -1);
        monthRange.setDateEnd(calClone.getTime());

        return monthRange;
    }

    /**
     * Get date range for quarter
     *
     * @param calendar
     * @return
     */
    public static DateRange getDateRangeForQuarter(Calendar calendar) {
        DateRange quarterRange;
        Calendar startMonth, endMonth;

        int month = calendar.get(Calendar.MONTH);
        int quarter = month / 3;

        startMonth = new GregorianCalendar(calendar.get(Calendar.YEAR), quarter * 3, 1);
        endMonth = new GregorianCalendar(calendar.get(Calendar.YEAR), quarter * 3, 1);
        endMonth.add(Calendar.MONTH, 3);
        endMonth.add(Calendar.DATE, -1);

        quarterRange = new DateRange(startMonth.getTime(), endMonth.getTime());

        return quarterRange;
    }

    /**
     * Add quarter to current
     *
     * @param calendar
     * @param quarterDelta
     * @return
     */
    public static Calendar addQuarter(Calendar calendar, int quarterDelta) {
        int month = calendar.get(Calendar.MONTH);
        int quarter = month / 3;

        Calendar startMonth = new GregorianCalendar(calendar.get(Calendar.YEAR), quarter * 3, 1);
        startMonth.add(Calendar.MONTH, quarterDelta * 3);

        return startMonth;
    }


    /**
     * Set the time of a date to 00:00.00 (up to the ms)
     *
     * @param date
     * @return
     */

    public static Date nullifyTime(Date date) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        nullifyTime(cal);

        return cal.getTime();
    }

    /**
     * Set the time of a calendar to 00:00.00 (up to the ms)
     *
     * @param cal
     * @return
     */
    public static void nullifyTime(Calendar cal) {
        // #1753473: thx to Uriah/umlsdl for pointing this out
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
    }

    /**
     * Set the time of a calendar to 23:59:59.999
     *
     * @param cal
     */
    public static void maximizeTime(Calendar cal) {
        // #1753473: thx to Uriah/umlsdl for pointing this out
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
    }

    /**
     * Set the time of a date to 23:59:59.999
     */
    public static Date maximizeTime(Date date) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        maximizeTime(cal);

        return cal.getTime();
    }

    /**
     * Create a sequence of dates from the date range
     *
     * @return
     */
    public static List<Date> createDateSequence(DateRange range, EhourConfig config) {
        List<Date> dateSequence = new ArrayList<>();
        Calendar calendar;

        calendar = DateUtil.getCalendar(config);
        calendar.setTime(range.getDateStart());

        while (calendar.getTime().before(range.getDateEnd())) {
            DateUtil.nullifyTime(calendar);
            dateSequence.add(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        return dateSequence;
    }

    /**
     * Get calendar with timezone set
     *
     * @param config
     * @return
     */
    public static Calendar getCalendar(EhourConfig config) {
        Calendar calendar = new GregorianCalendar(DateTimeZone.getDefault().toTimeZone());
        calendar.setFirstDayOfWeek(config.getFirstDayOfWeek());
        return calendar;
    }

    /**
     * Convert date to calendar (why didn't I use Jodatime from the beginning, argh!)
     *
     * @param date
     * @return
     */
    public static Calendar getCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return calendar;
    }


    /**
     * Get the dd/mm/yyyy date formatting pattern for a given locale
     * This is a bit of a hack..
     *
     * @param dateLocale
     * @return the pattern using DateFormatSymbols
     */
    public static String getPatternForDateLocale(Locale dateLocale) {
        SimpleDateFormat dateFormat = (SimpleDateFormat) (SimpleDateFormat.getDateInstance(DateFormat.SHORT, dateLocale));
        return dateFormat.toPattern();
    }

    public static String formatDate(Date date, Locale dateLocale) {
        return SimpleDateFormat.getDateInstance(DateFormat.SHORT, dateLocale).format(date);
    }

    public static String formatDate(DateTime date, Locale dateLocale) {
        return formatDate(date.toDate(), dateLocale);
    }

    public static String formatDate(LocalDate date, Locale dateLocale) {
        return formatDate(date.toDateTimeAtStartOfDay(), dateLocale);
    }


    /**
     * java.util.Calendar.SUNDAY = 1 while in jodatime it's day 7.
     *
     * @return
     */
    public static int fromCalendarToJodaTimeDayInWeek(int dayInWeek) {
        int jodatimeDayInWeek = dayInWeek - 1;

        return jodatimeDayInWeek == 0 ? DateTimeConstants.SUNDAY : jodatimeDayInWeek;
    }
}