/**
 * Created on Dec 14, 2006
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

package net.rrm.ehour.web.calendar;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

import net.rrm.ehour.web.form.UserIdForm;

/**
 * TODO 
 **/

public class NavCalendarForm extends UserIdForm
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8575001571840906825L;
	
	private	Integer	year;
	private	Integer	month;
	private	boolean		zeroBased = true;

	/**
	 * 
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request)
	{
		zeroBased = true;
		year = null;
		month = null;
	}	
		
	
	/**
	 * @return the month
	 */
	public Integer getMonth()
	{
		return month;
	}

	/**
	 * @param month the month to set
	 */
	public void setMonth(Integer month)
	{
		this.month = month;
	}

	/**
	 * @return the year
	 */
	public Integer getYear()
	{
		return year;
	}

	/**
	 * @param year the year to set
	 */
	public void setYear(Integer year)
	{
		this.year = year;
	}

	/**
	 * @return the zeroBased
	 */
	public boolean isZeroBased()
	{
		return zeroBased;
	}

	/**
	 * @param zeroBased the zeroBased to set
	 */
	public void setZeroBased(boolean zeroBased)
	{
		this.zeroBased = zeroBased;
	}
	
}
