/**
 * Created on 22-feb-2007
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

package net.rrm.ehour.web.report.reports;

import java.util.HashSet;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.reports.ProjectAssignmentAggregate;
import net.rrm.ehour.report.reports.ReportData;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;

/**
 * Generic class for the aggregate 3 level (web)reports
 * RK = root key, CK = client key 
 **/

public abstract class AggregateReport<RK extends Comparable, CK extends Comparable>
{
	protected ReportCriteria	reportCriteria;
	protected Logger			logger = Logger.getLogger(this.getClass());

	// nest nest nest...
	protected SortedMap<RK, SortedMap<CK, Set<ProjectAssignmentAggregate>>>	reportMap;
	
	/**
	 * Initialize the webreport
	 * @param reportData
	 */
	public void initialize(ReportData reportData)
	{
		RK									rootKey;
		CK									childKey;
		SortedMap<CK, Set<ProjectAssignmentAggregate>>	childMap;
		Set<ProjectAssignmentAggregate>		aggregatesPerChild;
		
		reportMap = new TreeMap<RK, SortedMap<CK, Set<ProjectAssignmentAggregate>>>();

		logger.debug("Initializing " + getReportName() + " report");

		reportCriteria = reportData.getReportCriteria();
		
		for (ProjectAssignmentAggregate aggregate : reportData.getProjectAssignmentAggregates())
		{
			logger.debug("Found aggregate : " + aggregate);
			
			rootKey = getRootKey(aggregate);
			childKey = getChildKey(aggregate);
			
			// first check if the child is in the root
			if (reportMap.containsKey(rootKey))
			{
				childMap = reportMap.get(rootKey);
			}
			else
			{
				logger.debug("Adding rootkey " + rootKey + " to report");
				childMap = new TreeMap<CK, Set<ProjectAssignmentAggregate>>();
			}
			
			// then check if the client is in the submap
			if (childMap.containsKey(childKey))
			{
				aggregatesPerChild = childMap.get(childKey);
			}
			else
			{
				logger.debug("Adding childkey " + childKey + " to rootkey " + rootKey);
				aggregatesPerChild = new HashSet<ProjectAssignmentAggregate>();
			}

			// add aggregate to child
			aggregatesPerChild.add(aggregate);
			
			// fold it back in
			childMap.put(childKey, aggregatesPerChild);
			reportMap.put(rootKey, childMap);
		}		
	}
	

	/**
	 * @return the reportCriteria
	 */
	public ReportCriteria getReportCriteria()
	{
		return reportCriteria;
	}	
	
	/**
	 * Get values
	 * @return
	 */
	public SortedMap<RK, SortedMap<CK, Set<ProjectAssignmentAggregate>>> getReportValues()
	{
		return reportMap;
	}	
	
	/**
	 * Get total hours for a rootKey/childKey
	 * @param key
	 * @return
	 */
	public float getHourTotal(RK rootKey, CK childKey)
	{
		Set<ProjectAssignmentAggregate>	aggregatesPerCustomer;
		float							totalHours = 0f;
		aggregatesPerCustomer = reportMap.get(rootKey).get(childKey);
		
		for (ProjectAssignmentAggregate aggregate : aggregatesPerCustomer)
		{
			totalHours += aggregate.getHours().floatValue();
		}
		
		return totalHours;
	}	
	
	/**
	 * Get total turn over for rootKey/childKey
	 * @param key
	 * @return
	 */
	public float getTurnOverTotal(RK rootKey, CK childKey)
	{
		Set<ProjectAssignmentAggregate>	aggregatesPerCustomer;
		float							totalTurnOver = 0f;
		aggregatesPerCustomer = reportMap.get(rootKey).get(childKey);
		
		for (ProjectAssignmentAggregate aggregate : aggregatesPerCustomer)
		{
			if (aggregate.getTurnOver() != null)
			{
				totalTurnOver += aggregate.getTurnOver().floatValue();
			}
		}
		
		return totalTurnOver;
	}	
	
	/**
	 * Get root key from aggregate
	 * @param aggregate
	 * @return
	 */
	protected abstract RK getRootKey(ProjectAssignmentAggregate aggregate);
	
	/**
	 * Get child key from aggregate
	 * @param aggregate
	 * @return
	 */
	protected abstract CK getChildKey(ProjectAssignmentAggregate aggregate);

	/**
	 * Get report name (used as attrib key in the request context)
	 *
	 */
	public abstract String getReportName();
	
	/**
	 * ToString
	 */
	@Override
	public String toString()
	{
		return new ToStringBuilder(this).append("reportMap", reportMap).toString();
	}
	
}
