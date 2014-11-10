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

package net.rrm.ehour.sort;

import net.rrm.ehour.domain.ProjectAssignmentObjectMother;
import net.rrm.ehour.report.reports.element.AssignmentAggregateReportElement;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class ProjectAssignmentAggregateComparatorTest
{
	private AssignmentAggregateReportElement a1;
	private AssignmentAggregateReportElement a2;
	
	@Before
	public void setUp()
	{
		a1 = new AssignmentAggregateReportElement();
		a2 = new AssignmentAggregateReportElement();
		
		a1.setProjectAssignment(ProjectAssignmentObjectMother.createProjectAssignment(1));
		a2.setProjectAssignment(ProjectAssignmentObjectMother.createProjectAssignment(2));
	}

	@Test
	public void testCompareProject()
	{
		int x = new ProjectAssignmentAggregateComparator(ProjectAssignmentAggregateComparator.SORT_ON_PROJECT).compare(a1, a2);
		
		assertTrue(x < 0);
	}

    @Test
	public void testCompareCustomer()
	{
		int x = new ProjectAssignmentAggregateComparator(ProjectAssignmentAggregateComparator.SORT_ON_CUSTOMER).compare(a1, a2);
		
		assertTrue(x < 0);
	}
	
	
}
