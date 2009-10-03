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
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;
import net.rrm.ehour.DummyDataGenerator;
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.domain.MailLogAssignment;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.domain.UserDepartment;
import net.rrm.ehour.mail.service.MailService;
import net.rrm.ehour.project.dao.ProjectDAO;
import net.rrm.ehour.project.service.ProjectAssignmentService;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.criteria.UserCriteria;
import net.rrm.ehour.report.dao.ReportAggregatedDAO;
import net.rrm.ehour.report.reports.ProjectManagerReport;
import net.rrm.ehour.report.reports.element.AssignmentAggregateReportElement;
import net.rrm.ehour.user.dao.UserDAO;

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
private	ProjectAssignmentService	assignmentService;
	private MailService mailService;
	
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

		assignmentService = createMock(ProjectAssignmentService.class);
		((AggregateReportServiceImpl)aggregateReportService).setProjectAssignmentService(assignmentService);
		
		mailService = createMock(MailService.class);
		((AggregateReportServiceImpl)aggregateReportService).setMailService(mailService);
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
		List<AssignmentAggregateReportElement> pags = new ArrayList<AssignmentAggregateReportElement>();
		ReportCriteria rc = new ReportCriteria(uc);
		
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
		ReportCriteria rc = new ReportCriteria(uc);
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
		ReportCriteria rc = new ReportCriteria(uc);
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
		ReportCriteria rc = new ReportCriteria(uc);
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
	
	public void testGetProjectManagerReport()
	{
		Project prj = new Project(1);
		prj.setProjectCode("PRJ");
		DateRange dr = new DateRange(new Date(), new Date());
	
		expect(projectDAO.findById(1))
			.andReturn(prj);
		
		List<AssignmentAggregateReportElement> elms = new ArrayList<AssignmentAggregateReportElement>();
		
		for (int i = 0; i < 4; i++)
		{
			for (int j = 0; j < 4; j++)
			{
				elms.add(DummyDataGenerator.getProjectAssignmentAggregate(j, i, i));	
			}
		}
		
		expect(reportAggregatedDAO.getCumulatedHoursPerAssignmentForProjects(isA(List.class), isA(DateRange.class)))
			.andReturn(elms);
		
		List<ProjectAssignment> assignments = new ArrayList<ProjectAssignment>();
		
		assignments.add(DummyDataGenerator.getProjectAssignment(2));
		
		expect(assignmentService.getProjectAssignments(prj, dr))
			.andReturn(assignments);
		
		expect(mailService.getSentMailForAssignment(isA(Integer[].class)))
			.andReturn(new ArrayList<MailLogAssignment>());
		
		replay(projectDAO);
		replay(reportAggregatedDAO);
		replay(assignmentService);
		replay(mailService);
		
		ProjectManagerReport report = aggregateReportService.getProjectManagerDetailedReport(dr, 1);
		verify(projectDAO);
		verify(reportAggregatedDAO);
		verify(assignmentService);
		verify(mailService);
		
		assertEquals(new Integer(1),  report.getProject().getPK());
		assertEquals(16, report.getAggregates().size());
				
	}
}

