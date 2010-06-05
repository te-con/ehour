/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package net.rrm.ehour.report.service;

import java.util.List;

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.project.dao.ProjectDao;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.criteria.UserCriteria;
import net.rrm.ehour.report.reports.ReportData;
import net.rrm.ehour.report.reports.element.ReportElement;
import net.rrm.ehour.user.dao.UserDao;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Abstract report service provides utility methods for dealing
 * with the usercriteria obj
 **/

public abstract class AbstractReportServiceImpl<RE extends ReportElement>
{
	private	Logger	logger = Logger.getLogger(this.getClass());
	
	@Autowired
	private	UserDao		userDAO;

	@Autowired
	private	ProjectDao	projectDAO;
	
	/**
	 * Get report data for criteria
	 * @param reportCriteria
	 * @return
	 */
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
		
		ReportData reportData = new ReportData(getReportElements(users, projects, reportRange), reportRange);
		
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
	public void setUserDAO(UserDao userDAO)
	{
		this.userDAO = userDAO;
	}

	/**
	 * @param projectDAO the projectDAO to set
	 */
	public void setProjectDAO(ProjectDao projectDAO)
	{
		this.projectDAO = projectDAO;
	}

	/**
	 * @return the projectDAO
	 */
	protected ProjectDao getProjectDAO()
	{
		return projectDAO;
	}
}
