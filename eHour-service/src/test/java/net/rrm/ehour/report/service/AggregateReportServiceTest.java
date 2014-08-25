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
import net.rrm.ehour.activity.service.ActivityService;
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.Activity;
import net.rrm.ehour.domain.ActivityMother;
import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.domain.MailLogAssignment;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.domain.UserDepartment;
import net.rrm.ehour.mail.service.MailService;
import net.rrm.ehour.persistence.project.dao.ProjectDao;
import net.rrm.ehour.persistence.report.dao.ReportAggregatedDao;
import net.rrm.ehour.persistence.user.dao.UserDao;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.criteria.UserCriteria;
import net.rrm.ehour.report.reports.ProjectManagerReport;
import net.rrm.ehour.report.reports.element.ActivityAggregateReportElement;
import net.rrm.ehour.report.reports.element.ActivityAggregateReportElementMother;

/**
 *  
 **/

@SuppressWarnings("unchecked")
public class AggregateReportServiceTest extends TestCase {
	private AggregateReportService aggregateReportService;
	private UserDao userDAO;
	private ProjectDao projectDAO;
	private ReportAggregatedDao reportAggregatedDAO;
	private ActivityService activityService;
	private MailService mailService;

	/**
	 * 
	 */
	protected void setUp() {
		aggregateReportService = new AggregateReportServiceImpl();

		reportAggregatedDAO = createMock(ReportAggregatedDao.class);
		((AggregateReportServiceImpl) aggregateReportService).setReportAggregatedDAO(reportAggregatedDAO);

		userDAO = createMock(UserDao.class);
		((AggregateReportServiceImpl) aggregateReportService).setUserDAO(userDAO);

		projectDAO = createMock(ProjectDao.class);
		((AggregateReportServiceImpl) aggregateReportService).setProjectDAO(projectDAO);

		activityService = createMock(ActivityService.class);
		((AggregateReportServiceImpl) aggregateReportService).setActivityService(activityService);

		mailService = createMock(MailService.class);
		((AggregateReportServiceImpl) aggregateReportService).setMailService(mailService);
	}

	// /**
	// *
	// *
	// */
	//	
	// public void testGetHoursPerAssignmentOnMonth()
	// {
	// List<AssignmentAggregateReportElement> results = new
	// ArrayList<AssignmentAggregateReportElement>();
	// Calendar cal = new GregorianCalendar();
	//		
	// results.add(new AssignmentAggregateReportElement());
	//		
	// List<User> l = new ArrayList<User>();
	// l.add(new User(1));
	// reportAggregatedDAO.getCumulatedHoursPerAssignmentForUsers(l,
	// DateUtil.calendarToMonthRange(cal));
	// expectLastCall().andReturn(results);
	//		
	// replay();
	//		
	// reportService.getHoursPerAssignmentInMonth(1, cal);
	//		
	// verify();
	// }

	public void testCreateProjectReportUserId() {
		DateRange dr = new DateRange();
		UserCriteria uc = new UserCriteria();
		uc.setReportRange(dr);
		List<User> l = new ArrayList<User>();
		l.add(new User(1));
		uc.setUsers(l);
		List<ActivityAggregateReportElement> activityAggregateReportElements = new ArrayList<ActivityAggregateReportElement>();
		ReportCriteria rc = new ReportCriteria(uc);

		activityAggregateReportElements.add(ActivityAggregateReportElementMother.createActivityAggregate(1, 1, 1));
		activityAggregateReportElements.add(ActivityAggregateReportElementMother.createActivityAggregate(2, 2, 2));
		activityAggregateReportElements.add(ActivityAggregateReportElementMother.createActivityAggregate(3, 3, 3));

		expect(reportAggregatedDAO.getCumulatedHoursPerActivityForUsers(isA(List.class), isA(DateRange.class))).andReturn(
				activityAggregateReportElements);
		replay(reportAggregatedDAO);
		aggregateReportService.getAggregateReportData(rc);
		verify(reportAggregatedDAO);
	}

