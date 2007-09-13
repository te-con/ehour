/**
 * Created on 22-feb-2007
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

package net.rrm.ehour.ui.report.reports.aggregate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
 **/

@SuppressWarnings("unchecked")
public abstract class AggregateReport implements Report
{
	protected ReportCriteria			reportCriteria;
	protected transient Logger			logger = Logger.getLogger(this.getClass());
	private	  Comparable<Serializable>	forId;
	private	  List<AggregateReportNode>	reportNodes;
	
	/**
	 * Initialize the report
	 * @param reportDataAggregate
	 */
	public void initialize(ReportDataAggregate reportDataAggregate)
	{
		initialize(reportDataAggregate, null);
	}
	
	/**
	 * Initialize the report for a specific id 
	 * @param reportDataAggregate
	 * @param forId the ID to generate the report for (null to ignore)
	 */
	public void initialize(ReportDataAggregate reportDataAggregate, Comparable<Serializable> forId)
	{
		Map<DomainObject, Map<DomainObject,Set<ProjectAssignmentAggregate>>>	reportMap;
		Date								profileStart = new Date();

		reportMap = generateReportMap(reportDataAggregate, forId);
		createReportNodes(reportMap);
		logger.debug(getReportName() + " took " + (new Date().getTime() - profileStart.getTime()) + "ms to create");
	}
	
	/**
	 * 
	 * @param reportMap
	 */
	private void createReportNodes(Map<DomainObject, Map<DomainObject, Set<ProjectAssignmentAggregate>>> reportMap)
	{
		reportNodes = new ArrayList<AggregateReportNode>();
		
		AggregateReportNode	node;
		
		for (DomainObject reportKey : reportMap.keySet())
		{
			node = new AggregateReportNode((DomainObject)reportKey, 
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
	private Map<DomainObject, 
				Map<DomainObject, Set<ProjectAssignmentAggregate>>> 
					generateReportMap(ReportDataAggregate reportDataAggregate, Comparable<Serializable> forId) 
	{
		Map<DomainObject, Map<DomainObject, Set<ProjectAssignmentAggregate>>>	reportMap;
		
		DomainObject rootKey;
		DomainObject childKey;
		Map<DomainObject, Set<ProjectAssignmentAggregate>>	childMap;
		Set<ProjectAssignmentAggregate>		aggregatesPerChild;
		
		reportMap = new HashMap<DomainObject, Map<DomainObject, Set<ProjectAssignmentAggregate>>>();

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
				childMap = new HashMap<DomainObject, Set<ProjectAssignmentAggregate>>();
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
	public List<AggregateReportNode> getReportNodes()
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
	protected abstract DomainObject getRootKey(ProjectAssignmentAggregate aggregate);
	
	/**
	 * Get child key from aggregate
	 * @param aggregate
	 * @return
	 */
	protected abstract DomainObject getChildKey(ProjectAssignmentAggregate aggregate);

	/**
	 * Get report name (used as attrib key in the request context)
	 *
	 */
	public abstract String getReportName();

	/**
	 * Get comparator for the client key
	 * @return
	 */
	protected abstract Comparator<? extends DomainObject<? extends Serializable, ? extends Serializable>> getComparator();
	
	/**
	 * Get value wrapper for root
	 * @return
	 */
	protected abstract ReportValueWrapperFactory getRootValueWrapperFactory();

	/**
	 * Get value wrapper for child
	 * @return
	 */
	protected abstract ReportValueWrapperFactory getChildValueWrapperFactory();

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
	protected Comparable<Serializable> getForId()
	{
		return forId;
	}
	
}
