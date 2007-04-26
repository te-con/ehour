/**
 * Created on Nov 4, 2006
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

package net.rrm.ehour.data;

import java.io.Serializable;
import java.util.Date;

import net.rrm.ehour.util.DateUtil;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Object containing a range of dates.
 * Time is set to 00:00 for the start date and 23:59 for the date end therefore
 * including the start & the end 
 **/

public class DateRange implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4901436851703213753L;
	private Date	dateStart;
	private	Date	dateEnd;

	public DateRange()
	{
	}
	
	public DateRange(Date dateStart, Date dateEnd)
	{
		setDateStart(dateStart);
		setDateEnd(dateEnd);
	}

	/**
	 * Get date end
	 * @return
	 */
	public Date getDateEnd()
	{
		return dateEnd;
	}


	/**
	 * Is DateRange empty?
	 * @return
	 */
	public boolean isEmpty()
	{
		return dateStart == null && dateEnd == null;
	}
	
	/**
	 * Set the date end, time is set to 23:59:59.999
	 * @param dateEnd
	 */
	public final void setDateEnd(Date dateEnd)
	{
		if (dateEnd != null)
		{
			this.dateEnd = DateUtil.maximizeTime(dateEnd);
		}
	}

	/**
	 * 
	 * @return
	 */
	public Date getDateStart()
	{
		return dateStart;
	}

	/**
	 * Set the date start, time is set to 00:00
	 * @param dateStart
	 */
	public final void setDateStart(Date dateStart)
	{
		if (dateStart != null)
		{
			this.dateStart = DateUtil.nullifyTime(dateStart);
		}
	}
	
	public String toString()
	{
		return "date start: " + ((dateStart != null) ? dateStart.toString() : "null") 
				+ ", date end: " + ((dateEnd != null) ? dateEnd.toString() : "null");
	}

	/**
	 * @see java.lang.Object#equals(Object)
	 */
	@Override
	public boolean equals(Object object)
	{
		if (!(object instanceof DateRange))
		{
			return false;
		}
		DateRange rhs = (DateRange) object;
		return new EqualsBuilder().append(this.dateEnd, rhs.dateEnd).append(this.dateStart, rhs.dateStart).isEquals();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		return new HashCodeBuilder(213586715, -454293689).append(this.dateEnd).append(this.dateStart).toHashCode();
	}
}
