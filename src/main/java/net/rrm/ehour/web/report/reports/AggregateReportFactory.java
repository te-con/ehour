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

import java.util.ArrayList;
import java.util.List;

import net.rrm.ehour.report.reports.ReportData;
import net.rrm.ehour.web.util.WebConstants;

/**
 * Factory for aggregate reports
 **/

public class AggregateReportFactory
{
	/**
	 * Create reports based on user role
	 * @param userRole
	 * @param reportData
	 * @return
	 */
	public static List<AggregateReport> createReports(String userRole, ReportData reportData)
	{
		List<AggregateReport>	reports = null;
		
		if (userRole.equals(WebConstants.ROLE_CONSULTANT))
		{
			reports = createConsultantRoleReports(reportData);
		}
		else if (userRole.equals(WebConstants.ROLE_REPORT))
		{
			reports = creatReportRoleReports(reportData);
		}
		return reports;
	}
	
	/**
	 * Create reports specific for consultant role
	 * @param reportData
	 * @return
	 */	
	private static List<AggregateReport> createConsultantRoleReports(ReportData reportData)
	{
		AggregateReport	customerReport;
		List<AggregateReport>	reports = new ArrayList<AggregateReport>();
		
		customerReport = new CustomerReport();
		customerReport.initialize(reportData);
		
		reports.add(customerReport);
		
		return reports;
	}
	
	/**
	 * Create reports specific for report role
	 * @param reportData
	 * @return
	 */
	private static List<AggregateReport> creatReportRoleReports(ReportData reportData)
	{
		AggregateReport			aggReport;
		List<AggregateReport>	reports = new ArrayList<AggregateReport>();
		
		aggReport = new CustomerReport();
		aggReport.initialize(reportData);
		reports.add(aggReport);

		aggReport = new UserReport();
		aggReport.initialize(reportData);
		reports.add(aggReport);

		return reports;
		
	}	
}
