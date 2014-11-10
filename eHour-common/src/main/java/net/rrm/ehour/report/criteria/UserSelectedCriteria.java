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

package net.rrm.ehour.report.criteria;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.domain.UserDepartment;
import net.rrm.ehour.util.DateUtil;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.*;

/**
 * User selected criteria
 */

public class UserSelectedCriteria implements Serializable {

    public enum ReportType {
        INDIVIDUAL_USER,
        PM,
        REPORT
    }

    private static final long serialVersionUID = 375613059093184619L;
    private DateRange reportRange;
    private boolean onlyActiveProjects = true;
    private boolean onlyActiveCustomers = true;
    private boolean onlyActiveUsers = true;
    private boolean onlyBillableProjects = false;
    private String customerFilter;
    private List<User> users;
    private List<Project> projects;
    private List<Customer> customers;
    private Sort customerSort;
    private Sort projectSort;
    private Collection<UserDepartment> userDepartments;
    private boolean infiniteStartDate;
    private boolean infiniteEndDate;
    private ReportType selectedReportType;
    private Project project;
    private Map<Object, Object> customParameters = Maps.newHashMap();
    private User pm;
    private boolean showZeroBookings = false;
    private AggregateBy aggregateBy = AggregateBy.DAY;

    public UserSelectedCriteria() {
        resetCustomerSelection();
        resetProjectSelection();

        resetUserDepartmentSelection();
        resetUserSelection();

        infiniteStartDate = false;
        infiniteEndDate = false;

        reportRange = DateUtil.getDateRangeForMonth(new GregorianCalendar());
    }

    public final void resetCustomerSelection() {
        onlyActiveCustomers = true;
        customerSort = Sort.NAME;
        customers = Lists.newArrayList();
    }

    public final void resetProjectSelection() {
        onlyActiveProjects = true;
        onlyBillableProjects = false;
        projectSort = Sort.NAME;
        projects = Lists.newArrayList();
    }

    public final void resetUserDepartmentSelection() {
        userDepartments = Lists.newArrayList();
    }

    public final void resetUserSelection() {
        onlyActiveUsers = true;

        users = Lists.newArrayList();
    }

    public ReportType getSelectedReportType() {
        return selectedReportType;
    }

    public void setSelectedReportType(ReportType selectedReportType) {
        this.selectedReportType = selectedReportType;
    }

    public void setReportTypeToGlobal() {
        setSelectedReportType(ReportType.REPORT);
    }

    public void setReportTypeToPM(User projectManager) {
        setSelectedReportType(ReportType.PM);
        setPm(projectManager);
    }

    public void setReportTypeToIndividualUser(User user) {
        setSelectedReportType(ReportType.INDIVIDUAL_USER);
        setUser(user);
    }

    public Sort getCustomerSort() {
        return customerSort;
    }

    public void setCustomerSort(Sort customerSort) {
        this.customerSort = customerSort;
    }

    public Sort getProjectSort() {
        return projectSort;
    }

    public void setProjectSort(Sort projectSort) {
        this.projectSort = projectSort;
    }

    private void setUser(User user) {
        if (users == null) {
            users = new ArrayList<User>();
        }

        users.add(user);
    }

    public User getPm() {
        return pm;
    }

    private void setPm(User pm) {
        this.pm = pm;
    }

    public boolean isForIndividualUser() {
        return selectedReportType == ReportType.INDIVIDUAL_USER;
    }

    public boolean isForPm() {
        return selectedReportType == ReportType.PM;
    }

    public boolean isForGlobalReport() {
        return selectedReportType == ReportType.REPORT;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("reportRange", reportRange)
                .append("users", users)
                .append("projects", projects)
                .append("customers", customers)
                .append("departments", userDepartments)
                .toString();
    }

    /**
     * No customers selected ?
     */
    public boolean isEmptyCustomers() {
        return customers == null || customers.size() == 0;
    }

    /**
     * No projects selected ?
     */

    public boolean isEmptyProjects() {
        return projects == null || projects.size() == 0;
    }

    /**
     * No departments selected ?
     */

    public boolean isEmptyDepartments() {
        return userDepartments == null || userDepartments.size() == 0;
    }

    /**
     * No users selected ?
     */
    public boolean isEmptyUsers() {
        return users == null || users.size() == 0;
    }

    public boolean isOnlyActiveCustomers() {
        return onlyActiveCustomers;
    }

    public void setOnlyActiveCustomers(boolean onlyActiveCustomers) {
        this.onlyActiveCustomers = onlyActiveCustomers;
    }

    public boolean isOnlyActiveProjects() {
        return onlyActiveProjects;
    }

    public void setOnlyActiveProjects(boolean onlyActiveProjects) {
        this.onlyActiveProjects = onlyActiveProjects;
    }

    public DateRange getReportRange() {
        return reportRange;
    }

    public UserSelectedCriteria setReportRange(DateRange reportRange) {
        this.reportRange = reportRange;

        return this;
    }

    public boolean isOnlyActiveUsers() {
        return onlyActiveUsers;
    }

    public void setOnlyActiveUsers(boolean onlyActiveUsers) {
        this.onlyActiveUsers = onlyActiveUsers;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }

    public Collection<UserDepartment> getDepartments() {
        return userDepartments;
    }

    public void setDepartments(Collection<UserDepartment> departments) {
        this.userDepartments = departments;
    }

    public boolean isInfiniteStartDate() {
        return infiniteStartDate;
    }

    public void setInfiniteStartDate(boolean infiniteStartDate) {
        this.infiniteStartDate = infiniteStartDate;
    }

    public boolean isInfiniteEndDate() {
        return infiniteEndDate;
    }

    public void setInfiniteEndDate(boolean infiniteEndDate) {
        this.infiniteEndDate = infiniteEndDate;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    public String getCustomerFilter() {
        return customerFilter;
    }

    public void setCustomerFilter(String customerFilter) {
        this.customerFilter = customerFilter;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Customer getCustomer() {
        return (customers != null && customers.size() >= 1) ? customers.get(0) : null;
    }

    public void setCustomer(Customer customer) {
        customers = new ArrayList<Customer>();
        customers.add(customer);
    }

    public void setCustomParameters(Map<Object, Object> customParameters) {
        this.customParameters = customParameters;
    }

    public Map<Object, Object> getCustomParameters() {
        return customParameters;
    }

    public boolean isOnlyBillableProjects() {
        return onlyBillableProjects;
    }

    public void setOnlyBillableProjects(boolean onlyBillableProjects) {
        this.onlyBillableProjects = onlyBillableProjects;
    }

    public boolean isShowZeroBookings() {
        return showZeroBookings;
    }

    public void setShowZeroBookings(boolean showZeroBookings) {
        this.showZeroBookings = showZeroBookings;
    }

    public AggregateBy getAggregateBy() {
        return aggregateBy;
    }

    public void setAggregateBy(AggregateBy aggregateBy) {
        this.aggregateBy = aggregateBy;
    }
}
