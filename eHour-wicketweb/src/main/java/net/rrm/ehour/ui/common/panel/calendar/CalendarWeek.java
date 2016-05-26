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

package net.rrm.ehour.ui.common.panel.calendar;

import java.io.Serializable;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Representation of a week
 */

public class CalendarWeek implements Serializable {
    private static final long serialVersionUID = -6927161077692797646L;

    private int week;
    private int year;

    private Map<Integer, CalendarDay> weekDays;

    private Calendar weekStart;
    private ElementLocation location;

    public CalendarWeek(ElementLocation location) {
        this.location = location;
        weekDays = new HashMap<>();
    }

    public void addDayInWeek(int weekDay, CalendarDay calendarDay) {
        weekDays.put(weekDay, calendarDay);
    }

    public CalendarDay getDay(int weekDay) {
        return weekDays.get(weekDay);
    }

    public void setLocation(ElementLocation location) {
        this.location = location;
    }

    public ElementLocation getLocation() {
        return location;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Calendar getWeekStart() {
        return weekStart;
    }

    public void setWeekStart(Calendar weekStart) {
        this.weekStart = weekStart;
    }
}
