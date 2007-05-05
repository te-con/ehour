/**
 * Created on May 5, 2007
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

package net.rrm.ehour.web.report.form;

import org.apache.struts.action.ActionForm;

/**
 * Quick date selection
 **/

public class QuickDateForm extends ActionForm
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6640676276516468056L;
	private	String	quickDateType;
	private Integer	relativity;
	private	Integer	absolute;
	private	Integer	year;
	/**
	 * @return the absolute
	 */
	public Integer getAbsolute()
	{
		return absolute;
	}
	/**
	 * @param absolute the absolute to set
	 */
	public void setAbsolute(Integer absolute)
	{
		this.absolute = absolute;
	}
	/**
	 * @return the quickDateType
	 */
	public String getQuickDateType()
	{
		return quickDateType;
	}
	/**
	 * @param quickDateType the quickDateType to set
	 */
	public void setQuickDateType(String quickDateType)
	{
		this.quickDateType = quickDateType;
	}
	/**
	 * @return the relativity
	 */
	public Integer getRelativity()
	{
		return relativity;
	}
	/**
	 * @param relativity the relativity to set
	 */
	public void setRelativity(Integer relativity)
	{
		this.relativity = relativity;
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
}
