<<<<<<< HEAD
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
import net.rrm.ehour.audit.annot.NonAuditable;
import net.rrm.ehour.domain.*;
import net.rrm.ehour.persistence.customer.dao.CustomerDao;
import net.rrm.ehour.persistence.report.dao.ReportAggregatedDao;
import net.rrm.ehour.report.criteria.AvailableCriteria;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.criteria.ReportCriteriaUpdateType;
import net.rrm.ehour.report.criteria.UserSelectedCriteria;
import net.rrm.ehour.timesheet.service.TimesheetLockService;
import net.rrm.ehour.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import scala.Tuple2;
import scala.collection.convert.WrapAsJava$;

import java.util.List;

import static net.rrm.ehour.report.criteria.ReportCriteriaUpdateType.UPDATE_CUSTOMERS_AND_PROJECTS;

/**
 * Report Criteria services
 */
@NonAuditable
@Service("reportCriteriaService")
public class ReportCriteriaServiceImpl implements ReportCriteriaService {
    private ReportAggregatedDao reportAggregatedDAO;

    private CustomerAndProjectCriteriaFilter customerAndProjectCriteriaFilter;

    private UserAndDepartmentCriteriaFilter userAndDepartmentCriteriaFilter;

    private IndividualUserCriteriaSync individualUserCriteriaSync;
    private TimesheetLockService lockService;
    private UserService userService;
    private CustomerDao customerDao;

    protected ReportCriteriaServiceImpl() {
    }

    @Autowired
    public ReportCriteriaServiceImpl(ReportAggregatedDao reportAggregatedDAO,
                                     CustomerAndProjectCriteriaFilter customerAndProjectCriteriaFilter,
                                     UserAndDepartmentCriteriaFilter userAndDepartmentCriteriaFilter,
                                     IndividualUserCriteriaSync individualUserCriteriaSync,
                                     TimesheetLockService lockService,
                                     UserService userService,
                                     CustomerDao customerDao) {
        this.reportAggregatedDAO = reportAggregatedDAO;
        this.customerAndProjectCriteriaFilter = customerAndProjectCriteriaFilter;
        this.userAndDepartmentCriteriaFilter = userAndDepartmentCriteriaFilter;
        this.individualUserCriteriaSync = individualUserCriteriaSync;
        this.lockService = lockService;
        this.userService = userService;
        this.customerDao = customerDao;
    }

    /**
     * Update available report criteria
     */
    public ReportCriteria syncUserReportCriteria(ReportCriteria reportCriteria, ReportCriteriaUpdateType updateType) {
        UserSelectedCriteria userSelectedCriteria = reportCriteria.getUserSelectedCriteria();
        AvailableCriteria availCriteria = reportCriteria.getAvailableCriteria();

        List<TimesheetLock> timesheetLocks = Lists.newArrayList(WrapAsJava$.MODULE$.asJavaCollection(lockService.findAll()));
        availCriteria.setTimesheetLocks(timesheetLocks);

        if (userSelectedCriteria.isForGlobalReport() || userSelectedCriteria.isForPm() || userSelectedCriteria.isCustomerReporter()) {
            if (updateType == UPDATE_CUSTOMERS_AND_PROJECTS ||
                    updateType == ReportCriteriaUpdateType.UPDATE_ALL) {

                List<Customer> updatedAvailableCustomers;

                if (userSelectedCriteria.isCustomerReporter()) {
                    updatedAvailableCustomers = customerDao.findAllCustomersHavingReporter(userSelectedCriteria.getLoggedInUser());
                } else {
                    Tuple2<List<Customer>, List<Project>> available = customerAndProjectCriteriaFilter.getAvailableCustomers(userSelectedCriteria);

                    updatedAvailableCustomers = available._1();

                    List<Project> updatedAvailableProjects = available._2();
                    availCriteria.setProjects(updatedAvailableProjects);
                    preserveSelectedProjects(userSelectedCriteria, updatedAvailableProjects);
                }
                availCriteria.setCustomers(updatedAvailableCustomers);
                preserveSelectedCustomers(userSelectedCriteria, updatedAvailableCustomers);

            }

            if (updateType == ReportCriteriaUpdateType.UPDATE_USERS_AND_DEPTS ||
                    updateType == ReportCriteriaUpdateType.UPDATE_ALL) {

                if (userSelectedCriteria.isCustomerReporter()) {
                    availCriteria.setUsers(userService.getAllUsersAssignedToCustomers(userSelectedCriteria.getCustomers()));
                } else {
                    Tuple2<List<UserDepartment>, List<User>> avail = userAndDepartmentCriteriaFilter.getAvailableUsers(userSelectedCriteria);

                    availCriteria.setUserDepartments(avail._1());
                    availCriteria.setUsers(avail._2());
                }
            }

            if (updateType == ReportCriteriaUpdateType.UPDATE_ALL) {
                availCriteria.setReportRange(reportAggregatedDAO.getMinMaxDateTimesheetEntry());
            }
        } else {
            individualUserCriteriaSync.syncCriteriaForIndividualUser(reportCriteria);
        }

        return reportCriteria;
    }

