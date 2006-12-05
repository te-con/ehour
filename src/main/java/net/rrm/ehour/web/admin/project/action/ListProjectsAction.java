/**
 * Created on Dec 5, 2006
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

package net.rrm.ehour.web.admin.project.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.rrm.ehour.project.domain.Project;
import net.rrm.ehour.web.admin.project.form.ProjectForm;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * Sinterklaas!
 **/

public class ListProjectsAction extends AdminProjectBaseAction
{
	/**
	 * 
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		List<Project>	projects;
		ActionForward	fwd;
		ProjectForm		projectForm = (ProjectForm)form;
		
		response.setHeader("Cache-Control", "no-cache");
		
		if (projectForm.isFromForm())
		{
			response.setContentType("text/xml");
			fwd = mapping.findForward("filteredList");
			
			projects = projectService.getAllProjects(projectForm.isHideInactive());
		}
		else
		{
			projects = projectService.getAllProjects(true);
			
			fwd = mapping.findForward("fullList");
		}
		
		request.setAttribute("projects", projects);
			
		return fwd;
	}
}
