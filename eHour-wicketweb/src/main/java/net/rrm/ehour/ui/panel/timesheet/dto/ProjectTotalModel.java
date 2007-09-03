/**
 * Created on Jul 10, 2007
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

import org.apache.wicket.model.Model;

/**
 * TODO 
 **/

public class ProjectTotalModel extends Model
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4141505357937939279L;
	private	TimesheetRow	row;
	
	public ProjectTotalModel(TimesheetRow row)
	{
		this.row = row;
	}
	
	@Override
	public Object getObject()
	{
		float	totalHours = 0;
		
		for (int i = 0; 
				i < row.getTimesheetCells().length;
				i++)
		{
			if (row.getTimesheetCells()[i] != null 
					&& row.getTimesheetCells()[i].getTimesheetEntry() != null 
					&& row.getTimesheetCells()[i].getTimesheetEntry().getHours() != null)
			{
				totalHours += row.getTimesheetCells()[i].getTimesheetEntry().getHours().floatValue();
			}			
		}
		
		return Float.valueOf(totalHours);
	}
}
