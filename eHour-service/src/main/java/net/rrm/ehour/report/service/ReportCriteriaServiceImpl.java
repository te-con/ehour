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

import net.rrm.ehour.audit.annot.NonAuditable;
import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.persistence.customer.dao.CustomerDao;
import net.rrm.ehour.persistence.project.dao.ProjectAssignmentDao;
import net.rrm.ehour.persistence.project.dao.ProjectDao;
import net.rrm.ehour.persistence.report.dao.ReportAggregatedDao;
import net.rrm.ehour.persistence.user.dao.UserDao;
import net.rrm.ehour.persistence.user.dao.UserDepartmentDao;
import net.rrm.ehour.project.util.ProjectUtil;
import net.rrm.ehour.report.criteria.AvailableCriteria;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.criteria.ReportCriteriaUpdateType;
import net.rrm.ehour.report.criteria.UserSelectedCriteria;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Report Criteria services
 */
@NonAuditable
@Service("reportCriteriaService")
public class ReportCriteriaServiceImpl implements ReportCriteriaService {
    @Autowired
    private UserDao userDAO;
    @Autowired
    private UserDepartmentDao userDepartmentDAO;
    @Autowired
    private CustomerDao customerDAO;
    @Autowired
    private ProjectDao projectDAO;
    @Autowired
    private ProjectAssignmentDao projectAssignmentDAO;
    @Autowired
    private ReportAggregatedDao reportAggregatedDAO;

    private static final Logger LOGGER = Logger.getLogger(ReportCriteriaServiceImpl.class);

    /**
     * Update available report criteria
     */
    public ReportCriteria syncUserReportCriteria(ReportCriteria reportCriteria, ReportCriteriaUpdateType updateType) {
        UserSelectedCriteria userSelectedCriteria = reportCriteria.getUserSelectedCriteria();
        AvailableCriteria availCriteria = reportCriteria.getAvailableCriteria();

        if (userSelectedCriteria.isForGlobalReport() || userSelectedCriteria.isForPm()) {
            if (updateType == ReportCriteriaUpdateType.UPDATE_CUSTOMERS ||
                    updateType == ReportCriteriaUpdateType.UPDATE_ALL) {
                availCriteria.setCustomers(getAvailableCustomers(userSelectedCriteria));
            }

            if (updateType == ReportCriteriaUpdateType.UPDATE_PROJECTS ||
                    updateType == ReportCriteriaUpdateType.UPDATE_ALL) {
                availCriteria.setProjects(getAvailableProjects(userSelectedCriteria));
            }

            if (updateType == ReportCriteriaUpdateType.UPDATE_ALL) {
                availCriteria.setUserDepartments(userDepartmentDAO.findAll());

                availCriteria.setReportRange(reportAggregatedDAO.getMinMaxDateTimesheetEntry());
            }

            if (updateType == ReportCriteriaUpdateType.UPDATE_USERS ||
                    updateType == ReportCriteriaUpdateType.UPDATE_ALL) {
                availCriteria.setUsers(getAvailableUsers(userSelectedCriteria));
            }
        } else {
            syncCriteriaForIndividualUser(reportCriteria);
        }

        return reportCriteria;
    }

    private List<User> getAvailableUsers(UserSelectedCriteria userSelectedCriteria) {
        List<User> users;

        if (userSelectedCriteria.isEmptyDepartments()) {
            users = userDAO.findUsers(userSelectedCriteria.isOnlyActiveUsers());
        } else {
            LOGGER.debug("Finding users for departments with filter '" + userSelectedCriteria.getUserFilter() + "'");
            users = userDAO.findUsersForDepartments(userSelectedCriteria.getUserFilter()
                    , userSelectedCriteria.getDepartments()
                    , userSelectedCriteria.isOnlyActiveUsers());
        }

        return users;
    }

    /**
     * Get available customers
     *
     * @param userSelectedCriteria
     * @return
     */
    private List<Customer> getAvailableCustomers(UserSelectedCriteria userSelectedCriteria) {
        List<Customer> customers;

        customers = fetchCustomers(userSelectedCriteria);

        List<Customer> billableCustomers = checkForOnlyBillableCustomers(userSelectedCriteria, customers);

        Collections.sort(billableCustomers);

        return billableCustomers;
    }

