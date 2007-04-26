/**
 * Created on Apr 7, 2007
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.project.util;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import junit.framework.TestCase;
import net.rrm.ehour.project.domain.ProjectAssignment;
import net.rrm.ehour.project.domain.ProjectAssignmentType;
import net.rrm.ehour.project.dto.AssignmentStatus;
import net.rrm.ehour.report.dao.ReportAggregatedDAO;
import net.rrm.ehour.report.reports.ProjectAssignmentAggregate;
import net.rrm.ehour.util.EhourConstants;

import org.junit.Before;
import org.junit.Test;

/**
 * TODO 
 **/

public class TimeAllottedUtilTest extends TestCase
{
	private	TimeAllottedUtil util;
	private	ReportAggregatedDAO	raDAO;
	
	@Before
	public void setUp() throws Exception
	{
		util = new TimeAllottedUtil();

		raDAO = createMock(ReportAggregatedDAO.class);
		util.setReportAggregatedDAO(raDAO);
	}

	@Test
	public final void testGetAssignmentStatusFixed()
	{
		ProjectAssignmentAggregate pag = new ProjectAssignmentAggregate();
		pag.setHours(new Double(25));
		
		ProjectAssignment assignment = new ProjectAssignment();
		ProjectAssignmentType type = new ProjectAssignmentType();
		type.setAssignmentTypeId(EhourConstants.ASSIGNMENT_TIME_ALLOTTED_FIXED);
		assignment.setAssignmentType(type);
		assignment.setAllottedHours(new Float(250f));
		
		expect(raDAO.getCumulatedHoursForAssignment(assignment))
				.andReturn(pag);
		
		replay(raDAO);
		
		AssignmentStatus status = util.getAssignmentStatus(assignment);
		
		verify(raDAO);
		
		assertEquals(AssignmentStatus.IN_ALLOTTED_PHASE, status.getAssignmentPhase());
	}
	
	@Test
	public final void testGetAssignmentStatusFixedOverAllotted()
	{
		ProjectAssignmentAggregate pag = new ProjectAssignmentAggregate();
		pag.setHours(new Double(260));
		
		ProjectAssignment assignment = new ProjectAssignment();
		ProjectAssignmentType type = new ProjectAssignmentType();
		type.setAssignmentTypeId(EhourConstants.ASSIGNMENT_TIME_ALLOTTED_FIXED);
		assignment.setAssignmentType(type);
		assignment.setAllottedHours(new Float(250f));
		
		expect(raDAO.getCumulatedHoursForAssignment(assignment))
				.andReturn(pag);
		
		replay(raDAO);
		
		AssignmentStatus status = util.getAssignmentStatus(assignment);
		
		verify(raDAO);
		
		assertEquals(AssignmentStatus.OVER_ALLOTTED_PHASE, status.getAssignmentPhase());
	}	
	
	@Test
	public final void testGetAssignmentStatusFlex()
	{
		ProjectAssignmentAggregate pag = new ProjectAssignmentAggregate();
		pag.setHours(new Double(25));
		
		ProjectAssignment assignment = new ProjectAssignment();
		ProjectAssignmentType type = new ProjectAssignmentType();
		type.setAssignmentTypeId(EhourConstants.ASSIGNMENT_TIME_ALLOTTED_FLEX);
		assignment.setAssignmentType(type);
		assignment.setAllottedHours(new Float(250f));
		assignment.setAllowedOverrun(new Float(200f));
		
		expect(raDAO.getCumulatedHoursForAssignment(assignment))
				.andReturn(pag);
		
		replay(raDAO);
		
		AssignmentStatus status = util.getAssignmentStatus(assignment);
		
		verify(raDAO);
		
		assertEquals(AssignmentStatus.IN_ALLOTTED_PHASE, status.getAssignmentPhase());
	}
	
	@Test
	public final void testGetAssignmentStatusFlexOverrun()
	{
		ProjectAssignmentAggregate pag = new ProjectAssignmentAggregate();
		pag.setHours(new Double(260));
		
		ProjectAssignment assignment = new ProjectAssignment();
		ProjectAssignmentType type = new ProjectAssignmentType();
		type.setAssignmentTypeId(EhourConstants.ASSIGNMENT_TIME_ALLOTTED_FLEX);
		assignment.setAssignmentType(type);
		assignment.setAllottedHours(new Float(250f));
		assignment.setAllowedOverrun(new Float(200f));
		
		expect(raDAO.getCumulatedHoursForAssignment(assignment))
				.andReturn(pag);
		
		replay(raDAO);
		
		AssignmentStatus status = util.getAssignmentStatus(assignment);
		
		verify(raDAO);
		
		assertEquals(AssignmentStatus.IN_OVERRUN_PHASE, status.getAssignmentPhase());
	}	
	
	@Test
	public final void testGetAssignmentStatusFlexOverOverrun()
	{
		ProjectAssignmentAggregate pag = new ProjectAssignmentAggregate();
		pag.setHours(new Double(560));
		
		ProjectAssignment assignment = new ProjectAssignment();
		ProjectAssignmentType type = new ProjectAssignmentType();
		type.setAssignmentTypeId(EhourConstants.ASSIGNMENT_TIME_ALLOTTED_FLEX);
		assignment.setAssignmentType(type);
		assignment.setAllottedHours(new Float(250f));
		assignment.setAllowedOverrun(new Float(200f));
		
		expect(raDAO.getCumulatedHoursForAssignment(assignment))
				.andReturn(pag);
		
		replay(raDAO);
		
		AssignmentStatus status = util.getAssignmentStatus(assignment);
		
		verify(raDAO);
		
		assertEquals(AssignmentStatus.OVER_OVERRUN_PHASE, status.getAssignmentPhase());
	}	
	
}
