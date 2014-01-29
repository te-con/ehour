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
import net.rrm.ehour.persistence.report.dao.DetailedReportDao;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.criteria.UserSelectedCriteria;
import net.rrm.ehour.report.reports.element.FlatReportElement;
import net.rrm.ehour.timesheet.service.TimesheetLockService;
import org.joda.time.Interval;
import org.junit.Before;
import org.junit.Test;
import scala.collection.convert.WrapAsScala$;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.easymock.EasyMock.*;

/**
 * DetailedReportServiceImplTest
 */
@SuppressWarnings({"unchecked"})
public class DetailedReportServiceImplTest {
    private DetailedReportDao detailedReportDAO;
    private DetailedReportServiceImpl detailedReportService;
    private ReportCriteria reportCriteria;
    private UserSelectedCriteria userSelectedCriteria;

    @Before
    public void setUp() throws Exception {
        detailedReportService = new DetailedReportServiceImpl();

        detailedReportDAO = createMock(DetailedReportDao.class);
        detailedReportService.setDetailedReportDAO(detailedReportDAO);

        userSelectedCriteria = new UserSelectedCriteria();
        reportCriteria = new ReportCriteria(userSelectedCriteria);

        TimesheetLockService timesheetLockService = createMock(TimesheetLockService.class);
        detailedReportService.setLockService(timesheetLockService);

        expect(timesheetLockService.findLockedDatesInRange(anyObject(Date.class), anyObject(Date.class)))
                .andReturn(WrapAsScala$.MODULE$.<Interval>asScalaBuffer(Lists.<Interval>newArrayList()));
        replay(timesheetLockService);
    }

    @Test
    public void testGetDetailedReportAll() {
        expect(detailedReportDAO.getHoursPerDay(reportCriteria.getReportRange())).andReturn(new ArrayList<FlatReportElement>());
        replay(detailedReportDAO);
        detailedReportService.getDetailedReportData(reportCriteria);
        verify(detailedReportDAO);
    }

    @Test
    public void testGetDetailedReportDataUsersOnly() {
        List<User> l = new ArrayList<User>();
        l.add(new User(1));
        userSelectedCriteria.setUsers(l);

        expect(detailedReportDAO.getHoursPerDayForUsers(isA(List.class), isA(DateRange.class)))
                .andReturn(new ArrayList<FlatReportElement>());
        replay(detailedReportDAO);
        detailedReportService.getDetailedReportData(reportCriteria);
        verify(detailedReportDAO);
    }

    @Test
    public void testGetDetailedReportDataProjectsOnly() {
        List<Project> l = new ArrayList<Project>();
        l.add(new Project(1));
        userSelectedCriteria.setProjects(l);

        expect(detailedReportDAO.getHoursPerDayForProjects(isA(List.class), isA(DateRange.class)))
                .andReturn(new ArrayList<FlatReportElement>());
        replay(detailedReportDAO);
        detailedReportService.getDetailedReportData(reportCriteria);
        verify(detailedReportDAO);
    }

    @Test
    public void testGetDetailedReportDataProjectsAndUsers() {
        List<Project> l = new ArrayList<Project>();
        l.add(new Project(1));
        userSelectedCriteria.setProjects(l);

        List<User> u = new ArrayList<User>();
        u.add(new User(1));
        userSelectedCriteria.setUsers(u);

        expect(detailedReportDAO.getHoursPerDayForProjectsAndUsers(isA(List.class), isA(List.class), isA(DateRange.class)))
                .andReturn(new ArrayList<FlatReportElement>());
        replay(detailedReportDAO);
        detailedReportService.getDetailedReportData(reportCriteria);
        verify(detailedReportDAO);
    }
}
