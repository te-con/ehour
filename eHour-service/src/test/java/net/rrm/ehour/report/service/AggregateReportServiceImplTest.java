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

import com.google.common.collect.Lists;
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.*;
import net.rrm.ehour.persistence.project.dao.ProjectDao;
import net.rrm.ehour.persistence.report.dao.ReportAggregatedDao;
import net.rrm.ehour.persistence.user.dao.UserDao;
import net.rrm.ehour.project.service.ProjectAssignmentService;
import net.rrm.ehour.project.service.ProjectService;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.criteria.UserSelectedCriteria;
import net.rrm.ehour.report.reports.ProjectManagerReport;
import net.rrm.ehour.report.reports.ReportData;
import net.rrm.ehour.report.reports.element.AssignmentAggregateReportElement;
import net.rrm.ehour.report.reports.element.AssignmentAggregateReportElementMother;
import net.rrm.ehour.timesheet.service.TimesheetLockService;
import org.joda.time.Interval;
import org.junit.Before;
import org.junit.Test;
import scala.collection.convert.WrapAsScala$;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;

@SuppressWarnings("unchecked")
public class AggregateReportServiceImplTest {
    private AggregateReportServiceImpl aggregateReportService;
    private UserDao userDao;
    private ProjectDao projectDao;
    private ReportAggregatedDao reportAggregatedDAO;
    private ProjectAssignmentService assignmentService;
    private ProjectService projectService;
    private TimesheetLockService timesheetLockService;

    @Before
    public void setUp() {
        reportAggregatedDAO = createMock(ReportAggregatedDao.class);
        assignmentService = createMock(ProjectAssignmentService.class);
        projectDao = createMock(ProjectDao.class);
        userDao = createMock(UserDao.class);
        projectService = createMock(ProjectService.class);
        timesheetLockService = createMock(TimesheetLockService.class);

        aggregateReportService = new AggregateReportServiceImpl(reportAggregatedDAO, assignmentService, userDao, projectDao, projectService, timesheetLockService);

        expect(timesheetLockService.findLockedDatesInRange(anyObject(Date.class), anyObject(Date.class)))
                .andReturn(WrapAsScala$.MODULE$.<Interval>asScalaBuffer(Lists.<Interval>newArrayList()));
        replay(timesheetLockService);
    }

    @Test
    public void should_create_report_for_single_user() {
        DateRange dr = new DateRange();
        UserSelectedCriteria uc = new UserSelectedCriteria();
        uc.setReportRange(dr);
        List<User> l = new ArrayList<User>();
        l.add(new User(1));
        uc.setUsers(l);
        List<AssignmentAggregateReportElement> pags = new ArrayList<AssignmentAggregateReportElement>();
        ReportCriteria rc = new ReportCriteria(uc);

        pags.add(AssignmentAggregateReportElementMother.createProjectAssignmentAggregate(1, 1, 1));
        pags.add(AssignmentAggregateReportElementMother.createProjectAssignmentAggregate(2, 2, 2));
        pags.add(AssignmentAggregateReportElementMother.createProjectAssignmentAggregate(3, 3, 3));

        expect(reportAggregatedDAO.getCumulatedHoursPerAssignmentForUsers(isA(List.class), isA(DateRange.class)))
                .andReturn(pags);
        replay(reportAggregatedDAO);
        aggregateReportService.getAggregateReportData(rc);
        verify(reportAggregatedDAO);
    }

    @Test
    public void should_create_global_report() {
        DateRange dr = new DateRange();
        UserSelectedCriteria uc = new UserSelectedCriteria();
        uc.setReportRange(dr);
        ReportCriteria rc = new ReportCriteria(uc);
        List<AssignmentAggregateReportElement> pags = new ArrayList<AssignmentAggregateReportElement>();

        pags.add(AssignmentAggregateReportElementMother.createProjectAssignmentAggregate(1, 1, 1));
        pags.add(AssignmentAggregateReportElementMother.createProjectAssignmentAggregate(2, 2, 2));
        pags.add(AssignmentAggregateReportElementMother.createProjectAssignmentAggregate(3, 3, 3));

        expect(reportAggregatedDAO.getCumulatedHoursPerAssignment(isA(DateRange.class))).andReturn(pags);
        replay(reportAggregatedDAO);
        aggregateReportService.getAggregateReportData(rc);
        verify(reportAggregatedDAO);
    }

    @Test
    public void should_create_report_for_department() {
        List<User> users = new ArrayList<User>();
        User user = new User(1);
        users.add(user);

        DateRange dr = new DateRange();
        UserSelectedCriteria uc = new UserSelectedCriteria();
        uc.setReportRange(dr);
        List<UserDepartment> l = new ArrayList<UserDepartment>();
        l.add(new UserDepartment(2));

        uc.setDepartments(l);
        uc.setOnlyActiveUsers(true);
        ReportCriteria rc = new ReportCriteria(uc);
        List<AssignmentAggregateReportElement> pags = new ArrayList<AssignmentAggregateReportElement>();

        pags.add(AssignmentAggregateReportElementMother.createProjectAssignmentAggregate(1, 1, 1));
        pags.add(AssignmentAggregateReportElementMother.createProjectAssignmentAggregate(2, 2, 2));
        pags.add(AssignmentAggregateReportElementMother.createProjectAssignmentAggregate(3, 3, 3));

        expect(reportAggregatedDAO.getCumulatedHoursPerAssignmentForUsers(isA(List.class), isA(DateRange.class)))
                .andReturn(pags);

        expect(userDao.findUsersForDepartments(l, true)).andReturn(users);

        replay(reportAggregatedDAO);
        replay(userDao);
        aggregateReportService.getAggregateReportData(rc);
        verify(reportAggregatedDAO);
        verify(userDao);
    }

