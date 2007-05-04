/**
 * Created on Apr 29, 2007
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

import java.util.Date;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.report.reports.ReportDataAggregate;
import net.rrm.ehour.web.report.form.ReportForm;
import net.rrm.ehour.web.report.reports.aggregate.AggregateReport;
import net.rrm.ehour.web.report.util.ReportSessionKey;
import net.rrm.ehour.web.util.AuthUtil;
import net.rrm.ehour.web.util.WebConstants;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * Base report action that takes care of:
 *  - fetching report data
 *  - generating aggregate report
 *  - clear session
 *  - put report data & aggregate on session
 *  - put aggregate report on request + config etc
 *  - fwd appropiately
 **/

public abstract class AbstractCreateAggregateReportAction extends Action
{
	protected Logger		logger = Logger.getLogger(this.getClass());
	protected EhourConfig	config;
	
	/**
	 * 
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
									HttpServletRequest request, HttpServletResponse response)
	{
		ReportDataAggregate	reportDataAggregate;
		String				sessionKey;
		HttpSession			session = request.getSession();
		String				reportName;
		AggregateReport		aggregateReport;
		ReportForm			reportForm;
		ActionForward		fwd;
		
		reportForm = (ReportForm)form;
		
		reportDataAggregate = getReportData(request, mapping, reportForm);
		reportName = getReportName(session, reportForm);

		aggregateReport = getAggregateReport(session, reportName, reportForm, reportDataAggregate);

		// with the data retrieved, provide a hook for any excel/chart reporting
		fwd = processReport(mapping, response, reportForm, reportName, reportDataAggregate, aggregateReport);
		
		// store the report data and the generated report on the session
		if (isReplaceSessionData())
		{
			removeOldReportData(session);
	
			sessionKey = generateSessionKey();
			session.setAttribute(ReportSessionKey.REPORT_DATA + "_" + sessionKey, reportDataAggregate);
			session.setAttribute(ReportSessionKey.REPORT_AGGREGATE + "_" + reportName + "_" + sessionKey, aggregateReport);
			session.setAttribute(ReportSessionKey.REPORT_NAME + "_" + sessionKey, aggregateReport.getReportName());
		}
		else
		{
			sessionKey = reportForm.getKey();
		}
		
		request.setAttribute("reportSessionKey", sessionKey);
		
		// store the report on the request as well
		request.setAttribute(reportName, aggregateReport);

		// put the rest of the data on the request
		request.setAttribute("config", config);
		request.setAttribute("currencySymbol", WebConstants.getCurrencies().get(config.getCurrency()));
		request.setAttribute("forId", reportForm.getForId());
		
		return fwd;
	}
	
	/**
	 * Get report data
	 * @param request
	 * @param mapping
	 * @param fwd
	 * @return
	 */
	protected abstract ReportDataAggregate getReportData(HttpServletRequest request,
												ActionMapping mapping,
												ReportForm form);
	
	/**
	 * Get aggregate report based on report data
	 * @param request
	 * @param reportName
	 * @param reportDataAggregate
	 */
	protected abstract AggregateReport getAggregateReport(HttpSession session,
															String reportName,
															ReportForm reportForm,
															ReportDataAggregate reportDataAggregate);
	
	/**
	 * Process report. Normally this defaults to just fetching the fwd
	 * @param request
	 * @param reportForm
	 * @param reportDataAggregate
	 * @param report
	 */
	protected ActionForward processReport(ActionMapping mapping,
											HttpServletResponse response,
											ReportForm reportForm,
											String reportName,
											ReportDataAggregate reportDataAggregate,
											AggregateReport report)
	{
		return mapping.findForward(reportName);
	}

	/**
	 * For new report data/aggregate reports the session data should be replaced as reports change
	 * For read-only operations such as excel and chart data this is not wanted
	 * @return
	 */
	protected abstract boolean isReplaceSessionData();

	
	/**
	 * Get the reportname either from the form or the session if it's not found there
	 * @param reportForm
	 * @return
	 */
	private String getReportName(HttpSession session, ReportForm reportForm)
	{
		String	reportName;
		String	sessionKey;
		
		reportName = (!AuthUtil.hasRole(WebConstants.ROLE_REPORT)) ? "customerReport" : reportForm.getReportName();
		
		if (reportName == null)
		{
			sessionKey = reportForm.getKey();
			
			if (sessionKey == null)
			{
				logger.error("No reportName provided in request or session");
			}
			else
			{
				reportName = (String)session.getAttribute(ReportSessionKey.REPORT_NAME + "_" + sessionKey);
			}
		}
		
		logger.debug(reportName + " requested");
		
		return reportName;
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
			
			if (attrib.startsWith(ReportSessionKey.REPORT_DATA + "_"))
			{
				logger.debug("Removing previous report data from session: " + attrib);
				session.removeAttribute(attrib);
			}
			else if (attrib.startsWith(ReportSessionKey.REPORT_AGGREGATE + "_"))
			{
				logger.debug("Removing previous aggregate report from session: " + attrib);
				session.removeAttribute(attrib);
			}
			else if (attrib.startsWith(ReportSessionKey.REPORT_NAME + "_"))
			{
				logger.debug("Removing previous report name from session: " + attrib);
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
		String	sessionKey = Long.toHexString(new Date().getTime());
		
		logger.debug("Generated session key for reports: " + sessionKey);
		
		return sessionKey.toString();
	}

	/**
	 * @param config the config to set
	 */
	public void setConfig(EhourConfig config)
	{
		this.config = config;
	}
}
