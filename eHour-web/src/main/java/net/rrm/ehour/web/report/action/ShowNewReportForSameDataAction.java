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

import javax.servlet.http.HttpSession;

import net.rrm.ehour.report.reports.ReportDataAggregate;
import net.rrm.ehour.web.report.form.ReportForm;
import net.rrm.ehour.web.report.reports.aggregate.AggregateReport;
import net.rrm.ehour.web.report.reports.aggregate.AggregateReportFactory;

/**
 * Show report by re-using reportData in the session and creating a new aggregate one
 **/

public class ShowNewReportForSameDataAction extends ReUseReportAction
{
	/**
	 * Get cached aggregate report from session (mainly for excel exports) 
	 */
	@Override
	protected AggregateReport getAggregateReport(HttpSession session,
												 String reportName,
												 ReportForm reportForm,
												 ReportDataAggregate reportDataAggregate)
	{
		AggregateReport	report;
		Integer			forId;
		
		forId = reportForm.getForId();
		
		logger.debug("Creating new " + reportName + " aggregate report");
		
		if (forId == null || forId.intValue() == 0)
		{
			report = AggregateReportFactory.createReport(reportName, reportDataAggregate);
		}
		else
		{
			report = AggregateReportFactory.createReport(reportName, reportDataAggregate, forId);
		}
		
		return report;
	}

	/**
	 * Session data should be replaced
	 */
	@Override
	protected boolean isReplaceSessionData()
	{
		return true;
	}

}
