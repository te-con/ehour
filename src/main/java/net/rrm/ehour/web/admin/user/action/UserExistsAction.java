/**
 * Created on Dec 1, 2006
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

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.rrm.ehour.web.admin.user.form.UserForm;

import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * TODO 
 **/

public class UserExistsAction extends AdminUserBaseAction
{
	/**
	 * 
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
								HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		UserForm		userForm = (UserForm)form;
		UserDetails	 	userDetails;
		boolean			userExists;
		PrintWriter		writer;
		String			xmlResponse;
		
		try
		{
			userDetails = userService.loadUserByUsername(userForm.getUsername());
			userExists = (userDetails != null);
		}
		// @todo acegi dependency
		catch (UsernameNotFoundException unfe)
		{
			userExists = false;
		}
		
	    response.setContentType("text/xml; charset=UTF-8");
	    response.setHeader("Cache-Control", "no-cache");		
		writer = response.getWriter();
		
		xmlResponse = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<userExists>" + userExists + "</userExists>\n";
		writer.append(xmlResponse);
		writer.close();
		
		return null;
	}
}
