/**
 * Created on Nov 26, 2006
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

package net.rrm.ehour.web.admin.user.action;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.rrm.ehour.user.domain.User;
import net.rrm.ehour.user.domain.UserRole;
import net.rrm.ehour.web.admin.user.form.UserForm;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * TODO 
 **/

public class GetUserAction extends AdminUserBaseAction
{
	/**
	 * 
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
								HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		ActionForward	fwd = mapping.findForward("success");
		UserForm		userForm = (UserForm)form;
		String			param;
		List			userDepartments;
		List			userRoles;
		User			user;
		
		response.setContentType("text/xml");
		response.setHeader("Cache-Control", "no-cache");
		
		param = mapping.getParameter();
		
		userDepartments = userService.getUserDepartments();
		userRoles = userService.getUserRoles();
		
		request.setAttribute("userDepartments", userDepartments);
		request.setAttribute("userRoles", userRoles);
		
		if (!"addOnly".equals(param))
		{
			user = userService.getUser(userForm.getUserId());
			request.setAttribute("user", user);
			
			request.setAttribute("userRolesString", rolesToString(user.getUserRoles()));
		}
			
		return fwd;
	}
	
	/**
	 *	Put all roles in one long string since JSTL can't do a contains on a Set 
	 * @param roles
	 * @return
	 */
	private String rolesToString(Set roles)
	{
		StringBuffer	roleString = new StringBuffer();
		UserRole		role;
		Iterator		i;
		
		i = roles.iterator();
		
		while (i.hasNext())
		{
			role = (UserRole)i.next();
			roleString.append("-");
			roleString.append(role.getRole());
			roleString.append("-");
		}
		
		return roleString.toString();
	}
}
