/**
 * Created on 15-dec-2006
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

import java.text.ParseException;
import java.util.GregorianCalendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.rrm.ehour.exception.ParentChildConstraintException;
import net.rrm.ehour.project.domain.Project;
import net.rrm.ehour.project.domain.ProjectAssignment;
import net.rrm.ehour.user.domain.User;
import net.rrm.ehour.util.DateUtil;
import net.rrm.ehour.web.admin.assignment.form.ProjectAssignmentForm;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * TODO 
 **/

public class DeleteAssignmentAction extends AdminProjectAssignmentBaseAction
{
	/**
	 * 
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
								HttpServletRequest request, HttpServletResponse response) throws ParseException
	{
		ActionForward fwd = mapping.findForward("success");
		ProjectAssignmentForm paf = (ProjectAssignmentForm)form;
		User				user;
		List<Project>		allProjects;
		ProjectAssignment	pa;
		List<ProjectAssignment>	assignments;
		
		response.setContentType("text/xml");
		response.setHeader("Cache-Control", "no-cache");
		
		allProjects = projectService.getAllProjects(true);
		request.setAttribute("allProjects", allProjects);
		
		try
		{
			projectAssignmentService.deleteProjectAssignment(paf.getAssignmentId());
			
			// upon success, display new assignment form
			pa = new ProjectAssignment();
			pa.setDateRange(DateUtil.calendarToMonthRange(new GregorianCalendar()));
		}
		catch (ParentChildConstraintException e)
		{
			request.setAttribute("error", e.getMessage());
			e.printStackTrace();
			
			pa = projectAssignmentService.getProjectAssignment(paf.getAssignmentId());
		}

		assignments = projectService.getAllProjectsForUser(paf.getUserId());
		request.setAttribute("assignments", assignments);		
		
		request.setAttribute("assignment", pa);
		
		user = userService.getUser(paf.getUserId());
		request.setAttribute("user", user);
		
		return fwd;
	}
}
