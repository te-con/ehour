/**
 * Created on Jun 13, 2007
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.ui.panel.calendar;

import java.io.Serializable;

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

}
