/**
 * Created on Nov 14, 2006
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

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public abstract class BaseAjaxAction extends Action
{

	/**
	 * 
	 */
	public final ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
	{

		String xml = null;
		
		try
		{
			xml = getXmlContent(form, request);
		} catch (Exception ex)
		{
			// Send back a 500 error code.
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Can not create response");
			return null;
		}

		// Set content to xml
		response.setContentType("text/xml");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter pw = response.getWriter();
		pw.write(xml);
		pw.close();

		return null;
	}

	/**
	 * Get XML content
	 * @param form
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public abstract String getXmlContent(ActionForm form, HttpServletRequest request) throws Exception;
}
