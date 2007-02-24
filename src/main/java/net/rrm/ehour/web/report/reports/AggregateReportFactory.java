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

package net.rrm.ehour.web.report.reports;

import net.rrm.ehour.report.reports.ReportData;

/**
 * Factory for aggregate reports
 **/

public class AggregateReportFactory
{
	public final static String	USER_REPORT ="userReport";
	public final static String	CUSTOMER_REPORT ="customerReport";
	public final static String	PROJECT_REPORT ="projectReport";
	
	
	/**
	 * 
	 * @param reportName
	 * @param reportData
	 * @return
	 */
	public static AggregateReport createReport(String reportName, ReportData reportData)
	{
		return createReport(reportName, reportData, null);
	}
	
	/**
	 * Create reports based on user role
	 * @param userRole
	 * @param reportData
	 * @return
	 */
	public static AggregateReport createReport(String reportName, ReportData reportData, Integer forId)
	{
		AggregateReport<?, ?, Integer>	report;
		
		// TODO make this a bit more configurable
		if (reportName.equals(USER_REPORT))
		{
			report = new UserReport();
		}
		else if (reportName.equals(CUSTOMER_REPORT))
		{
			report = new CustomerReport();
		}
		else if (reportName.equals(PROJECT_REPORT))
		{
			report = new ProjectReport();
		}
		else
		{
			return null;
		}
		
		report.initialize(reportData, forId);

		return report;
	}
}
