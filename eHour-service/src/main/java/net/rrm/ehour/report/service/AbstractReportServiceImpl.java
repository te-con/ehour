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
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.criteria.UserSelectedCriteria;
import net.rrm.ehour.report.reports.ReportData;
import net.rrm.ehour.report.reports.element.ProjectStructuredReportElement;
import net.rrm.ehour.timesheet.service.TimesheetLockService;
import net.rrm.ehour.timesheet.service.TimesheetLockService$;
import org.joda.time.Interval;
import scala.collection.Seq;

import java.util.Date;
import java.util.List;

/**
 * Abstract report service provides utility methods for dealing
 * with the usercriteria obj
 */

public abstract class AbstractReportServiceImpl<RE extends ProjectStructuredReportElement> {
    private ProjectDao projectDao;
    private TimesheetLockService lockService;
    protected ReportAggregatedDao reportAggregatedDao;
    private ReportCriteriaService reportCriteriaService;


    AbstractReportServiceImpl() {
    }

    protected AbstractReportServiceImpl(ReportCriteriaService reportCriteriaService, ProjectDao projectDao, TimesheetLockService lockService, ReportAggregatedDao reportAggregatedDao) {
        this.reportCriteriaService = reportCriteriaService;
        this.projectDao = projectDao;
        this.lockService = lockService;
        this.reportAggregatedDao = reportAggregatedDao;
    }

    ReportData getReportData(ReportCriteria reportCriteria) {
        UserSelectedCriteria userSelectedCriteria = reportCriteria.getUserSelectedCriteria();

        DateRange reportRange = reportCriteria.getReportRange();

        Seq<Interval> lockedDatesAsIntervals = lockService.findLockedDatesInRange(reportRange.getDateStart(), reportRange.getDateEnd());
        List<Date> lockedDates = TimesheetLockService$.MODULE$.intervalToJavaDates(lockedDatesAsIntervals);

        List<RE> allReportElements = generateReport(userSelectedCriteria, lockedDates, reportRange);

        if (userSelectedCriteria.isForPm()) {
            List<ProjectStructuredReportElement> elem = evictNonPmReportElements(userSelectedCriteria, allReportElements);
            return new ReportData(lockedDates, elem, reportRange, userSelectedCriteria);
        } else {
            return new ReportData(lockedDates, allReportElements, reportRange, userSelectedCriteria);
        }
    }

    private List<ProjectStructuredReportElement> evictNonPmReportElements(UserSelectedCriteria userSelectedCriteria, List<RE> allReportElements) {
        List<Integer> projectIds = fetchAllowedProjectIds(userSelectedCriteria);

        List<ProjectStructuredReportElement> allowedElements = Lists.newArrayList();

        for (ProjectStructuredReportElement reportElement : allReportElements) {
            if (projectIds.contains(reportElement.getProjectId())) {
                allowedElements.add(reportElement);
            }
        }
        return allowedElements;
    }

    private List<Integer> fetchAllowedProjectIds(UserSelectedCriteria userSelectedCriteria) {
        List<Project> allowedProjects = projectDao.findActiveProjectsWhereUserIsPM(userSelectedCriteria.getPm());

        List<Integer> projectIds = Lists.newArrayList();

        for (Project allowedProject : allowedProjects) {
            projectIds.add(allowedProject.getProjectId());
        }
        return projectIds;
    }


    private List<RE> generateReport(UserSelectedCriteria userSelectedCriteria, List<Date> lockedDates, DateRange reportRange) {
        UsersAndProjects usersAndProjects = reportCriteriaService.criteriaToUsersAndProjects(userSelectedCriteria);

        return getReportElements(usersAndProjects.getUsers(),
                usersAndProjects.getProjects(),
                lockedDates,
                reportRange,
                userSelectedCriteria.isShowZeroBookings());
    }

    /**
     * Get the actual data
     */
    protected abstract List<RE> getReportElements(List<User> users,
                                                  List<Project> projects,
                                                  List<Date> lockedDates,
                                                  DateRange reportRange,
                                                  boolean showZeroBookings);



    protected List<ProjectAssignment> getAssignmentsWithoutBookings(DateRange reportRange, List<Integer> userIds, List<Integer> projectIds) {
        List<ProjectAssignment> assignmentsWithoutBookings = reportAggregatedDao.getAssignmentsWithoutBookings(reportRange);

        List<ProjectAssignment> filteredAssignmentsWithoutBookings = Lists.newArrayList();

        for (ProjectAssignment assignmentsWithoutBooking : assignmentsWithoutBookings) {
            boolean passedUserFilter = userIds == null || userIds.contains(assignmentsWithoutBooking.getUser().getUserId());
            boolean passedProjectFilter = projectIds == null || projectIds.contains(assignmentsWithoutBooking.getProject().getProjectId());

            if (passedUserFilter && passedProjectFilter) {
                filteredAssignmentsWithoutBookings.add(assignmentsWithoutBooking);
            }
        }
        return filteredAssignmentsWithoutBookings;
    }
}
