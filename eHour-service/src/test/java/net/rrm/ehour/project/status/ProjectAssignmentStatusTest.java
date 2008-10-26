/**
 * Created on Mar 16, 2008
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
	 * Test method for {@link net.rrm.ehour.project.status.ProjectAssignmentStatus#isAssignmentBookable()}.
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
