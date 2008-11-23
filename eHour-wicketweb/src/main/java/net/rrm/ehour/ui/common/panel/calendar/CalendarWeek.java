/**
 * Created on Jun 13, 2007
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
 *
 * This Software is copyright TE-CON 2007. This Software is not open source by definition. The source of the Software is available for educational purposes.
 * TE-CON holds all the ownership rights on the Software.
 * TE-CON freely grants the right to use the Software. Any reproduction or modification of this Software, whether for commercial use or open source,
 * is subject to obtaining the prior express authorization of TE-CON.
 *
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
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
