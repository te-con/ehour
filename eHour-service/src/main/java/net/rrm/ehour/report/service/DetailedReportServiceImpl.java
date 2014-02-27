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
import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.persistence.project.dao.ProjectDao;
import net.rrm.ehour.persistence.report.dao.DetailedReportDao;
import net.rrm.ehour.persistence.report.dao.ReportAggregatedDao;
import net.rrm.ehour.persistence.user.dao.UserDao;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.reports.ReportData;
import net.rrm.ehour.report.reports.element.FlatReportElement;
import net.rrm.ehour.report.reports.element.LockableDate;
import net.rrm.ehour.timesheet.service.TimesheetLockService;
import net.rrm.ehour.util.DomainUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Report service for detailed reports implementation
 */
@Service("detailedReportService")
public class DetailedReportServiceImpl extends AbstractReportServiceImpl<FlatReportElement> implements DetailedReportService {
    private DetailedReportDao detailedReportDao;

    DetailedReportServiceImpl() {
    }

    @Autowired
    public DetailedReportServiceImpl(UserDao userDao, ProjectDao projectDao, TimesheetLockService lockService, DetailedReportDao detailedReportDao, ReportAggregatedDao reportAggregatedDAO) {
        super(userDao, projectDao, lockService, reportAggregatedDAO);
        this.detailedReportDao = detailedReportDao;
    }

    public ReportData getDetailedReportData(ReportCriteria reportCriteria) {
        return getReportData(reportCriteria);
    }

    @Override
    protected List<FlatReportElement> getReportElements(List<User> users,
                                                        List<Project> projects,
                                                        List<Date> lockedDates,
                                                        DateRange reportRange,
                                                        boolean showZeroBookings) {
        List<Integer> userIds = DomainUtil.getIdsFromDomainObjects(users);
        List<Integer> projectIds = DomainUtil.getIdsFromDomainObjects(projects);

        List<FlatReportElement> elements = getElements(userIds, projectIds, reportRange);

        for (FlatReportElement element : elements) {
            Date date = element.getDayDate();
            element.setLockableDate(new LockableDate(date, lockedDates.contains(date)));
        }

        if (showZeroBookings) {
            List<FlatReportElement> filterAssignmentsWithoutBookings = getElementsWithoutBookings(reportRange, userIds, projectIds);

            filterAssignmentsWithoutBookings.addAll(elements);

            return filterAssignmentsWithoutBookings;
        } else {
            return elements;
        }
    }

    private List<FlatReportElement> getElementsWithoutBookings(DateRange reportRange, List<Integer> userIds, List<Integer> projectIds) {
        List<ProjectAssignment> assignments = getAssignmentsWithoutBookings(reportRange, userIds, projectIds);

        List<FlatReportElement> elements = Lists.newArrayList();

        for (ProjectAssignment assignment : assignments) {
            FlatReportElement element = new FlatReportElement();
            element.setAssignmentId(assignment.getAssignmentId());
            element.setRole(assignment.getRole());

            Project project = assignment.getProject();
            Customer customer = project.getCustomer();

            element.setCustomerCode(customer.getCode());
            element.setCustomerId(customer.getCustomerId());
            element.setCustomerName(customer.getName());

            element.setEmptyEntry(true);
            element.setProjectCode(project.getProjectCode());
            element.setProjectId(project.getProjectId());
            element.setProjectName(project.getName());

            element.setRate(assignment.getHourlyRate());

            element.setUserFirstName(assignment.getUser().getFirstName());
            element.setUserLastName(assignment.getUser().getLastName());

            elements.add(element);
        }

        return elements;
    }

    private List<FlatReportElement> getElements(List<Integer> userIds, List<Integer> projectIds, DateRange reportRange) {
        List<FlatReportElement> elements;

        if (userIds == null && projectIds == null) {
            elements = detailedReportDao.getHoursPerDay(reportRange);
        } else if (projectIds == null) {
            elements = detailedReportDao.getHoursPerDayForUsers(userIds, reportRange);
        } else if (userIds == null) {
            elements = detailedReportDao.getHoursPerDayForProjects(projectIds, reportRange);
        } else {
            elements = detailedReportDao.getHoursPerDayForProjectsAndUsers(projectIds, userIds, reportRange);
        }
        return elements;
    }
}
