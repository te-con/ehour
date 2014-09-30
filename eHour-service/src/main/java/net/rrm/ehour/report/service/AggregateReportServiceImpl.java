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
import net.rrm.ehour.domain.Activity;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.persistence.project.dao.ProjectDao;
import net.rrm.ehour.persistence.report.dao.ReportAggregatedDao;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.reports.ProjectManagerReport;
import net.rrm.ehour.report.reports.ReportData;
import net.rrm.ehour.report.reports.element.ActivityAggregateReportElement;
import net.rrm.ehour.timesheet.service.TimesheetLockService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Provides reporting services on timesheets.
 *
 * @author Thies
 */
@Service("aggregateReportService")
public class AggregateReportServiceImpl extends AbstractReportServiceImpl<ActivityAggregateReportElement>
        implements AggregateReportService {
    private ReportAggregatedDao reportAggregatedDAO;

    private ActivityService activityService;

    AggregateReportServiceImpl() {
        super();
    }

    @Autowired
    public AggregateReportServiceImpl(ActivityService activityService, ProjectDao projectDao, TimesheetLockService lockService, ReportAggregatedDao reportAggregatedDAO) {
        super(projectDao, lockService, reportAggregatedDAO);
        this.reportAggregatedDAO = reportAggregatedDAO;
        this.activityService = activityService;
    }

    @Override
    public List<ActivityAggregateReportElement> getHoursPerActivity(List<Integer> activityIds) {
        return reportAggregatedDAO.getCumulatedHoursPerActivityForActivities(activityIds);
    }

    @Override
    public List<ActivityAggregateReportElement> getHoursPerActivityInRange(Integer userId, DateRange dateRange) {
        List<ActivityAggregateReportElement> activityAggregateReportElements;

        List<User> users = new ArrayList<User>();
        users.add(new User(userId));
        activityAggregateReportElements = reportAggregatedDAO.getCumulatedHoursPerActivityForUsers(users, dateRange);

        return activityAggregateReportElements;
    }

    @Override
    public ReportData getAggregateReportData(ReportCriteria criteria) {
        return getReportData(criteria);
    }

    @Override
    protected List<ActivityAggregateReportElement> getReportElements(List<User> users,
                                                                     List<Project> projects,
                                                                     List<Date> lockedDates,
                                                                     DateRange reportRange,
                                                                     boolean showZeroBookings) {
        List<ActivityAggregateReportElement> aggregates = Lists.newArrayList();

        if (users == null && projects == null) {
            aggregates = reportAggregatedDAO.getCumulatedHoursPerActivity(reportRange);
        } else if (projects == null) {
            if (!CollectionUtils.isEmpty(users)) {
                aggregates = reportAggregatedDAO.getCumulatedHoursPerActivityForUsers(users, reportRange);
            }
        } else if (users == null) {
            if (!CollectionUtils.isEmpty(projects)) {
                aggregates = reportAggregatedDAO.getCumulatedHoursPerActivityForProjects(projects, reportRange);
            }
        } else {
            if (!CollectionUtils.isEmpty(users) && !CollectionUtils.isEmpty(projects)) {
                aggregates = reportAggregatedDAO.getCumulatedHoursPerActivityForUsers(users, projects, reportRange);
            }
        }

        return aggregates;
    }


    @Override
    public ProjectManagerReport getProjectManagerDetailedReport(Project project) {
        ProjectManagerReport report = new ProjectManagerReport();

        // get the project
        report.setProject(project);

        // get a proper report range
        DateRange reportRange = getReportRangeForProject(project);
        report.setReportRange(reportRange);

        // get all aggregates
        List<Project> projects = Arrays.asList(project);
        SortedSet<ActivityAggregateReportElement> aggregates = new TreeSet<ActivityAggregateReportElement>(reportAggregatedDAO.getCumulatedHoursPerActivityForProjects(projects, reportRange));

        // filter out just the id's
        List<Integer> activityIds = new ArrayList<Integer>();
        for (ActivityAggregateReportElement aggregate : aggregates) {
            activityIds.add(aggregate.getActivity().getId());
        }

        // get all activities for this period regardless whether they booked hours on it
        List<Activity> allActivities = activityService.getActivities(project, reportRange);

        for (Activity activity : allActivities) {
            if (!activityIds.contains(activity.getId())) {
                ActivityAggregateReportElement emptyAggregate = new ActivityAggregateReportElement();
                emptyAggregate.setActivity(activity);

                aggregates.add(emptyAggregate);
            }
        }

        report.setAggregates(aggregates);
        report.deriveTotals();

        return report;
    }

    private DateRange getReportRangeForProject(Project project) {
        DateRange minMaxRange = reportAggregatedDAO.getMinMaxDateTimesheetEntry(project);
        return new DateRange(minMaxRange.getDateStart(), minMaxRange.getDateEnd());
    }
}
