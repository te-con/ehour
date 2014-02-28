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
import net.rrm.ehour.persistence.report.dao.DetailedReportDao;
import net.rrm.ehour.persistence.report.dao.ReportAggregatedDao;
import net.rrm.ehour.persistence.user.dao.UserDao;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.criteria.UserSelectedCriteria;
import net.rrm.ehour.report.reports.ReportData;
import net.rrm.ehour.report.reports.element.FlatReportElement;
import net.rrm.ehour.report.reports.element.FlatReportElementBuilder;
import net.rrm.ehour.timesheet.service.TimesheetLockService;
import org.easymock.Capture;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.junit.Before;
import org.junit.Test;
import scala.collection.convert.WrapAsScala$;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * DetailedReportServiceImplTest
 */
@SuppressWarnings({"unchecked"})
public class DetailedReportServiceImplTest {
    private DetailedReportDao detailedReportDao;
    private DetailedReportServiceImpl detailedReportService;
    private ReportCriteria reportCriteria;
    private UserSelectedCriteria userSelectedCriteria;
    private UserDao userDao;
    private TimesheetLockService timesheetLockService;
    private ProjectDao projectDao;
    private ReportAggregatedDao reportAggregatedDao;

    @Before
    public void setUp() throws Exception {
        detailedReportDao = createMock(DetailedReportDao.class);
        projectDao = createMock(ProjectDao.class);
        userDao = createMock(UserDao.class);

        userSelectedCriteria = new UserSelectedCriteria();
        userSelectedCriteria.setShowZeroBookings(true);

        reportCriteria = new ReportCriteria(userSelectedCriteria);

        timesheetLockService = createMock(TimesheetLockService.class);

        reportAggregatedDao = createMock(ReportAggregatedDao.class);

        detailedReportService = new DetailedReportServiceImpl(userDao, projectDao, timesheetLockService, detailedReportDao, reportAggregatedDao);
    }

    private void provideNoLocks() {
        expect(timesheetLockService.findLockedDatesInRange(anyObject(Date.class), anyObject(Date.class)))
                .andReturn(WrapAsScala$.MODULE$.<Interval>asScalaBuffer(Lists.<Interval>newArrayList()));
        replay(timesheetLockService);
    }

    @Test
    public void should_get_all() {
        provideNoLocks();
        provideNoAssignmentsWithoutBookings();

        provideNoData();
        detailedReportService.getDetailedReportData(reportCriteria);
        verify(detailedReportDao);
    }

    private void provideNoAssignmentsWithoutBookings() {
        expect(reportAggregatedDao.getAssignmentsWithoutBookings(reportCriteria.getReportRange())).andReturn(Lists.<ProjectAssignment>newArrayList());
        replay(reportAggregatedDao);
    }

    private void provideNoData() {
        expect(detailedReportDao.getHoursPerDay(reportCriteria.getReportRange())).andReturn(Arrays.asList(createFlatReportElement()));
        replay(detailedReportDao);
    }

    @Test
    public void should_filter_on_user() {
        provideNoLocks();
        singleUserSelected();
        provideNoAssignmentsWithoutBookings();

        expect(detailedReportDao.getHoursPerDayForUsers(isA(List.class), isA(DateRange.class))).andReturn(Arrays.asList(createFlatReportElement()));
        replay(detailedReportDao);
        detailedReportService.getDetailedReportData(reportCriteria);
        verify(detailedReportDao);
    }

    private FlatReportElement createFlatReportElement() {
        ProjectAssignment assignment = ProjectAssignmentObjectMother.createProjectAssignment(1);
        return FlatReportElementBuilder.buildFlatReportElement(assignment);
    }

    @Test
    public void should_filter_on_project() {
        provideNoLocks();
        provideNoAssignmentsWithoutBookings();
        singleProjectSelected();

        expect(detailedReportDao.getHoursPerDayForProjects(isA(List.class), isA(DateRange.class))).andReturn(new ArrayList<FlatReportElement>());
        replay(detailedReportDao);
        detailedReportService.getDetailedReportData(reportCriteria);
        verify(detailedReportDao);
    }

    @Test
    public void should_filter_on_project_and_user() {
        provideNoLocks();
        singleProjectSelected();
        singleUserSelected();
        provideNoAssignmentsWithoutBookings();

        expect(detailedReportDao.getHoursPerDayForProjectsAndUsers(isA(List.class), isA(List.class), isA(DateRange.class)))
                .andReturn(new ArrayList<FlatReportElement>());
        replay(detailedReportDao);
        detailedReportService.getDetailedReportData(reportCriteria);
        verify(detailedReportDao);
    }

