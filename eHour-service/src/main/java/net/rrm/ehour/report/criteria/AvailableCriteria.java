/**
 * Created on Jan 14, 2007
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.report.criteria;

import java.util.List;

import net.rrm.ehour.customer.domain.Customer;
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.project.domain.Project;
import net.rrm.ehour.user.domain.User;
import net.rrm.ehour.user.domain.UserDepartment;

/**
 * ReportData Criteria
 * Contains input as well as output
 **/

public class AvailableCriteria
{
	private List<User>				users;
	private List<UserDepartment>	userDepartments;
	private List<Customer>			customers;
	private List<Project>			projects;
	private DateRange				reportRange;

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
}
