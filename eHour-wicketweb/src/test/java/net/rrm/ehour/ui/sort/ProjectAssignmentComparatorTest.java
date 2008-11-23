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

import static org.junit.Assert.assertTrue;
import net.rrm.ehour.ui.common.DummyDataGenerator;
import net.rrm.ehour.ui.common.sort.ProjectAssignmentComparator;

import org.junit.Test;

/**
 * TODO 
 **/

public class ProjectAssignmentComparatorTest
{

	/**
	 * Test method for {@link net.rrm.ehour.ui.common.sort.ProjectAssignmentComparator#compare(net.rrm.ehour.project.domain.ProjectAssignment, net.rrm.ehour.project.domain.ProjectAssignment)}.
	 */
	@Test
	public void testCompareCustDatePrj()
	{
		int x = new ProjectAssignmentComparator(ProjectAssignmentComparator.ASSIGNMENT_COMPARE_CUSTDATEPRJ)
					.compare(DummyDataGenerator.getProjectAssignment(2), DummyDataGenerator.getProjectAssignment(1));
		
		assertTrue(x > 0);
	}
	
	@Test
	public void testCompareName()
	{
		int x = new ProjectAssignmentComparator(ProjectAssignmentComparator.ASSIGNMENT_COMPARE_NAME)
					.compare(DummyDataGenerator.getProjectAssignment(2), DummyDataGenerator.getProjectAssignment(1));
		
		assertTrue(x > 0);
	}	
	
	@Test
	public void testCompareStart()
	{
		int x = new ProjectAssignmentComparator(ProjectAssignmentComparator.ASSIGNMENT_COMPARE_START)
					.compare(DummyDataGenerator.getProjectAssignment(1), DummyDataGenerator.getProjectAssignment(1));
		
		assertTrue(x == 0);
	}	

}