    private void preserveSelectedProjects(UserSelectedCriteria userSelectedCriteria, List<Project> updatedAvailableProjects) {
        List<Project> updatedSelectedProjects = Lists.newArrayList();

        if (userSelectedCriteria.getProjects() != null) {
            for (Project updatedAvailableProject : updatedAvailableProjects) {
                if (userSelectedCriteria.getProjects().contains(updatedAvailableProject)) {
                    updatedSelectedProjects.add(updatedAvailableProject);
                }
            }
        }

        userSelectedCriteria.setProjects(updatedSelectedProjects);
    }

    private void preserveSelectedCustomers(UserSelectedCriteria userSelectedCriteria, List<Customer> updatedAvailableCustomers) {
        List<Customer> updatedSelectedCustomers = Lists.newArrayList();

        if (userSelectedCriteria.getCustomers() != null) {
            for (Customer updatedAvailableCustomer : updatedAvailableCustomers) {
                if (userSelectedCriteria.getCustomers().contains(updatedAvailableCustomer)) {
                    updatedSelectedCustomers.add(updatedAvailableCustomer);
                }
            }
        }

        userSelectedCriteria.setCustomers(updatedSelectedCustomers);
    }
}
=======
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.rrm.ehour.audit.annot.NonAuditable;
import net.rrm.ehour.domain.Activity;
import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.persistence.activity.dao.ActivityDao;
import net.rrm.ehour.persistence.customer.dao.CustomerDao;
import net.rrm.ehour.persistence.project.dao.ProjectDao;
import net.rrm.ehour.persistence.report.dao.ReportAggregatedDao;
import net.rrm.ehour.persistence.user.dao.UserDao;
import net.rrm.ehour.project.util.ProjectUtil;
import net.rrm.ehour.report.criteria.AvailableCriteria;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.criteria.ReportCriteriaUpdateType;
import net.rrm.ehour.report.criteria.UserCriteria;
import net.rrm.ehour.user.service.UserService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Report Criteria services
 **/
@NonAuditable
@Service("reportCriteriaService")
public class ReportCriteriaServiceImpl implements ReportCriteriaService {
	@Autowired
	private UserDao userDAO;

	@Autowired
	private CustomerDao customerDAO;
	@Autowired
	private ProjectDao projectDAO;

	@Autowired
	private ActivityDao activityDao;

	@Autowired
	private UserService userService;

	@Autowired
	private ReportAggregatedDao reportAggregatedDAO;

