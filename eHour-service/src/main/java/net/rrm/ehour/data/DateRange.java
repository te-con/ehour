/**
 * Created on Nov 4, 2006
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
 *
 * This Software is copyright TE-CON 2007. This Software is not open source by definition. The source of the Software is available for educational purposes.
 * TE-CON holds all the ownership rights on the Software.
 * TE-CON freely grants the right to use the Software. Any reproduction or modification of this Software, whether for commercial use or open source,
 * is subject to obtaining the prior express authorization of TE-CON.
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
	 * Get date end for display (as in, time is set to 12:00pm)
	 * TODO pretty bad hack as this is a timezone issue
	 * @return
	 */
	public Date getDateEndForDisplay()
	{
		return DateUtil.getDateEndForDisplay(getDateEnd());
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
