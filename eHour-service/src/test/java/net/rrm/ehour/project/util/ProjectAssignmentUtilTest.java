/**
 * Created on Nov 2, 2007
 * Created by Thies Edeling
 * Created by Thies Edeling
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

package net.rrm.ehour.project.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import net.rrm.ehour.project.domain.ProjectAssignment;
import net.rrm.ehour.project.dto.AssignmentStatus;
import net.rrm.ehour.report.reports.ProjectAssignmentAggregate;
import net.rrm.ehour.util.EhourConstants;

import org.junit.Test;

/**
 * 
 **/

public class ProjectAssignmentUtilTest
{
	@Test
	public void testIsEmptyAggregateList()
	{
		List<ProjectAssignmentAggregate> aggs = new ArrayList<ProjectAssignmentAggregate>();
		
		ProjectAssignmentAggregate agg = new ProjectAssignmentAggregate();
		agg.setHours(0);
		aggs.add(agg);
		
		agg = new ProjectAssignmentAggregate();
		aggs.add(agg);
		
		assertTrue(ProjectAssignmentUtil.isEmptyAggregateList(aggs));
		
	}

	@Test
	public void testGetAssignmentIds()
	{
		List<ProjectAssignment> assignments = new ArrayList<ProjectAssignment>();
		
		{
			ProjectAssignment assignment = new ProjectAssignment();
			assignment.setAssignmentId(1);
			assignments.add(assignment);
		}

		{
			ProjectAssignment assignment = new ProjectAssignment();
			assignment.setAssignmentId(2);
			assignments.add(assignment);
		}

		assertEquals(2, ProjectAssignmentUtil.getAssignmentIds(assignments).size());
	}

	@Test
	public void testGetAssignmentStatusDate()
	{
		ProjectAssignment assignment = new ProjectAssignment();
		assignment.setAssignmentType(EhourConstants.ASSIGNMENT_TYPE_DATE);
		
		AssignmentStatus status = new ProjectAssignmentUtil().getAssignmentStatus(assignment);
		
		assertEquals(AssignmentStatus.IN_DATERANGE_PHASE, status.getAssignmentPhase());
	}
}
