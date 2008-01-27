/**
 * Created on Jan 14, 2007
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
 *
 * This Software is copyright TE-CON 2007. This Software is not open source by definition. The source of the Software is available for educational purposes.
 * TE-CON holds all the ownership rights on the Software.
 * TE-CON freely grants the right to use the Software. Any reproduction or modification of this Software, whether for commercial use or open source,
 * is subject to obtaining the prior express authorization of TE-CON.
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.report.criteria;

import java.io.Serializable;
import java.util.List;

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.domain.UserDepartment;

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
