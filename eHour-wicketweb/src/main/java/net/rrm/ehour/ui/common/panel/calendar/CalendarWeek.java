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
 **/

public class CalendarWeek implements Serializable
{
	private static final long serialVersionUID = -6927161077692797646L;
	private	int		week;
	private	int		year;

	private Map<Integer, CalendarDay>	weekDays;

	private Calendar	weekStart;

	/**
	 * 
	 */
	public CalendarWeek()
	{
		weekDays = new HashMap<Integer, CalendarDay>();
	}

	/**
	 * 
	 * @param weekDay
	 * @param monthDay
	 * @param booked
	 */
	public void addDayInWeek(int weekDay, CalendarDay calendarDay)
	{
		weekDays.put(weekDay, calendarDay);
	}

	/**
	 * Get day 
	 * @param weekDay
	 * @return
	 */
	public CalendarDay getDay(int weekDay)
	{
		return weekDays.get(weekDay);
	}


	/**
	 * @return the week
	 */
	public int getWeek()
	{
		return week;
	}
	/**
	 * @param week the week to set
	 */
	public void setWeek(int week)
	{
		this.week = week;
	}
	/**
	 * @return the year
	 */
	public int getYear()
	{
		return year;
	}
	/**
	 * @param year the year to set
	 */
	public void setYear(int year)
	{
		this.year = year;
	}

	/**
	 * @return the weekStart
	 */
	public Calendar getWeekStart()
	{
		return weekStart;
	}

	/**
	 * @param weekStart the weekStart to set
	 */
	public void setWeekStart(Calendar weekStart)
	{
		this.weekStart = weekStart;
	}
}
