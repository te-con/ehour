/**
 * Created on Feb 24, 2007
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

package net.rrm.ehour.web.report.action.reuse;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.report.reports.ReportData;
import net.rrm.ehour.web.report.form.ReportChartForm;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * TODO 
 **/

public abstract class ReUseReportAction extends Action
{
	protected 	EhourConfig	config;
	protected	Logger			logger = Logger.getLogger(this.getClass());
	
	/**
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{
		ReportChartForm	chartForm = (ReportChartForm)form;
		String			sessionKey;
		HttpSession		session;
		ReportData		reportData;
		ActionForward	fwd = null;
		
		response.setHeader("Cache-Control", "no-cache");

		session = request.getSession();
		sessionKey = chartForm.getKey();
		
		request.setAttribute("reportSessionKey", sessionKey);
		request.setAttribute("forId", chartForm.getForId());

		fwd = reUseReport(mapping, request, response, chartForm, sessionKey);
		
		return fwd;
	}

	/**
	 * Do something with the report
	 * @param response
	 * @param chartForm
	 * @param reportData
	 * @return
	 */
	protected abstract ActionForward reUseReport(ActionMapping mapping,
													HttpServletRequest request,
													HttpServletResponse response,
													ReportChartForm chartForm,
													String sessionKey)  throws IOException;	

	/**
	 * @param config the config to set
	 */
	public void setConfig(EhourConfig config)
	{
		this.config = config;
	}
}
