/**
 * Created on 14-dec-2006
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

import net.rrm.ehour.exception.ProjectAlreadyAssignedException;
import net.rrm.ehour.project.domain.Project;
import net.rrm.ehour.project.domain.ProjectAssignment;
import net.rrm.ehour.user.domain.User;
import net.rrm.ehour.util.DateUtil;
import net.rrm.ehour.web.admin.assignment.form.ProjectAssignmentForm;
import net.rrm.ehour.web.util.DomainAssembler;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
/**
 * TODO 
 **/

public class EditAssignmentAction extends AdminProjectAssignmentBaseAction
{
	/**
	 * 
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
								HttpServletRequest request, HttpServletResponse response) throws ParseException
	{
		ActionForward fwd = mapping.findForward("success");
		ProjectAssignmentForm paf = (ProjectAssignmentForm)form;
		ProjectAssignment	pa = null;
		User				user;
		List<Project>		allProjects;
		List<ProjectAssignment>	assignments;
		ActionMessages		messages = new ActionMessages();

		response.setHeader("Cache-Control", "no-cache");
		
		try
		{
			pa = DomainAssembler.getProjectAssignment(paf);
			
			if (pa.getDateStart().after(pa.getDateEnd()))
			{
				messages.add("dateStart", new ActionMessage("admin.assignment.errorStartAfterEnd"));
			}
			else
			{
				projectService.assignUserToProject(pa);
				
				// upon success, display new assignment form
				pa = new ProjectAssignment();
				pa.setDateRange(DateUtil.calendarToMonthRange(new GregorianCalendar()));
			}

		} catch (ParseException e1)
		{
			// @todo - could be dateEnd as well. quite unlikely this will happen though
			messages.add("dateStart", new ActionMessage("errors.invalidDate"));
			
			// we couldn't parse it so init
			pa = new ProjectAssignment();
			pa.setDateRange(DateUtil.calendarToMonthRange(new GregorianCalendar()));
		}
		catch (ProjectAlreadyAssignedException e)
		{
			messages.add("project", new ActionMessage("admin.assignment.errorAlreadyAssigned"));
		}

		// retrieve info
		allProjects = projectService.getAllProjects(true);
		request.setAttribute("allProjects", allProjects);
		
		assignments = projectService.getAllProjectsForUser(paf.getUserId());
		request.setAttribute("assignments", assignments);		
		
		request.setAttribute("assignment", pa);
		
		user = userService.getUser(paf.getUserId());
		request.setAttribute("user", user);
		
		//
		saveErrors(request, messages);
		
		return fwd;
	}
}