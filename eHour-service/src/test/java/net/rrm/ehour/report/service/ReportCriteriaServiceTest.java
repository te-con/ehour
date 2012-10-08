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

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.*;
import net.rrm.ehour.persistence.customer.dao.CustomerDao;
import net.rrm.ehour.persistence.project.dao.ProjectAssignmentDao;
import net.rrm.ehour.persistence.project.dao.ProjectDao;
import net.rrm.ehour.persistence.report.dao.ReportAggregatedDao;
import net.rrm.ehour.persistence.user.dao.UserDao;
import net.rrm.ehour.persistence.user.dao.UserDepartmentDao;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.criteria.ReportCriteriaUpdateType;
import net.rrm.ehour.report.criteria.UserCriteria;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;

/**
 * test case for report criteria 
 **/

public class ReportCriteriaServiceTest  
{
	private	ReportCriteriaService	reportCriteriaService;
	
	private	ReportAggregatedDao		reportAggregatedDAO;
	private	UserDao			userDAO;
	private	ProjectAssignmentDao	prjAssignmentDAO;
	private	CustomerDao		customerDAO;
	private	ProjectDao		projectDAO;
	private UserDepartmentDao userDepartmentDAO;
	
	@Before
	public void setup()
	{
		reportCriteriaService = new ReportCriteriaServiceImpl();

		reportAggregatedDAO = createMock(ReportAggregatedDao.class);
		((ReportCriteriaServiceImpl)reportCriteriaService).setReportAggregatedDAO(reportAggregatedDAO);
		
		prjAssignmentDAO = createMock(ProjectAssignmentDao.class);
		((ReportCriteriaServiceImpl)reportCriteriaService).setProjectAssignmentDAO(prjAssignmentDAO);

		userDAO = createMock(UserDao.class);
		((ReportCriteriaServiceImpl)reportCriteriaService).setUserDAO(userDAO);

		customerDAO = createMock(CustomerDao.class);
		((ReportCriteriaServiceImpl)reportCriteriaService).setCustomerDAO(customerDAO);

		projectDAO = createMock(ProjectDao.class);
		((ReportCriteriaServiceImpl)reportCriteriaService).setProjectDAO(projectDAO);

		userDepartmentDAO = createMock(UserDepartmentDao.class);
		((ReportCriteriaServiceImpl)reportCriteriaService).setUserDepartmentDAO(userDepartmentDAO);
	}
	
	@Test
	public void testSyncUserReportCriteriaUserSingle()
	{
		ReportCriteria		reportCriteria;
		UserCriteria		userCriteria;
		
		List<ProjectAssignment>	prjAsgs = new ArrayList<ProjectAssignment>();
		
		
		prjAsgs.add(ProjectAssignmentObjectMother.createProjectAssignment(1));
		prjAsgs.add(ProjectAssignmentObjectMother.createProjectAssignment(2));
		
		// bit odd but otherwise unnecc. stuff is called
//		ReportCriteriaService rsMock = createMock(ReportCriteriaService.class);
//		reportCriteria.setReportCriteriaService(rsMock);
		
		userCriteria = new UserCriteria();
		userCriteria.setSingleUser(true);
		List<User> ids = new ArrayList<User>();
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
		
		prjAsgs.add(ProjectAssignmentObjectMother.createProjectAssignment(1));
		prjAsgs.add(ProjectAssignmentObjectMother.createProjectAssignment(2));
		
		reportCriteria = new ReportCriteria();
		// bit odd but otherwise unnecc. stuff is called
//		ReportCriteriaService rsMock = createMock(ReportCriteriaService.class);
//		reportCriteria.setReportCriteriaService(rsMock);
		
		userCriteria = new UserCriteria();
		userCriteria.setOnlyActiveUsers(false);
		userCriteria.setOnlyActiveCustomers(true);
		userCriteria.setOnlyActiveProjects(false);
		reportCriteria = new ReportCriteria(userCriteria);
		
		expect(userDAO.findUsers(false) ).andReturn(new ArrayList<User>());
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
		
		prjAsgs.add(ProjectAssignmentObjectMother.createProjectAssignment(1));
		prjAsgs.add(ProjectAssignmentObjectMother.createProjectAssignment(2));
		
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
		
		expect(userDAO.findUsers(false) ).andReturn(new ArrayList<User>());
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
