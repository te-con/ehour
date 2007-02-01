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

import java.util.List;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import net.rrm.ehour.customer.domain.Customer;
import net.rrm.ehour.report.criteria.UserCriteria;

import org.apache.log4j.Logger;

/**
 * TODO 
 **/

public class ProjectReport
{
	private	UserCriteria	userCriteria;
	private	Logger			logger = Logger.getLogger(this.getClass());
	private	SortedMap<Customer, SortedSet<ProjectAssignmentAggregate>>	reportMap;
	
	/**
	 * Initialize the report based on the supplied aggregates
	 * @param aggregates
	 */
	public void initialize(List<ProjectAssignmentAggregate> aggregates)
	{
		Customer	customer;
		SortedSet<ProjectAssignmentAggregate>	aggregateSet;
		
		logger.debug("Initializing project report");
		
		reportMap = new TreeMap<Customer, SortedSet<ProjectAssignmentAggregate>>();
		
		for (ProjectAssignmentAggregate aggregate : aggregates)
		{
			customer = aggregate.getProjectAssignment().getProject().getCustomer();
			
			if (reportMap.containsKey(customer))
			{
				aggregateSet = reportMap.get(customer);
			}
			else
			{
				logger.debug("Adding customer " + customer + " to report");
				aggregateSet = new TreeSet<ProjectAssignmentAggregate>();
			}
			
			aggregateSet.add(aggregate);
			
			reportMap.put(customer, aggregateSet);
		}
	}


	/**
	 * Get the report values
	 * @return
	 */
	public SortedMap<Customer, SortedSet<ProjectAssignmentAggregate>> getReportValues()
	{
		return reportMap;
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
