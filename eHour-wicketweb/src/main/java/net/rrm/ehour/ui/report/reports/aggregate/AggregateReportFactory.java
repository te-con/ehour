/**
 * Created on 22-feb-2007
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

package net.rrm.ehour.ui.report.reports.aggregate;

import net.rrm.ehour.report.reports.ReportDataAggregate;

/**
 * Factory for aggregate reports
 **/

public class AggregateReportFactory
{
	/**
	 * 
	 * @param reportName
	 * @param reportDataAggregate
	 * @return
	 */
	public static AggregateReport<?, ?, Integer> createReport(ReportType reportType, ReportDataAggregate reportDataAggregate)
	{
		return createReport(reportType, reportDataAggregate, null);
	}
	
	/**
	 * Create reports based on user role
	 * @param userRole
	 * @param reportDataAggregate
	 * @return
	 */
	public static AggregateReport<?, ?, Integer> createReport(ReportType reportType, ReportDataAggregate reportDataAggregate, Integer forId)
	{
		AggregateReport<?, ?, Integer>	report;

		if (reportType == ReportType.USER_REPORT)
		{
			report = new UserReport();
		}
		else if (reportType == ReportType.CUSTOMER_REPORT)
		{
			report = new CustomerReport();
		}
		else if (reportType == ReportType.PROJECT_REPORT)
		{
			report = new ProjectReport();
		}
		else
		{
			return null;
		}
		
		report.initialize(reportDataAggregate, forId);

		return report;
	}
}
