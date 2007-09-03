/**
 * Created on Jan 14, 2007
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
 *
 * This Software is copyright TE-CON 2007. This Software is not open source by definition. The source of the Software is available for educational purposes.
 * TE-CON holds all the ownership rights on the Software.
 * TE-CON freely grants the right to use the Software. Any reproduction or modification of this Software, whether for commercial use or open source,
 * is subject to obtaining the prior express authorization of TE-CON.
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.report.criteria;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.project.domain.Project;
import net.rrm.ehour.user.domain.User;
import net.rrm.ehour.util.DateUtil;

import org.apache.log4j.Logger;

/**
 * Container for the report criteria, available and user selected 
 **/

public class ReportCriteria implements Serializable
{
	private static final long serialVersionUID = 7406265452950554098L;
	private	transient Logger	logger = Logger.getLogger(this.getClass());
	private	AvailableCriteria	availableCriteria;
	private	UserCriteria		userCriteria;
	
	/**
	 * Default constructor
	 *
	 */
	public ReportCriteria()
	{
		userCriteria = new UserCriteria();
		availableCriteria = new AvailableCriteria();
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
		}
		else
		{
			reportRange = userCriteria.getReportRange();
			
			if (reportRange.getDateStart() == null)
			{
				if (availableCriteria == null || availableCriteria.getReportRange() == null)
				{
					reportRange.setDateStart(new Date());
				}
				else
				{
					reportRange.setDateStart(availableCriteria.getReportRange().getDateStart());
				}
			}
			
			if (reportRange.getDateEnd() == null)
			{
				if (availableCriteria == null || availableCriteria.getReportRange() == null)
				{
					reportRange.setDateEnd(new Date());
				}
				else
				{
					reportRange.setDateEnd(availableCriteria.getReportRange().getDateEnd());
				}
				
			}
		}

		userCriteria.setReportRange(reportRange);

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
	public void validate(int updateType)
	{
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
		List<Integer>	userIdsValid = new ArrayList<Integer>();
		
		if (userCriteria.getUserIds() != null)
		{
			for (Integer userId : userCriteria.getUserIds())
			{
				if (availableCriteria.getUsers().contains(new User(userId)))
				{
					userIdsValid.add(userId);
				}
			}
			
			userCriteria.setUserIds(userIdsValid);
		}
	}
		
	/**
	 * 
	 *
	 */
	private void checkProjects()
	{
		List<Integer>	projectIdsValid = new ArrayList<Integer>();
		
		if (userCriteria.getProjectIds() != null)
		{
			for (Integer projectId : userCriteria.getProjectIds())
			{
				if (availableCriteria.getProjects().contains(new Project(projectId)))
				{
					projectIdsValid.add(projectId);
				}
			}
			
			userCriteria.setProjectIds(projectIdsValid);
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