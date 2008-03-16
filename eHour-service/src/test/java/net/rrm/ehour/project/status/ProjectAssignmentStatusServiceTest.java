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

package net.rrm.ehour.project.status;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.GregorianCalendar;

import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.domain.ProjectAssignmentType;
import net.rrm.ehour.report.dao.ReportAggregatedDAO;
import net.rrm.ehour.report.reports.element.AssignmentAggregateReportElement;
import net.rrm.ehour.util.EhourConstants;

import org.junit.Before;
import org.junit.Test;

import sun.util.calendar.Gregorian;

/**
 * 
 **/

public class ProjectAssignmentStatusServiceTest
{
	private	ProjectAssignmentStatusServiceImpl util;
	private	ReportAggregatedDAO	raDAO;
	
	@Before
	public void setUp() throws Exception
	{
		util = new ProjectAssignmentStatusServiceImpl();

		raDAO = createMock(ReportAggregatedDAO.class);
		util.setReportAggregatedDAO(raDAO);
	}

	@Test
	public final void testGetAssignmentStatusDateIn()
	{
		ProjectAssignment assignment = new ProjectAssignment();
		ProjectAssignmentType type = new ProjectAssignmentType();
		type.setAssignmentTypeId(EhourConstants.ASSIGNMENT_DATE);
		assignment.setAssignmentType(type);
		
		Calendar startCal = new GregorianCalendar();
		startCal.add(Calendar.DAY_OF_YEAR, -5);
		assignment.setDateStart(startCal.getTime());

		Calendar endCal = new GregorianCalendar();
		endCal.add(Calendar.DAY_OF_YEAR, 2);
		assignment.setDateEnd(endCal.getTime());
		
		ProjectAssignmentStatus status = util.getAssignmentStatus(assignment);

		assertTrue(status.getStatusses().contains(ProjectAssignmentStatus.Status.RUNNING));
		assertEquals(1, status.getStatusses().size());
	}	
	
	@Test
	public final void testGetAssignmentStatusDateOut()
	{
		ProjectAssignment assignment = new ProjectAssignment();
		ProjectAssignmentType type = new ProjectAssignmentType();
		type.setAssignmentTypeId(EhourConstants.ASSIGNMENT_DATE);
		assignment.setAssignmentType(type);
		
		Calendar startCal = new GregorianCalendar();
		startCal.add(Calendar.DAY_OF_YEAR, -5);
		assignment.setDateStart(startCal.getTime());

		Calendar endCal = new GregorianCalendar();
		endCal.add(Calendar.DAY_OF_YEAR, -2);
		assignment.setDateEnd(endCal.getTime());
		
		ProjectAssignmentStatus status = util.getAssignmentStatus(assignment);

		assertTrue(status.getStatusses().contains(ProjectAssignmentStatus.Status.AFTER_DEADLINE));
		assertEquals(1, status.getStatusses().size());
	}	
	
	@Test
	public final void testGetAssignmentStatusFixed()
	{
		AssignmentAggregateReportElement pag = new AssignmentAggregateReportElement();
		pag.setHours(new Double(25));
		
		ProjectAssignment assignment = new ProjectAssignment();
		ProjectAssignmentType type = new ProjectAssignmentType();
		type.setAssignmentTypeId(EhourConstants.ASSIGNMENT_TIME_ALLOTTED_FIXED);
		assignment.setAssignmentType(type);
		assignment.setAllottedHours(new Float(250f));
		
		expect(raDAO.getCumulatedHoursForAssignment(assignment))
				.andReturn(pag);
		
		replay(raDAO);
		
		ProjectAssignmentStatus status = util.getAssignmentStatus(assignment);
		
		verify(raDAO);

		assertTrue(status.getStatusses().contains(ProjectAssignmentStatus.Status.IN_ALLOTTED));
		assertTrue(status.getStatusses().contains(ProjectAssignmentStatus.Status.RUNNING));
		assertEquals(2, status.getStatusses().size());
	}
	
	@Test
	public final void testGetAssignmentStatusFixedOverAllotted()
	{
		AssignmentAggregateReportElement pag = new AssignmentAggregateReportElement();
		pag.setHours(new Double(260));
		
		ProjectAssignment assignment = new ProjectAssignment();
		ProjectAssignmentType type = new ProjectAssignmentType();
		type.setAssignmentTypeId(EhourConstants.ASSIGNMENT_TIME_ALLOTTED_FIXED);
		assignment.setAssignmentType(type);
		assignment.setAllottedHours(new Float(250f));
		
		expect(raDAO.getCumulatedHoursForAssignment(assignment))
				.andReturn(pag);
		
		replay(raDAO);
		
		ProjectAssignmentStatus status = util.getAssignmentStatus(assignment);
		
		verify(raDAO);
		
		assertTrue(status.getStatusses().contains(ProjectAssignmentStatus.Status.OVER_ALLOTTED));
		assertTrue(status.getStatusses().contains(ProjectAssignmentStatus.Status.RUNNING));
		assertEquals(2, status.getStatusses().size());
	}	
	
	@Test
	public final void testGetAssignmentStatusFlex()
	{
		AssignmentAggregateReportElement pag = new AssignmentAggregateReportElement();
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
		
		ProjectAssignmentStatus status = util.getAssignmentStatus(assignment);
		
		verify(raDAO);
		
		assertTrue(status.getStatusses().contains(ProjectAssignmentStatus.Status.IN_ALLOTTED));
		assertTrue(status.getStatusses().contains(ProjectAssignmentStatus.Status.RUNNING));
		assertEquals(2, status.getStatusses().size());
	}
	
	@Test
	public final void testGetAssignmentStatusFlexOverrun()
	{
		AssignmentAggregateReportElement pag = new AssignmentAggregateReportElement();
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
		
		ProjectAssignmentStatus status = util.getAssignmentStatus(assignment);
		
		verify(raDAO);
		
		assertTrue(status.getStatusses().contains(ProjectAssignmentStatus.Status.IN_OVERRUN));
		assertTrue(status.getStatusses().contains(ProjectAssignmentStatus.Status.RUNNING));
		assertEquals(2, status.getStatusses().size());
	}	
	
	@Test
	public final void testGetAssignmentStatusFlexOverOverrun()
	{
		AssignmentAggregateReportElement pag = new AssignmentAggregateReportElement();
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
		
		ProjectAssignmentStatus status = util.getAssignmentStatus(assignment);
		
		verify(raDAO);
		
		assertTrue(status.getStatusses().contains(ProjectAssignmentStatus.Status.OVER_OVERRUN));
		assertTrue(status.getStatusses().contains(ProjectAssignmentStatus.Status.RUNNING));
		assertEquals(2, status.getStatusses().size());
	}	

//	@Test
//	public void testGetAssignmentStatusFixed()
//	{
//		ProjectAssignment assignment = new ProjectAssignment();
//		ProjectAssignmentType type = new ProjectAssignmentType(EhourConstants.ASSIGNMENT_TIME_ALLOTTED_FIXED);
//		assignment.setAssignmentType(type);
//		
//	}

}
