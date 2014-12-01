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
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import scala.collection.convert.WrapAsScala$;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
@RunWith(MockitoJUnitRunner.class)
public class AggregateReportServiceImplTest {
    private AggregateReportServiceImpl aggregateReportService;
    
    @Mock
    private UserDao userDao;

    @Mock
    private ProjectDao projectDao;
    
    @Mock
    private ReportAggregatedDao reportAggregatedDao;
    
    @Mock
    private ProjectAssignmentService assignmentService;

    @Mock
    private TimesheetLockService timesheetLockService;
    
    @Before
    public void setUp() {
        aggregateReportService = new AggregateReportServiceImpl(assignmentService, userDao, projectDao, timesheetLockService, reportAggregatedDao);

        when(timesheetLockService.findLockedDates(any(Interval.class)))
                .thenReturn(WrapAsScala$.MODULE$.asScalaBuffer(Lists.<Interval>newArrayList()));
    }

    @Test
    public void should_create_report_for_single_user() {
        DateRange dr = new DateRange();
        UserSelectedCriteria uc = new UserSelectedCriteria();
        uc.setReportRange(dr);

        List<User> users = Lists.newArrayList(UserObjectMother.createUser());
        uc.setUsers(users);
        ReportCriteria rc = new ReportCriteria(uc);

        List<AssignmentAggregateReportElement> pags = createAssignmentAggregateReportElements();

        when(reportAggregatedDao.getCumulatedHoursPerAssignmentForUsers(eq(users), any(DateRange.class))).thenReturn(pags);

        ReportData data = aggregateReportService.getAggregateReportData(rc);

        assertEquals(3, data.getReportElements().size());
    }


    @Test
    public void should_create_global_report() {
        DateRange dr = new DateRange();
        UserSelectedCriteria uc = new UserSelectedCriteria();
        uc.setReportRange(dr);
        ReportCriteria rc = new ReportCriteria(uc);
        List<AssignmentAggregateReportElement> pags = createAssignmentAggregateReportElements();

        when(reportAggregatedDao.getCumulatedHoursPerAssignment(any(DateRange.class))).thenReturn(pags);

        ReportData data = aggregateReportService.getAggregateReportData(rc);

        assertEquals(3, data.getReportElements().size());
    }

    @Test
    public void should_create_report_for_department() {
        List<User> users = Lists.newArrayList(UserObjectMother.createUser());

        DateRange dr = new DateRange();
        UserSelectedCriteria uc = new UserSelectedCriteria();
        uc.setReportRange(dr);
        List<UserDepartment> l = new ArrayList<UserDepartment>();
        l.add(new UserDepartment(2));

        uc.setDepartments(l);
        uc.setOnlyActiveUsers(true);
        ReportCriteria rc = new ReportCriteria(uc);
        List<AssignmentAggregateReportElement> pags = createAssignmentAggregateReportElements();

        when(reportAggregatedDao.getCumulatedHoursPerAssignmentForUsers(eq(users), any(DateRange.class))).thenReturn(pags);
        when(userDao.findUsers(true)).thenReturn(users);

        ReportData data = aggregateReportService.getAggregateReportData(rc);

        assertEquals(3, data.getReportElements().size());
    }

    @Test
    public void should_create_report_for_specific_project() {
        List<Customer> customers = Lists.newArrayList(CustomerObjectMother.createCustomer(1));
        List<Project> projects = Lists.newArrayList(ProjectObjectMother.createProject(1));

        DateRange dr = new DateRange();
        UserSelectedCriteria uc = new UserSelectedCriteria();
        uc.setReportRange(dr);
        uc.setCustomers(customers);

        ReportCriteria rc = new ReportCriteria(uc);
        List<AssignmentAggregateReportElement> pags = createAssignmentAggregateReportElements();

        when(reportAggregatedDao.getCumulatedHoursPerAssignmentForProjects(eq(projects), any(DateRange.class))).thenReturn(pags);
        when(projectDao.findProjectForCustomers(customers, true)).thenReturn(projects);

        ReportData data = aggregateReportService.getAggregateReportData(rc);

        assertEquals(3, data.getReportElements().size());
    }

    private List<AssignmentAggregateReportElement> createAssignmentAggregateReportElements() {
        List<AssignmentAggregateReportElement> pags = new ArrayList<AssignmentAggregateReportElement>();

        pags.add(AssignmentAggregateReportElementMother.createProjectAssignmentAggregate(1, 1, 1));
        pags.add(AssignmentAggregateReportElementMother.createProjectAssignmentAggregate(2, 2, 2));
        pags.add(AssignmentAggregateReportElementMother.createProjectAssignmentAggregate(3, 3, 3));
        return pags;
    }

    @Test
    public void should_create_pm_detailed_report() {
        Project project = new Project(1);
        project.setProjectCode("PRJ");
        DateRange dr = new DateRange(new Date(), new Date());
        when(reportAggregatedDao.getMinMaxDateTimesheetEntry(project)).thenReturn(dr);

        List<AssignmentAggregateReportElement> elms = new ArrayList<AssignmentAggregateReportElement>();

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                elms.add(AssignmentAggregateReportElementMother.createProjectAssignmentAggregate(j, i, i));
            }
        }

        when(reportAggregatedDao.getCumulatedHoursPerAssignmentForProjects(any(List.class), any(DateRange.class)))
                .thenReturn(elms);


        List<ProjectAssignment> assignments = new ArrayList<ProjectAssignment>();
        assignments.add(ProjectAssignmentObjectMother.createProjectAssignment(2));

        when(assignmentService.getProjectAssignments(project, dr)).thenReturn(assignments);

        ProjectManagerReport report = aggregateReportService.getProjectManagerDetailedReport(project);

        assertEquals(new Integer(1), report.getProject().getPK());
        assertEquals(16, report.getAggregates().size());
    }

    @Test
    public void should_create_pm_report() {
        UserSelectedCriteria criteria = new UserSelectedCriteria();

        User projectManager = new User(2);
        criteria.setReportTypeToPM(projectManager);
        Project pmProject = ProjectObjectMother.createProject(1);
        when(projectDao.findActiveProjectsWhereUserIsPM(projectManager)).thenReturn(Lists.newArrayList(pmProject));

        UserDepartment userDepartment = new UserDepartment(2);
        User user = new User(1);
        user.setUserDepartment(userDepartment);

        ProjectAssignment assignment = ProjectAssignmentObjectMother.createProjectAssignment(1);
        assignment.getProject().setProjectId(1);
        user.addProjectAssignment(assignment);

        List<User> users = Lists.newArrayList(user);
        List<UserDepartment> departments = Lists.newArrayList(userDepartment);

        when(userDao.findUsers(true)).thenReturn(users);
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

        when(reportAggregatedDao.getCumulatedHoursPerAssignmentForUsers(any(List.class), any(DateRange.class))).thenReturn(elements);

        ReportData reportData = aggregateReportService.getAggregateReportData(reportCriteria);

        assertEquals(1, reportData.getReportElements().size());
    }
}