	public void testCreateProjectReportNoUserId() {
		DateRange dr = new DateRange();
		UserCriteria uc = new UserCriteria();
		uc.setReportRange(dr);
		List<ActivityAggregateReportElement> activityAggregateReportElements = new ArrayList<ActivityAggregateReportElement>();
		ReportCriteria rc = new ReportCriteria(uc);

		activityAggregateReportElements.add(ActivityAggregateReportElementMother.createActivityAggregate(1, 1, 1));
		activityAggregateReportElements.add(ActivityAggregateReportElementMother.createActivityAggregate(2, 2, 2));
		activityAggregateReportElements.add(ActivityAggregateReportElementMother.createActivityAggregate(3, 3, 3));

		expect(reportAggregatedDAO.getCumulatedHoursPerActivity(isA(DateRange.class))).andReturn(activityAggregateReportElements);
		replay(reportAggregatedDAO);
		aggregateReportService.getAggregateReportData(rc);
		verify(reportAggregatedDAO);
	}

	public void testCreateProjectReportNoUserIdDptId() {
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
		List<ActivityAggregateReportElement> activityAggregateReportElements = new ArrayList<ActivityAggregateReportElement>();
		ReportCriteria rc = new ReportCriteria(uc);

		activityAggregateReportElements.add(ActivityAggregateReportElementMother.createActivityAggregate(1, 1, 1));
		activityAggregateReportElements.add(ActivityAggregateReportElementMother.createActivityAggregate(2, 2, 2));
		activityAggregateReportElements.add(ActivityAggregateReportElementMother.createActivityAggregate(3, 3, 3));

		expect(reportAggregatedDAO.getCumulatedHoursPerActivityForUsers(isA(List.class), isA(DateRange.class))).andReturn(
				activityAggregateReportElements);

		expect(userDAO.findUsersForDepartments(null, l, true)).andReturn(users);

		replay(reportAggregatedDAO);
		replay(userDAO);
		aggregateReportService.getAggregateReportData(rc);
		verify(reportAggregatedDAO);
		verify(userDAO);
	}

	public void testCreateProjectReport() {
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
		List<ActivityAggregateReportElement> activityAggregateReportElements = new ArrayList<ActivityAggregateReportElement>();
		ReportCriteria rc = new ReportCriteria(uc);

		activityAggregateReportElements.add(ActivityAggregateReportElementMother.createActivityAggregate(1, 1, 1));
		activityAggregateReportElements.add(ActivityAggregateReportElementMother.createActivityAggregate(2, 2, 2));
		activityAggregateReportElements.add(ActivityAggregateReportElementMother.createActivityAggregate(3, 3, 3));

		expect(reportAggregatedDAO.getCumulatedHoursPerActivityForProjects(isA(List.class), isA(DateRange.class))).andReturn(
				activityAggregateReportElements);
		expect(projectDAO.findProjectForCustomers(customers, true)).andReturn(prjs);

		replay(reportAggregatedDAO);
		replay(projectDAO);

		aggregateReportService.getAggregateReportData(rc);
		verify(reportAggregatedDAO);
		verify(projectDAO);
	}

	public void testGetProjectManagerReport() {
		Project prj = new Project(1);
		prj.setProjectCode("PRJ");
		DateRange dr = new DateRange(new Date(), new Date());

		expect(projectDAO.findById(1)).andReturn(prj);

		List<ActivityAggregateReportElement> elms = new ArrayList<ActivityAggregateReportElement>();

		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				elms.add(ActivityAggregateReportElementMother.createActivityAggregate(j, i, i));
			}
		}
		
		System.out.println("No of Aggregates : "+elms.size());

		expect(reportAggregatedDAO.getCumulatedHoursPerActivityForProjects(isA(List.class), isA(DateRange.class))).andReturn(elms);

		List<Activity> activities = new ArrayList<Activity>();
		activities.add(ActivityMother.createActivity(2));

		expect(activityService.getActivities(prj, dr)).andReturn(activities);

		expect(mailService.getSentMailForAssignment(isA(Integer[].class))).andReturn(new ArrayList<MailLogAssignment>());

		replay(projectDAO);
		replay(reportAggregatedDAO);
		replay(activityService);
		replay(mailService);

		ProjectManagerReport report = aggregateReportService.getProjectManagerDetailedReport(dr, 1);
		verify(projectDAO);
		verify(reportAggregatedDAO);
		verify(activityService);
		verify(mailService);

		assertEquals(new Integer(1), report.getProject().getPK());
		assertEquals(16, report.getAggregates().size());

	}
}
