/**
 * Created on Jul 12, 2007
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
