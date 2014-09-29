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

import com.google.common.collect.Lists;
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.Activity;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.persistence.project.dao.ProjectDao;
<<<<<<< HEAD
import net.rrm.ehour.persistence.report.dao.ReportAggregatedDao;
import net.rrm.ehour.persistence.user.dao.UserDao;
=======
>>>>>>> 9f7e93a... EHV-52 - changed concept, User will be combination of db user and LDAP and UserService combines UserDao and LDAP - always enriching the User object
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.criteria.UserSelectedCriteria;
import net.rrm.ehour.report.reports.ReportData;
<<<<<<< HEAD
import net.rrm.ehour.report.reports.element.ProjectStructuredReportElement;
import net.rrm.ehour.timesheet.service.TimesheetLockService;
import net.rrm.ehour.timesheet.service.TimesheetLockService$;
import org.joda.time.Interval;
import scala.collection.Seq;
=======
import net.rrm.ehour.report.reports.element.ReportElement;
import org.springframework.beans.factory.annotation.Autowired;
>>>>>>> 9f7e93a... EHV-52 - changed concept, User will be combination of db user and LDAP and UserService combines UserDao and LDAP - always enriching the User object

import java.util.Date;
import java.util.List;

/**
 * Abstract report service provides utility methods for dealing
 * with the usercriteria obj
<<<<<<< HEAD
 */

public abstract class AbstractReportServiceImpl<RE extends ProjectStructuredReportElement> {
    private UserDao userDAO;

    private ProjectDao projectDAO;

    private TimesheetLockService lockService;
    protected ReportAggregatedDao reportAggregatedDao;

    AbstractReportServiceImpl() {
    }

    protected AbstractReportServiceImpl(UserDao userDAO, ProjectDao projectDAO, TimesheetLockService lockService, ReportAggregatedDao reportAggregatedDao) {
        this.userDAO = userDAO;
        this.projectDAO = projectDAO;
        this.lockService = lockService;
        this.reportAggregatedDao = reportAggregatedDao;
    }

    ReportData getReportData(ReportCriteria reportCriteria) {
        UserSelectedCriteria userSelectedCriteria = reportCriteria.getUserSelectedCriteria();

        DateRange reportRange = reportCriteria.getReportRange();

        Seq<Interval> lockedDatesAsIntervals = lockService.findLockedDatesInRange(reportRange.getDateStart(), reportRange.getDateEnd());
        List<Date> lockedDates = TimesheetLockService$.MODULE$.intervalToJavaList(lockedDatesAsIntervals);

        List<RE> allReportElements = generateReport(userSelectedCriteria, lockedDates, reportRange);

        if (userSelectedCriteria.isForPm()) {
            List<ProjectStructuredReportElement> elem = evictNonPmReportElements(userSelectedCriteria, allReportElements);
            return new ReportData(lockedDates, elem, reportRange, userSelectedCriteria);
        } else {
            return new ReportData(lockedDates, allReportElements, reportRange, userSelectedCriteria);
        }
    }

    private List<ProjectStructuredReportElement> evictNonPmReportElements(UserSelectedCriteria userSelectedCriteria, List<RE> allReportElements) {
        List<Integer> projectIds = fetchAllowedProjectIds(userSelectedCriteria);

        List<ProjectStructuredReportElement> allowedElements = Lists.newArrayList();

        for (ProjectStructuredReportElement reportElement : allReportElements) {
            if (projectIds.contains(reportElement.getProjectId())) {
                allowedElements.add(reportElement);
            }
        }
        return allowedElements;
    }

    private List<Integer> fetchAllowedProjectIds(UserSelectedCriteria userSelectedCriteria) {
        List<Project> allowedProjects = projectDAO.findActiveProjectsWhereUserIsPM(userSelectedCriteria.getPm());

        List<Integer> projectIds = Lists.newArrayList();

        for (Project allowedProject : allowedProjects) {
            projectIds.add(allowedProject.getProjectId());
        }
        return projectIds;
    }


    private List<RE> generateReport(UserSelectedCriteria userSelectedCriteria, List<Date> lockedDates, DateRange reportRange) {
        boolean noUserRestrictionProvided = userSelectedCriteria.isEmptyDepartments() && userSelectedCriteria.isEmptyUsers();
        boolean noProjectRestrictionProvided = userSelectedCriteria.isEmptyCustomers() && userSelectedCriteria.isEmptyProjects();

        List<Project> projects = null;
        List<User> users = null;

        if (!noProjectRestrictionProvided || !noUserRestrictionProvided) {
            if (noProjectRestrictionProvided) {
                users = getUsersForSelectedDepartments(userSelectedCriteria);
            } else if (noUserRestrictionProvided) {
                projects = getProjects(userSelectedCriteria);
            } else {
                users = getUsersForSelectedDepartments(userSelectedCriteria);
                projects = getProjects(userSelectedCriteria);
            }
        }

        if (userSelectedCriteria.isOnlyBillableProjects() && projects == null) {
            projects = getBillableProjects(userSelectedCriteria);
        }

        return getReportElements(users, projects, lockedDates, reportRange, userSelectedCriteria.isShowZeroBookings());
    }

