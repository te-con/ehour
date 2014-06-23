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

package net.rrm.ehour.persistence.report.dao;

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.persistence.dao.AbstractAnnotationDaoHibernate4Impl;
import net.rrm.ehour.report.reports.element.AssignmentAggregateReportElement;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Reporting data operations
 *
 * @author Thies
 */
@Repository("reportAggregatedDao")
@SuppressWarnings("unchecked")
public class ReportAggregatedDaoHibernateImpl extends AbstractAnnotationDaoHibernate4Impl implements ReportAggregatedDao {
    private static final String CACHEREGION = "query.Report";

    @Override
    public List<AssignmentAggregateReportElement> getCumulatedHoursPerAssignmentForUsers(List<User> users, DateRange dateRange) {
        String[] keys = new String[]{"dateStart", "dateEnd", "users"};
        Object[] params = new Object[]{dateRange.getDateStart(), dateRange.getDateEnd(), users};

        return findByNamedQueryAndNamedParam("Report.getCumulatedHoursPerAssignmentOnDateForUsers", keys, params);
    }

    @Override
    public List<AssignmentAggregateReportElement> getCumulatedHoursPerAssignmentForUsers(List<User> users) {
        return findByNamedQueryAndNamedParam("Report.getCumulatedHoursPerAssignmentForUsers", "users", users);
    }

    @Override
    public DateRange getMinMaxDateTimesheetEntry() {
        List<DateRange> results = getSession().getNamedQuery("Report.getMinMaxTimesheetEntryDate").list();

        return results.size() > 0 ? results.get(0) : new DateRange();
    }

    @Override
    public List<AssignmentAggregateReportElement> getCumulatedHoursPerAssignmentForUsers(List<User> users, List<Project> projects) {
        String[] keys = new String[]{"users", "projects"};
        Object[] params = new Object[]{users, projects};

        return findByNamedQueryAndNamedParam("Report.getCumulatedHoursPerAssignmentForUsersAndProjects", keys, params);
    }

    @Override
    public List<AssignmentAggregateReportElement> getCumulatedHoursPerAssignmentForUsers(List<User> users,
                                                                                         List<Project> projects,
                                                                                         DateRange dateRange) {
        String[] keys = new String[]{"dateStart", "dateEnd", "users", "projects"};
        Object[] params = new Object[]{dateRange.getDateStart(), dateRange.getDateEnd(), users, projects};

        return findByNamedQueryAndNamedParam("Report.getCumulatedHoursPerAssignmentOnDateForUsersAndProjects", keys, params);
    }

    @Override
    public DateRange getMinMaxDateTimesheetEntry(User user) {
        List<DateRange> results = findByNamedQueryAndNamedParam("Report.getMinMaxTimesheetEntryDateForUser", "user", Arrays.asList(user));

        return results.size() > 0 ? results.get(0) : new DateRange();
    }

    @Override
    public List<AssignmentAggregateReportElement> getCumulatedHoursPerAssignment(DateRange dateRange) {
        String[] keys = new String[]{"dateStart", "dateEnd"};
        Object[] params = new Object[]{dateRange.getDateStart(), dateRange.getDateEnd()};

        return findByNamedQueryAndNamedParam("Report.getCumulatedHoursPerAssignment", keys, params);
    }

    @Override
    public List<AssignmentAggregateReportElement> getCumulatedHoursPerAssignmentForProjects(List<Project> projects, DateRange dateRange) {
        String[] keys = new String[]{"dateStart", "dateEnd", "projects"};
        Object[] params = new Object[]{dateRange.getDateStart(), dateRange.getDateEnd(), projects};

        return findByNamedQueryAndNamedParam("Report.getCumulatedHoursPerAssignmentOnDateForProjects", keys, params);
    }

    @Override
    public AssignmentAggregateReportElement getCumulatedHoursForAssignment(ProjectAssignment projectAssignment) {
        List<AssignmentAggregateReportElement> results = findByNamedQueryAndNamedParam("Report.getCumulatedHoursForAssignment",
                new String[]{"assignment"},
                new Object[]{projectAssignment});

        return (results != null && results.size() > 0) ? results.get(0) : null;
    }

    @Override
    public List<AssignmentAggregateReportElement> getCumulatedHoursPerAssignmentForAssignments(List<? extends Serializable> projectAssignmentIds) {
        return findByNamedQueryAndNamedParam("Report.getCumulatedHoursPerAssignmentForAssignmentIds", "assignmentIds", projectAssignmentIds);
    }

    @Override
    public DateRange getMinMaxDateTimesheetEntry(Project project) {
        List<DateRange> results = findByNamedQueryAndNamedParam("Report.getMinMaxTimesheetEntryDateForProject", "project", Arrays.asList(project));
        return (results != null && results.size() > 0) ? results.get(0) : new DateRange();
    }

    @Override
    public List<ProjectAssignment> getAssignmentsWithoutBookings(DateRange dateRange) {
        String[] keys = new String[]{"dateStart", "dateEnd"};
        Object[] params = new Object[]{dateRange.getDateStart(), dateRange.getDateEnd()};

        return findByNamedQueryAndNamedParam("Report.getAssignmentsWithoutBookings", keys, params);
    }

    @SuppressWarnings("unchecked")
    private <C> List<C> findByNamedQueryAndNamedParam(final String queryName,
                                                      final String[] paramNames,
                                                      final Object[] values) {
        Query query = getSession().getNamedQuery(queryName);

        for (int i = 0; i < values.length; i++) {
            if (values[i] instanceof Collection) {
                query.setParameterList(paramNames[i], (Collection) values[i]);
            } else {
                query.setParameter(paramNames[i], values[i]);
            }
        }

        return executeQuery(query);
    }

    @SuppressWarnings("unchecked")
    private <C> List<C> findByNamedQueryAndNamedParam(final String queryName,
                                                      final String paramName,
                                                      final List<?> values) {
        Query query = getSession().getNamedQuery(queryName);

        query.setParameterList(paramName, values);

        return executeQuery(query);
    }

    private <C> List<C> executeQuery(Query query) {
        query.setCacheable(true);
        query.setCacheRegion(CACHEREGION);

        return (List<C>) query.list();
    }
}
