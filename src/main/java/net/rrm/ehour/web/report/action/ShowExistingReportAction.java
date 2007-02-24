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

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.rrm.ehour.report.reports.ReportData;
import net.rrm.ehour.web.report.form.ReportChartForm;
import net.rrm.ehour.web.report.reports.AggregateReport;
import net.rrm.ehour.web.report.reports.AggregateReportFactory;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * Show report by re-using reportData in the session
 **/

public class ShowExistingReportAction extends ReUseReportAction
{
	protected ActionForward reUseReport(ActionMapping mapping,
										HttpServletRequest request,
										HttpServletResponse response,
										ReportChartForm chartForm,
										ReportData reportData)  throws IOException
	{
		createAndStoreReport(request, chartForm.getReportName(), chartForm.getForId(), reportData);
		return mapping.findForward(chartForm.getReportName());
	}

	/**
	 * Create and store report
	 * @param request
	 * @param reportName
	 * @param reportData
	 */
	protected void createAndStoreReport(HttpServletRequest request, String reportName, Integer forId, ReportData reportData)
	{
		AggregateReport	report;
		
		if (forId == null || forId.intValue() == 0)
		{
			report = AggregateReportFactory.createReport(reportName, reportData);
		}
		else
		{
			report = AggregateReportFactory.createReport(reportName, reportData, forId);
		}
		
		request.setAttribute(reportName, report);
	}
}
