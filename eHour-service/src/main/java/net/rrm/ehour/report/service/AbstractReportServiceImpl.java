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

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.persistence.project.dao.ProjectDao;
import net.rrm.ehour.persistence.user.dao.UserDao;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.criteria.UserSelectedCriteria;
import net.rrm.ehour.report.reports.ReportData;
import net.rrm.ehour.report.reports.element.ReportElement;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Abstract report service provides utility methods for dealing
 * with the usercriteria obj
 **/

public abstract class AbstractReportServiceImpl<RE extends ReportElement>
{
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
		UserSelectedCriteria userSelectedCriteria;
		List<Project>	projects = null;
		List<User>		users = null;
		boolean			ignoreUsers;
		boolean			ignoreProjects;
		DateRange		reportRange;
		
		userSelectedCriteria = reportCriteria.getUserSelectedCriteria();

		reportRange = reportCriteria.getReportRange();
		
		ignoreUsers = userSelectedCriteria.isEmptyDepartments() && userSelectedCriteria.isEmptyUsers();
		ignoreProjects = userSelectedCriteria.isEmptyCustomers() && userSelectedCriteria.isEmptyProjects();

        if (!ignoreProjects || !ignoreUsers) {
            if (ignoreProjects)
            {
                users = getUsers(userSelectedCriteria);
            }
            else if (ignoreUsers) {
                projects = getProjects(userSelectedCriteria);
            } else {
                users = getUsers(userSelectedCriteria);
                projects = getProjects(userSelectedCriteria);
            }
        }

        return new ReportData(getReportElements(users, projects, reportRange), reportRange);
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
	 * @param userSelectedCriteria
	 * @return
	 */
	protected List<Project> getProjects(UserSelectedCriteria userSelectedCriteria)
	{
		List<Project>	projects;
		
		// No projects selected by the user, use any given customer limitation 
		if (userSelectedCriteria.isEmptyProjects())
		{
			if (!userSelectedCriteria.isEmptyCustomers())
			{
				projects = projectDAO.findProjectForCustomers(userSelectedCriteria.getCustomers(),
																userSelectedCriteria.isOnlyActiveProjects());
			}
			else
			{
				projects = null;
			}
		}
		else
		{
			projects = userSelectedCriteria.getProjects();
		}
		
		return projects;
	}	
	
	/**
	 * Get users based on selected departments
	 * @param userSelectedCriteria
	 * @return
	 */
	protected List<User> getUsers(UserSelectedCriteria userSelectedCriteria)
	{
		List<User>		users;
		
		if (userSelectedCriteria.isEmptyUsers())
		{
			if (!userSelectedCriteria.isEmptyDepartments())
			{
				users = userDAO.findUsersForDepartments(null,
														userSelectedCriteria.getDepartments(),
														userSelectedCriteria.isOnlyActiveUsers());
			}
			else
			{
				users = null;
			}
		}
		else
		{
			users = userSelectedCriteria.getUsers();
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
