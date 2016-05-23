package net.rrm.ehour.ui.common.panel.calendar;

import net.rrm.ehour.timesheet.service.IOverviewTimesheet;
import net.rrm.ehour.ui.common.util.WebUtils;
import net.rrm.ehour.util.DateUtil;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.joda.time.LocalDate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CalendarWeekFactory implements Serializable {
    @SpringBean
    private IOverviewTimesheet overviewTimesheet;

    public CalendarWeekFactory() {
    }

    // junit testing
    protected CalendarWeekFactory(IOverviewTimesheet overviewTimesheet) {
        this.overviewTimesheet = overviewTimesheet;
    }

    public List<CalendarWeek> createWeeks(Integer firstDayOfWeek, Integer userId, Calendar thisMonth) {
        List<CalendarWeek> calendarWeeks = new ArrayList<>();
        thisMonth.setFirstDayOfWeek(firstDayOfWeek);

        List<LocalDate> bookedDays = getOverviewTimesheet().getBookedDaysMonthOverview(userId, thisMonth);

        thisMonth.set(Calendar.DAY_OF_MONTH, 1);

        // could have passed in calendar as well but than equals does not work anymore
        LocalDate thisMonthLocal = new LocalDate(thisMonth.getTime());

        int currentMonth = thisMonth.get(Calendar.MONTH);

        CalendarWeek week = new CalendarWeek(ElementLocation.FIRST);
        int weekOfYear = thisMonth.get(Calendar.WEEK_OF_YEAR);

        week.setWeek(weekOfYear);

        if (weekOfYear > 51 && currentMonth == Calendar.JANUARY) {
            week.setYear(thisMonth.get(Calendar.YEAR) - 1);
        } else {
            week.setYear(thisMonth.get(Calendar.YEAR));
        }
        week.setWeekStart((Calendar) thisMonth.clone());

        DateUtil.dayOfWeekFix(week.getWeekStart());
        week.getWeekStart().set(Calendar.DAY_OF_WEEK, firstDayOfWeek);

        int previousWeek = -1;

        do {
            int dayInMonth = thisMonth.get(Calendar.DAY_OF_MONTH);
            int dayInWeek = thisMonth.get(Calendar.DAY_OF_WEEK);

            boolean isBooked = bookedDays.contains(thisMonthLocal.withDayOfMonth(dayInMonth));

            CalendarDay day = new CalendarDay(dayInMonth, isBooked);

            week.addDayInWeek(dayInWeek, day);

            thisMonth.add(Calendar.DAY_OF_MONTH, 1);

            // next week? add current week and create a new one
            if (thisMonth.get(Calendar.DAY_OF_WEEK) == firstDayOfWeek) {
                calendarWeeks.add(week);

                week = new CalendarWeek(ElementLocation.MIDDLE);
                week.setWeek(thisMonth.get(Calendar.WEEK_OF_YEAR));
                week.setWeekStart((Calendar) thisMonth.clone());

                // fix that the year is still the old year but the week is already in the next year
                if (previousWeek != -1 && previousWeek > thisMonth.get(Calendar.WEEK_OF_YEAR)) {
                    week.setYear(thisMonth.get(Calendar.YEAR) + 1);
                } else {
                    week.setYear(thisMonth.get(Calendar.YEAR));
                }

                previousWeek = thisMonth.get(Calendar.WEEK_OF_YEAR);
            }

        } while (thisMonth.get(Calendar.MONTH) == currentMonth);

        // first day of week is already stored
        if (thisMonth.get(Calendar.DAY_OF_WEEK) != firstDayOfWeek) {
            calendarWeeks.add(week);
        }

        calendarWeeks.get(calendarWeeks.size() - 1).setLocation(ElementLocation.LAST);

        return calendarWeeks;
    }

    private IOverviewTimesheet getOverviewTimesheet() {
        if (overviewTimesheet == null) {
            WebUtils.springInjection(this);
        }
        return overviewTimesheet;
    }
}