    @Test
    public void should_create_report_for_specific_project() {
        Customer cust = new Customer(1);
        List<Customer> customers = new ArrayList<Customer>();
        customers.add(cust);

        List<Project> prjs = new ArrayList<Project>();
        Project prj = new Project(1);
        prjs.add(prj);

        DateRange dr = new DateRange();
        UserSelectedCriteria uc = new UserSelectedCriteria();
        uc.setReportRange(dr);
        uc.setCustomers(customers);
        ReportCriteria rc = new ReportCriteria(uc);
        List<AssignmentAggregateReportElement> pags = new ArrayList<AssignmentAggregateReportElement>();

        pags.add(AssignmentAggregateReportElementMother.createProjectAssignmentAggregate(1, 1, 1));
        pags.add(AssignmentAggregateReportElementMother.createProjectAssignmentAggregate(2, 2, 2));
        pags.add(AssignmentAggregateReportElementMother.createProjectAssignmentAggregate(3, 3, 3));

        expect(reportAggregatedDAO.getCumulatedHoursPerAssignmentForProjects(isA(List.class), isA(DateRange.class)))
                .andReturn(pags);
        expect(projectDao.findProjectForCustomers(customers, true)).andReturn(prjs);

        replay(reportAggregatedDAO);
        replay(projectDao);

        aggregateReportService.getAggregateReportData(rc);
        verify(reportAggregatedDAO);
        verify(projectDao);
    }

    @Test
    public void should_create_pm_detailed_report() {
        Project project = new Project(1);
        project.setProjectCode("PRJ");

        List<AssignmentAggregateReportElement> elms = new ArrayList<AssignmentAggregateReportElement>();

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                elms.add(AssignmentAggregateReportElementMother.createProjectAssignmentAggregate(j, i, i));
            }
        }

        expect(reportAggregatedDAO.getCumulatedHoursPerAssignmentForProjects(isA(List.class), isA(DateRange.class)))
                .andReturn(elms);

        DateRange dr = new DateRange(new Date(), new Date());
        expect(reportAggregatedDAO.getMinMaxDateTimesheetEntry(project)).andReturn(dr);

        List<ProjectAssignment> assignments = new ArrayList<ProjectAssignment>();

        assignments.add(ProjectAssignmentObjectMother.createProjectAssignment(2));

        expect(assignmentService.getProjectAssignments(project, dr)).andReturn(assignments);

        replay(reportAggregatedDAO);
        replay(assignmentService);

        ProjectManagerReport report = aggregateReportService.getProjectManagerDetailedReport(project);
        verify(reportAggregatedDAO);
        verify(assignmentService);

        assertEquals(new Integer(1), report.getProject().getPK());
        assertEquals(16, report.getAggregates().size());
    }

    @Test
    public void should_create_pm_report() {
        UserSelectedCriteria criteria = new UserSelectedCriteria();

        User projectManager = new User(2);
        criteria.setReportTypeToPM(projectManager);
        Project pmProject = ProjectObjectMother.createProject(1);
        expect(projectService.getProjectManagerProjects(projectManager)).andReturn(Lists.newArrayList(pmProject));

        User user = new User(1);

        ProjectAssignment assignment = ProjectAssignmentObjectMother.createProjectAssignment(1);
        assignment.getProject().setProjectId(1);
        user.addProjectAssignment(assignment);

        List<User> users = Lists.newArrayList(user);
        List<UserDepartment> departments = Lists.newArrayList(new UserDepartment(2));

        expect(userDao.findUsersForDepartments(departments, true)).andReturn(users);
        criteria.setDepartments(departments);

        DateRange dateRange = new DateRange();
        criteria.setReportRange(dateRange);

        criteria.setOnlyActiveUsers(true);

        ReportCriteria reportCriteria = new ReportCriteria(criteria);
        List<AssignmentAggregateReportElement> elements = Lists.newArrayList();

        AssignmentAggregateReportElement aggregate = AssignmentAggregateReportElementMother.createProjectAssignmentAggregate(1, 1, 1);
        aggregate.getProjectAssignment().setProject(pmProject);
        elements.add(aggregate);
        elements.add(AssignmentAggregateReportElementMother.createProjectAssignmentAggregate(2, 2, 2));
        elements.add(AssignmentAggregateReportElementMother.createProjectAssignmentAggregate(3, 3, 3));

        expect(reportAggregatedDAO.getCumulatedHoursPerAssignmentForUsers(isA(List.class), isA(DateRange.class))).andReturn(elements);

        replay(reportAggregatedDAO, userDao, projectService);

        ReportData reportData = aggregateReportService.getAggregateReportData(reportCriteria);
        assertEquals(1, reportData.getReportElements().size());

        verify(reportAggregatedDAO);
        verify(userDao);
        verify(projectService);
    }
}

