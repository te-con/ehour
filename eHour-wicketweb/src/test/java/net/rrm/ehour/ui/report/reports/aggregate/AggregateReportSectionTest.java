/**
 * Created on Jul 13, 2007
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

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import net.rrm.ehour.customer.domain.Customer;
import net.rrm.ehour.project.domain.Project;
import net.rrm.ehour.report.reports.ProjectAssignmentAggregate;
import net.rrm.ehour.ui.report.reports.aggregate.AggregateReportNode.SectionChild;
import net.rrm.ehour.ui.report.value.CustomerValueWrapperFactory;
import net.rrm.ehour.ui.report.value.ProjectValueWrapperFactory;

import org.junit.Before;
import org.junit.Test;

/**
 * TODO 
 **/

public class AggregateReportSectionTest
{
	private	Customer	custA;
	private	Project		prjA, prjB, prjC;
	private	ProjectAssignmentAggregate	pagA, pagB, pagC, pagD, pagE;
	private Map<Project, Set<ProjectAssignmentAggregate>>	content;
	
	@Before
	public void setUp()
	{
		custA = new Customer(1);
		custA.setName("CA");
		
		prjA = new Project(3);
		prjA.setName("PA");

		prjB = new Project(4);
		prjB.setName("PB");

		prjC = new Project(5);
		prjC.setName("PC");

		pagA = new ProjectAssignmentAggregate();
		pagA.setHours(new Float(1));
		
		pagB = new ProjectAssignmentAggregate();
		pagB.setHours(new Float(2));

		pagC = new ProjectAssignmentAggregate();
		pagC.setHours(new Float(3));
		
		pagD = new ProjectAssignmentAggregate();
		pagD.setHours(new Float(4));

		pagE = new ProjectAssignmentAggregate();
		pagE.setHours(new Float(5));
		
		content = new TreeMap<Project, Set<ProjectAssignmentAggregate>>();
		{
			Set<ProjectAssignmentAggregate>	set = new HashSet<ProjectAssignmentAggregate>();
			set.add(pagA);
			set.add(pagB);
			set.add(pagC);
			content.put(prjA, set);
		}

		{
			Set<ProjectAssignmentAggregate>	set = new HashSet<ProjectAssignmentAggregate>();
			set.add(pagD);
			content.put(prjB, set);
		}
		
		{
			Set<ProjectAssignmentAggregate>	set = new HashSet<ProjectAssignmentAggregate>();
			set.add(pagD);
			content.put(prjC, set);
		}		
	}
	
	@Test
	public void testConstructor()
	{
		AggregateReportNode<Customer, Project> section;
		
		section = new AggregateReportNode<Customer, Project>(custA, content, new CustomerValueWrapperFactory(), new ProjectValueWrapperFactory());
		
		assertEquals("CA", section.getNode().getName());
		assertEquals(3, section.getChildNodes().size());
		
		List<AggregateReportNode<?, ?>.SectionChild> children = section.getChildNodes();
		
		for (SectionChild sectionChild : children)
		{
			if (sectionChild.getNode().getName().equals("PA"))
			{
				assertEquals(3, sectionChild.getAggregates().size());
				
				Set<ProjectAssignmentAggregate> aggs = sectionChild.getAggregates();
				
				float hours = 0;
				
				for (ProjectAssignmentAggregate projectAssignmentAggregate : aggs)
				{
					hours += projectAssignmentAggregate.getHours().floatValue();
				}
				
				assertEquals(6, hours, 0);
				
			}

			if (sectionChild.getNode().getName().equals("PB"))
			{
				assertEquals(1, sectionChild.getAggregates().size());
			}

			if (sectionChild.getNode().getName().equals("PC"))
			{
				assertEquals(1, sectionChild.getAggregates().size());
			}

			
			
		}
	}

}
