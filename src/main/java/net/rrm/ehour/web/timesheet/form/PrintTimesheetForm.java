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

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

import net.rrm.ehour.web.calendar.NavCalendarForm;


/**
 * TODO 
 **/

public class PrintTimesheetForm extends NavCalendarForm
{
	
	/**
	 * 
	 */

	private static final long serialVersionUID = -358079213446195520L;

	private boolean		fromForm = false;
	private	boolean		ajaxCall = false;
	private	Integer[]	projectId;

	
	/**
	 * 
	 *
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request)
	{
		fromForm = false;
		ajaxCall = false;
		super.reset(mapping, request);
	}	
	
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
	 * @return the projectId
	 */
	public Integer[] getProjectId()
	{
		return projectId;
	}

	/**
	 * @param projectId the projectId to set
	 */
	public void setProjectId(Integer[] projectIds)
	{
		this.projectId = projectIds;
	}

	/**
	 * @return the ajaxCall
	 */
	public boolean isAjaxCall()
	{
		return ajaxCall;
	}

	/**
	 * @param ajaxCall the ajaxCall to set
	 */
	public void setAjaxCall(boolean ajaxCall)
	{
		this.ajaxCall = ajaxCall;
	}
}


