/**
 * Created on Mar 24, 2007
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
 *
 * This Software is copyright TE-CON 2007. This Software is not open source by definition. The source of the Software is available for educational purposes.
 * TE-CON holds all the ownership rights on the Software.
 * TE-CON freely grants the right to use the Software. Any reproduction or modification of this Software, whether for commercial use or open source,
 * is subject to obtaining the prior express authorization of TE-CON.
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.project.service;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.exception.ObjectNotFoundException;
import net.rrm.ehour.exception.OverBudgetException;
import net.rrm.ehour.project.dao.ProjectAssignmentDAO;
import net.rrm.ehour.project.dao.ProjectDAO;
import net.rrm.ehour.project.status.ProjectAssignmentStatus;
import net.rrm.ehour.project.status.ProjectAssignmentStatusService;
import net.rrm.ehour.project.status.ProjectAssignmentStatusServiceImpl;
import net.rrm.ehour.report.dao.ReportAggregatedDAO;
import net.rrm.ehour.report.reports.element.AssignmentAggregateReportElement;
import net.rrm.ehour.timesheet.dao.TimesheetDAO;
import net.rrm.ehour.util.EhourConstants;

import org.junit.Before;
import org.junit.Test;

/**
 * TODO 
 **/

public class ProjectAssignmentServiceTest
{
	private	ProjectAssignmentService	projectAssignmentService;
	private	ProjectDAO				projectDAO;
	private	ProjectAssignmentDAO	projectAssignmentDAO;
	private	TimesheetDAO 			timesheetDAO;
	private ReportAggregatedDAO		reportAggregatedDAO;
	private ProjectAssignmentStatusService	statusService;
	
	/**
	 * 
	 */
	@Before
	public void setUp()
	{
		projectAssignmentService = new ProjectAssignmentServiceImpl();

		projectDAO = createMock(ProjectDAO.class);
		((ProjectAssignmentServiceImpl)projectAssignmentService).setProjectDAO(projectDAO);
		
		projectAssignmentDAO = createMock(ProjectAssignmentDAO.class);
		((ProjectAssignmentServiceImpl)projectAssignmentService).setProjectAssignmentDAO(projectAssignmentDAO);
		
		timesheetDAO = createMock(TimesheetDAO.class);
		((ProjectAssignmentServiceImpl)projectAssignmentService).setTimesheetDAO(timesheetDAO);
		
		reportAggregatedDAO = createMock(ReportAggregatedDAO.class);
		((ProjectAssignmentServiceImpl)projectAssignmentService).setReportAggregatedDAO(reportAggregatedDAO);
		
		statusService = createMock(ProjectAssignmentStatusService.class);
		((ProjectAssignmentServiceImpl)projectAssignmentService).setProjectAssignmentStatusService(statusService);
	}
	
	/**
	 * @throws ObjectNotFoundException 
	 * 
	 *
	 */
	@Test
	public void testGetProjectAssignment() throws ObjectNotFoundException
	{
		ProjectAssignment pa = new ProjectAssignment();
		
		expect(projectAssignmentDAO.findById(new Integer(1)))
			.andReturn(pa);

		List<Serializable>	ids = new ArrayList<Serializable>();
		ids.add(1);

		expect(reportAggregatedDAO.getCumulatedHoursPerAssignmentForAssignments(ids))
				.andReturn(new ArrayList<AssignmentAggregateReportElement>());

		replay(projectAssignmentDAO);
		replay(timesheetDAO);
		replay(reportAggregatedDAO);
		
		projectAssignmentService.getProjectAssignment(1);
		
		verify(projectAssignmentDAO);
		verify(timesheetDAO);
		verify(reportAggregatedDAO);
	}	
	
	@Test
	public void testCheckAndNotifyOK() throws OverBudgetException
	{
		ProjectAssignment assignment = new ProjectAssignment(1);
		assignment.setAssignmentType(EhourConstants.ASSIGNMENT_TYPE_DATE);

		expect(projectAssignmentDAO.findById(new Integer(1)))
			.andReturn(assignment);
		replay(projectAssignmentDAO);

		ProjectAssignmentStatus status = new ProjectAssignmentStatus();
		status.setAssignmentPhase(ProjectAssignmentStatus.IN_DATERANGE_PHASE);
		
		expect(statusService.getAssignmentStatus(assignment))
			.andReturn(status);
		replay(statusService);
		
		
		projectAssignmentService.checkAndNotify(assignment);

		verify(projectAssignmentDAO);
		verify(statusService);

	}
}
