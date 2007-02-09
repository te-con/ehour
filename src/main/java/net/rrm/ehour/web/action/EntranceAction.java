/**
 * Created on Nov 19, 2006
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

package net.rrm.ehour.web.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.rrm.ehour.web.timesheet.action.TimesheetOverviewAction;
import net.rrm.ehour.web.util.AuthUtil;

import org.acegisecurity.Authentication;
import org.acegisecurity.context.SecurityContextHolder;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *  
 **/

public class EntranceAction extends Action
{
	private	Logger			logger = Logger.getLogger(TimesheetOverviewAction.class);	

	/**
	 * 
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		ActionForward	fwd = null;
		Authentication	authUser;
		
		authUser = SecurityContextHolder.getContext().getAuthentication();
		
		// @todo make this less hardcoded
		
		if (!AuthUtil.hasRole("ROLE_CONSULTANT", authUser.getAuthorities()) &&
			 (AuthUtil.hasRole("ROLE_ADMIN", authUser.getAuthorities()) ||
			  AuthUtil.hasRole("ROLE_PROJECTADMIN", authUser.getAuthorities())))
		{
			logger.debug("User on entrance with admin only rights");
			fwd = mapping.findForward("adminOnly");
		}
		else if (AuthUtil.hasRole("ROLE_REPORT", authUser.getAuthorities()))
		{
			fwd = mapping.findForward("report");
		}
		else
		{
			fwd = mapping.findForward("billable");
		}
		
		return fwd;
	}
}
