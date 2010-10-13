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

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.report.reports.element.AssignmentAggregateReportElement;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Reporting data operations 
 * @author Thies
 *
 */

public class ReportAggregatedDAOHibernateImpl extends HibernateDaoSupport implements ReportAggregatedDAO
{
	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.report.dao.ReportAggregatedDAO#getCumulatedHoursPerAssignmentForUsers(java.util.List, net.rrm.ehour.data.DateRange)
	 */
	@SuppressWarnings("unchecked")
	public List<AssignmentAggregateReportElement> getCumulatedHoursPerAssignmentForUsers(List<User> users, DateRange dateRange)
	{
		List		results;
		String[]	keys = new String[3];
		Object[]	params = new Object[3];
		
		keys[0] = "dateStart";
		keys[1] = "dateEnd";
		keys[2] = "users";
		
		params[0] = dateRange.getDateStart();
		params[1] = dateRange.getDateEnd();
		params[2] = users.toArray();
		
		results = getHibernateTemplate().findByNamedQueryAndNamedParam("Report.getCumulatedHoursPerAssignmentOnDateForUsers"
																		, keys, params);
		
		return results;		
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.report.dao.ReportAggregatedDAO#getCumulatedHoursPerAssignmentForUsers(java.util.List)
	 */
	@SuppressWarnings("unchecked")
	public List<AssignmentAggregateReportElement> getCumulatedHoursPerAssignmentForUsers(List<User> users)
	{
		List		results;

		results = getHibernateTemplate().findByNamedQueryAndNamedParam("Report.getCumulatedHoursPerAssignmentForUsers"
																		, "users", users.toArray());
		
		return results;		
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
		List		results;
		String[]	keys = new String[2];
		Object[]	params = new Object[2];
		
		keys[0] = "users";
		keys[1] = "projects";
		
		params[0] = users.toArray();
		params[1] = projects.toArray();
		
		results = getHibernateTemplate().findByNamedQueryAndNamedParam("Report.getCumulatedHoursPerAssignmentForUsersAndProjects"
																		, keys, params);
		return results;
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
		List		results;
		String[]	keys = new String[4];
		Object[]	params = new Object[4];
		
		keys[0] = "dateStart";
		keys[1] = "dateEnd";
		keys[2] = "users";
		keys[3] = "projects";
		
		params[0] = dateRange.getDateStart();
		params[1] = dateRange.getDateEnd();
		params[2] = users.toArray();
		params[3] = projects.toArray();
		
		results = getHibernateTemplate().findByNamedQueryAndNamedParam("Report.getCumulatedHoursPerAssignmentOnDateForUsersAndProjects"
																		, keys, params);
		return results;
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
		List		results;
		String[]	keys = new String[2];
		Object[]	params = new Object[2];
		
		keys[0] = "dateStart";
		keys[1] = "dateEnd";		

		params[0] = dateRange.getDateStart();
		params[1] = dateRange.getDateEnd();
		
		results = getHibernateTemplate().findByNamedQueryAndNamedParam("Report.getCumulatedHoursPerAssignment"
																			, keys, params);
		return results;		
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.report.dao.ReportAggregatedDAO#getCumulatedHoursPerAssignmentForProjects(java.util.List, net.rrm.ehour.data.DateRange)
	 */
	@SuppressWarnings("unchecked")
	public List<AssignmentAggregateReportElement> getCumulatedHoursPerAssignmentForProjects(List<Project> projects, DateRange dateRange)
	{
		List		results;
		String[]	keys = new String[3];
		Object[]	params = new Object[3];
		
		keys[0] = "dateStart";
		keys[1] = "dateEnd";		
		keys[2] = "projects";

		params[0] = dateRange.getDateStart();
		params[1] = dateRange.getDateEnd();
		params[2] = projects.toArray();
		
		results = getHibernateTemplate().findByNamedQueryAndNamedParam("Report.getCumulatedHoursPerAssignmentOnDateForProjects"
																			, keys, params);
		return results;
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
		List		results;
		
		results = getHibernateTemplate().findByNamedQueryAndNamedParam("Report.getCumulatedHoursPerAssignmentForAssignmentIds"
																		, "assignmentIds", projectAssignmentIds.toArray());
		return results;		
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
