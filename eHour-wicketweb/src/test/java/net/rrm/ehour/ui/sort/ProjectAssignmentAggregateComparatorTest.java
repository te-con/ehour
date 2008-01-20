/**
 * Created on Jan 20, 2008
 * Author: Thies
 *
 * Copyright (C) 2007 TE-CON, All Rights Reserved.
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

package net.rrm.ehour.ui.sort;

import static org.junit.Assert.*;
import net.rrm.ehour.report.reports.element.AssignmentAggregateReportElement;
import net.rrm.ehour.ui.common.DummyDataGenerator;

import org.junit.Before;
import org.junit.Test;
/**
 * TODO 
 **/

public class ProjectAssignmentAggregateComparatorTest
{
	private AssignmentAggregateReportElement a1;
	private AssignmentAggregateReportElement a2;
	
	@Before
	public void setUp()
	{
		a1 = new AssignmentAggregateReportElement();
		a2 = new AssignmentAggregateReportElement();
		
		a1.setProjectAssignment(DummyDataGenerator.getProjectAssignment(1));
		a2.setProjectAssignment(DummyDataGenerator.getProjectAssignment(2));
	}

	/**
	 * Test method for {@link net.rrm.ehour.ui.sort.ProjectAssignmentAggregateComparator#compare(net.rrm.ehour.report.reports.element.AssignmentAggregateReportElement, net.rrm.ehour.report.reports.element.AssignmentAggregateReportElement)}.
	 */
	@Test
	public void testCompareProject()
	{
		int x = new ProjectAssignmentAggregateComparator(ProjectAssignmentAggregateComparator.SORT_ON_PROJECT).compare(a1, a2);
		
		assertTrue(x < 0);
	}

	public void testCompareCustomer()
	{
		int x = new ProjectAssignmentAggregateComparator(ProjectAssignmentAggregateComparator.SORT_ON_CUSTOMER).compare(a1, a2);
		
		assertTrue(x < 0);
	}
	
	
}
