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

package net.rrm.ehour.web.report.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.rrm.ehour.report.reports.ReportData;
import net.rrm.ehour.web.report.form.ReportForm;
import net.rrm.ehour.web.report.reports.AggregateReport;
import net.rrm.ehour.web.report.util.ReportSessionKey;

import org.apache.struts.action.ActionMapping;

/**
 * Provides basic implementations for reportData and aggregateReport where
 * both are fetched from the session 
 **/

public abstract class ReUseReportAction extends AbstractCreateAggregateReportAction
{
	/**
	 * Get cached aggregate report from session (mainly for excel exports) 
	 */
	@Override
	protected AggregateReport getAggregateReport(HttpSession session,
												 String reportName,
												 ReportForm reportForm,
												 ReportData reportData)
	{
		String			sessionKey = reportForm.getKey();
		AggregateReport	aggregateReport = null;
		
		if (sessionKey != null)
		{
			aggregateReport = (AggregateReport)session.getAttribute(ReportSessionKey.REPORT_AGGREGATE + 
																	"_" +  reportName + "_" +  sessionKey);
			
			if (aggregateReport == null)
			{
				logger.error("No report in session found for key " + sessionKey + " and report name " + reportName);
			}
			else
			{
				logger.info(reportName + " report found on session for key " + sessionKey);
			}
		}
		else
		{
			logger.error("No session key provided!");
		}		
		
		return aggregateReport;
	}

	/**
	 * Get cached report data from session (mainly for charts and new types of report on the same data)
	 */
	@Override
	protected ReportData getReportData(HttpServletRequest request, ActionMapping mapping, ReportForm form)
	{
		ReportData		reportData = null;
		HttpSession		session;
		String			sessionKey;
		
		session = request.getSession();
		sessionKey = form.getKey();
		
		if (sessionKey != null)
		{
			reportData = (ReportData)session.getAttribute(ReportSessionKey.REPORT_DATA + "_" +  sessionKey);
			
			if (reportData != null)
			{
				logger.info("report data found on session for key " + sessionKey);
			}
			else
			{
				logger.error("No report data in session found for key " + sessionKey);
			}
		}
		else
		{
			logger.error("No session key provided!");
		}	
		
		return reportData;
	}
}
