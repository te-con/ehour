/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package net.rrm.ehour.ui.common.sort;

import static org.junit.Assert.assertTrue;
import net.rrm.ehour.report.reports.element.AssignmentAggregateReportElement;
import net.rrm.ehour.ui.common.DummyWebDataGenerator;

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
		
		a1.setProjectAssignment(DummyWebDataGenerator.getProjectAssignment(1));
		a2.setProjectAssignment(DummyWebDataGenerator.getProjectAssignment(2));
	}

	/**
	 * Test method for {@link net.rrm.ehour.ui.common.sort.ProjectAssignmentAggregateComparator#compare(net.rrm.ehour.report.reports.element.AssignmentAggregateReportElement, net.rrm.ehour.report.reports.element.AssignmentAggregateReportElement)}.
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
