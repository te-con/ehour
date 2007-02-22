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

import java.util.List;
import java.util.Map;

import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.reports.ProjectAssignmentAggregate;
import net.rrm.ehour.report.reports.ReportData;
import net.rrm.ehour.user.domain.User;

import org.apache.log4j.Logger;

/**
 * UserReport
 **/

public class UserReport extends AggregateReport<User>
{
	private	ReportCriteria	reportCriteria;
	private	Logger			logger = Logger.getLogger(this.getClass());
	private	Map<User, List<ProjectAssignmentAggregate>>	reportMap;

	/**
	 * Initialize user report
	 */
	public void initialize(ReportData reportData)
	{
		logger.debug("Initializing user report");
		
	}

	public String getReportName()
	{
		return "userReport";
	}
}
