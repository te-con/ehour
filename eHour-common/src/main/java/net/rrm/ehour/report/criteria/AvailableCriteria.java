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

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.domain.UserDepartment;

import java.io.Serializable;
import java.util.List;

/**
 * ReportData Criteria
 * Contains input as well as output
 **/

public class AvailableCriteria implements Serializable
{
	private static final long serialVersionUID = -6687214845760958691L;
	private DateRange				reportRange;
	private List<Customer>			customers;
	private List<Project>			projects;
	private List<User>				users;
	private List<UserDepartment>	userDepartments;

    public AvailableCriteria() {
    }

    public AvailableCriteria(List<Customer> customers, List<Project> projects, List<User> users, List<UserDepartment> userDepartments) {
        this.customers = customers;
        this.projects = projects;
        this.users = users;
        this.userDepartments = userDepartments;
    }

    /**
	 * @return the userDepartments
	 */
	public List<UserDepartment> getUserDepartments()
	{
		return userDepartments;
	}
	/**
	 * @param userDepartments the userDepartments to set
	 */
	public void setUserDepartments(List<UserDepartment> userDepartments)
	{
		this.userDepartments = userDepartments;
	}
	/**
	 * @return the users
	 */
	public List<User> getUsers()
	{
		return users;
	}
	/**
	 * @param users the users to set
	 */
	public void setUsers(List<User> users)
	{
		this.users = users;
	}
	
	/**
	 * @return the reportRange
	 */
	public DateRange getReportRange()
	{
		return reportRange;
	}
	/**
	 * @param reportRange the reportRange to set
	 */
	public void setReportRange(DateRange reportRange)
	{
		this.reportRange = reportRange;
	}
	
	/**
	 * @return the customers
	 */
	public List<Customer> getCustomers()
	{
		return customers;
	}
	/**
	 * @param customers the customers to set
	 */
	public void setCustomers(List<Customer> customers)
	{
		this.customers = customers;
	}
	/**
	 * @return the projects
	 */
	public List<Project> getProjects()
	{
		return projects;
	}
	/**
	 * @param projects the projects to set
	 */
	public void setProjects(List<Project> projects)
	{
		this.projects = projects;
	}
}
