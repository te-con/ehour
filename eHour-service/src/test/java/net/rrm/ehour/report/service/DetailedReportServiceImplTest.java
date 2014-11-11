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
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import scala.collection.convert.WrapAsScala$;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

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
        detailedReportDao = mock(DetailedReportDao.class);
        projectDao = mock(ProjectDao.class);
        userDao = mock(UserDao.class);

        userSelectedCriteria = new UserSelectedCriteria();
        userSelectedCriteria.setShowZeroBookings(true);

        reportCriteria = new ReportCriteria(userSelectedCriteria);

        timesheetLockService = mock(TimesheetLockService.class);

        reportAggregatedDao = mock(ReportAggregatedDao.class);

        detailedReportService = new DetailedReportServiceImpl(userDao, projectDao, timesheetLockService, detailedReportDao, reportAggregatedDao);
    }

    private void provideNoLocks() {
        when(timesheetLockService.findLockedDatesInRange(any(Date.class), any(Date.class)))
                .thenReturn(WrapAsScala$.MODULE$.<Interval>asScalaBuffer(Lists.<Interval>newArrayList()));
    }

    @Test
    public void should_get_all() {
        provideNoLocks();
        provideNoAssignmentsWithoutBookings();

        provideNoData();
        detailedReportService.getDetailedReportData(reportCriteria);
    }

    private void provideNoAssignmentsWithoutBookings() {
        when(reportAggregatedDao.getAssignmentsWithoutBookings(reportCriteria.getReportRange())).thenReturn(Lists.<ProjectAssignment>newArrayList());
    }

    private void provideNoData() {
        when(detailedReportDao.getHoursPerDay(reportCriteria.getReportRange())).thenReturn(Arrays.asList(createFlatReportElement()));
    }

    @Test
    public void should_filter_on_user() {
        provideNoLocks();
        singleUserSelected();
        provideNoAssignmentsWithoutBookings();

        when(detailedReportDao.getHoursPerDayForUsers(any(List.class), any(DateRange.class))).thenReturn(Arrays.asList(createFlatReportElement()));
        detailedReportService.getDetailedReportData(reportCriteria);
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

        when(detailedReportDao.getHoursPerDayForProjects(any(List.class), any(DateRange.class))).thenReturn(new ArrayList<FlatReportElement>());
        detailedReportService.getDetailedReportData(reportCriteria);
        verify(detailedReportDao).getHoursPerDayForProjects(any(List.class), any(DateRange.class));
    }

    @Test
    public void should_filter_on_project_and_user() {
        provideNoLocks();
        singleProjectSelected();
        singleUserSelected();
        provideNoAssignmentsWithoutBookings();

        when(detailedReportDao.getHoursPerDayForProjectsAndUsers(any(List.class), any(List.class), any(DateRange.class)))
                .thenReturn(new ArrayList<FlatReportElement>());
        detailedReportService.getDetailedReportData(reportCriteria);
        verify(detailedReportDao).getHoursPerDayForProjectsAndUsers(any(List.class), any(List.class), any(DateRange.class));
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

        when(userDao.findUsers(true)).thenReturn(Arrays.asList(new User(1)));

        when(detailedReportDao.getHoursPerDayForProjectsAndUsers(any(List.class), any(List.class), any(DateRange.class)))
                .thenReturn(new ArrayList<FlatReportElement>());

        detailedReportService.getDetailedReportData(reportCriteria);

        verify(userDao).findUsers(true);
        verify(detailedReportDao).getHoursPerDayForProjectsAndUsers(any(List.class), any(List.class), any(DateRange.class));
    }

    private void singleProjectSelected() {
        userSelectedCriteria.setProjects(Arrays.asList(new Project(1)));
    }

    @Test
    public void should_add_locked_days_to_detailed_report() {
        DateTime dateTime = new DateTime(reportCriteria.getReportRange().getDateStart());
        Interval interval = new Interval(dateTime, dateTime);
        provideNoAssignmentsWithoutBookings();

        when(timesheetLockService.findLockedDatesInRange(any(Date.class), any(Date.class)))
                .thenReturn(WrapAsScala$.MODULE$.<Interval>asScalaBuffer(Lists.newArrayList(interval)));

        FlatReportElement reportElement = new FlatReportElement();
        reportElement.setDayDate(dateTime.toDate());

        when(detailedReportDao.getHoursPerDay(any(DateRange.class)))
                .thenReturn(Arrays.asList(reportElement));

        ReportData reportData = detailedReportService.getDetailedReportData(reportCriteria);

        FlatReportElement flat = (FlatReportElement) reportData.getReportElements().get(0);
        assertTrue(flat.getLockableDate().isLocked());

        verify(detailedReportDao).getHoursPerDay(any(DateRange.class));
        verify(timesheetLockService).findLockedDatesInRange(any(Date.class), any(Date.class));
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

        when(projectDao.findAllActive()).thenReturn(Arrays.asList(billableProject, notBillableProject));

        ArgumentCaptor<List> projectIdListCapture = ArgumentCaptor.forClass(List.class);
        when(detailedReportDao.getHoursPerDayForProjects(projectIdListCapture.capture(), any(DateRange.class)))
                .thenReturn(new ArrayList<FlatReportElement>());

        detailedReportService.getDetailedReportData(reportCriteria);

        assertEquals(1, projectIdListCapture.getValue().size());
        assertEquals(billableProject.getPK(), projectIdListCapture.getValue().get(0));
    }

    @Test
    public void should_provide_assignments_without_bookings() {
        provideNoLocks();

        when(reportAggregatedDao.getAssignmentsWithoutBookings(reportCriteria.getReportRange())).thenReturn(Arrays.asList(ProjectAssignmentObjectMother.createProjectAssignment(1)));

        userSelectedCriteria.setShowZeroBookings(true);

        Project billableProject = ProjectObjectMother.createProject(1);
        billableProject.setBillable(true);

        Project notBillableProject = ProjectObjectMother.createProject(2);
        notBillableProject.setBillable(false);

        when(projectDao.findAllActive()).thenReturn(Arrays.asList(billableProject, notBillableProject));

        when(detailedReportDao.getHoursPerDay(reportCriteria.getReportRange())).thenReturn(Arrays.asList(createFlatReportElement()));

        ReportData reportData = detailedReportService.getDetailedReportData(reportCriteria);

        FlatReportElement element = (FlatReportElement) reportData.getReportElements().get(0);

        assertTrue(element.isEmptyEntry());

        verify(detailedReportDao).getHoursPerDay(reportCriteria.getReportRange());
        verify(reportAggregatedDao).getAssignmentsWithoutBookings(reportCriteria.getReportRange());
    }
}
