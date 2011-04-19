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
import net.rrm.ehour.domain.ProjectAssignmentMother;

import org.junit.Test;

public class ProjectAssignmentComparatorTest
{
	@Test
	public void testCompareCustDatePrj()
	{
		int x = new ProjectAssignmentComparator(ProjectAssignmentComparator.ASSIGNMENT_COMPARE_CUSTDATEPRJ)
					.compare(ProjectAssignmentMother.createProjectAssignment(2), ProjectAssignmentMother.createProjectAssignment(1));
		
		assertTrue(x > 0);
	}
	
	@Test
	public void testCompareName()
	{
		int x = new ProjectAssignmentComparator(ProjectAssignmentComparator.ASSIGNMENT_COMPARE_NAME)
					.compare(ProjectAssignmentMother.createProjectAssignment(2), ProjectAssignmentMother.createProjectAssignment(1));
		
		assertTrue(x > 0);
	}	
	
	@Test
	public void testCompareStart()
	{
		int x = new ProjectAssignmentComparator(ProjectAssignmentComparator.ASSIGNMENT_COMPARE_START)
					.compare(ProjectAssignmentMother.createProjectAssignment(1), ProjectAssignmentMother.createProjectAssignment(1));
		
		assertTrue(x == 0);
	}	

}
