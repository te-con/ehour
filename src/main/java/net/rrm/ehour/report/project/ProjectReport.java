/**
 * Created on Jan 31, 2007
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

package net.rrm.ehour.report.project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.rrm.ehour.customer.domain.Customer;
import net.rrm.ehour.report.criteria.UserCriteria;

import org.apache.log4j.Logger;

/**
 * TODO 
 **/

public class ProjectReport
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6365903846883586472L;
	
	private	UserCriteria	userCriteria;
	private	Logger			logger = Logger.getLogger(this.getClass());
	private	Map<Customer, List<ProjectAssignmentAggregate>>	report;
	
	/**
	 * Initialize the report based on the supplied aggregates
	 * @param aggregates
	 */
	public void initialize(List<ProjectAssignmentAggregate> aggregates)
	{
		Customer							customer;
		List<ProjectAssignmentAggregate>	aggregatesPerCustomer;
		
		report = new HashMap<Customer, List<ProjectAssignmentAggregate>>();
		
		logger.debug("Initializing project report");
		
		for (ProjectAssignmentAggregate aggregate : aggregates)
		{
			logger.debug("Found aggregate : " + aggregate);
			customer = aggregate.getProjectAssignment().getProject().getCustomer();
			
			if (report.containsKey(customer))
			{
				aggregatesPerCustomer = report.get(customer);
			}
			else
			{
				logger.debug("Adding customer " + customer + " to report");
				aggregatesPerCustomer = new ArrayList<ProjectAssignmentAggregate>();
			}
			
			aggregatesPerCustomer.add(aggregate);
			
			report.put(customer, aggregatesPerCustomer);
		}
	}

	/**
	 * Get total hours for a key
	 * @param key
	 * @return
	 */
	public float getHourTotal(Customer key)
	{
		List<ProjectAssignmentAggregate>	aggregatesPerCustomer;
		float								totalHours = 0f;
		aggregatesPerCustomer = report.get(key);
		
		for (ProjectAssignmentAggregate aggregate : aggregatesPerCustomer)
		{
			totalHours += aggregate.getHours().floatValue();
		}
		
		return totalHours;
	}
	
	/**
	 * Get total turn over for key
	 * @param key
	 * @return
	 */
	public float getTurnOverTotal(Customer key)
	{
		List<ProjectAssignmentAggregate>	aggregatesPerCustomer;
		float								totalTurnOver = 0f;
		aggregatesPerCustomer = report.get(key);
		
		for (ProjectAssignmentAggregate aggregate : aggregatesPerCustomer)
		{
			totalTurnOver += aggregate.getTurnOver().floatValue();
		}
		
		return totalTurnOver;
	}
	
	/**
	 * Get customers in this report 
	 * @return
	 */
	public Set<Customer> getCustomers()
	{
		return report.keySet();
	}
	
	/**
	 * Get values
	 * @return
	 */
	public Map<Customer, List<ProjectAssignmentAggregate>> getReportValues()
	{
		return report;
	}

	/**
	 * @return the userCriteria
	 */
	public UserCriteria getUserCriteria()
	{
		return userCriteria;
	}


	/**
	 * @param userCriteria the userCriteria to set
	 */
	public void setUserCriteria(UserCriteria userCriteria)
	{
		this.userCriteria = userCriteria;
	}
}
