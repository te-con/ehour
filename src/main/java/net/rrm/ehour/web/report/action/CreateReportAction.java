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

package net.rrm.ehour.web.report.action;

import java.text.ParseException;
import java.util.Date;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.criteria.UserCriteria;
import net.rrm.ehour.report.reports.ReportData;
import net.rrm.ehour.report.service.ReportService;
import net.rrm.ehour.user.domain.User;
import net.rrm.ehour.web.report.form.ReportCriteriaForm;
import net.rrm.ehour.web.report.util.UserCriteriaAssembler;
import net.rrm.ehour.web.util.AuthUtil;
import net.rrm.ehour.web.util.WebConstants;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * TODO 
 **/

public class CreateReportAction extends ShowExistingReportAction
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
		UserCriteria		uc;
		ReportData			reportData;
		String				sessionKey;
		HttpSession			session = request.getSession();
		String				param;
		
		param = mapping.getParameter();
		
		uc = getUserCriteria(param, rcForm);
		reportCriteria.setUserCriteria(uc);
		
		reportData = reportService.createReportData(reportCriteria);

		removeOldReportData(session);
		
		super.createAndStoreReport(request, rcForm.getReportName(), null, reportData);

		sessionKey = generateSessionKey();
		session.setAttribute(sessionKey, reportData);
		request.setAttribute("reportSessionKey", sessionKey);
		request.setAttribute("config", config);
		
		response.setHeader("Cache-Control", "no-cache");
		return mapping.findForward(rcForm.getReportName());
	}
	
	/**
	 * Get the user criteria from the form and check authorization
	 * @param param
	 * @param rcForm
	 * @return
	 * @throws ParseException
	 */
	private UserCriteria getUserCriteria(String param, ReportCriteriaForm rcForm) throws ParseException
	{
		UserCriteria 	uc;
		User			loggedInUser;
		
		uc = UserCriteriaAssembler.getUserCriteria(rcForm);
		
		if (param.equals("report") &&
			AuthUtil.hasRole(WebConstants.ROLE_REPORT))
		{
			uc.setSingleUser(false);
		}
		else
		{
			loggedInUser = AuthUtil.getLoggedInUser();
			uc.setUserIds(new Integer[]{loggedInUser.getUserId()});	
			uc.setSingleUser(true);
			
			if (!(param.equals("consultant")))
			{
				logger.warn(loggedInUser + " tried to access overall reporting without proper privileges !");
			}
		}		
		
		return uc;
	}
	
	/**
	 * Remove old reports from the session
	 * @param session
	 */
	private void removeOldReportData(HttpSession session)
	{
		Enumeration attribs = session.getAttributeNames();
		String		attrib;
		
		logger.debug("Removing old reports from session..");
		while (attribs.hasMoreElements())
		{
			attrib = (String)attribs.nextElement();
			
			if (attrib.startsWith("report_"))
			{
				logger.debug("Removing old report data from session: " + attrib);
				session.removeAttribute(attrib);
			}
		}
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