	private static final Logger LOGGER = Logger.getLogger(ReportCriteriaServiceImpl.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.rrm.ehour.persistence.persistence.report.service.ReportCriteriaService
	 * #
	 * syncUserReportCriteria(net.rrm.ehour.persistence.persistence.report.criteria
	 * .ReportCriteria)
	 */
	public ReportCriteria syncUserReportCriteria(ReportCriteria reportCriteria) {
		return syncUserReportCriteria(reportCriteria, null);
	}

	/**
	 * Update available report criteria
	 */

	public ReportCriteria syncUserReportCriteria(ReportCriteria reportCriteria, ReportCriteriaUpdateType updateType) {
		UserCriteria userCriteria = reportCriteria.getUserCriteria();
		AvailableCriteria availCriteria = reportCriteria.getAvailableCriteria();

		if (userCriteria.isSingleUser()) {
			syncCriteriaForSingleUser(reportCriteria);
		} else {
			if (updateType == ReportCriteriaUpdateType.UPDATE_CUSTOMERS || updateType == ReportCriteriaUpdateType.UPDATE_ALL) {
				availCriteria.setCustomers(getAvailableCustomers(userCriteria));
			}

			if (updateType == ReportCriteriaUpdateType.UPDATE_PROJECTS || updateType == ReportCriteriaUpdateType.UPDATE_ALL) {
				availCriteria.setProjects(getAvailableProjects(userCriteria));
			}

			if (updateType == ReportCriteriaUpdateType.UPDATE_ALL) {
				availCriteria.setReportRange(reportAggregatedDAO.getMinMaxDateTimesheetEntry());
			}

			if (updateType == ReportCriteriaUpdateType.UPDATE_USERS || updateType == ReportCriteriaUpdateType.UPDATE_ALL) {
				availCriteria.setUsers(getAvailableUsers(userCriteria));
			}
		}

		return reportCriteria;
	}

	/**
	 * Get available users
	 * 
	 * @param userCriteria
	 * @return
	 */
	private List<User> getAvailableUsers(UserCriteria userCriteria) {
		List<User> users = null;
		if (userCriteria.isCustomerReporter()) {
			Set<User> allUsersAssignedToCustomers = userService.getAllUsersAssignedToCustomers(userCriteria.getCustomers(), userCriteria.isOnlyActiveUsers());
			if (allUsersAssignedToCustomers != null && !allUsersAssignedToCustomers.isEmpty()) {
				users = new ArrayList<User>();
				users.addAll(allUsersAssignedToCustomers);
			}
		} else {
			users = userDAO.findUsersByNameMatch(userCriteria.getUserFilter(), userCriteria.isOnlyActiveUsers());
		}
		return users;
	}

	/**
	 * Get available customers
	 * 
	 * @param userCriteria
	 * @return
	 */
	private List<Customer> getAvailableCustomers(UserCriteria userCriteria) {
		List<Customer> customers = fetchCustomers(userCriteria);

		Collections.sort(customers);

		userCriteria.setCustomers(customers);

		return customers;
	}

	private List<Customer> fetchCustomers(UserCriteria userCriteria) {
		List<Customer> customers;
		if (userCriteria.isCustomerReporter()) {
			customers = customerDAO.findAllCustomersHavingReporter(userCriteria.getLoggedInUser(), userCriteria.isOnlyActiveCustomers());
		} else {
			if (userCriteria.isOnlyActiveCustomers()) {
				customers = customerDAO.findAllActive();
			} else {
				customers = customerDAO.findAll();
			}
		}
		return customers;
	}

	/**
	 * Get available projects depended on the userCriteria
	 * 
	 * @param userCriteria
	 * @return
	 */
	private List<Project> getAvailableProjects(UserCriteria userCriteria) {
		List<Project> projects;

		if (userCriteria.isEmptyCustomers()) {
			projects = fetchProjects(userCriteria);
		} else {
			projects = projectDAO.findProjectForCustomers(userCriteria.getCustomers(), userCriteria.isOnlyActiveProjects());
		}

		projects = checkForOnlyBillableProjects(userCriteria, projects);

		return projects;
	}

	private List<Project> checkForOnlyBillableProjects(UserCriteria userCriteria, List<Project> projects) {
		if (userCriteria.isOnlyBillableProjects()) {
			projects = ProjectUtil.getBillableProjects(projects);
		}
		return projects;
	}

	private List<Project> fetchProjects(UserCriteria userCriteria) {
		List<Project> projects;
		if (userCriteria.isOnlyActiveProjects()) {
			LOGGER.debug("Fetching only active projects");

			projects = projectDAO.findAllActive();
		} else {
			LOGGER.debug("Fetching all projects");

			projects = projectDAO.findAll();
		}
		return projects;
	}

	/**
	 * Sync criteria for users, only customers & projects are displayed for
	 * users in this list
	 * 
	 * @param reportCriteria
	 */
	private void syncCriteriaForSingleUser(ReportCriteria reportCriteria) {
		Set<Customer> customers = new HashSet<Customer>();
		Set<Project> projects = new HashSet<Project>();
		AvailableCriteria availCriteria = reportCriteria.getAvailableCriteria();
		User user;

		user = reportCriteria.getUserCriteria().getUsers().get(0);

		List<Activity> activities = activityDao.findActivitiesForUser(user.getUserId(), reportCriteria.getUserCriteria().getReportRange());

		for (Activity activity : activities) {
			customers.add(activity.getProject().getCustomer());
			projects.add(activity.getProject());
		}

		availCriteria.setCustomers(new ArrayList<Customer>(customers));
		availCriteria.setProjects(new ArrayList<Project>(projects));

		availCriteria.setReportRange(reportAggregatedDAO.getMinMaxDateTimesheetEntry(user));
	}

	/**
	 * @param userDAO
	 *            the userDAO to set
	 */
	public void setUserDAO(UserDao userDAO) {
		this.userDAO = userDAO;
	}

	/**
	 * @param customerDAO
	 *            the customerDAO to set
	 */
	public void setCustomerDAO(CustomerDao customerDAO) {
		this.customerDAO = customerDAO;
	}

	/**
	 * @param projectDAO
	 *            the projectDAO to set
	 */
	public void setProjectDAO(ProjectDao projectDAO) {
		this.projectDAO = projectDAO;
	}

	public void setActivityDao(ActivityDao activityDao) {
		this.activityDao = activityDao;
	}

	/**
	 *  
	 *
	 */
	public void setReportAggregatedDAO(ReportAggregatedDao reportAggregatedDAO) {
		this.reportAggregatedDAO = reportAggregatedDAO;
	}

}
>>>>>>> 13bdb68... EHV-16: Implemented comments related to removal of billable flags in Reporting section. Made active flag work in different scenarios related to CustomerReviewer role
