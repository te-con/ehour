/**
 * Created on Jan 2, 2007
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
 *
 * This Software is copyright TE-CON 2007. This Software is not open source by definition. The source of the Software is available for educational purposes.
 * TE-CON holds all the ownership rights on the Software.
 * TE-CON freely grants the right to use the Software. Any reproduction or modification of this Software, whether for commercial use or open source,
 * is subject to obtaining the prior express authorization of TE-CON.
 * 
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.ui.panel.timesheet.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.SortedMap;

import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.domain.TimesheetComment;
import net.rrm.ehour.domain.TimesheetCommentId;
import net.rrm.ehour.domain.TimesheetEntry;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.project.status.ProjectAssignmentStatus;
import net.rrm.ehour.timesheet.dto.CustomerFoldPreferenceList;

/**
 * Representation of a timesheet
 **/

public class Timesheet implements Serializable
{
	/**
	 * 
	 */
	private static final long 	serialVersionUID = -547682050331580675L;
	private SortedMap<Customer, List<TimesheetRow>>	customers;
	private	Date[]				dateSequence;
	private	Date				weekStart;
	private	Date				weekEnd;
	private	User				user;
	private	TimesheetComment	comment;
	private	CustomerFoldPreferenceList foldPreferences;	
	private	float				maxHoursPerDay;

	/**
	 * Update failed projects
	 * @param failedProjectStatusses
	 */
	public void updateFailedProjects(List<ProjectAssignmentStatus> failedProjectStatusses)
	{
		clearAssignmentStatus();
		
		for (ProjectAssignmentStatus projectAssignmentStatus : failedProjectStatusses)
		{
			setAssignmentStatus(projectAssignmentStatus);
		} 
	}
	
	/**
	 * Set assignment status
	 * @param status
	 */
	private void setAssignmentStatus(ProjectAssignmentStatus status)
	{
		for (Customer customer : customers.keySet())
		{
			for (TimesheetRow row : customers.get(customer))
			{
				if (row.getProjectAssignment().equals(status.getAggregate().getProjectAssignment()))
				{
					row.setAssignmentStatus(status);
					return;
				}
			}
		}
	}	
	
	/**
	 * Clear each assignment status
	 */
	private void clearAssignmentStatus()
	{
		for (Customer customer : customers.keySet())
		{
			for (TimesheetRow row : customers.get(customer))
			{
				row.setAssignmentStatus(null);
			}
		}
	}
	
	
	/**
	 * Get comment for persist
	 * @return
	 */
	public TimesheetComment getCommentForPersist()
	{
		// check comment id
		if (getComment().getCommentId() == null)
		{
			TimesheetCommentId id = new TimesheetCommentId();
			id.setUserId(getUser().getUserId());
			id.setCommentDate(getWeekStart());
			
			getComment().setCommentId(id);
		}
		
		return getComment();
	}
	
	/**
	 * Get the timesheet entries of this timesheet
	 * @return
	 */
	public List<TimesheetEntry> getTimesheetEntries()
	{
		List<TimesheetEntry> timesheetEntries = new ArrayList<TimesheetEntry>();
		
		Collection<List<TimesheetRow>> rows = getCustomers().values();
		
		for (List<TimesheetRow> list : rows)
		{
			for (TimesheetRow timesheetRow : list)
			{
				timesheetEntries.addAll(timesheetRow.getTimesheetEntries());
			}
		}	
		
		return timesheetEntries;
	}
	
	/**
	 * Get remaining hours for a day based on maxHoursPerDay
	 * @param day
	 * @return
	 */
	public Float getRemainingHoursForDay(int day)
	{
		float remainingHours = maxHoursPerDay;
		
		for (Customer customer : customers.keySet())
		{
			for (TimesheetRow row: customers.get(customer))
			{
				TimesheetCell cell = row.getTimesheetCells()[day];
				
				if (cell != null && cell.getTimesheetEntry() != null && cell.getTimesheetEntry().getHours() != null)
				{
					remainingHours -= cell.getTimesheetEntry().getHours().floatValue();
				}
			}
		}
		
		return remainingHours;
	}
	
	/**
	 * Get total booked hours
	 * @return
	 */
	public Float getTotalBookedHours()
	{
		float totalHours = 0;
		
		for (Customer customer : customers.keySet())
		{
			for (TimesheetRow row: customers.get(customer))
			{
				for (TimesheetCell cell : row.getTimesheetCells())
				{
					if (cell != null 
							&& cell.getTimesheetEntry() != null 
							&& cell.getTimesheetEntry().getHours() != null)
					{
						totalHours += cell.getTimesheetEntry().getHours().floatValue();
					}
				}
			}
		}	
		
		return totalHours;
	}
	
	/**
	 * @return the weekStart
	 */
	public Date getWeekStart()
	{
		return weekStart;
	}
	/**
	 * @param weekStart the weekStart to set
	 */
	public void setWeekStart(Date weekStart)
	{
		this.weekStart = weekStart;
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
	 * @return the customers
	 */
	public SortedMap<Customer, List<TimesheetRow>> getCustomers()
	{
		return customers;
	}
	/**
	 * @param customers the customers to set
	 */
	public void setCustomers(SortedMap<Customer, List<TimesheetRow>> customers)
	{
		this.customers = customers;
	}
	/**
	 * @return the dateSequence
	 */
	public Date[] getDateSequence()
	{
		return dateSequence;
	}
	/**
	 * @param dateSequence the dateSequence to set
	 */
	public void setDateSequence(Date[] dateSequence)
	{
		this.dateSequence = dateSequence;
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
	public float getMaxHoursPerDay()
	{
		return maxHoursPerDay;
	}
	public void setMaxHoursPerDay(float maxHoursPerDay)
	{
		this.maxHoursPerDay = maxHoursPerDay;
	}

	public Date getWeekEnd()
	{
		return weekEnd;
	}

	public void setWeekEnd(Date weekEnd)
	{
		this.weekEnd = weekEnd;
	}

}
