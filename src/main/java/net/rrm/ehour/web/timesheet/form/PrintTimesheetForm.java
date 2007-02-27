/**
 * Created on 27-feb-2007
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


/**
 * TODO 
 **/

public class PrintTimesheetForm extends TimesheetViewForm
{
	
	/**
	 * 
	 */

	private static final long serialVersionUID = -358079213446195520L;

	private boolean		fromForm = false;
	private	Integer[]	projectIds;

	/**
	 * @return the fromForm
	 */
	public boolean isFromForm()
	{
		return fromForm;
	}

	/**
	 * @param fromForm the fromForm to set
	 */
	public void setFromForm(boolean fromForm)
	{
		this.fromForm = fromForm;
	}

	/**
	 * @return the projectIds
	 */
	public Integer[] getProjectIds()
	{
		return projectIds;
	}

	/**
	 * @param projectIds the projectIds to set
	 */
	public void setProjectIds(Integer[] projectIds)
	{
		this.projectIds = projectIds;
	}
}


