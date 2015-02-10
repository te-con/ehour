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

package net.rrm.ehour.project.status;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import net.rrm.ehour.project.status.ProjectAssignmentStatus.Status;

import org.junit.Test;

public class ProjectAssignmentStatusTest
{
	/**
	 * Test method for {@link net.rrm.ehour.persistence.persistence.project.status.ProjectAssignmentStatus#isAssignmentBookable()}.
	 */
	@Test
	public void testIsAssignmentBookable()
	{
		ProjectAssignmentStatus status = new ProjectAssignmentStatus();

		List<Status> statusses = new ArrayList<Status>();
		statusses.add(ProjectAssignmentStatus.Status.RUNNING);
		statusses.add(ProjectAssignmentStatus.Status.IN_ALLOTTED);
		
		status.setStatusses(statusses);

		assertTrue(status.isAssignmentBookable());
	}

	@Test
	public void testIsAssignmentBookableFalse()
	{
		ProjectAssignmentStatus status = new ProjectAssignmentStatus();

		status.addStatus(ProjectAssignmentStatus.Status.BEFORE_START);
		status.addStatus(ProjectAssignmentStatus.Status.IN_OVERRUN);

		assertFalse(status.isAssignmentBookable());
	}

	@Test
	public void testIsAssignmentBookableFalseAfterDeadline()
	{
		ProjectAssignmentStatus status = new ProjectAssignmentStatus();

		status.addStatus(ProjectAssignmentStatus.Status.AFTER_DEADLINE);
		status.addStatus(ProjectAssignmentStatus.Status.IN_ALLOTTED);

		assertFalse(status.isAssignmentBookable());
	}

}
