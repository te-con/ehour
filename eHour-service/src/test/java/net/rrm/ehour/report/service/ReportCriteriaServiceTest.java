/**
 * Created on 21-feb-2007
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

package net.rrm.ehour.report.service;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import net.rrm.ehour.DummyDataGenerator;
import net.rrm.ehour.customer.dao.CustomerDAO;
import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.domain.UserDepartment;
import net.rrm.ehour.project.dao.ProjectAssignmentDAO;
import net.rrm.ehour.project.dao.ProjectDAO;
import net.rrm.ehour.report.criteria.AvailableCriteria;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.criteria.ReportCriteriaUpdate;
import net.rrm.ehour.report.criteria.UserCriteria;
import net.rrm.ehour.report.dao.ReportAggregatedDAO;
import net.rrm.ehour.user.dao.UserDAO;
import net.rrm.ehour.user.dao.UserDepartmentDAO;

/**
 * test case for report criteria 
 **/

@SuppressWarnings("unchecked")
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
//		ReportCriteriaService rsMock = createMock(ReportCriteriaService.class);
//		reportCriteria.setReportCriteriaService(rsMock);
		
		userCriteria = new UserCriteria();
		userCriteria.setSingleUser(true);
		List ids = new ArrayList();
		ids.add(new User(1));
		userCriteria.setUsers(ids);
		reportCriteria.setUserCriteria(userCriteria);
		
		availCriteria = new AvailableCriteria();
		reportCriteria.setAvailableCriteria(availCriteria);
		
		prjAssignmentDAO.findProjectAssignmentsForUser(new User(1));
		expectLastCall().andReturn(prjAsgs);

		reportAggregatedDAO.getMinMaxDateTimesheetEntry(new User(1));
		expectLastCall().andReturn(null);
		
		replay(prjAssignmentDAO);
		replay(reportAggregatedDAO);
		
		reportCriteriaService.syncUserReportCriteria(reportCriteria, ReportCriteriaUpdate.UPDATE_ALL);
		
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
//		ReportCriteriaService rsMock = createMock(ReportCriteriaService.class);
//		reportCriteria.setReportCriteriaService(rsMock);
		
		userCriteria = new UserCriteria();
		userCriteria.setOnlyActiveUsers(false);
		userCriteria.setOnlyActiveCustomers(true);
		userCriteria.setOnlyActiveProjects(false);
		reportCriteria.setUserCriteria(userCriteria);
		
		availCriteria = new AvailableCriteria();
		reportCriteria.setAvailableCriteria(availCriteria);
		
		expect(userDAO.findUsersByNameMatch(null, false) ).andReturn(new ArrayList<User>());
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
		
		reportCriteriaService.syncUserReportCriteria(reportCriteria, ReportCriteriaUpdate.UPDATE_ALL);
		
		verify(reportAggregatedDAO);
		verify(projectDAO);
		verify(customerDAO);
		verify(userDAO);
	}	
	
}
