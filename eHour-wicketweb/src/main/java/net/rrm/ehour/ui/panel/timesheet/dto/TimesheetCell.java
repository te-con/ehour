/**
 * Created on Dec 29, 2006
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
 *
 * This Software is copyright TE-CON 2007. This Software is not open source by definition. The source of the Software is available for educational purposes.
 * TE-CON holds all the ownership rights on the Software.
 * TE-CON freely grants the right to use the Software. Any reproduction or modification of this Software, whether for commercial use or open source,
 * is subject to obtaining the prior express authorization of TE-CON.
 * 
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.ui.panel.timesheet.dto;

import java.io.Serializable;

import net.rrm.ehour.domain.TimesheetEntry;

/**
 * Representation of a cell (day in a project) in the timesheet
 **/

public class TimesheetCell implements Comparable<TimesheetCell>, Serializable
{
	private static final long serialVersionUID = -2708559856313387714L;
	private TimesheetEntry	timesheetEntry;
	private	boolean			isValid;

	/**
	 * @return the isValid
	 */
	public boolean isValid()
	{
		return isValid;
	}
	/**
	 * @param isValid the isValid to set
	 */
	public void setValid(boolean isValid)
	{
		this.isValid = isValid;
	}
	/**
	 * @return the timesheetEntry
	 */
	public TimesheetEntry getTimesheetEntry()
	{
		return timesheetEntry;
	}
	/**
	 * @param timesheetEntry the timesheetEntry to set
	 */
	public void setTimesheetEntry(TimesheetEntry timesheetEntry)
	{
		this.timesheetEntry = timesheetEntry;
	}
	
	
	public int compareTo(TimesheetCell o)
	{
		return getTimesheetEntry().getEntryId().getEntryDate().compareTo(o.getTimesheetEntry().getEntryId().getEntryDate());
	}
}
