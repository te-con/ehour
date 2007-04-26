/**
 * Created on Mar 24, 2007
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

package net.rrm.ehour.project.service;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import net.rrm.ehour.project.dao.ProjectAssignmentDAO;
import net.rrm.ehour.project.dao.ProjectDAO;
import net.rrm.ehour.project.domain.ProjectAssignment;
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
	}
	
	/**
	 * 
	 *
	 */
	public void testGetProjectAssignment()
	{
		ProjectAssignment pa = new ProjectAssignment();
		
		expect(projectAssignmentDAO.findById(new Integer(1)))
			.andReturn(pa);
		
		expect(timesheetDAO.getTimesheetEntryCountForAssignment(1))
			.andReturn(0);

		replay(projectAssignmentDAO);
		replay(timesheetDAO);
		
		projectAssignmentService.getProjectAssignment(1);
		
		verify(projectAssignmentDAO);
		verify(timesheetDAO);		
	}	
}
