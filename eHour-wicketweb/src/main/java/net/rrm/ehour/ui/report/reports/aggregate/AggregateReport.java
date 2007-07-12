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

package net.rrm.ehour.ui.report.reports.aggregate;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import net.rrm.ehour.domain.DomainObject;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.reports.ProjectAssignmentAggregate;
import net.rrm.ehour.report.reports.ReportDataAggregate;
import net.rrm.ehour.ui.report.reports.Report;
import net.rrm.ehour.ui.report.value.ReportValueWrapperFactory;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;

/**
 * Generic class for the aggregate 3 level (web)reports
 * RK = root key, CK = client key, PK = primary key of the root key
 **/

public abstract class AggregateReport<RK extends DomainObject<?, ?>,
									 CK extends DomainObject<?, ?>,
									 PK extends Comparable<PK>> implements Report
{
	protected ReportCriteria	reportCriteria;
	protected transient Logger	logger = Logger.getLogger(this.getClass());
	private	  PK				forId;
	private	  List<AggregateReportNode<RK, CK>>	reportNodes;
	
	/**
	 * Initialize the webreport
	 * @param reportDataAggregate
	 */
	public void initialize(ReportDataAggregate reportDataAggregate)
	{
		initialize(reportDataAggregate, null);
	}
	
	/**
	 * Initialize the webreport for a specific id 
	 * @param reportDataAggregate
	 * @param forID the ID to generate the report for (null to ignore)
	 */
	public void initialize(ReportDataAggregate reportDataAggregate, PK forId)
	{
		SortedMap<RK, SortedMap<CK, Set<ProjectAssignmentAggregate>>>	reportMap;
		Date								profileStart = new Date();

		reportMap = generateReportMap(reportDataAggregate, forId);
		createReportNodes(reportMap);
		logger.debug(getReportName() + " took " + (new Date().getTime() - profileStart.getTime()) + "ms to create");
	}
	
	/**
	 * 
	 * @param reportMap
	 */
	private void createReportNodes(SortedMap<RK, SortedMap<CK, Set<ProjectAssignmentAggregate>>> reportMap)
	{
		reportNodes = new ArrayList<AggregateReportNode<RK, CK>>();
		
		AggregateReportNode<RK, CK>	node;
		
		for (RK reportKey : reportMap.keySet())
		{
			node = new AggregateReportNode<RK, CK>(reportKey, 
													reportMap.get(reportKey), 
													getRootValueWrapperFactory(),
													getChildValueWrapperFactory());
			reportNodes.add(node);
		}
	}

	
	/**
	 * Generate report map
	 * @param reportDataAggregate
	 * @param forId
	 * @return
	 */
	private SortedMap<RK, SortedMap<CK, Set<ProjectAssignmentAggregate>>> generateReportMap(ReportDataAggregate reportDataAggregate, PK forId)
	{
		SortedMap<RK, SortedMap<CK, Set<ProjectAssignmentAggregate>>>	reportMap;
		
		RK									rootKey;
		CK									childKey;
		SortedMap<CK, Set<ProjectAssignmentAggregate>>	childMap;
		Set<ProjectAssignmentAggregate>		aggregatesPerChild;
		
		this.forId = forId;
		
		reportMap = new TreeMap<RK, SortedMap<CK, Set<ProjectAssignmentAggregate>>>();

		logger.debug("Initializing aggregate " + getReportName() + " report" + ((forId != null) ? " for id " + forId : ""));

		reportCriteria = reportDataAggregate.getReportCriteria();
		
		for (ProjectAssignmentAggregate aggregate : reportDataAggregate.getProjectAssignmentAggregates())
		{
			rootKey = getRootKey(aggregate);
			
			// should we ignore this entry?
			if (forId != null && !(rootKey.getPK().equals(forId)))
			{
				continue;
			}
			
			childKey = getChildKey(aggregate);
			
			// first check if the child is in the root
			if (reportMap.containsKey(rootKey))
			{
				childMap = reportMap.get(rootKey);
			}
			else
			{
				childMap = new TreeMap<CK, Set<ProjectAssignmentAggregate>>(getComparator());
			}
			
			// then check if the client is in the submap
			if (childMap.containsKey(childKey))
			{
				aggregatesPerChild = childMap.get(childKey);
			}
			else
			{
				aggregatesPerChild = new HashSet<ProjectAssignmentAggregate>();
			}

			// add aggregate to child
			aggregatesPerChild.add(aggregate);
			
			// fold it back in
			childMap.put(childKey, aggregatesPerChild);
			reportMap.put(rootKey, childMap);
		}
		
		return reportMap;
	}
	
	/**
	 * @return the reportCriteria
	 */
	public ReportCriteria getReportCriteria()
	{
		return reportCriteria;
	}	
	
	/**
	 * Get report nodes (the actual content)
	 * @return
	 */
	public List<AggregateReportNode<RK, CK>> getReportNodes()
	{
		return reportNodes;
	}
	
	
//	/**
//	 * Get total hours for a rootKey/childKey
//	 * @param key
//	 * @return
//	 */
//	public float getHourTotal(RK rootKey, CK childKey)
//	{
//		Set<ProjectAssignmentAggregate>	aggregatesPerCustomer;
//		float							totalHours = 0f;
//		aggregatesPerCustomer = reportMap.get(rootKey).get(childKey);
//		
//		for (ProjectAssignmentAggregate aggregate : aggregatesPerCustomer)
//		{
//			totalHours += aggregate.getHours().floatValue();
//		}
//		
//		return totalHours;
//	}	
//	
//	/**
//	 * Get total turn over for rootKey/childKey
//	 * @param key
//	 * @return
//	 */
//	public float getTurnOverTotal(RK rootKey, CK childKey)
//	{
//		Set<ProjectAssignmentAggregate>	aggregatesPerCustomer;
//		float							totalTurnOver = 0f;
//		aggregatesPerCustomer = reportMap.get(rootKey).get(childKey);
//		
//		for (ProjectAssignmentAggregate aggregate : aggregatesPerCustomer)
//		{
//			if (aggregate.getTurnOver() != null)
//			{
//				totalTurnOver += aggregate.getTurnOver().floatValue();
//			}
//		}
//		
//		return totalTurnOver;
//	}	
	
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
	 * Get comparator for the client key
	 * @return
	 */
	public abstract Comparator<CK> getComparator();
	
	/**
	 * Get value wrapper for root
	 * @return
	 */
	public abstract ReportValueWrapperFactory getRootValueWrapperFactory();

	/**
	 * Get value wrapper for child
	 * @return
	 */
	public abstract ReportValueWrapperFactory getChildValueWrapperFactory();

	/**
	 * ToString
	 */
	@Override
	public String toString()
	{
		return new ToStringBuilder(this).append("reportName", getReportName())
										.append("reportNodes", reportNodes)
										.toString();
	}
	
	/**
	 * @return the forId
	 */
	public PK getForId()
	{
		return forId;
	}
	
}
