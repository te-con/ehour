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

import java.util.Date;

/**
 * Object containing a range of dates 
 **/

public class DateRange
{
	private Date	dateStart;
	private	Date	dateEnd;

	public DateRange()
	{
	}
	
	public DateRange(Date dateStart, Date dateEnd)
	{
		this.dateStart = dateStart;
		this.dateEnd = dateEnd;
	}

	public Date getDateEnd()
	{
		return dateEnd;
	}

	public void setDateEnd(Date dateEnd)
	{
		this.dateEnd = dateEnd;
	}

	public Date getDateStart()
	{
		return dateStart;
	}

	public void setDateStart(Date dateStart)
	{
		this.dateStart = dateStart;
	}

	@Override
	public int hashCode()
	{
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((dateEnd == null) ? 0 : dateEnd.hashCode());
		result = PRIME * result + ((dateStart == null) ? 0 : dateStart.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final DateRange other = (DateRange) obj;
		if (dateEnd == null)
		{
			if (other.dateEnd != null)
				return false;
		} else if (!dateEnd.equals(other.dateEnd))
			return false;
		if (dateStart == null)
		{
			if (other.dateStart != null)
				return false;
		} else if (!dateStart.equals(other.dateStart))
			return false;
		return true;
	}
}
