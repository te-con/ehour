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

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.project.domain.Project;
import net.rrm.ehour.project.domain.ProjectAssignment;
import net.rrm.ehour.project.service.ProjectAssignmentService;
import net.rrm.ehour.project.service.ProjectService;
import net.rrm.ehour.user.service.UserService;
import net.rrm.ehour.web.admin.assignment.form.ProjectAssignmentForm;
import net.rrm.ehour.web.sort.ProjectAssignmentComparator;
import net.rrm.ehour.web.sort.ProjectComparator;
import net.rrm.ehour.web.util.WebConstants;

import org.apache.struts.action.Action;

/**
 * TODO 
 **/

public class AdminProjectAssignmentBaseAction extends Action
{
	protected ProjectAssignmentService	projectAssignmentService;
	protected ProjectService			projectService;
	protected UserService				userService;
	protected EhourConfig				config;
	
	/**
	 * Set assignments and currency on context
	 * @param request
	 */
	protected void setAssignmentsOnContext(HttpServletRequest request, ProjectAssignmentForm paForm)
	{
		List<Project>			allProjects;
		List<ProjectAssignment>	assignments;
		
		request.setAttribute("currencySymbol", WebConstants.getCurrencies().get(config.getCurrency()));
		
		allProjects = projectService.getAllProjects(true);
		Collections.sort(allProjects, new ProjectComparator());
		request.setAttribute("allProjects", allProjects);
		
		assignments = projectService.getAllProjectsForUser(paForm.getUserId());
		Collections.sort(assignments, new ProjectAssignmentComparator(ProjectAssignmentComparator.ASSIGNMENT_COMPARE_CUSTDATEPRJ));
		request.setAttribute("assignments", assignments);
	}
	
	/**
	 * 
	 * @param projectService
	 */
	public void setProjectAssignmentService(ProjectAssignmentService projectAssignmentService)
	{
		this.projectAssignmentService = projectAssignmentService;
	}
	
	public void setUserService(UserService userService)
	{
		this.userService = userService;
	}

	/**
	 * @param config the config to set
	 */
	public void setConfig(EhourConfig config)
	{
		this.config = config;
	}

	/**
	 * @param projectService the projectService to set
	 */
	public void setProjectService(ProjectService projectService)
	{
		this.projectService = projectService;
	}
}
