/**
 * Created on Nov 4, 2006
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
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import junit.framework.TestCase;
import net.rrm.ehour.DummyDataGenerator;
import net.rrm.ehour.customer.domain.Customer;
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.project.dao.ProjectDAO;
import net.rrm.ehour.project.domain.Project;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.criteria.UserCriteria;
import net.rrm.ehour.report.dao.ReportAggregatedDAO;
import net.rrm.ehour.report.dao.ReportPerMonthDAO;
import net.rrm.ehour.report.reports.ProjectAssignmentAggregate;
import net.rrm.ehour.user.dao.UserDAO;
import net.rrm.ehour.user.domain.User;
import net.rrm.ehour.util.DateUtil;

/**
 *  
 **/

@SuppressWarnings("unchecked")
public class ReportServiceTest extends TestCase
{
	private	ReportService	reportService;
	private UserDAO			userDAO;
	private ProjectDAO			projectDAO;
	private	ReportAggregatedDAO		reportAggregatedDAO;
	private	ReportPerMonthDAO	reportMonthDAO;
	private	ReportCriteria 	rc;
	private ReportCriteriaService rsMock; 
	/**
	 * 
	 */
	protected void setUp()
	{
		reportService = new ReportServiceImpl();

		reportAggregatedDAO = createMock(ReportAggregatedDAO.class);
		((ReportServiceImpl)reportService).setReportAggregatedDAO(reportAggregatedDAO);

		userDAO = createMock(UserDAO.class);
		((ReportServiceImpl)reportService).setUserDAO(userDAO);

		reportMonthDAO = createMock(ReportPerMonthDAO.class);
		((ReportServiceImpl)reportService).setReportPerMonthDAO(reportMonthDAO);

		projectDAO = createMock(ProjectDAO.class);
		((ReportServiceImpl)reportService).setProjectDAO(projectDAO);
	
		rc = new ReportCriteria();
		rsMock = createMock(ReportCriteriaService.class);
//		rc.setReportCriteriaService(rsMock);
	}
	
	/**
	 * 
	 *
	 */
	
	public void testGetHoursPerAssignmentOnMonth()
	{
		List<ProjectAssignmentAggregate>	results = new ArrayList<ProjectAssignmentAggregate>();
		Calendar	cal = new GregorianCalendar();
		
		results.add(new ProjectAssignmentAggregate());
		
		reportAggregatedDAO.getCumulatedHoursPerAssignmentForUsers(getAsList(1), DateUtil.calendarToMonthRange(cal));
		expectLastCall().andReturn(results);
		
		replay();
		
		reportService.getHoursPerAssignmentInMonth(1, cal);
		
		verify();
	}
	
	public void testCreateProjectReportUserId()
	{
		DateRange dr = new DateRange();
		UserCriteria uc = new UserCriteria();
		uc.setReportRange(dr);
		uc.setUserIds(getAsList(1));
		rc.setUserCriteria(uc);
		List<ProjectAssignmentAggregate> pags = new ArrayList<ProjectAssignmentAggregate>();
		
		pags.add(DummyDataGenerator.getProjectAssignmentAggregate(1, 1, 1));
		pags.add(DummyDataGenerator.getProjectAssignmentAggregate(2, 2, 2));
		pags.add(DummyDataGenerator.getProjectAssignmentAggregate(3, 3, 3));
		
		expect(reportAggregatedDAO.getCumulatedHoursPerAssignmentForUsers(isA(List.class), isA(DateRange.class)))
					.andReturn(pags);
		replay(reportAggregatedDAO);
		reportService.createAggregateReportData(rc);
		verify(reportAggregatedDAO);
	}

	public void testCreateProjectReportNoUserId()
	{
		DateRange dr = new DateRange();
		UserCriteria uc = new UserCriteria();
		uc.setReportRange(dr);
		rc.setUserCriteria(uc);
		List<ProjectAssignmentAggregate> pags = new ArrayList<ProjectAssignmentAggregate>();
		
		pags.add(DummyDataGenerator.getProjectAssignmentAggregate(1, 1, 1));
		pags.add(DummyDataGenerator.getProjectAssignmentAggregate(2, 2, 2));
		pags.add(DummyDataGenerator.getProjectAssignmentAggregate(3, 3, 3));
		
		expect(reportAggregatedDAO.getCumulatedHoursPerAssignment(isA(DateRange.class))).andReturn(pags);
		replay(reportAggregatedDAO);
		reportService.createAggregateReportData(rc);
		verify(reportAggregatedDAO);
	}

	public void testCreateProjectReportNoUserIdDptId()
	{
		List<User> users = new ArrayList<User>();
		User user = new User(1);
		users.add(user);

		DateRange dr = new DateRange();
		UserCriteria uc = new UserCriteria();
		uc.setReportRange(dr);
		uc.setDepartmentIds(getAsList(2));
		uc.setOnlyActiveUsers(true);
		rc.setUserCriteria(uc);
		List<ProjectAssignmentAggregate> pags = new ArrayList<ProjectAssignmentAggregate>();
		
		pags.add(DummyDataGenerator.getProjectAssignmentAggregate(1, 1, 1));
		pags.add(DummyDataGenerator.getProjectAssignmentAggregate(2, 2, 2));
		pags.add(DummyDataGenerator.getProjectAssignmentAggregate(3, 3, 3));
		
		expect(reportAggregatedDAO.getCumulatedHoursPerAssignmentForUsers(isA(List.class), isA(DateRange.class)))
		.andReturn(pags);
		
		expect(userDAO.findUsersForDepartments(null, getAsList(2), true)).andReturn(users);
		
		replay(reportAggregatedDAO);
		replay(userDAO);
		reportService.createAggregateReportData(rc);
		verify(reportAggregatedDAO);
		verify(userDAO);
	}
	
	public void testCreateProjectReport()
	{
		Customer cust = new Customer(1);
		List<Customer> customers = new ArrayList<Customer>();
		customers.add(cust);

		List<Project> prjs = new ArrayList<Project>();
		Project prj = new Project(1);
		prjs.add(prj);		
		
		DateRange dr = new DateRange();
		UserCriteria uc = new UserCriteria();
		uc.setReportRange(dr);
		uc.setCustomers(customers);
		rc.setUserCriteria(uc);
		List<ProjectAssignmentAggregate> pags = new ArrayList<ProjectAssignmentAggregate>();
		
		pags.add(DummyDataGenerator.getProjectAssignmentAggregate(1, 1, 1));
		pags.add(DummyDataGenerator.getProjectAssignmentAggregate(2, 2, 2));
		pags.add(DummyDataGenerator.getProjectAssignmentAggregate(3, 3, 3));
		
		expect(reportAggregatedDAO.getCumulatedHoursPerAssignmentForProjects(isA(List.class), isA(DateRange.class)))
		.andReturn(pags);
		expect(projectDAO.findProjectForCustomers(customers, true)).andReturn(prjs);
		
		replay(reportAggregatedDAO);
		replay(projectDAO);

		reportService.createAggregateReportData(rc);
		verify(reportAggregatedDAO);
		verify(projectDAO);
	}

	private List<Integer> getAsList(Integer id)
	{
		List ids = new ArrayList();
		ids.add(id);
		
		return ids;
		
	}
	
}

