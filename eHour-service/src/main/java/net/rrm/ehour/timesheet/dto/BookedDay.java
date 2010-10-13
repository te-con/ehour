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

package net.rrm.ehour.timesheet.dto;

import java.util.Date;

/**
 * Booked day dto
 **/

public class BookedDay
{
	private	Date	date;
	private	Number	hours;
	
	
	public BookedDay()
	{
		
	}
	
	public BookedDay(Date date, Number hours)
	{
		this.date = date;
		this.hours = hours;
	}
	
	public Date getDate()
	{
		return date;
	}
	public void setDate(Date date)
	{
		this.date = date;
	}
	public Number getHours()
	{
		return hours;
	}
	public void setHours(Number hours)
	{
		this.hours = hours;
	}
	

//	public boolean equals(Object other)
//	{
//		if ((this == other))
//			return true;
//		if (!(other instanceof BookedDay))
//			return false;
//		BookedDay castOther = (BookedDay) other;
//		return new EqualsBuilder().append(this.getDate(), castOther.getDate()).isEquals();
//	}
//
//	public int hashCode()
//	{
//		return new HashCodeBuilder().append(getDate())
//									.append(getHours()).toHashCode();
//	}	
}
