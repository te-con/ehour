/**
 * Created on Apr 22, 2007
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

import net.rrm.ehour.web.report.form.ReportChartForm;
import net.rrm.ehour.web.report.reports.AggregateReport;
import net.rrm.ehour.web.report.util.ReportSessionKey;
import net.rrm.ehour.web.util.WebConstants;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * Base class for aggregate excel reports 
 **/

public abstract class ReUseForAggregateExcelAction extends ReUseReportAction
{

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.web.report.action.reuse.ReUseReportAction#reUseReport(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, net.rrm.ehour.web.report.form.ReportChartForm, java.lang.String)
	 */
	@Override
	protected ActionForward reUseReport(ActionMapping mapping,
										HttpServletRequest request,
										HttpServletResponse response,
										ReportChartForm chartForm,
										String sessionKey) throws IOException
	{
		AggregateReport	aggregateReport;
		HttpSession		session;
		ActionForward	fwd = null;
		
		session = request.getSession();
		
		request.setAttribute("currencySymbol", WebConstants.getCurrencies().get(config.getCurrency()));
		
		// TODO find out how sessionKey can be null ?
		if (sessionKey != null)
		{
			aggregateReport = (AggregateReport)session.getAttribute(ReportSessionKey.REPORT_AGGREGATE + "_" +  sessionKey);
			
			if (aggregateReport != null)
			{
				fwd = reUseReport(mapping, request, response, chartForm, aggregateReport);
			}
			else
			{
				logger.error("No report in session found for key " + sessionKey);
			}
		}
		else
		{
			logger.error("No session key provided while creating chart?");
		}
		
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
													AggregateReport reportData)  throws IOException;	

}