    /**
     * Get the actual data
     */
    protected abstract List<RE> getReportElements(List<User> users,
                                                  List<Project> projects,
                                                  List<Date> lockedDates,
                                                  DateRange reportRange,
                                                  boolean showZeroBookings);

    /**
     * Get project id's based on selected customers
     */
    private List<Project> getProjects(UserSelectedCriteria userSelectedCriteria) {
        List<Project> projects;

        // No projects selected by the user, use any given customer limitation
        if (userSelectedCriteria.isEmptyProjects()) {
            if (!userSelectedCriteria.isEmptyCustomers()) {
                projects = projectDAO.findProjectForCustomers(userSelectedCriteria.getCustomers(),
                        userSelectedCriteria.isOnlyActiveProjects());
            } else {
                projects = null;
            }
        } else {
            projects = userSelectedCriteria.getProjects();
        }

        return projects;
    }

    private List<Project> getBillableProjects(UserSelectedCriteria criteria) {
        List<Project> projects = criteria.isOnlyActiveProjects() ? projectDAO.findAllActive() : projectDAO.findAll();

        List<Project> filteredProjects = Lists.newArrayList();

        for (Project project : projects) {
            boolean billableFilter = project.isBillable();
            boolean customerFilter = !criteria.isOnlyActiveCustomers() || project.getCustomer().isActive();

            if (billableFilter && customerFilter) {
                filteredProjects.add(project);
            }
        }

        return filteredProjects;
    }

    private List<User> getUsersForSelectedDepartments(UserSelectedCriteria userSelectedCriteria) {
        List<User> users;

        if (userSelectedCriteria.isEmptyUsers()) {
            if (!userSelectedCriteria.isEmptyDepartments()) {
                users = userDAO.findUsersForDepartments(userSelectedCriteria.getDepartments(), userSelectedCriteria.isOnlyActiveUsers());
            } else {
                users = null;
            }
        } else {
            users = userSelectedCriteria.getUsers();
        }

        return users;
    }

    protected List<Activity> getActivitiesWithoutBookings(DateRange reportRange, List<Integer> userIds, List<Integer> projectIds) {
        List<Activity> assignmentsWithoutBookings = reportAggregatedDao.getActivitiesWithoutBookings(reportRange);

        List<Activity> filteredActivitiesWithoutBookings = Lists.newArrayList();

        for (Activity activityWithoutBooking : assignmentsWithoutBookings) {
            boolean passedUserFilter = userIds == null || userIds.contains(activityWithoutBooking.getAssignedUser().getUserId());
            boolean passedProjectFilter = projectIds == null || projectIds.contains(activityWithoutBooking.getProject().getProjectId());

            if (passedUserFilter && passedProjectFilter) {
                filteredActivitiesWithoutBookings.add(activityWithoutBooking);
            }
        }
        return filteredActivitiesWithoutBookings;
    }
=======
 **/

public abstract class AbstractReportServiceImpl<RE extends ReportElement>
{
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

		reportRange = reportCriteria.getReportRange();
		
		ignoreUsers = userCriteria.isEmptyUsers();
		ignoreProjects = userCriteria.isEmptyCustomers() && userCriteria.isEmptyProjects();

		if (ignoreProjects && ignoreUsers)
		{
		}
		else if (ignoreProjects && !ignoreUsers)
		{
			users = userCriteria.getUsers();
		}
		else if (ignoreUsers)
		{
			projects = getProjects(userCriteria);
		}
		else
		{
			users = userCriteria.getUsers();
			projects = getProjects(userCriteria);
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
				projects = projectDAO.findProjectForCustomers(userCriteria.getCustomers(),
																userCriteria.isOnlyActiveProjects());
			}
			else
			{
				projects = null;
			}
		}
		else
		{
			projects = userCriteria.getProjects();
		}
		
		return projects;
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


>>>>>>> 9f7e93a... EHV-52 - changed concept, User will be combination of db user and LDAP and UserService combines UserDao and LDAP - always enriching the User object
}
