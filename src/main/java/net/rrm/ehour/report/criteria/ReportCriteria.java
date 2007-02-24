/**
 * Created on Jan 14, 2007
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

package net.rrm.ehour.report.criteria;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.List;

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.project.domain.Project;
import net.rrm.ehour.report.service.ReportCriteriaService;
import net.rrm.ehour.user.domain.User;
import net.rrm.ehour.util.DateUtil;

import org.apache.log4j.Logger;


/**
 * TODO 
 **/

public class ReportCriteria
{
	public static final int		UPDATE_ALL = 0;
	public static final int		UPDATE_CUSTOMERS = 1;
	public static final int		UPDATE_PROJECTS = 2;
	public static final int		UPDATE_USERS = 3;
	
	private	Logger				logger = Logger.getLogger(this.getClass());
	private	AvailableCriteria	availableCriteria;
	private	UserCriteria		userCriteria;
	private	ReportCriteriaService	reportCriteriaService;
	
	/**
	 * To avoid NPE's
	 *
	 */
	public ReportCriteria()
	{
		userCriteria = new UserCriteria();
		availableCriteria = new AvailableCriteria();
	}
	
	/**
	 * Initialize the available criteria so they're available
	 * right after object construction
	 *
	 */
	public void initialize()
	{
		logger.debug("Initializing report criteria");
		
		availableCriteria = new AvailableCriteria();
		userCriteria = new UserCriteria();
	}
	
	/**
	 * Get report range, use the available criteria if the user didn't supply any (yet)
	 * @return
	 */
	
	public DateRange getReportRange()
	{
		DateRange	reportRange;
		
		if (userCriteria.getReportRange() == null)
		{
			reportRange = availableCriteria.getReportRange();
			userCriteria.setReportRange(reportRange);
		}
		else
		{
			reportRange = userCriteria.getReportRange();
		}
		
		// if no timesheets were specified, use the current month as the range
		if (reportRange == null || reportRange.isEmpty())
		{
			reportRange = DateUtil.calendarToMonthRange(new GregorianCalendar());
		}
		
		logger.debug("Using report range " + reportRange);
		
		return reportRange;
	}
	
	/**
	 * 
	 *
	 */
	public void updateAvailableCriteria()
	{
		updateAvailableCriteria(ReportCriteria.UPDATE_ALL);
	}
	
	/**
	 * 
	 *
	 */
	public void updateAvailableCriteria(int updateType)
	{
		reportCriteriaService.syncUserReportCriteria(this, updateType);
		
		if (!userCriteria.isSingleUser())
		{
			checkIfUserCriteriaAreAvailable();
		}
	}	
	/**
	 * After the available criteria are synced, check if the user criteria are still valid
	 *
	 */
	private void checkIfUserCriteriaAreAvailable()
	{
		checkProjects();
		checkUsers();
	}
	
	/**
	 *  TODO use commons-collection ListUtils.retainAll however commons-config depends on collections 2.1 while
	 *  retainAll is introduced in 3.2
	 *
	 */
	private void checkUsers()
	{
		
		List<Integer>	userIds;
		List<Integer>	userIdsValid = new ArrayList<Integer>();
		
		if (userCriteria.getUserIds() != null)
		{
			userIds = Arrays.asList(userCriteria.getUserIds());
			
			for (Integer userId : userIds)
			{
				if (availableCriteria.getUsers().contains(new User(userId)))
				{
					userIdsValid.add(userId);
				}
			}
			
			userCriteria.setUserIds(userIdsValid.toArray(new Integer[userIdsValid.size()]));
		}
	}
		
	/**
	 * 
	 *
	 */
	private void checkProjects()
	{
		List<Integer>	projectIds;
		List<Integer>	projectIdsValid = new ArrayList<Integer>();
		
		if (userCriteria.getProjectIds() != null)
		{
			projectIds = Arrays.asList(userCriteria.getProjectIds());
			
			for (Integer projectId : projectIds)
			{
				if (availableCriteria.getProjects().contains(new Project(projectId)))
				{
					projectIdsValid.add(projectId);
				}
			}
			
			userCriteria.setProjectIds(projectIdsValid.toArray(new Integer[projectIdsValid.size()]));
		}
	}

	/**
	 * @return the userCriteria
	 */
	public UserCriteria getUserCriteria()
	{
		return userCriteria;
	}
	
	/**
	 * @param userCriteria the userCriteria to set
	 */
	public void setUserCriteria(UserCriteria userCriteria)
	{
		this.userCriteria = userCriteria;
	}

	/**
	 * @param reportCriteriaService the reportCriteriaService to set
	 */
	public void setReportCriteriaService(ReportCriteriaService reportCriteriaService)
	{
		this.reportCriteriaService = reportCriteriaService;
	}

	/**
	 * @return the availableCriteria
	 */
	public AvailableCriteria getAvailableCriteria()
	{
		return availableCriteria;
	}

	/**
	 * @param availableCriteria the availableCriteria to set
	 */
	public void setAvailableCriteria(AvailableCriteria availableCriteria)
	{
		this.availableCriteria = availableCriteria;
	}
}