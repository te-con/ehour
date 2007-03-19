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

package net.rrm.ehour.web.admin.assignment.action;

import java.util.GregorianCalendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.rrm.ehour.project.domain.Project;
import net.rrm.ehour.project.domain.ProjectAssignment;
import net.rrm.ehour.user.domain.User;
import net.rrm.ehour.util.DateUtil;
import net.rrm.ehour.web.admin.assignment.form.ProjectAssignmentForm;
import net.rrm.ehour.web.util.WebConstants;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * TODO 
 **/

public class GetAssignmentAction extends AdminProjectAssignmentBaseAction
{
	/**
	 * 
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
								HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		ActionForward			fwd = mapping.findForward("success");
		ProjectAssignmentForm	paForm = (ProjectAssignmentForm)form;
		String					param;
		List<Project>			allProjects;
		List<ProjectAssignment>	assignments;
		User					user;
		ProjectAssignment		assignment;
		
		request.setAttribute("currencySymbol", WebConstants.getCurrencies().get(config.getCurrency()));
		
		param = mapping.getParameter();
		
		allProjects = projectService.getAllProjects(true);
		request.setAttribute("allProjects", allProjects);
		
		assignments = projectService.getAllProjectsForUser(paForm.getUserId());
		request.setAttribute("assignments", assignments);
		
		if (!"addOnly".equals(param))
		{
			assignment = projectService.getProjectAssignment(paForm.getAssignmentId());
			user = assignment.getUser();
		}
		else
		{
			user = userService.getUser(paForm.getUserId());
			
			// set default date start & end to the current month 
			assignment = new ProjectAssignment();
			assignment.setDateRange(DateUtil.calendarToMonthRange(new GregorianCalendar()));
		}
		
		request.setAttribute("user", user);
		request.setAttribute("assignment", assignment);
			
		return fwd;		
	}
}
