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

import java.util.ArrayList;
import java.util.List;

import net.rrm.ehour.exception.ObjectNotFoundException;
import net.rrm.ehour.project.dao.ProjectAssignmentDAO;
import net.rrm.ehour.project.dao.ProjectDAO;
import net.rrm.ehour.project.domain.ProjectAssignment;
import net.rrm.ehour.report.dao.ReportAggregatedDAO;
import net.rrm.ehour.report.reports.ProjectAssignmentAggregate;
import net.rrm.ehour.timesheet.dao.TimesheetDAO;
import junit.framework.TestCase;

/**
 * TODO 
 **/

public class ProjectAssignmentServiceTest extends TestCase
{
	private	ProjectAssignmentService	projectAssignmentService;
	private	ProjectDAO				projectDAO;
	private	ProjectAssignmentDAO	projectAssignmentDAO;
	private	TimesheetDAO 			timesheetDAO;
	private ReportAggregatedDAO		reportAggregatedDAO;
	
	/**
	 * 
	 */
	protected void setUp()
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
		
	}
	
	/**
	 * @throws ObjectNotFoundException 
	 * 
	 *
	 */
	public void testGetProjectAssignment() throws ObjectNotFoundException
	{
		ProjectAssignment pa = new ProjectAssignment();
		
		expect(projectAssignmentDAO.findById(new Integer(1)))
			.andReturn(pa);

		List<Integer>	ids = new ArrayList<Integer>();
		ids.add(1);

		expect(reportAggregatedDAO.getCumulatedHoursPerAssignmentForAssignments(ids))
				.andReturn(new ArrayList<ProjectAssignmentAggregate>());

		replay(projectAssignmentDAO);
		replay(timesheetDAO);
		replay(reportAggregatedDAO);
		
		projectAssignmentService.getProjectAssignment(1);
		
		verify(projectAssignmentDAO);
		verify(timesheetDAO);
		verify(reportAggregatedDAO);
	}	
}
