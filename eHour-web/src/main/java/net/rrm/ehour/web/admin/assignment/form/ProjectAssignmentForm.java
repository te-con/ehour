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

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

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
	private	Integer	assignmentTypeId;
	private	String	dateStart;
	private boolean infiniteStartDate;
	private	String	dateEnd;
	private boolean	infiniteEndDate;
	private	Integer	projectId;
	private	Float	hourlyRate;
	private	String	role;
	private Float	allottedHours;
	private Float	allowedOverrun;
	private boolean	notifyPm;

	/**
	 * 
	 *
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request)
	{
		notifyPm = false;
		infiniteStartDate = false;
		infiniteEndDate = false;
	}	
	
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
		if (hourlyRate.floatValue() > 0)
		{
			this.hourlyRate = hourlyRate;
		}
		else
		{
			this.hourlyRate = null;
		}
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

	public String getRole()
	{
		return role;
	}

	public void setRole(String description)
	{
		if (description.equals(""))
		{
			this.role = null;
		}
		else
		{
			this.role = description;
		}
	}

	/**
	 * @return the assignmentTypeId
	 */
	public Integer getAssignmentTypeId()
	{
		return assignmentTypeId;
	}

	/**
	 * @param assignmentTypeId the assignmentTypeId to set
	 */
	public void setAssignmentTypeId(Integer assignmentTypeId)
	{
		this.assignmentTypeId = assignmentTypeId;
	}

	/**
	 * @return the allotted
	 */
	public Float getAllottedHours()
	{
		return allottedHours;
	}

	/**
	 * @param allotted the allotted to set
	 */
	public void setAllottedHours(Float allotted)
	{
		this.allottedHours = allotted;
	}

	/**
	 * @return the notifyPm
	 */
	public boolean isNotifyPm()
	{
		return notifyPm;
	}

	/**
	 * @param notifyPm the notifyPm to set
	 */
	public void setNotifyPm(boolean notifyPm)
	{
		this.notifyPm = notifyPm;
	}

	/**
	 * @return the allowedOverrun
	 */
	public Float getAllowedOverrun()
	{
		return allowedOverrun;
	}

	/**
	 * @param allowedOverrun the allowedOverrun to set
	 */
	public void setAllowedOverrun(Float allowedOverrun)
	{
		this.allowedOverrun = allowedOverrun;
	}

	/**
	 * @return the infiniteEndDate
	 */
	public boolean isInfiniteEndDate()
	{
		return infiniteEndDate;
	}

	/**
	 * @param infiniteEndDate the infiniteEndDate to set
	 */
	public void setInfiniteEndDate(boolean infiniteEndDate)
	{
		this.infiniteEndDate = infiniteEndDate;
	}

	/**
	 * @return the infiniteStartDate
	 */
	public boolean isInfiniteStartDate()
	{
		return infiniteStartDate;
	}

	/**
	 * @param infiniteStartDate the infiniteStartDate to set
	 */
	public void setInfiniteStartDate(boolean infiniteStartDate)
	{
		this.infiniteStartDate = infiniteStartDate;
	}
	
}
