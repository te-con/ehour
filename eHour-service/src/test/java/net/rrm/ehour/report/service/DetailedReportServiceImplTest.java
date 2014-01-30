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
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.domain.UserDepartment;
import net.rrm.ehour.persistence.project.dao.ProjectDao;
import net.rrm.ehour.persistence.report.dao.DetailedReportDao;
import net.rrm.ehour.persistence.user.dao.UserDao;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.criteria.UserSelectedCriteria;
import net.rrm.ehour.report.reports.ReportData;
import net.rrm.ehour.report.reports.element.FlatReportElement;
import net.rrm.ehour.timesheet.service.TimesheetLockService;
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

    @Before
    public void setUp() throws Exception {
        detailedReportDao = createMock(DetailedReportDao.class);
        ProjectDao projectDao = createMock(ProjectDao.class);
        userDao = createMock(UserDao.class);

        userSelectedCriteria = new UserSelectedCriteria();
        reportCriteria = new ReportCriteria(userSelectedCriteria);

        timesheetLockService = createMock(TimesheetLockService.class);

        detailedReportService = new DetailedReportServiceImpl(detailedReportDao, userDao, projectDao, timesheetLockService);
    }

    private void provideNoLocks() {
        expect(timesheetLockService.findLockedDatesInRange(anyObject(Date.class), anyObject(Date.class)))
                .andReturn(WrapAsScala$.MODULE$.<Interval>asScalaBuffer(Lists.<Interval>newArrayList()));
        replay(timesheetLockService);
    }

    @Test
    public void testGetDetailedReportAll() {
        provideNoLocks();

        provideNoData();
        detailedReportService.getDetailedReportData(reportCriteria);
        verify(detailedReportDao);
    }

    private void provideNoData() {
        expect(detailedReportDao.getHoursPerDay(reportCriteria.getReportRange())).andReturn(new ArrayList<FlatReportElement>());
        replay(detailedReportDao);
    }

    @Test
    public void testGetDetailedReportDataUsersOnly() {
        provideNoLocks();
        singleUserSelected();

        expect(detailedReportDao.getHoursPerDayForUsers(isA(List.class), isA(DateRange.class))).andReturn(new ArrayList<FlatReportElement>());
        replay(detailedReportDao);
        detailedReportService.getDetailedReportData(reportCriteria);
        verify(detailedReportDao);
    }

    @Test
    public void testGetDetailedReportDataProjectsOnly() {
        provideNoLocks();

        singleProjectSelected();

        expect(detailedReportDao.getHoursPerDayForProjects(isA(List.class), isA(DateRange.class))).andReturn(new ArrayList<FlatReportElement>());
        replay(detailedReportDao);
        detailedReportService.getDetailedReportData(reportCriteria);
        verify(detailedReportDao);
    }

    @Test
    public void testGetDetailedReportDataProjectsAndUsers() {
        provideNoLocks();
        singleProjectSelected();
        singleUserSelected();

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
    public void testGetDetailedReportDataProjectsAndUsersOnDepartments() {
        provideNoLocks();
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
    public void testGetDetailedReportDataWithLockedDays() {
        DateTime dateTime = new DateTime(reportCriteria.getReportRange().getDateStart());
        Interval interval = new Interval(dateTime, dateTime);

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

}