    private void singleUserSelected() {
        userSelectedCriteria.setUsers(Arrays.asList(new User(1)));
    }

    @Test
    public void should_filter_on_user_department() {
        provideNoLocks();
        provideNoAssignmentsWithoutBookings();
        singleProjectSelected();
        List<UserDepartment> departments = Arrays.asList(new UserDepartment(1));
        userSelectedCriteria.setDepartments(departments);

        expect(userDao.findUsersForDepartments(departments, true)).andReturn(Arrays.asList(new User(1)));

        expect(detailedReportDao.getHoursPerDayForProjectsAndUsers(isA(List.class), isA(List.class), isA(DateRange.class)))
                .andReturn(new ArrayList<FlatReportElement>());
        replay(detailedReportDao, userDao);
        detailedReportService.getDetailedReportData(reportCriteria);
        verify(detailedReportDao, userDao);
    }

    private void singleProjectSelected() {
        userSelectedCriteria.setProjects(Arrays.asList(new Project(1)));
    }

    @Test
    public void should_add_locked_days_to_detailed_report() {
        DateTime dateTime = new DateTime(reportCriteria.getReportRange().getDateStart());
        Interval interval = new Interval(dateTime, dateTime);
        provideNoAssignmentsWithoutBookings();

        expect(timesheetLockService.findLockedDatesInRange(anyObject(Date.class), anyObject(Date.class)))
                .andReturn(WrapAsScala$.MODULE$.<Interval>asScalaBuffer(Lists.newArrayList(interval)));
        replay(timesheetLockService);

        FlatReportElement reportElement = new FlatReportElement();
        reportElement.setDayDate(dateTime.toDate());

        expect(detailedReportDao.getHoursPerDay(isA(DateRange.class)))
                .andReturn(Arrays.asList(reportElement));

        replay(detailedReportDao, userDao);

        ReportData reportData = detailedReportService.getDetailedReportData(reportCriteria);

        FlatReportElement flat = (FlatReportElement) reportData.getReportElements().get(0);
        assertTrue(flat.getLockableDate().isLocked());

        verify(detailedReportDao, userDao, timesheetLockService);
    }

    @Test
    public void should_only_include_billable_without_customer_or_project_selection() {
        provideNoLocks();
        provideNoAssignmentsWithoutBookings();

        userSelectedCriteria.setOnlyBillableProjects(true);

        Project billableProject = ProjectObjectMother.createProject(1);
        billableProject.setBillable(true);

        Project notBillableProject = ProjectObjectMother.createProject(2);
        notBillableProject.setBillable(false);

        expect(projectDao.findAllActive()).andReturn(Arrays.asList(billableProject, notBillableProject));

        Capture<List<Integer>> projectIdListCapture = new Capture<List<Integer>>();
        expect(detailedReportDao.getHoursPerDayForProjects(capture(projectIdListCapture), isA(DateRange.class)))
                .andReturn(new ArrayList<FlatReportElement>());
        replay(detailedReportDao, projectDao);

        detailedReportService.getDetailedReportData(reportCriteria);

        assertEquals(1, projectIdListCapture.getValue().size());
        assertEquals(billableProject.getPK(), projectIdListCapture.getValue().get(0));

        verify(detailedReportDao);
    }

    @Test
    public void should_provide_assignments_without_bookings() {
        provideNoLocks();

        expect(reportAggregatedDao.getAssignmentsWithoutBookings(reportCriteria.getReportRange())).andReturn(Arrays.asList(ProjectAssignmentObjectMother.createProjectAssignment(1)));
        replay(reportAggregatedDao);

        userSelectedCriteria.setShowZeroBookings(true);

        Project billableProject = ProjectObjectMother.createProject(1);
        billableProject.setBillable(true);

        Project notBillableProject = ProjectObjectMother.createProject(2);
        notBillableProject.setBillable(false);

        expect(projectDao.findAllActive()).andReturn(Arrays.asList(billableProject, notBillableProject));

        expect(detailedReportDao.getHoursPerDay(reportCriteria.getReportRange())).andReturn(Arrays.asList(createFlatReportElement()));
        replay(detailedReportDao, projectDao);

        ReportData reportData = detailedReportService.getDetailedReportData(reportCriteria);
        FlatReportElement element = (FlatReportElement) reportData.getReportElements().get(0);
        assertTrue(element.isEmptyEntry());

        verify(detailedReportDao, reportAggregatedDao);
    }

}
