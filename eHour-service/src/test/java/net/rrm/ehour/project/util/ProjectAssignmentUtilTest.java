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

import static org.easymock.EasyMock.createMock;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.domain.ProjectAssignmentType;
import net.rrm.ehour.report.dao.ReportAggregatedDAO;
import net.rrm.ehour.report.reports.element.AssignmentAggregateReportElement;
import net.rrm.ehour.util.EhourConstants;

import org.junit.Before;
import org.junit.Test;

/**
 * 
 **/

public class ProjectAssignmentUtilTest
{
	@Test
	public void testGetAssignmentStatusFixed()
	{
		ProjectAssignment assignment = new ProjectAssignment();
		ProjectAssignmentType type = new ProjectAssignmentType(EhourConstants.ASSIGNMENT_TIME_ALLOTTED_FIXED);
		assignment.setAssignmentType(type);
		
	}
	
	@Test
	public void testIsEmptyAggregateList()
	{
		List<AssignmentAggregateReportElement> aggs = new ArrayList<AssignmentAggregateReportElement>();
		
		AssignmentAggregateReportElement agg = new AssignmentAggregateReportElement();
		agg.setHours(0);
		aggs.add(agg);
		
		agg = new AssignmentAggregateReportElement();
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

}
