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
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.ArrayList;
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
import net.rrm.ehour.report.reports.element.AssignmentAggregateReportElement;
import net.rrm.ehour.user.dao.UserDAO;
import net.rrm.ehour.user.domain.User;
import net.rrm.ehour.user.domain.UserDepartment;

/**
 *  
 **/

@SuppressWarnings("unchecked")
public class AggregateReportServiceTest extends TestCase
{
	private	AggregateReportService	aggregateReportService;
	private UserDAO			userDAO;
	private ProjectDAO			projectDAO;
	private	ReportAggregatedDAO		reportAggregatedDAO;
	private	ReportCriteria 	rc;
	private ReportCriteriaService rsMock; 
	
	/**
	 * 
	 */
	protected void setUp()
	{
		aggregateReportService = new AggregateReportServiceImpl();

		reportAggregatedDAO = createMock(ReportAggregatedDAO.class);
		((AggregateReportServiceImpl)aggregateReportService).setReportAggregatedDAO(reportAggregatedDAO);

		userDAO = createMock(UserDAO.class);
		((AggregateReportServiceImpl)aggregateReportService).setUserDAO(userDAO);

		projectDAO = createMock(ProjectDAO.class);
		((AggregateReportServiceImpl)aggregateReportService).setProjectDAO(projectDAO);
		
		rc = new ReportCriteria();
		rsMock = createMock(ReportCriteriaService.class);
		
	}
	
//	/**
//	 * 
//	 *
//	 */
//	
//	public void testGetHoursPerAssignmentOnMonth()
//	{
//		List<AssignmentAggregateReportElement>	results = new ArrayList<AssignmentAggregateReportElement>();
//		Calendar	cal = new GregorianCalendar();
//		
//		results.add(new AssignmentAggregateReportElement());
//		
//		List<User> l = new ArrayList<User>();
//		l.add(new User(1));
//		reportAggregatedDAO.getCumulatedHoursPerAssignmentForUsers(l, DateUtil.calendarToMonthRange(cal));
//		expectLastCall().andReturn(results);
//		
//		replay();
//		
//		reportService.getHoursPerAssignmentInMonth(1, cal);
//		
//		verify();
//	}
	
	public void testCreateProjectReportUserId()
	{
		DateRange dr = new DateRange();
		UserCriteria uc = new UserCriteria();
		uc.setReportRange(dr);
		List<User> l = new ArrayList<User>();
		l.add(new User(1));
		uc.setUsers(l);
		rc.setUserCriteria(uc);
		List<AssignmentAggregateReportElement> pags = new ArrayList<AssignmentAggregateReportElement>();
		
		pags.add(DummyDataGenerator.getProjectAssignmentAggregate(1, 1, 1));
		pags.add(DummyDataGenerator.getProjectAssignmentAggregate(2, 2, 2));
		pags.add(DummyDataGenerator.getProjectAssignmentAggregate(3, 3, 3));
		
		expect(reportAggregatedDAO.getCumulatedHoursPerAssignmentForUsers(isA(List.class), isA(DateRange.class)))
					.andReturn(pags);
		replay(reportAggregatedDAO);
		aggregateReportService.getAggregateReportData(rc);
		verify(reportAggregatedDAO);
	}

	public void testCreateProjectReportNoUserId()
	{
		DateRange dr = new DateRange();
		UserCriteria uc = new UserCriteria();
		uc.setReportRange(dr);
		rc.setUserCriteria(uc);
		List<AssignmentAggregateReportElement> pags = new ArrayList<AssignmentAggregateReportElement>();
		
		pags.add(DummyDataGenerator.getProjectAssignmentAggregate(1, 1, 1));
		pags.add(DummyDataGenerator.getProjectAssignmentAggregate(2, 2, 2));
		pags.add(DummyDataGenerator.getProjectAssignmentAggregate(3, 3, 3));
		
		expect(reportAggregatedDAO.getCumulatedHoursPerAssignment(isA(DateRange.class))).andReturn(pags);
		replay(reportAggregatedDAO);
		aggregateReportService.getAggregateReportData(rc);
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
		List<UserDepartment> l = new ArrayList<UserDepartment>();
		l.add(new UserDepartment(2));
		
		uc.setDepartments(l);
		uc.setOnlyActiveUsers(true);
		rc.setUserCriteria(uc);
		List<AssignmentAggregateReportElement> pags = new ArrayList<AssignmentAggregateReportElement>();
		
		pags.add(DummyDataGenerator.getProjectAssignmentAggregate(1, 1, 1));
		pags.add(DummyDataGenerator.getProjectAssignmentAggregate(2, 2, 2));
		pags.add(DummyDataGenerator.getProjectAssignmentAggregate(3, 3, 3));
		
		expect(reportAggregatedDAO.getCumulatedHoursPerAssignmentForUsers(isA(List.class), isA(DateRange.class)))
		.andReturn(pags);
		
		expect(userDAO.findUsersForDepartments(null, l, true)).andReturn(users);
		
		replay(reportAggregatedDAO);
		replay(userDAO);
		aggregateReportService.getAggregateReportData(rc);
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
		List<AssignmentAggregateReportElement> pags = new ArrayList<AssignmentAggregateReportElement>();
		
		pags.add(DummyDataGenerator.getProjectAssignmentAggregate(1, 1, 1));
		pags.add(DummyDataGenerator.getProjectAssignmentAggregate(2, 2, 2));
		pags.add(DummyDataGenerator.getProjectAssignmentAggregate(3, 3, 3));
		
		expect(reportAggregatedDAO.getCumulatedHoursPerAssignmentForProjects(isA(List.class), isA(DateRange.class)))
		.andReturn(pags);
		expect(projectDAO.findProjectForCustomers(customers, true)).andReturn(prjs);
		
		replay(reportAggregatedDAO);
		replay(projectDAO);

		aggregateReportService.getAggregateReportData(rc);
		verify(reportAggregatedDAO);
		verify(projectDAO);
	}
}

