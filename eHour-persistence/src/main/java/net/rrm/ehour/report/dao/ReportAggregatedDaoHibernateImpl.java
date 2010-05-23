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

package net.rrm.ehour.report.dao;

import java.io.Serializable;
import java.util.List;

import net.rrm.ehour.dao.AbstractAnnotationDaoHibernateImpl;
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.report.reports.element.AssignmentAggregateReportElement;

import org.springframework.stereotype.Repository;

/**
 * Reporting data operations 
 * @author Thies
 *
 */
@Repository("reportAggregatedDao")
public class ReportAggregatedDaoHibernateImpl extends AbstractAnnotationDaoHibernateImpl implements ReportAggregatedDao
{
	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.report.dao.ReportAggregatedDAO#getCumulatedHoursPerAssignmentForUsers(java.util.List, net.rrm.ehour.data.DateRange)
	 */
	@SuppressWarnings("unchecked")
	public List<AssignmentAggregateReportElement> getCumulatedHoursPerAssignmentForUsers(List<User> users, DateRange dateRange)
	{
		String[] keys = new String[]{"dateStart", "dateEnd", "users"};
		Object[] params = new Object[]{dateRange.getDateStart(), dateRange.getDateEnd(), users.toArray()};

		
		return getHibernateTemplate().findByNamedQueryAndNamedParam("Report.getCumulatedHoursPerAssignmentOnDateForUsers"
																		, keys, params);
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.report.dao.ReportAggregatedDAO#getCumulatedHoursPerAssignmentForUsers(java.util.List)
	 */
	@SuppressWarnings("unchecked")
	public List<AssignmentAggregateReportElement> getCumulatedHoursPerAssignmentForUsers(List<User> users)
	{
		return getHibernateTemplate().findByNamedQueryAndNamedParam("Report.getCumulatedHoursPerAssignmentForUsers"
																		, "users", users.toArray());
	}	

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.report.dao.ReportAggregatedDAO#getMinMaxDateTimesheetEntry()
	 */
	@SuppressWarnings("unchecked")
	public DateRange getMinMaxDateTimesheetEntry()
	{
		List<DateRange>	results;
		results = getHibernateTemplate().findByNamedQuery("Report.getMinMaxTimesheetEntryDate");
		return results.get(0);
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.report.dao.ReportAggregatedDAO#getCumulatedHoursPerAssignmentForUsers(java.util.List, java.util.List)
	 */
	@SuppressWarnings("unchecked")
	public List<AssignmentAggregateReportElement> getCumulatedHoursPerAssignmentForUsers(List<User> users, List<Project> projects)
	{
		String[] keys = new String[]{"users", "projects"};
		Object[] params = new Object[]{users.toArray(), projects.toArray()};
		
		return getHibernateTemplate().findByNamedQueryAndNamedParam("Report.getCumulatedHoursPerAssignmentForUsersAndProjects"
																		, keys, params);
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.report.dao.ReportAggregatedDAO#getCumulatedHoursPerAssignmentForUsers(java.util.List, java.util.List, net.rrm.ehour.data.DateRange)
	 */
	@SuppressWarnings("unchecked")
	public List<AssignmentAggregateReportElement> getCumulatedHoursPerAssignmentForUsers(List<User> users,
																					List<Project> projects,
																					DateRange dateRange)
	{
		String[] keys = new String[]{"dateStart", "dateEnd", "users", "projects"};
		Object[] params = new Object[]{dateRange.getDateStart(), dateRange.getDateEnd(), users.toArray(), projects.toArray()};
		
		return getHibernateTemplate().findByNamedQueryAndNamedParam("Report.getCumulatedHoursPerAssignmentOnDateForUsersAndProjects", keys, params);
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.report.dao.ReportAggregatedDAO#getMinMaxDateTimesheetEntry(java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	public DateRange getMinMaxDateTimesheetEntry(User user)
	{
		List<DateRange>	results;
		results = getHibernateTemplate().findByNamedQueryAndNamedParam("Report.getMinMaxTimesheetEntryDateForUser"
																		, "user", user);
		return results.get(0);
	}


	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.report.dao.ReportAggregatedDAO#getCumulatedHoursPerAssignment(net.rrm.ehour.data.DateRange)
	 */
	@SuppressWarnings("unchecked")
	public List<AssignmentAggregateReportElement> getCumulatedHoursPerAssignment(DateRange dateRange)
	{
		String[] keys = new String[]{"dateStart", "dateEnd"};
		Object[] params = new Object[]{dateRange.getDateStart(), dateRange.getDateEnd()};
		
		return getHibernateTemplate().findByNamedQueryAndNamedParam("Report.getCumulatedHoursPerAssignment"
																			, keys, params);
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.report.dao.ReportAggregatedDAO#getCumulatedHoursPerAssignmentForProjects(java.util.List, net.rrm.ehour.data.DateRange)
	 */
	@SuppressWarnings("unchecked")
	public List<AssignmentAggregateReportElement> getCumulatedHoursPerAssignmentForProjects(List<Project> projects, DateRange dateRange)
	{
		String[]	keys = new String[]{"dateStart", "dateEnd", "projects"};
		Object[]	params = new Object[]{dateRange.getDateStart(), dateRange.getDateEnd(), projects.toArray()};	

		return getHibernateTemplate().findByNamedQueryAndNamedParam("Report.getCumulatedHoursPerAssignmentOnDateForProjects"
																			, keys, params);
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.report.dao.ReportAggregatedDAO#getCumulatedHoursForAssignmentForUser(java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	public AssignmentAggregateReportElement getCumulatedHoursForAssignment(ProjectAssignment projectAssignment)
	{
		List<AssignmentAggregateReportElement>	results;
		
		results = getHibernateTemplate().findByNamedQueryAndNamedParam("Report.getCumulatedHoursForAssignment",
																		"assignment",
																		projectAssignment);

		return (results != null && results.size() > 0) ? results.get(0) : null;
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.report.dao.ReportAggregatedDAO#getCumulatedHoursPerAssignmentForAssignments(java.util.List)
	 */
	@SuppressWarnings("unchecked")
	public List<AssignmentAggregateReportElement> getCumulatedHoursPerAssignmentForAssignments(List<Serializable> projectAssignmentIds)
	{
		return getHibernateTemplate().findByNamedQueryAndNamedParam("Report.getCumulatedHoursPerAssignmentForAssignmentIds"
																		, "assignmentIds", projectAssignmentIds.toArray());
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.report.dao.ReportAggregatedDAO#getMixMaxDateTimesheetEntry(net.rrm.ehour.project.domain.Project)
	 */
	@SuppressWarnings("unchecked")
	public DateRange getMinMaxDateTimesheetEntry(Project project)
	{
		List<DateRange>	results;
		results = getHibernateTemplate().findByNamedQueryAndNamedParam("Report.getMinMaxTimesheetEntryDateForProject"
																		, "project",
																		project);
		return results.get(0);	}
}
