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
import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.persistence.project.dao.ProjectDao;
import net.rrm.ehour.persistence.report.dao.ReportAggregatedDao;
import net.rrm.ehour.project.service.ProjectAssignmentService;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.reports.ProjectManagerReport;
import net.rrm.ehour.report.reports.ReportData;
import net.rrm.ehour.report.reports.element.AssignmentAggregateReportElement;
import net.rrm.ehour.timesheet.service.TimesheetLockService;
import net.rrm.ehour.util.DomainUtil;
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
public class AggregateReportServiceImpl extends AbstractReportServiceImpl<AssignmentAggregateReportElement> implements AggregateReportService {
    private ReportAggregatedDao reportAggregatedDAO;

    private ProjectAssignmentService projectAssignmentService;

    AggregateReportServiceImpl() {
        super();
    }

    @Autowired
    public AggregateReportServiceImpl(ProjectAssignmentService projectAssignmentService, ReportCriteriaService reportCriteriaService, ProjectDao projectDao, TimesheetLockService lockService, ReportAggregatedDao reportAggregatedDAO) {
        super(reportCriteriaService, projectDao, lockService, reportAggregatedDAO);
        this.reportAggregatedDAO = reportAggregatedDAO;
        this.projectAssignmentService = projectAssignmentService;
    }

    @Override
    public List<AssignmentAggregateReportElement> getHoursPerAssignment(List<Integer> projectAssignmentIds) {
        return reportAggregatedDAO.getCumulatedHoursPerAssignmentForAssignments(projectAssignmentIds);
    }

    @Override
    public List<AssignmentAggregateReportElement> getHoursPerAssignmentInRange(Integer userId, DateRange dateRange) {
        List<AssignmentAggregateReportElement> assignmentAggregateReportElements;

        List<User> users = new ArrayList<>();
        users.add(new User(userId));
        assignmentAggregateReportElements = reportAggregatedDAO.getCumulatedHoursPerAssignmentForUsers(users, dateRange);

        return assignmentAggregateReportElements;
    }

    @Override
    public ReportData getAggregateReportData(ReportCriteria criteria) {
        return getReportData(criteria);
    }

    @Override
    protected List<AssignmentAggregateReportElement> getReportElements(List<User> users, List<Project> projects, List<Date> lockedDates, DateRange reportRange, boolean showZeroBookings) {
        List<AssignmentAggregateReportElement> aggregates = findAggregates(users, projects, reportRange);

        List<AssignmentAggregateReportElement> noBookings = findAssignmentsWithoutBookings(users, projects, reportRange, showZeroBookings);

        noBookings.addAll(aggregates);

        return noBookings;
    }

    private List<AssignmentAggregateReportElement> findAssignmentsWithoutBookings(List<User> users, List<Project> projects, DateRange reportRange, boolean showZeroBookings) {
        List<AssignmentAggregateReportElement> elements = Lists.newArrayList();

        if (showZeroBookings) {
            List<Integer> userIds = DomainUtil.getIdsFromDomainObjects(users);
            List<Integer> projectIds = DomainUtil.getIdsFromDomainObjects(projects);

            List<ProjectAssignment> filterAssignmentsWithoutBookings = getAssignmentsWithoutBookings(reportRange, userIds, projectIds);

            for (ProjectAssignment filterAssignmentsWithoutBooking : filterAssignmentsWithoutBookings) {
                elements.add(new AssignmentAggregateReportElement(filterAssignmentsWithoutBooking));
            }
        }
        return elements;
    }

    private List<AssignmentAggregateReportElement> findAggregates(List<User> users, List<Project> projects, DateRange reportRange) {
        List<AssignmentAggregateReportElement> aggregates = new ArrayList<>();

        if (users.isEmpty() && projects.isEmpty()) {
            aggregates = reportAggregatedDAO.getCumulatedHoursPerAssignment(reportRange);
        } else if (projects.isEmpty()) {
            if (!CollectionUtils.isEmpty(users)) {
                aggregates = reportAggregatedDAO.getCumulatedHoursPerAssignmentForUsers(users, reportRange);
            }
        } else if (users.isEmpty()) {
            if (!CollectionUtils.isEmpty(projects)) {
                aggregates = reportAggregatedDAO.getCumulatedHoursPerAssignmentForProjects(projects, reportRange);
            }
        } else {
            if (!CollectionUtils.isEmpty(users) && !CollectionUtils.isEmpty(projects)) {
                aggregates = reportAggregatedDAO.getCumulatedHoursPerAssignmentForUsers(users, projects, reportRange);
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
        SortedSet<AssignmentAggregateReportElement> aggregates = new TreeSet<AssignmentAggregateReportElement>(reportAggregatedDAO.getCumulatedHoursPerAssignmentForProjects(projects, reportRange));

        // filter out just the id's
        List<Integer> assignmentIds = new ArrayList<>();
        for (AssignmentAggregateReportElement aggregate : aggregates) {
            assignmentIds.add(aggregate.getProjectAssignment().getAssignmentId());
        }

        // get all assignments for this period regardless whether they booked hours on it
        List<ProjectAssignment> allAssignments = projectAssignmentService.getProjectAssignments(project, reportRange);

        for (ProjectAssignment assignment : allAssignments) {
            if (!assignmentIds.contains(assignment.getAssignmentId())) {
                AssignmentAggregateReportElement emptyAggregate = new AssignmentAggregateReportElement();
                emptyAggregate.setProjectAssignment(assignment);

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
