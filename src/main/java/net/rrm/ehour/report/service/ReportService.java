/**
 * Created on Nov 4, 2006
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

package net.rrm.ehour.report.service;

import java.util.Calendar;
import java.util.List;

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.report.dto.AvailReportCriteria;
import net.rrm.ehour.report.dto.ProjectReport;
import net.rrm.ehour.report.dto.ReportCriteria;


/**
 * Provides reporting services on timesheets.
 * 
 * @author Thies
 *
 */

public interface ReportService
{
	
	/**
	 * Get the booked hours per project assignment for a month
	 * @param userId
	 * @param calendar
	 * @return List with projectReport objects
	 */
	public List<ProjectReport> getHoursPerAssignmentInMonth(Integer userId, Calendar calendar);

	/**
	 * Get the booked hours per project assignment for a date range
	 * @param userId
	 * @param calendar
	 * @return List with projectReport objects
	 */

	public List<ProjectReport> getHoursPerAssignmentInRange(Integer userId, DateRange dateRange);
	
	
	/**
	 * Get available report criteria (users, departments, customers, etc)
	 * @param bookedOnly to list only criteria which have entries booked 
	 * @return
	 */
	public AvailReportCriteria getAvailableReportCriteria(ReportCriteria reportCriteria);
}
