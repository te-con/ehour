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
import java.util.Date;
import java.util.List;
import java.util.SortedMap;

import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.domain.TimesheetComment;
import net.rrm.ehour.domain.User;
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
	private	int					maxHoursPerDay; // TODO int? should change to float

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
	public int getMaxHoursPerDay()
	{
		return maxHoursPerDay;
	}
	public void setMaxHoursPerDay(int maxHoursPerDay)
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
