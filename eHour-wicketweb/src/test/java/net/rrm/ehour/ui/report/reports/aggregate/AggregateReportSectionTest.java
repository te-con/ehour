/**
 * Created on Jul 13, 2007
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
		
		assertEquals("CA", section.getRootValue().getName());
		assertEquals(3, section.getChildNodes().size());
		
		List<AggregateReportNode<Customer, Project>.SectionChild> children = section.getChildNodes();
		
		for (SectionChild sectionChild : children)
		{
			if (sectionChild.getChildValue().getName().equals("PA"))
			{
				assertEquals(6, sectionChild.getHours(), 0);
				
			}

			if (sectionChild.getChildValue().getName().equals("PB"))
			{
				assertEquals(4, sectionChild.getHours(), 0);
			}

			if (sectionChild.getChildValue().getName().equals("PC"))
			{
				assertEquals(4, sectionChild.getHours(), 0);
			}

			
			
		}
	}

}
