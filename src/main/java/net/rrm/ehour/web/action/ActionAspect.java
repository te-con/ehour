/**
 * Created on Mar 17, 2007
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
import javax.servlet.jsp.jstl.core.Config;

import net.rrm.ehour.config.EhourConfig;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * Aspect to set i18n stuff for struts actions
 **/

// TODO can't use annotations due to acegi 1.0.. waiting for Spring Security
//@Aspect
public class ActionAspect
{
	private Logger	logger = Logger.getLogger(this.getClass());
	private EhourConfig	config;
	
	/**
	 * Set the configured locale in the request
	 * Couldn't use fmt:setLocale in the layoutBase due to tiles having a seperate response context (at least,
	 * that's what I suspect)
	 * @param mapping
	 * @param form
	 * @param request
	 * @throws Throwable
	 */
//	@Before("execution(* net.rrm.ehour.web.*.action.*Action.execute(..)) and args(mapping, form, request,..)")
	public void setLocale(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Throwable
	{
		logger.debug("Setting locale and content-type");
		Config.set(request, Config.FMT_LOCALE, config.getLocale());
		
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
	}

	/**
	 * @param ehourConfig the ehourConfig to set
	 */
	public void setConfig(EhourConfig ehourConfig)
	{
		this.config = ehourConfig;
	}
}
