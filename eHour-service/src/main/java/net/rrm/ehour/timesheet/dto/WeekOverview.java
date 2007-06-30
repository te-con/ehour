/**
 * Created on Dec 28, 2006
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

package net.rrm.ehour.timesheet.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.rrm.ehour.customer.domain.Customer;
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.project.domain.ProjectAssignment;
import net.rrm.ehour.timesheet.domain.TimesheetComment;
import net.rrm.ehour.timesheet.domain.TimesheetEntry;
import net.rrm.ehour.user.domain.User;

/**
 * Value object for timesheet entries of a week and corresponding comments
 **/

public class WeekOverview implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3281374385102106958L;
	private List<TimesheetEntry>	timesheetEntries;
	private TimesheetComment		comment;
	private	List<ProjectAssignment>	projectAssignments;
	private	DateRange				weekRange;
	private	CustomerFoldPreferenceList foldPreferences;
	private	User					user;
	private	Set<Customer>			customers;

	/**
	 * 
	 */
	public void initCustomers()
	{
		customers = new HashSet<Customer>();
		
		for (ProjectAssignment assignment : projectAssignments)
		{
			customers.add(assignment.getProject().getCustomer());
		}

		for (TimesheetEntry entry : timesheetEntries)
		{
			customers.add(entry.getEntryId().getProjectAssignment().getProject().getCustomer());
		}
	}
	
	
	/**
	 * Get customser
	 * @return
	 */
	public Set<Customer> getCustomers()
	{
		return customers;
	}
	
	/**
	 * @return the timesheetEntries
	 */
	public List<TimesheetEntry> getTimesheetEntries()
	{
		return timesheetEntries;
	}
	/**
	 * @param timesheetEntries the timesheetEntries to set
	 */
	public void setTimesheetEntries(List<TimesheetEntry> timesheetEntries)
	{
		this.timesheetEntries = timesheetEntries;
	}
	/**
	 * @return the projectAssignments
	 */
	public List<ProjectAssignment> getProjectAssignments()
	{
		return projectAssignments;
	}
	/**
	 * @param projectAssignments the projectAssignments to set
	 */
	public void setProjectAssignments(List<ProjectAssignment> projectAssignments)
	{
		this.projectAssignments = projectAssignments;
	}
	/**
	 * @return the weekRange
	 */
	public DateRange getWeekRange()
	{
		return weekRange;
	}
	/**
	 * @param weekRange the weekRange to set
	 */
	public void setWeekRange(DateRange weekRange)
	{
		this.weekRange = weekRange;
	}
	/**
	 * @return the comment
	 */
	public TimesheetComment getComment()
	{
		return comment;
	}
	/**
	 * @param comment the comment to set
	 */
	public void setComment(TimesheetComment comment)
	{
		this.comment = comment;
	}
	/**
	 * @return the foldPreferences
	 */
	public CustomerFoldPreferenceList getFoldPreferences()
	{
		return foldPreferences;
	}
	/**
	 * @param foldPreferences the foldPreferences to set
	 */
	public void setFoldPreferences(CustomerFoldPreferenceList foldPreferences)
	{
		this.foldPreferences = foldPreferences;
	}

	/**
	 * @return the user
	 */
	public User getUser()
	{
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(User user)
	{
		this.user = user;
	}
}
