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
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.domain.UserDepartment;
import net.rrm.ehour.sort.CustomerComparator;
import net.rrm.ehour.sort.ProjectComparator;
import net.rrm.ehour.sort.UserComparator;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * ReportData Criteria
 * Contains input as well as output
 */

public class AvailableCriteria implements Serializable {
    private static final long serialVersionUID = -6687214845760958691L;
    private static final ProjectComparator PROJECT_COMPARATOR = new ProjectComparator();
    private static final UserComparator USER_COMPARATOR = new UserComparator(false);
    private DateRange reportRange;
    private List<Customer> customers = Lists.newArrayList();
    private List<Project> projects = Lists.newArrayList();
    private List<User> users = Lists.newArrayList();
    private List<UserDepartment> userDepartments;

    private Sort customerSort = Sort.CODE;
    private Sort projectSort = Sort.CODE;
    private Sort userSort = Sort.CODE;


    public AvailableCriteria() {
    }

    public AvailableCriteria(List<Customer> customers, List<Project> projects, List<User> users, List<UserDepartment> userDepartments) {
        this.customers = customers;
        this.projects = projects;
        this.users = users;
        this.userDepartments = userDepartments;
    }

    public List<UserDepartment> getUserDepartments() {
        return userDepartments;
    }

    public void setUserDepartments(List<UserDepartment> userDepartments) {
        this.userDepartments = userDepartments;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(Collection<User> users) {
        this.users.clear();
        this.users.addAll(users);
        Collections.sort(this.users, USER_COMPARATOR);
    }

   public DateRange getReportRange() {
        return reportRange;
    }

    public void setReportRange(DateRange reportRange) {
        this.reportRange = reportRange;
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(Collection<Customer> customers) {
        this.customers.clear();
        this.customers.addAll(customers);
        sortCustomers();
    }

    public void setCustomerSortOrderAndSort(Sort sort) {
        customerSort = sort;
        sortCustomers();
    }

    private void sortCustomers() {
        Collections.sort(this.customers, new CustomerComparator(customerSort));
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(Collection<Project> projects) {
        this.projects.clear();
        this.projects.addAll(projects);
        Collections.sort(this.projects, PROJECT_COMPARATOR);
    }
}
