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
import net.rrm.ehour.activity.service.ActivityService;
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.*;
import net.rrm.ehour.persistence.project.dao.ProjectDao;
import net.rrm.ehour.persistence.report.dao.ReportAggregatedDao;
import net.rrm.ehour.persistence.user.dao.UserDao;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.criteria.UserSelectedCriteria;
import net.rrm.ehour.report.reports.ReportData;
import net.rrm.ehour.report.reports.element.ActivityAggregateReportElement;
import net.rrm.ehour.report.reports.element.ActivityAggregateReportElementMother;
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
    private ActivityService ActivityService;

    @Mock
    private TimesheetLockService timesheetLockService;

    @Before
    public void setUp() {
        aggregateReportService = new AggregateReportServiceImpl(ActivityService, projectDao, timesheetLockService, reportAggregatedDao);

        when(timesheetLockService.findLockedDatesInRange(any(Date.class), any(Date.class)))
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

        List<ActivityAggregateReportElement> pags = createAggregateReportElements();

        when(reportAggregatedDao.getCumulatedHoursPerActivityForUsers(eq(Lists.newArrayList(1)), any(DateRange.class))).thenReturn(pags);

        ReportData data = aggregateReportService.getAggregateReportData(rc);

        assertEquals(3, data.getReportElements().size());
    }


    @Test
    public void should_create_global_report() {
        DateRange dr = new DateRange();
        UserSelectedCriteria uc = new UserSelectedCriteria();
        uc.setReportRange(dr);
        ReportCriteria rc = new ReportCriteria(uc);

        List<ActivityAggregateReportElement> pags = createAggregateReportElements();

        when(reportAggregatedDao.getCumulatedHoursPerActivity(any(DateRange.class))).thenReturn(pags);

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
        List<ActivityAggregateReportElement> pags = createAggregateReportElements();
        when(reportAggregatedDao.getCumulatedHoursPerActivityForProjects(eq(Lists.newArrayList(1)), any(DateRange.class))).thenReturn(pags);
        when(projectDao.findProjectForCustomers(customers, true)).thenReturn(projects);

        ReportData data = aggregateReportService.getAggregateReportData(rc);

        assertEquals(3, data.getReportElements().size());
    }

    private List<ActivityAggregateReportElement> createAggregateReportElements() {
        List<ActivityAggregateReportElement> pags = new ArrayList<ActivityAggregateReportElement>();

        pags.add(ActivityAggregateReportElementMother.createActivityAggregate(1, 1, 1));
        pags.add(ActivityAggregateReportElementMother.createActivityAggregate(2, 2, 2));
        pags.add(ActivityAggregateReportElementMother.createActivityAggregate(3, 3, 3));

        return pags;
    }
}