    private List<Customer> checkForOnlyBillableCustomers(UserSelectedCriteria userSelectedCriteria, List<Customer> customers) {
        List<Customer> billableCustomers = new ArrayList<Customer>();

        LOGGER.debug("Finding on billable only: " + userSelectedCriteria.isOnlyBillableProjects());

        if (userSelectedCriteria.isOnlyBillableProjects()) {
            for (Customer customer : customers) {
                List<Project> billableProjects;

                if (userSelectedCriteria.isOnlyActiveProjects()) {
                    billableProjects = ProjectUtil.getBillableProjects(customer.getActiveProjects());
                } else {
                    billableProjects = ProjectUtil.getBillableProjects(customer.getProjects());
                }

                if (!CollectionUtils.isEmpty(billableProjects)) {
                    billableCustomers.add(customer);
                }
            }
        } else {
            return customers;
        }

        return billableCustomers;
    }

    private List<Customer> fetchCustomers(UserSelectedCriteria userSelectedCriteria) {
        List<Customer> customers;
        if (userSelectedCriteria.isOnlyActiveCustomers()) {
            customers = customerDAO.findAllActive();
        } else {
            customers = customerDAO.findAll();
        }
        return customers;
    }

    /**
     * Get available projects depended on the userSelectedCriteria
     *
     * @param userSelectedCriteria
     * @return
     */
    private List<Project> getAvailableProjects(UserSelectedCriteria userSelectedCriteria) {
        List<Project> projects;

        if (userSelectedCriteria.isEmptyCustomers()) {
            projects = fetchProjects(userSelectedCriteria);
        } else {
            projects = projectDAO.findProjectForCustomers(userSelectedCriteria.getCustomers(),
                    userSelectedCriteria.isOnlyActiveProjects());
        }

        projects = checkForOnlyBillableProjects(userSelectedCriteria, projects);

        return projects;
    }

    private List<Project> checkForOnlyBillableProjects(UserSelectedCriteria userSelectedCriteria, List<Project> projects) {
        if (userSelectedCriteria.isOnlyBillableProjects()) {
            projects = ProjectUtil.getBillableProjects(projects);
        }
        return projects;
    }

    private List<Project> fetchProjects(UserSelectedCriteria userSelectedCriteria) {
        List<Project> projects;
        if (userSelectedCriteria.isOnlyActiveProjects()) {
            LOGGER.debug("Fetching only active projects");

            projects = projectDAO.findAllActive();
        } else {
            LOGGER.debug("Fetching all projects");

            projects = projectDAO.findAll();
        }
        return projects;
    }

    /**
     * Sync criteria for users, only customers & projects
     * are displayed for users in this list
     *
     * @param reportCriteria
     */
    private void syncCriteriaForIndividualUser(ReportCriteria reportCriteria) {
        Set<Customer> customers = new HashSet<Customer>();
        Set<Project> projects = new HashSet<Project>();
        AvailableCriteria availCriteria = reportCriteria.getAvailableCriteria();

        User user = reportCriteria.getUserSelectedCriteria().getUsers().get(0);

        List<ProjectAssignment> assignments = projectAssignmentDAO.findProjectAssignmentsForUser(user.getUserId(), reportCriteria.getUserSelectedCriteria().getReportRange());

        for (ProjectAssignment assignment : assignments) {
            customers.add(assignment.getProject().getCustomer());
            projects.add(assignment.getProject());
        }

        availCriteria.setCustomers(new ArrayList<Customer>(customers));
        availCriteria.setProjects(new ArrayList<Project>(projects));

        availCriteria.setReportRange(reportAggregatedDAO.getMinMaxDateTimesheetEntry(user));
    }

    public void setUserDAO(UserDao userDAO) {
        this.userDAO = userDAO;
    }

    public void setCustomerDAO(CustomerDao customerDAO) {
        this.customerDAO = customerDAO;
    }

    public void setProjectDAO(ProjectDao projectDAO) {
        this.projectDAO = projectDAO;
    }

    public void setUserDepartmentDAO(UserDepartmentDao userDepartmentDAO) {
        this.userDepartmentDAO = userDepartmentDAO;
    }

    public void setProjectAssignmentDAO(ProjectAssignmentDao projectAssignmentDAO) {
        this.projectAssignmentDAO = projectAssignmentDAO;
    }

    public void setReportAggregatedDAO(ReportAggregatedDao reportAggregatedDAO) {
        this.reportAggregatedDAO = reportAggregatedDAO;
    }
}
