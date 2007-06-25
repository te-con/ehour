/**
 * Created on Jan 2, 2007
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

package net.rrm.ehour.ui.panel.timesheet.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.SortedMap;

import net.rrm.ehour.customer.domain.Customer;
import net.rrm.ehour.timesheet.domain.TimesheetComment;

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
	private	Integer				userId;
	private	TimesheetComment	comment;
	

	/**
	 * @return the userId
	 */
	public Integer getUserId()
	{
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(Integer userId)
	{
		this.userId = userId;
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

}
