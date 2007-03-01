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

package net.rrm.ehour.web.timesheet.action;

import java.util.Calendar;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.rrm.ehour.project.domain.ProjectAssignment;
import net.rrm.ehour.project.service.ProjectService;
import net.rrm.ehour.util.DateUtil;
import net.rrm.ehour.web.calendar.CalendarUtil;
import net.rrm.ehour.web.timesheet.form.PrintTimesheetForm;
import net.rrm.ehour.web.util.AuthUtil;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * TODO 
 **/

public class PrintTimesheetAction extends Action
{
	private ProjectService	projectService;
	private	CalendarUtil	calendarUtil;
	
	/**
	 * 
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
	{
		ActionForward			fwd;
		PrintTimesheetForm		psForm = (PrintTimesheetForm)form;
		Integer					userId;
		Calendar				printDate;
		Set<ProjectAssignment>	projectAssignments;
		
		userId = AuthUtil.getUserId(psForm);
		
		printDate = calendarUtil.getRequestedMonth(request, psForm);
		
		if (psForm != null && psForm.isFromForm())
		{
			fwd = null;
		}
		else
		{
			projectAssignments = getProjects(userId, printDate);
			request.setAttribute("projectAssignments", projectAssignments);
			request.setAttribute("printDate", printDate);
			fwd = mapping.findForward( (psForm.isAjaxCall()) ? "printForm" : "printLayout");
		}

		return fwd;
	}

	/**
	 * Get projects for this user in this month
	 * @param userId
	 * @param printDate
	 */
	private Set<ProjectAssignment> getProjects(Integer userId, Calendar printDate)
	{
		Set<ProjectAssignment>	assignments;
		assignments = projectService.getProjectsForUser(userId, DateUtil.calendarToMonthRange(printDate));
		return assignments;
	}
	
	/**
	 * @param projectService the projectService to set
	 */
	public void setProjectService(ProjectService projectService)
	{
		this.projectService = projectService;
	}

	/**
	 * @param calendarUtil the calendarUtil to set
	 */
	public void setCalendarUtil(CalendarUtil calendarUtil)
	{
		this.calendarUtil = calendarUtil;
	}
}
