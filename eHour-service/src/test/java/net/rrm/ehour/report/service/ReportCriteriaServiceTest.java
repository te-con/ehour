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

package net.rrm.ehour.report.service;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import net.rrm.ehour.customer.dao.CustomerDAO;
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.domain.ProjectAssignmentMother;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.domain.UserDepartment;
import net.rrm.ehour.project.dao.ProjectAssignmentDAO;
import net.rrm.ehour.project.dao.ProjectDAO;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.criteria.ReportCriteriaUpdateType;
import net.rrm.ehour.report.criteria.UserCriteria;
import net.rrm.ehour.report.dao.ReportAggregatedDAO;
import net.rrm.ehour.user.dao.UserDAO;
import net.rrm.ehour.user.dao.UserDepartmentDAO;

import org.junit.Before;
import org.junit.Test;

/**
 * test case for report criteria 
 **/

@SuppressWarnings("unchecked")

public class ReportCriteriaServiceTest  
{
	private	ReportCriteriaService	reportCriteriaService;
	
	private	ReportAggregatedDAO		reportAggregatedDAO;
	private	UserDAO			userDAO;
	private	ProjectAssignmentDAO	prjAssignmentDAO;
	private	CustomerDAO		customerDAO;
	private	ProjectDAO		projectDAO;
	private UserDepartmentDAO userDepartmentDAO;
	
	@Before
	public void setup()
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
	
	@Test
	public void testSyncUserReportCriteriaUserSingle()
	{
		ReportCriteria		reportCriteria;
		UserCriteria		userCriteria;
		
		List<ProjectAssignment>	prjAsgs = new ArrayList<ProjectAssignment>();
		
		
		prjAsgs.add(ProjectAssignmentMother.createProjectAssignment(1));
		prjAsgs.add(ProjectAssignmentMother.createProjectAssignment(2));
		
		// bit odd but otherwise unnecc. stuff is called
//		ReportCriteriaService rsMock = createMock(ReportCriteriaService.class);
//		reportCriteria.setReportCriteriaService(rsMock);
		
		userCriteria = new UserCriteria();
		userCriteria.setSingleUser(true);
		List ids = new ArrayList();
		ids.add(new User(1));
		userCriteria.setUsers(ids);
		reportCriteria = new ReportCriteria(userCriteria);		
		prjAssignmentDAO.findProjectAssignmentsForUser(isA(Integer.class), isA(DateRange.class));
		expectLastCall().andReturn(prjAsgs);

		reportAggregatedDAO.getMinMaxDateTimesheetEntry(isA(User.class));
		expectLastCall().andReturn(null);
		
		replay(prjAssignmentDAO);
		replay(reportAggregatedDAO);
		
		reportCriteriaService.syncUserReportCriteria(reportCriteria, ReportCriteriaUpdateType.UPDATE_ALL);
		
		verify(reportAggregatedDAO);
		verify(prjAssignmentDAO);
		
		assertEquals(2, reportCriteria.getAvailableCriteria().getCustomers().size());
	}	

	@Test
	public void testSyncUserReportCriteriaUserAll()
	{
		ReportCriteria		reportCriteria;
		UserCriteria		userCriteria;
		
		List<ProjectAssignment>	prjAsgs = new ArrayList<ProjectAssignment>();
		
		prjAsgs.add(ProjectAssignmentMother.createProjectAssignment(1));
		prjAsgs.add(ProjectAssignmentMother.createProjectAssignment(2));
		
		reportCriteria = new ReportCriteria();
		// bit odd but otherwise unnecc. stuff is called
//		ReportCriteriaService rsMock = createMock(ReportCriteriaService.class);
//		reportCriteria.setReportCriteriaService(rsMock);
		
		userCriteria = new UserCriteria();
		userCriteria.setOnlyActiveUsers(false);
		userCriteria.setOnlyActiveCustomers(true);
		userCriteria.setOnlyActiveProjects(false);
		reportCriteria = new ReportCriteria(userCriteria);
		
		expect(userDAO.findUsersByNameMatch(null, false) ).andReturn(new ArrayList<User>());
		replay(userDAO);
		
		expect(customerDAO.findAllActive()).andReturn(new ArrayList<Customer>());
		replay(customerDAO);
		
		expect(projectDAO.findAll()).andReturn(new ArrayList<Project>());
		replay(projectDAO);

		expect(userDepartmentDAO.findAll()).andReturn(new ArrayList<UserDepartment>());
		replay(userDepartmentDAO);

		reportAggregatedDAO.getMinMaxDateTimesheetEntry();
		expectLastCall().andReturn(null);
		replay(reportAggregatedDAO);
		
		reportCriteriaService.syncUserReportCriteria(reportCriteria, ReportCriteriaUpdateType.UPDATE_ALL);
		
		verify(reportAggregatedDAO);
		verify(projectDAO);
		verify(customerDAO);
		verify(userDAO);
	}
	
	@Test
	public void shouldSyncUserReportCriteriaUserAllBillable()
	{
		ReportCriteria		reportCriteria;
		UserCriteria		userCriteria;
		
		List<ProjectAssignment>	prjAsgs = new ArrayList<ProjectAssignment>();
		
		prjAsgs.add(ProjectAssignmentMother.createProjectAssignment(1));
		prjAsgs.add(ProjectAssignmentMother.createProjectAssignment(2));
		
		reportCriteria = new ReportCriteria();
		// bit odd but otherwise unnecc. stuff is called
//		ReportCriteriaService rsMock = createMock(ReportCriteriaService.class);
//		reportCriteria.setReportCriteriaService(rsMock);
		
		userCriteria = new UserCriteria();
		userCriteria.setOnlyActiveUsers(false);
		userCriteria.setOnlyActiveCustomers(true);
		userCriteria.setOnlyActiveProjects(false);
		userCriteria.setOnlyBillableProjects(true);
		reportCriteria = new ReportCriteria(userCriteria);
		
		expect(userDAO.findUsersByNameMatch(null, false) ).andReturn(new ArrayList<User>());
		replay(userDAO);
		
		expect(customerDAO.findAllActive()).andReturn(new ArrayList<Customer>());
		replay(customerDAO);
		
		expect(projectDAO.findAll()).andReturn(new ArrayList<Project>());
		replay(projectDAO);

		expect(userDepartmentDAO.findAll()).andReturn(new ArrayList<UserDepartment>());
		replay(userDepartmentDAO);

		reportAggregatedDAO.getMinMaxDateTimesheetEntry();
		expectLastCall().andReturn(null);
		replay(reportAggregatedDAO);
		
		reportCriteriaService.syncUserReportCriteria(reportCriteria, ReportCriteriaUpdateType.UPDATE_ALL);
		
		verify(reportAggregatedDAO);
		verify(projectDAO);
		verify(customerDAO);
		verify(userDAO);
	}	
	
}
