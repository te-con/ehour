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

import java.util.GregorianCalendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.rrm.ehour.exception.ProjectAlreadyAssignedException;
import net.rrm.ehour.project.domain.ProjectAssignment;
import net.rrm.ehour.user.domain.User;
import net.rrm.ehour.util.DateUtil;
import net.rrm.ehour.util.EhourConstants;
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
								HttpServletRequest request, HttpServletResponse response) 
	{
		ActionForward fwd = mapping.findForward("success");
		ProjectAssignmentForm paf = (ProjectAssignmentForm)form;
		ProjectAssignment	pa = null;
		User				user;
		ActionMessages		messages = new ActionMessages();

		try
		{
			pa = DomainAssembler.getProjectAssignment(paf);
			
			messages = validateProjectAssignment(pa);

			if (messages.size() == 0)
			{
				projectAssignmentService.assignUserToProject(pa);
			
				// upon success, display new assignment form
				pa = new ProjectAssignment();
				pa.setDateRange(DateUtil.calendarToMonthRange(new GregorianCalendar()));
			}
		}
		catch (ProjectAlreadyAssignedException e)
		{
			messages.add("project", new ActionMessage("admin.assignment.errorAlreadyAssigned"));
		}

		// retrieve info
		super.setAssignmentsOnContext(request, paf);

		request.setAttribute("assignment", pa);
		
		user = userService.getUser(paf.getUserId());
		request.setAttribute("user", user);
		
		//
		saveErrors(request, messages);
		
		return fwd;
	}
	
	/**
	 * Validate
	 * @param pa
	 * @return
	 */
	private ActionMessages validateProjectAssignment(ProjectAssignment pa)
	{
		ActionMessages messages = new ActionMessages();
		
		if (pa.getAssignmentType().getAssignmentTypeId().intValue() == EhourConstants.ASSIGNMENT_DATE)
		{
//			if (pa.getDateStart() == null)
//			{
//				messages.add("dateStart", new ActionMessage("errors.invalidDate"));
//			}
//			else if (pa.getDateEnd() == null)
//			{
//				messages.add("dateEnd", new ActionMessage("errors.invalidDate"));
//			}
//			else 
			if (pa.getDateStart() != null
					&& pa.getDateEnd() != null
					&& pa.getDateStart().after(pa.getDateEnd()))
			{
				messages.add("dateStart", new ActionMessage("admin.assignment.errorStartAfterEnd"));
			}

		} 
		else if (pa.getAssignmentType().getAssignmentTypeId().intValue() == EhourConstants.ASSIGNMENT_TIME_ALLOTTED_FIXED)
		{
			if (pa.getAllottedHours() == null)
			{
				messages.add("allottedHours", new ActionMessage("admin.assignment.required"));
			}
		}
		else if (pa.getAssignmentType().getAssignmentTypeId().intValue() == EhourConstants.ASSIGNMENT_TIME_ALLOTTED_FLEX)
		{
			if (pa.getAllottedHours() == null)
			{
				messages.add("allottedHours", new ActionMessage("admin.assignment.required"));
			}

			if (pa.getAllowedOverrun() == null)
			{
				messages.add("allowedOverrun", new ActionMessage("admin.assignment.required"));
			}
		
		}
		
		
		return messages;
	}
}