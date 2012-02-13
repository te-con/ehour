package net.rrm.ehour.ui.common.panel.calendar;

import net.rrm.ehour.timesheet.dto.BookedDay;
import net.rrm.ehour.timesheet.service.TimesheetService;
import net.rrm.ehour.ui.common.util.WebUtils;
import net.rrm.ehour.util.DateUtil;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static java.util.Calendar.DAY_OF_MONTH;

public class CalendarWeekFactory implements Serializable {
    @SpringBean
    private TimesheetService timesheetService;

    public CalendarWeekFactory() {
    }

    // junit testing
    protected CalendarWeekFactory(TimesheetService timesheetService) {
        this.timesheetService = timesheetService;
    }

    public List<CalendarWeek> createWeeks(Integer firstDayOfWeek, Integer userId, Calendar dateIterator) {
        List<CalendarWeek> calendarWeeks = new ArrayList<CalendarWeek>();
        boolean[] bookedDays;
        CalendarWeek week;

        // grab date
        bookedDays = getMonthNavCalendar(userId, dateIterator);

        dateIterator.set(Calendar.DAY_OF_MONTH, 1);
        int currentMonth = dateIterator.get(Calendar.MONTH);

        week = new CalendarWeek(ElementLocation.FIRST);
        int weekOfYear = dateIterator.get(Calendar.WEEK_OF_YEAR);
        week.setWeek(weekOfYear);

        if (weekOfYear > 51 && currentMonth == Calendar.JANUARY) {
            week.setYear(dateIterator.get(Calendar.YEAR) - 1);
        } else {
            week.setYear(dateIterator.get(Calendar.YEAR));
        }
        week.setWeekStart((Calendar) dateIterator.clone());

        DateUtil.dayOfWeekFix(week.getWeekStart());
        week.getWeekStart().set(Calendar.DAY_OF_WEEK, firstDayOfWeek);

        int previousWeek = -1;

        do {
            int dayInMonth = dateIterator.get(Calendar.DAY_OF_MONTH);
            int dayInWeek = dateIterator.get(Calendar.DAY_OF_WEEK);
            boolean inWeekend = (dayInWeek == Calendar.SUNDAY || dayInWeek == Calendar.SATURDAY);

            CalendarDay day = new CalendarDay(dayInMonth, bookedDays[dayInMonth - 1], inWeekend);

            week.addDayInWeek(dayInWeek, day);

            dateIterator.add(Calendar.DAY_OF_MONTH, 1);

            // next week? add current week and create a new one
            if (dateIterator.get(Calendar.DAY_OF_WEEK) == firstDayOfWeek) {
                calendarWeeks.add(week);

                week = new CalendarWeek(ElementLocation.MIDDLE);
                week.setWeek(dateIterator.get(Calendar.WEEK_OF_YEAR));
                week.setWeekStart((Calendar) dateIterator.clone());

                // fix that the year is still the old year but the week is already in the next year
                if (previousWeek != -1 && previousWeek > dateIterator.get(Calendar.WEEK_OF_YEAR)) {
                    week.setYear(dateIterator.get(Calendar.YEAR) + 1);
                } else {
                    week.setYear(dateIterator.get(Calendar.YEAR));
                }

                previousWeek = dateIterator.get(Calendar.WEEK_OF_YEAR);
            }

        } while (dateIterator.get(Calendar.MONTH) == currentMonth);

        // first day of week is already stored
        if (dateIterator.get(Calendar.DAY_OF_WEEK) != firstDayOfWeek) {
            calendarWeeks.add(week);
        }

        calendarWeeks.get(calendarWeeks.size() - 1).setLocation(ElementLocation.LAST);

        return calendarWeeks;
    }


    private boolean[] getMonthNavCalendar(Integer userId, Calendar requestedMonth) {
        List<BookedDay> bookedDays;
        boolean[] monthOverview;
        Calendar cal;

        bookedDays = getTimesheetService().getBookedDaysMonthOverview(userId, requestedMonth);

        monthOverview = new boolean[DateUtil.getDaysInMonth(requestedMonth)];

        for (BookedDay day : bookedDays) {
            cal = new GregorianCalendar();
            cal.setTime(day.getDate());

            // just in case.. it shouldn't happen that the returned month
            // is longer than the reserved space for this month
            if (cal.get(DAY_OF_MONTH) <= monthOverview.length) {
                monthOverview[cal.get(DAY_OF_MONTH) - 1] = true;
            }
        }

        return monthOverview;
    }

    private TimesheetService getTimesheetService() {
        if (timesheetService == null) {
            WebUtils.springInjection(this);
        }
        return timesheetService;
    }
}
