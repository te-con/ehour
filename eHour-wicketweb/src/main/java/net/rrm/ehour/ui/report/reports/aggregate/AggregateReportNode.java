/**
 * Created on Jul 12, 2007
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
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.rrm.ehour.domain.DomainObject;
import net.rrm.ehour.report.reports.ProjectAssignmentAggregate;
import net.rrm.ehour.ui.report.value.ReportValueWrapper;
import net.rrm.ehour.ui.report.value.ReportValueWrapperFactory;

/**
 * Representation of a node in the report
 * TODO remove generics?
 **/

public class AggregateReportNode<RN extends DomainObject<? extends Serializable, ? extends Serializable>,
									CN extends DomainObject<? extends Serializable, ? extends Serializable>> implements Serializable
{
	private static final long serialVersionUID = 6751211217287222516L;
	private ReportValueWrapper	rootValue;
	private List<SectionChild>	childNodes;
	
	/**
	 * 
	 * @param rootNode
	 * @param childMap
	 */
	public AggregateReportNode(RN rootNode, Map<CN, Set<ProjectAssignmentAggregate>> childMap,
								ReportValueWrapperFactory rootWrapperFactory, 
								ReportValueWrapperFactory childWrapperFactory)
	{
		this.rootValue = rootWrapperFactory.createReportValueWrapper(rootNode);

		childNodes = new ArrayList<SectionChild>();
		
		for (CN childNode : childMap.keySet())
		{
			childNodes.add(new SectionChild(childNode, childWrapperFactory, childMap.get(childNode)));
		}
	}
	
	/**
	 * 
	 * @author Thies
	 *
	 * @param <CNN>
	 */
	public class SectionChild implements Serializable
	{
		private static final long serialVersionUID = 8652911992468812544L;
		private ReportValueWrapper	childValue;
		private	float				hours;
		private	float				turnOver;
		
		
		public SectionChild(CN node, ReportValueWrapperFactory wrapperFactory, Set<ProjectAssignmentAggregate> aggregates)
		{
			childValue = wrapperFactory.createReportValueWrapper(node);
			
			for (ProjectAssignmentAggregate projectAssignmentAggregate : aggregates)
			{
				if (projectAssignmentAggregate.getHours() != null)
				{
					hours += projectAssignmentAggregate.getHours().floatValue();
				}
				
				if (projectAssignmentAggregate.getTurnOver() != null)
				{
					turnOver += projectAssignmentAggregate.getTurnOver().floatValue();
					
				}
			}
		}
		
		public float getHours()
		{
			return hours;
		}
		
		public float getTurnOver()
		{
			return turnOver;
		}
		
		public ReportValueWrapper getChildValue()
		{
			return childValue;
		}
	}

	/**
	 * @return the node
	 */
	public ReportValueWrapper getRootValue()
	{
		return rootValue;
	}

	/**
	 * @return the childNodes
	 */
	public List<SectionChild> getChildNodes()
	{
		return childNodes;
	}
}
