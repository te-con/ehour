/**
 * Created on Dec 29, 2006
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

package net.rrm.ehour.web.timesheet.form;

import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.servlet.http.HttpServletRequest;

import net.rrm.ehour.web.form.UserIdForm;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMapping;

/**
 * TODO 
 **/

public class TimesheetForm extends UserIdForm
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6237853557480051991L;
	private	Integer day;
	private	Integer year;
	private	Integer month;
	private	Integer	sheetDay;
	private	Integer	sheetMonth;
	private	Integer	sheetYear;
	private	String	comment;
	private	Integer	relativeWeek;
	private	boolean	moveRelative;
	private	transient Logger	logger = Logger.getLogger(TimesheetForm.class);
	
	/**
	 * 
	 *
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request)
	{
		moveRelative = false;
		super.reset(mapping, request);
	}	
	
	/**
	 * Get supplied date as calendar or current date if none given
	 * @return
	 */
	public Calendar getCalendar()
	{
		Calendar cal;
		
		if (day == null || year == null || month == null)
		{
			logger.debug("no date supplied when converting to calendar");
			cal = new GregorianCalendar();
		}
		else
		{
			cal = new GregorianCalendar(year, month, day);
		}
		
		return cal;
	}

	/**
	 * @return the day
	 */
	public Integer getDay()
	{
		return day;
	}

	/**
	 * @param day the day to set
	 */
	public void setDay(Integer day)
	{
		this.day = day;
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
	 * @return the sheetMonth
	 */
	public Integer getSheetMonth()
	{
		return sheetMonth;
	}

	/**
	 * @param sheetMonth the sheetMonth to set
	 */
	public void setSheetMonth(Integer sheetMonth)
	{
		this.sheetMonth = sheetMonth;
	}

	/**
	 * @return the sheetYear
	 */
	public Integer getSheetYear()
	{
		return sheetYear;
	}

	/**
	 * @param sheetYear the sheetYear to set
	 */
	public void setSheetYear(Integer sheetYear)
	{
		this.sheetYear = sheetYear;
	}

	/**
	 * @return the sheetDay
	 */
	public Integer getSheetDay()
	{
		return sheetDay;
	}

	/**
	 * @param sheetDay the sheetDay to set
	 */
	public void setSheetDay(Integer sheetDay)
	{
		this.sheetDay = sheetDay;
	}

	/**
	 * @return the comment
	 */
	public String getComment()
	{
		return comment;
	}

	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment)
	{
		this.comment = comment;
	}

	/**
	 * @return the relativeWeek
	 */
	public Integer getRelativeWeek()
	{
		return relativeWeek;
	}

	/**
	 * @param relativeWeek the relativeWeek to set
	 */
	public void setRelativeWeek(Integer relativeWeek)
	{
		this.relativeWeek = relativeWeek;
	}

	/**
	 * @return the moveRelative
	 */
	public boolean isMoveRelative()
	{
		return moveRelative;
	}

	/**
	 * @param moveRelative the moveRelative to set
	 */
	public void setMoveRelative(boolean moveRelative)
	{
		this.moveRelative = moveRelative;
	}

}
