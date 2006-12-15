/**
 * Created on Dec 11, 2006
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

package net.rrm.ehour.web.admin.assignment.form;

import java.util.Date;

import org.apache.struts.action.ActionForm;

/**
 * TODO 
 **/

public class ProjectAssignmentForm extends ActionForm
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2963443441788534965L;
	
	private	Integer	userId;
	private	Integer	assignmentId;
	private	String	dateStart;
	private	String	dateEnd;
	private	Integer	projectId;
	private	Float	hourlyRate;
	private	String	description;

	/**
	 * @return the userId
	 */
	public Integer getUserId()
	{
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(Integer userId)
	{
		this.userId = userId;
	}

	public Integer getAssignmentId()
	{
		return assignmentId;
	}

	public void setAssignmentId(Integer assignmentId)
	{
		this.assignmentId = assignmentId;
	}


	public Integer getProjectId()
	{
		return projectId;
	}

	public void setProjectId(Integer projectId)
	{
		this.projectId = projectId;
	}

	public Float getHourlyRate()
	{
		return hourlyRate;
	}

	public void setHourlyRate(Float hourlyRate)
	{
		this.hourlyRate = hourlyRate;
	}

	public String getDateEnd()
	{
		return dateEnd;
	}

	public void setDateEnd(String dateEnd)
	{
		this.dateEnd = dateEnd;
	}

	public String getDateStart()
	{
		return dateStart;
	}

	public void setDateStart(String dateStart)
	{
		this.dateStart = dateStart;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}
	
}
