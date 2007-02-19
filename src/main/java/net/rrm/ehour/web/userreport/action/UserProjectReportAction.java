/**
 * Created on 1-feb-2007
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

package net.rrm.ehour.web.userreport.action;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.criteria.UserCriteria;
import net.rrm.ehour.report.project.ProjectReport;
import net.rrm.ehour.report.service.ReportService;
import net.rrm.ehour.web.report.form.ReportCriteriaForm;
import net.rrm.ehour.web.report.util.UserCriteriaAssembler;
import net.rrm.ehour.web.util.AuthUtil;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * TODO 
 **/

public class UserProjectReportAction extends Action
{
	private ReportCriteria 	reportCriteria;
	private	ReportService	reportService;
	private	Logger			logger = Logger.getLogger(this.getClass());
	private EhourConfig		config;
	
	/**
	 * 
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{
		ReportCriteriaForm	rcForm = (ReportCriteriaForm)form;
		Integer				userId;
		UserCriteria		uc;
		ProjectReport		report;
		String				sessionKey;
		HttpSession			session = request.getSession();
		
		uc = UserCriteriaAssembler.getUserCriteria(rcForm);
		
		// sanity check to prevent abuse
		userId = AuthUtil.getUserId(rcForm);
//		uc = reportCriteria.getUserCriteria();
		uc.setUserIds(new Integer[]{userId});
		uc.setUserActivityFilter(UserCriteria.USER_SINGLE);
		reportCriteria.setUserCriteria(uc);
		
		report = reportService.createProjectReport(reportCriteria);
		sessionKey = generateSessionKey();
		session.setAttribute(sessionKey, report);
		request.setAttribute("report", report);
		request.setAttribute("reportSessionKey", sessionKey);
		request.setAttribute("config", config);
		
		return mapping.findForward("success");
	}

	/**
	 * Generate session key for this report 
	 * @param userId
	 * @return
	 */
	private String generateSessionKey()
	{
		StringBuffer 	sessionKey = new StringBuffer("report_");
		
		sessionKey.append(new Date().getTime());
		
		logger.debug("generated session key for report: " + sessionKey);
		
		return sessionKey.toString();
	}
	
	
	/**
	 * @param reportCriteria the reportCriteria to set
	 */
	public void setReportCriteria(ReportCriteria reportCriteria)
	{
		this.reportCriteria = reportCriteria;
	}

	/**
	 * @param reportService the reportService to set
	 */
	public void setReportService(ReportService reportService)
	{
		this.reportService = reportService;
	}

	/**
	 * @param config the config to set
	 */
	public void setConfig(EhourConfig config)
	{
		this.config = config;
	}
}
