/**
 * Created on Nov 6, 2006
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
