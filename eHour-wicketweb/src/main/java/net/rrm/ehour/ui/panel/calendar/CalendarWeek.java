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

package net.rrm.ehour.ui.panel.calendar;

import java.io.Serializable;
import java.util.Date;

/**
 * Representation of a week
 **/

public class CalendarWeek implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6927161077692797646L;
	private	int		week;
	private	int		year;
	private Date	weekStart;
	
	private	int[]		days = new int[7];
	private	boolean[]	daysBooked = new boolean[7];
	
	public void setDayInWeek(int weekDay, int monthDay, boolean booked)
	{
		days[weekDay] = monthDay;
		daysBooked[weekDay] = booked;
	}
	
	/**
	 * @return the days
	 */
	public int[] getDays()
	{
		return days;
	}
	/**
	 * @param days the days to set
	 */
	public void setDays(int[] days)
	{
		this.days = days;
	}
	/**
	 * @return the daysBooked
	 */
	public boolean[] getDaysBooked()
	{
		return daysBooked;
	}
	/**
	 * @param daysBooked the daysBooked to set
	 */
	public void setDaysBooked(boolean[] daysBooked)
	{
		this.daysBooked = daysBooked;
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

	public Date getWeekStart()
	{
		return weekStart;
	}

	public void setWeekStart(Date weekStart)
	{
		this.weekStart = weekStart;
	}

}
