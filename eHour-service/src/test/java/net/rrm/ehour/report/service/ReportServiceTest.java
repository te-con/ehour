/**
 * Created on Nov 4, 2006
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
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import junit.framework.TestCase;
import net.rrm.ehour.DummyDataGenerator;
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
		rc.setReportCriteriaService(rsMock);

	}
	
	/**
	 * 
	 *
	 */
	
	public void testGetHoursPerAssignmentOnMonth()
	{
		List<ProjectAssignmentAggregate>	results = new ArrayList<ProjectAssignmentAggregate>();
		Integer		userId = 1;
		Calendar	cal = new GregorianCalendar();
		
		results.add(new ProjectAssignmentAggregate());
		
		reportAggregatedDAO.getCumulatedHoursPerAssignmentForUsers(new Integer[]{userId}, DateUtil.calendarToMonthRange(cal));
		expectLastCall().andReturn(results);
		
		replay();
		
		reportService.getHoursPerAssignmentInMonth(userId, cal);
		
		verify();
	}
	
	public void testCreateProjectReportUserId()
	{
		Integer[] userID = new Integer[]{1};

		DateRange dr = new DateRange();
		UserCriteria uc = new UserCriteria();
		uc.setReportRange(dr);
		uc.setUserIds(userID);
		rc.setUserCriteria(uc);
		List<ProjectAssignmentAggregate> pags = new ArrayList<ProjectAssignmentAggregate>();
		
		pags.add(DummyDataGenerator.getProjectAssignmentAggregate(1, 1, 1));
		pags.add(DummyDataGenerator.getProjectAssignmentAggregate(2, 2, 2));
		pags.add(DummyDataGenerator.getProjectAssignmentAggregate(3, 3, 3));
		
		expect(reportAggregatedDAO.getCumulatedHoursPerAssignmentForUsers(isA(Integer[].class), isA(DateRange.class)))
					.andReturn(pags);
		replay(reportAggregatedDAO);
		reportService.createAggregateReportData(rc);
		verify(reportAggregatedDAO);
	}

	public void testCreateProjectReportNoUserId()
	{
		Integer[] userID = new Integer[]{1};

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
		Integer[] userID = new Integer[]{1};
		Integer[]	dptId = new Integer[]{2};
		List<User> users = new ArrayList<User>();
		User user = new User(1);
		users.add(user);

		DateRange dr = new DateRange();
		UserCriteria uc = new UserCriteria();
		uc.setReportRange(dr);
		uc.setDepartmentIds(dptId);
		uc.setOnlyActiveUsers(true);
		rc.setUserCriteria(uc);
		List<ProjectAssignmentAggregate> pags = new ArrayList<ProjectAssignmentAggregate>();
		
		pags.add(DummyDataGenerator.getProjectAssignmentAggregate(1, 1, 1));
		pags.add(DummyDataGenerator.getProjectAssignmentAggregate(2, 2, 2));
		pags.add(DummyDataGenerator.getProjectAssignmentAggregate(3, 3, 3));
		
		expect(reportAggregatedDAO.getCumulatedHoursPerAssignmentForUsers(isA(Integer[].class), isA(DateRange.class)))
		.andReturn(pags);
		
		expect(userDAO.findUsersForDepartments(null, dptId, true)).andReturn(users);
		
		replay(reportAggregatedDAO);
		replay(userDAO);
		reportService.createAggregateReportData(rc);
		verify(reportAggregatedDAO);
		verify(userDAO);
	}
	
	public void testCreateProjectReport()
	{
		Integer[] customerID = new Integer[]{1};

		List<Project> prjs = new ArrayList<Project>();
		Project prj = new Project(1);
		prjs.add(prj);		
		
		DateRange dr = new DateRange();
		UserCriteria uc = new UserCriteria();
		uc.setReportRange(dr);
		uc.setCustomerIds(customerID);
		rc.setUserCriteria(uc);
		List<ProjectAssignmentAggregate> pags = new ArrayList<ProjectAssignmentAggregate>();
		
		pags.add(DummyDataGenerator.getProjectAssignmentAggregate(1, 1, 1));
		pags.add(DummyDataGenerator.getProjectAssignmentAggregate(2, 2, 2));
		pags.add(DummyDataGenerator.getProjectAssignmentAggregate(3, 3, 3));
		
		expect(reportAggregatedDAO.getCumulatedHoursPerAssignmentForProjects(isA(Integer[].class), isA(DateRange.class)))
		.andReturn(pags);
		expect(projectDAO.findProjectForCustomers(customerID, true)).andReturn(prjs);
		
		replay(reportAggregatedDAO);
		replay(projectDAO);

		reportService.createAggregateReportData(rc);
		verify(reportAggregatedDAO);
		verify(projectDAO);
	}

}

