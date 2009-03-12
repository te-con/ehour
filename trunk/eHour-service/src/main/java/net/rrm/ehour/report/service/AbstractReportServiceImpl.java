/**
 * Created on May 2, 2008
 * Author: Thies
 *
 * Copyright (C) 2007 TE-CON, All Rights Reserved.
 *
 * This Software is copyright TE-CON 2007. This Software is not open source by definition. The source of the Software is available for educational purposes.
 * TE-CON holds all the ownership rights on the Software.
 * TE-CON freely grants the right to use the Software. Any reproduction or modification of this Software, whether for commercial use or open source,
 * is subject to obtaining the prior express authorization of TE-CON.
 * 
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.report.service;

import java.util.List;

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.project.dao.ProjectDAO;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.criteria.UserCriteria;
import net.rrm.ehour.report.reports.ReportData;
import net.rrm.ehour.report.reports.element.ReportElement;
import net.rrm.ehour.user.dao.UserDAO;

import org.apache.log4j.Logger;

/**
 * Abstract report service provides utility methods for dealing
 * with the usercriteria obj
 **/

public abstract class AbstractReportServiceImpl<RE extends ReportElement>
{
	private	Logger	logger = Logger.getLogger(this.getClass());
	private	UserDAO		userDAO;
	private	ProjectDAO	projectDAO;
	
	/**
	 * Get report data for criteria
	 * @param reportCriteria
	 * @return
	 */
	// TODO why reportCrweria? userCriteria should be sufficient
	protected ReportData getReportData(ReportCriteria reportCriteria)
	{
		UserCriteria	userCriteria;
		List<Project>	projects = null;
		List<User>		users = null;
		boolean			ignoreUsers;
		boolean			ignoreProjects;
		DateRange		reportRange;
		
		userCriteria = reportCriteria.getUserCriteria();
		logger.debug("Getting report data for " + userCriteria);
		
		reportRange = reportCriteria.getReportRange();
		
		ignoreUsers = userCriteria.isEmptyDepartments() && userCriteria.isEmptyUsers();
		ignoreProjects = userCriteria.isEmptyCustomers() && userCriteria.isEmptyProjects();
		
		if (ignoreProjects && ignoreUsers)
		{
			logger.debug("creating full report");
		}
		else if (ignoreProjects && !ignoreUsers)
		{
			logger.debug("creating report for only selected users");
			users = getUsers(userCriteria);
		}
		else if (!ignoreProjects && ignoreUsers)
		{
			logger.debug("creating report for only selected project");
			projects = getProjects(userCriteria);
		}
		else
		{
			logger.debug("creating report for selected users & projects");
			users = getUsers(userCriteria);
			projects = getProjects(userCriteria);
		}		
		
		ReportData reportData = new ReportData(getReportElements(users, projects, reportRange));
		
		return reportData;
	}

	/**
	 * Get the actual data
	 * @param users
	 * @param projects
	 * @param reportRange
	 * @return
	 */
	protected abstract List<RE> getReportElements(List<User> users,
													List<Project >projects,
													DateRange reportRange);
	
	/**
	 * Get project id's based on selected customers
	 * @param userCriteria
	 * @return
	 */
	protected List<Project> getProjects(UserCriteria userCriteria)
	{
		List<Project>	projects;
		
		// No projects selected by the user, use any given customer limitation 
		if (userCriteria.isEmptyProjects())
		{
			if (!userCriteria.isEmptyCustomers())
			{
				logger.debug("Using customers to determine projects");
				projects = projectDAO.findProjectForCustomers(userCriteria.getCustomers(),
																userCriteria.isOnlyActiveProjects());
			}
			else
			{
				logger.debug("No customers or projects selected");
				projects = null;
			}
		}
		else
		{
			logger.debug("Using user provided projects");
			projects = userCriteria.getProjects();
		}
		
		return projects;
	}	
	
	/**
	 * Get users based on selected departments
	 * @param userCriteria
	 * @return
	 */
	protected List<User> getUsers(UserCriteria userCriteria)
	{
		List<User>		users;
		
		if (userCriteria.isEmptyUsers())
		{
			if (!userCriteria.isEmptyDepartments())
			{
				logger.debug("Using departments to determine users");
				users = userDAO.findUsersForDepartments(null,
														userCriteria.getDepartments(),
														userCriteria.isOnlyActiveUsers());
			}
			else
			{
				logger.debug("No departments or users selected");
				users = null;
			}
		}
		else
		{
			logger.debug("Using user provided users");
			users = userCriteria.getUsers();
		}
		
		return users;
	}


	/**
	 * @param userDAO the userDAO to set
	 */
	public void setUserDAO(UserDAO userDAO)
	{
		this.userDAO = userDAO;
	}

	/**
	 * @param projectDAO the projectDAO to set
	 */
	public void setProjectDAO(ProjectDAO projectDAO)
	{
		this.projectDAO = projectDAO;
	}

	/**
	 * @return the projectDAO
	 */
	protected ProjectDAO getProjectDAO()
	{
		return projectDAO;
	}
}
