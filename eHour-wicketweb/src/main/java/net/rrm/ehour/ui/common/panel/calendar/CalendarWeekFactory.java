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

    public List<CalendarWeek> createWeeks(Integer firstDayOfWeek, Integer userId, Calendar calendar) {
        List<CalendarWeek> calendarWeeks = new ArrayList<CalendarWeek>();
        calendar.setFirstDayOfWeek(firstDayOfWeek);

        // grab date
        boolean[] bookedDays = getMonthNavCalendar(userId, calendar);

        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int currentMonth = calendar.get(Calendar.MONTH);

        CalendarWeek week = new CalendarWeek(ElementLocation.FIRST);
        int weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);
        System.out.println(weekOfYear);


        week.setWeek(weekOfYear);

        if (weekOfYear > 51 && currentMonth == Calendar.JANUARY) {
            week.setYear(calendar.get(Calendar.YEAR) - 1);
        } else {
            week.setYear(calendar.get(Calendar.YEAR));
        }
        week.setWeekStart((Calendar) calendar.clone());

        DateUtil.dayOfWeekFix(week.getWeekStart());
        week.getWeekStart().set(Calendar.DAY_OF_WEEK, firstDayOfWeek);

        int previousWeek = -1;

        do {
            int dayInMonth = calendar.get(Calendar.DAY_OF_MONTH);
            int dayInWeek = calendar.get(Calendar.DAY_OF_WEEK);

            CalendarDay day = new CalendarDay(dayInMonth, bookedDays[dayInMonth - 1]);

            week.addDayInWeek(dayInWeek, day);

            calendar.add(Calendar.DAY_OF_MONTH, 1);

            // next week? add current week and create a new one
            if (calendar.get(Calendar.DAY_OF_WEEK) == firstDayOfWeek) {
                calendarWeeks.add(week);

                week = new CalendarWeek(ElementLocation.MIDDLE);
                week.setWeek(calendar.get(Calendar.WEEK_OF_YEAR));
                week.setWeekStart((Calendar) calendar.clone());

                // fix that the year is still the old year but the week is already in the next year
                if (previousWeek != -1 && previousWeek > calendar.get(Calendar.WEEK_OF_YEAR)) {
                    week.setYear(calendar.get(Calendar.YEAR) + 1);
                } else {
                    week.setYear(calendar.get(Calendar.YEAR));
                }

                previousWeek = calendar.get(Calendar.WEEK_OF_YEAR);
            }

        } while (calendar.get(Calendar.MONTH) == currentMonth);

        // first day of week is already stored
        if (calendar.get(Calendar.DAY_OF_WEEK) != firstDayOfWeek) {
            calendarWeeks.add(week);
        }

        calendarWeeks.get(calendarWeeks.size() - 1).setLocation(ElementLocation.LAST);

        return calendarWeeks;
    }


    private boolean[] getMonthNavCalendar(Integer userId, Calendar requestedMonth) {
        List<BookedDay> bookedDays = getTimesheetService().getBookedDaysMonthOverview(userId, requestedMonth);

        boolean[] monthOverview = new boolean[DateUtil.getDaysInMonth(requestedMonth)];

        for (BookedDay day : bookedDays) {
            Calendar cal = new GregorianCalendar();
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
