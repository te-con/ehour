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

package net.rrm.ehour.ui.timesheet.panel.util;

import net.rrm.ehour.config.EhourConfigStub;
import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.domain.ProjectAssignmentObjectMother;
import net.rrm.ehour.ui.timesheet.dto.TimesheetRow;
import net.rrm.ehour.ui.timesheet.util.TimesheetRowComparator;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author thies
 *
 */
public class TimesheetRowComparatorTest
{
	@Test
	public void testCompare()
	{
		ProjectAssignment pA = ProjectAssignmentObjectMother.createProjectAssignment(1);
		pA.getProject().setName("A");
		TimesheetRow rowA = new TimesheetRow(new EhourConfigStub());
		rowA.setProjectAssignment(pA);
		
		ProjectAssignment pB = ProjectAssignmentObjectMother.createProjectAssignment(1);
		pB.getProject().setName("b");
		TimesheetRow rowB = new TimesheetRow(new EhourConfigStub());
		rowB.setProjectAssignment(pB);
		
		assertEquals(-1, TimesheetRowComparator.INSTANCE.compare(rowA, rowB));
	}

}
