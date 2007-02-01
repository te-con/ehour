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

package net.rrm.ehour.report.dao;

import java.util.List;

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.report.project.ProjectAssignmentAggregate;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Reporting data operations 
 * @author Thies
 *
 */

public class ReportDAOHibernateImpl extends HibernateDaoSupport implements ReportDAO
{
	/**
	 * Get cumulated hours per project assignment for a user between a date range
	 * @param userId
	 * @param dateRange
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ProjectAssignmentAggregate> getCumulatedHoursPerAssignmentForUsers(Integer userIds[], DateRange dateRange)
	{
		List		results;
		String[]	keys = new String[3];
		Object[]	params = new Object[3];
		
		keys[0] = "dateStart";
		keys[1] = "dateEnd";
		keys[2] = "userIds";
		
		params[0] = dateRange.getDateStart();
		params[1] = dateRange.getDateEnd();
		params[2] = userIds;
		
		results = getHibernateTemplate().findByNamedQueryAndNamedParam("Report.getCumulatedHoursPerAssignmentOnDateForUserIds"
																		, keys, params);
		
		return results;		
	}
	

	/**
	 * Get cumulated hours per project assignment for users
	 * @param userId
	 * @param dateRange
	 * @return
	 */	
	@SuppressWarnings("unchecked")
	public List<ProjectAssignmentAggregate> getCumulatedHoursPerAssignmentForUsers(Integer userIds[])
	{
		List		results;

		results = getHibernateTemplate().findByNamedQueryAndNamedParam("Report.getCumulatedHoursPerAssignmentForUserIds"
																		, "userIds", userIds);
		
		return results;		
	}	

	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	public DateRange getMinMaxDateTimesheetEntry()
	{
		List<DateRange>	results;
		results = getHibernateTemplate().findByNamedQuery("Report.getMinMaxTimesheetEntryDate");
		return results.get(0);
	}


	/**
	 * Get cumulated hours per project assignment for users, projects
	 * @param userId
	 * @param projectId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ProjectAssignmentAggregate> getCumulatedHoursPerAssignmentForUsers(Integer[] userIds, Integer[] projectIds)
	{
		List		results;
		String[]	keys = new String[2];
		Object[]	params = new Object[2];
		
		keys[0] = "userIds";
		keys[1] = "projectIds";
		
		params[0] = userIds;
		params[1] = projectIds;
		
		results = getHibernateTemplate().findByNamedQueryAndNamedParam("Report.getCumulatedHoursPerAssignmentForUserIdsAndProjectIds"
																		, keys, params);
		return results;
	}

	/**
	 * Get cumulated hours per project assignment for users, projects in a date range
	 * @param userId
	 * @param projectId
	 * @param dateRange
	 * @return
	 *
	 */
	@SuppressWarnings("unchecked")
	public List<ProjectAssignmentAggregate> getCumulatedHoursPerAssignmentForUsers(Integer[] userIds,
																					Integer[] projectIds,
																					DateRange dateRange)
	{
		List		results;
		String[]	keys = new String[4];
		Object[]	params = new Object[4];
		
		keys[0] = "dateStart";
		keys[1] = "dateEnd";
		keys[2] = "userIds";
		keys[3] = "projectIds";
		
		params[0] = dateRange.getDateStart();
		params[1] = dateRange.getDateEnd();
		params[2] = userIds;
		params[3] = projectIds;
		
		results = getHibernateTemplate().findByNamedQueryAndNamedParam("Report.getCumulatedHoursPerAssignmentOnDateForUserIdsAndProjectIds"
																		, keys, params);
		return results;
	}
}
