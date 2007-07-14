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
 **/

public class AggregateReportNode<RN extends DomainObject<? extends Serializable, ? extends Serializable>,
									CN extends DomainObject<? extends Serializable, ? extends Serializable>> implements Serializable
{
	private static final long serialVersionUID = 6751211217287222516L;
	private ReportValueWrapper	node;
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
		this.node = rootWrapperFactory.createReportValueWrapper(rootNode);

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
	public class SectionChild
	{
		private Set<ProjectAssignmentAggregate>	aggregates;
		private ReportValueWrapper	node;
		
		public SectionChild(CN node, ReportValueWrapperFactory wrapperFactory, Set<ProjectAssignmentAggregate> aggregates)
		{
			this.node = wrapperFactory.createReportValueWrapper(node);
			this.aggregates = aggregates;
		}
		
		public  Set<ProjectAssignmentAggregate> getAggregates()
		{
			return aggregates;
		}
		
		public ReportValueWrapper getNode()
		{
			return node;
		}
	}

	/**
	 * @return the node
	 */
	public ReportValueWrapper getNode()
	{
		return node;
	}

	/**
	 * @return the childNodes
	 */
	public List<SectionChild> getChildNodes()
	{
		return childNodes;
	}
}
