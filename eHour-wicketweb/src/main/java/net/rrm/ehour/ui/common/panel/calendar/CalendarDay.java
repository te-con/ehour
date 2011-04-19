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

public class CalendarDay implements Serializable
{
	private static final long serialVersionUID = 1L;
	private int monthDay;
	private boolean booked;


	public CalendarDay(int monthDay, boolean booked, boolean weekendDay)
	{
		this.monthDay = monthDay;
		this.booked = booked;
	}

	/**
	 * @return the monthDay
	 */
	public int getMonthDay()
	{
		return monthDay;
	}

	/**
	 * @return the booked
	 */
	public boolean isBooked()
	{
		return booked;
	}

}
