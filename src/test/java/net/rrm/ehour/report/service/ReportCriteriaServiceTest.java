/**
 * Created on 21-feb-2007
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

package net.rrm.ehour.report.service;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.ArrayList;
import java.util.List;

import net.rrm.ehour.DummyDataGenerator;
import net.rrm.ehour.customer.dao.CustomerDAO;
import net.rrm.ehour.customer.domain.Customer;
import net.rrm.ehour.project.dao.ProjectAssignmentDAO;
import net.rrm.ehour.project.dao.ProjectDAO;
import net.rrm.ehour.project.domain.Project;
import net.rrm.ehour.project.domain.ProjectAssignment;
import net.rrm.ehour.report.criteria.AvailableCriteria;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.criteria.UserCriteria;
import net.rrm.ehour.report.dao.ReportAggregatedDAO;
import net.rrm.ehour.user.dao.UserDAO;
import net.rrm.ehour.user.dao.UserDepartmentDAO;
import net.rrm.ehour.user.domain.User;
import net.rrm.ehour.user.domain.UserDepartment;
import junit.framework.TestCase;

/**
 * TODO 
 **/

public class ReportCriteriaServiceTest  extends TestCase
{
	private	ReportCriteriaService	reportCriteriaService;
	
	private	ReportAggregatedDAO		reportAggregatedDAO;
	private	UserDAO			userDAO;
	private	ProjectAssignmentDAO	prjAssignmentDAO;
	private	CustomerDAO		customerDAO;
	private	ProjectDAO		projectDAO;
	private UserDepartmentDAO userDepartmentDAO;
	
	/**
	 * 
	 */
	protected void setUp()
	{
		reportCriteriaService = new ReportCriteriaServiceImpl();

		reportAggregatedDAO = createMock(ReportAggregatedDAO.class);
		((ReportCriteriaServiceImpl)reportCriteriaService).setReportAggregatedDAO(reportAggregatedDAO);
		
		prjAssignmentDAO = createMock(ProjectAssignmentDAO.class);
		((ReportCriteriaServiceImpl)reportCriteriaService).setProjectAssignmentDAO(prjAssignmentDAO);

		userDAO = createMock(UserDAO.class);
		((ReportCriteriaServiceImpl)reportCriteriaService).setUserDAO(userDAO);

		customerDAO = createMock(CustomerDAO.class);
		((ReportCriteriaServiceImpl)reportCriteriaService).setCustomerDAO(customerDAO);

		projectDAO = createMock(ProjectDAO.class);
		((ReportCriteriaServiceImpl)reportCriteriaService).setProjectDAO(projectDAO);

		userDepartmentDAO = createMock(UserDepartmentDAO.class);
		((ReportCriteriaServiceImpl)reportCriteriaService).setUserDepartmentDAO(userDepartmentDAO);
	}
	
	public void testSyncUserReportCriteriaUserSingle()
	{
		ReportCriteria		reportCriteria;
		UserCriteria		userCriteria;
		AvailableCriteria	availCriteria;
		
		List<ProjectAssignment>	prjAsgs = new ArrayList<ProjectAssignment>();
		
		
		prjAsgs.add(DummyDataGenerator.getProjectAssignment(1));
		prjAsgs.add(DummyDataGenerator.getProjectAssignment(2));
		
		reportCriteria = new ReportCriteria();
		// bit odd but otherwise unnecc. stuff is called
		ReportCriteriaService rsMock = createMock(ReportCriteriaService.class);
		reportCriteria.setReportCriteriaService(rsMock);
		
		userCriteria = new UserCriteria();
		userCriteria.setSingleUser(true);
		userCriteria.setUserIds(new Integer[]{1});
		reportCriteria.setUserCriteria(userCriteria);
		
		availCriteria = new AvailableCriteria();
		reportCriteria.setAvailableCriteria(availCriteria);
		
		prjAssignmentDAO.findProjectAssignmentsForUser(1);
		expectLastCall().andReturn(prjAsgs);

		reportAggregatedDAO.getMinMaxDateTimesheetEntry(1);
		expectLastCall().andReturn(null);
		
		replay(prjAssignmentDAO);
		replay(reportAggregatedDAO);
		
		reportCriteriaService.syncUserReportCriteria(reportCriteria, ReportCriteria.UPDATE_ALL);
		
		verify(reportAggregatedDAO);
		verify(prjAssignmentDAO);
		
		assertEquals(2, availCriteria.getCustomers().size());
	}	

	/**
	 * 
	 *
	 */
	public void testSyncUserReportCriteriaUserAll()
	{
		ReportCriteria		reportCriteria;
		UserCriteria		userCriteria;
		AvailableCriteria	availCriteria;
		
		List<ProjectAssignment>	prjAsgs = new ArrayList<ProjectAssignment>();
		
		prjAsgs.add(DummyDataGenerator.getProjectAssignment(1));
		prjAsgs.add(DummyDataGenerator.getProjectAssignment(2));
		
		reportCriteria = new ReportCriteria();
		// bit odd but otherwise unnecc. stuff is called
		ReportCriteriaService rsMock = createMock(ReportCriteriaService.class);
		reportCriteria.setReportCriteriaService(rsMock);
		
		userCriteria = new UserCriteria();
		userCriteria.setOnlyActiveUsers(false);
		userCriteria.setOnlyActiveCustomers(true);
		userCriteria.setOnlyActiveProjects(false);
		reportCriteria.setUserCriteria(userCriteria);
		
		availCriteria = new AvailableCriteria();
		reportCriteria.setAvailableCriteria(availCriteria);
		
		expect(userDAO.findAll()).andReturn(new ArrayList<User>());
		replay(userDAO);
		
		expect(customerDAO.findAll(true)).andReturn(new ArrayList<Customer>());
		replay(customerDAO);
		
		expect(projectDAO.findAll()).andReturn(new ArrayList<Project>());
		replay(projectDAO);

		expect(userDepartmentDAO.findAll()).andReturn(new ArrayList<UserDepartment>());
		replay(userDepartmentDAO);

		reportAggregatedDAO.getMinMaxDateTimesheetEntry();
		expectLastCall().andReturn(null);
		replay(reportAggregatedDAO);
		
		reportCriteriaService.syncUserReportCriteria(reportCriteria, ReportCriteria.UPDATE_ALL);
		
		verify(reportAggregatedDAO);
		verify(projectDAO);
		verify(customerDAO);
		verify(userDAO);
	}	
	
}
