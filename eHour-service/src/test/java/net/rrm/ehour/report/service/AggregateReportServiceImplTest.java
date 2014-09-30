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
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.criteria.UserSelectedCriteria;
import net.rrm.ehour.report.reports.ProjectManagerReport;
import net.rrm.ehour.report.reports.ReportData;
import net.rrm.ehour.report.reports.element.ActivityAggregateReportElement;
import net.rrm.ehour.report.reports.element.ActivityAggregateReportElementMother;
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
    private ProjectDao projectDao;
    private ReportAggregatedDao reportAggregatedDao;
    private ActivityService ActivityService;

    @Before
    public void setUp() {
        reportAggregatedDao = createMock(ReportAggregatedDao.class);
        ActivityService = createMock(ActivityService.class);
        projectDao = createMock(ProjectDao.class);
        TimesheetLockService timesheetLockService = createMock(TimesheetLockService.class);

        aggregateReportService = new AggregateReportServiceImpl(ActivityService, projectDao, timesheetLockService, reportAggregatedDao);

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
        List<ActivityAggregateReportElement> pags = new ArrayList<ActivityAggregateReportElement>();
        ReportCriteria rc = new ReportCriteria(uc);

        pags.add(ActivityAggregateReportElementMother.createActivityAggregate(1, 1, 1));
        pags.add(ActivityAggregateReportElementMother.createActivityAggregate(2, 2, 2));
        pags.add(ActivityAggregateReportElementMother.createActivityAggregate(3, 3, 3));

        expect(reportAggregatedDao.getCumulatedHoursPerActivityForUsers(isA(List.class), isA(DateRange.class)))
                .andReturn(pags);
        replay(reportAggregatedDao);
        aggregateReportService.getAggregateReportData(rc);
        verify(reportAggregatedDao);
    }

    @Test
    public void should_create_global_report() {
        DateRange dr = new DateRange();
        UserSelectedCriteria uc = new UserSelectedCriteria();
        uc.setReportRange(dr);
        ReportCriteria rc = new ReportCriteria(uc);
        List<ActivityAggregateReportElement> pags = new ArrayList<ActivityAggregateReportElement>();

        pags.add(ActivityAggregateReportElementMother.createActivityAggregate(1, 1, 1));
        pags.add(ActivityAggregateReportElementMother.createActivityAggregate(2, 2, 2));
        pags.add(ActivityAggregateReportElementMother.createActivityAggregate(3, 3, 3));

        expect(reportAggregatedDao.getCumulatedHoursPerActivity(isA(DateRange.class))).andReturn(pags);
        replay(reportAggregatedDao);
        aggregateReportService.getAggregateReportData(rc);
        verify(reportAggregatedDao);
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
        List<ActivityAggregateReportElement> pags = new ArrayList<ActivityAggregateReportElement>();

        pags.add(ActivityAggregateReportElementMother.createActivityAggregate(1, 1, 1));
        pags.add(ActivityAggregateReportElementMother.createActivityAggregate(2, 2, 2));
        pags.add(ActivityAggregateReportElementMother.createActivityAggregate(3, 3, 3));

        expect(reportAggregatedDao.getCumulatedHoursPerActivityForProjects(isA(List.class), isA(DateRange.class)))
                .andReturn(pags);
        expect(projectDao.findProjectForCustomers(customers, true)).andReturn(prjs);

        replay(reportAggregatedDao);
        replay(projectDao);

        aggregateReportService.getAggregateReportData(rc);
        verify(reportAggregatedDao);
        verify(projectDao);
    }

    @Test
    public void should_create_pm_detailed_report() {
        Project project = new Project(1);
        project.setProjectCode("PRJ");

        List<ActivityAggregateReportElement> elms = new ArrayList<ActivityAggregateReportElement>();

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                elms.add(ActivityAggregateReportElementMother.createActivityAggregate(j, i, i));
            }
        }

        expect(reportAggregatedDao.getCumulatedHoursPerActivityForProjects(isA(List.class), isA(DateRange.class)))
                .andReturn(elms);

        DateRange dr = new DateRange(new Date(), new Date());
        expect(reportAggregatedDao.getMinMaxDateTimesheetEntry(project)).andReturn(dr);

        List<Activity> Activitys = new ArrayList<Activity>();

        Activitys.add(ActivityMother.createActivity(2));

        expect(ActivityService.getActivities(project, dr)).andReturn(Activitys);

        replay(reportAggregatedDao);
        replay(ActivityService);

        ProjectManagerReport report = aggregateReportService.getProjectManagerDetailedReport(project);
        verify(reportAggregatedDao);
        verify(ActivityService);

        assertEquals(new Integer(1), report.getProject().getPK());
        assertEquals(16, report.getAggregates().size());
    }

    @Test
    public void should_create_pm_report() {
        UserSelectedCriteria criteria = new UserSelectedCriteria();

        User projectManager = new User(2);
        criteria.setReportTypeToPM(projectManager);
        Project pmProject = ProjectObjectMother.createProject(1);
        expect(projectDao.findActiveProjectsWhereUserIsPM(projectManager)).andReturn(Lists.newArrayList(pmProject));

        User user = new User(1);

        Activity activity = ActivityMother.createActivity(1);
        activity.getProject().setProjectId(1);
        activity.setAssignedUser(user);

        DateRange dateRange = new DateRange();
        criteria.setReportRange(dateRange);

        criteria.setOnlyActiveUsers(true);

        ReportCriteria reportCriteria = new ReportCriteria(criteria);
        List<ActivityAggregateReportElement> elements = Lists.newArrayList();

        ActivityAggregateReportElement aggregate = ActivityAggregateReportElementMother.createActivityAggregate(1, 1, 1);
        aggregate.getActivity().setProject(pmProject);
        elements.add(aggregate);
        elements.add(ActivityAggregateReportElementMother.createActivityAggregate(2, 2, 2));
        elements.add(ActivityAggregateReportElementMother.createActivityAggregate(3, 3, 3));

        expect(reportAggregatedDao.getCumulatedHoursPerActivityForUsers(isA(List.class), isA(DateRange.class))).andReturn(elements);

        replay(reportAggregatedDao, projectDao);

        ReportData reportData = aggregateReportService.getAggregateReportData(reportCriteria);
        assertEquals(1, reportData.getReportElements().size());

        verify(reportAggregatedDao);
        verify(projectDao);
    }
}

