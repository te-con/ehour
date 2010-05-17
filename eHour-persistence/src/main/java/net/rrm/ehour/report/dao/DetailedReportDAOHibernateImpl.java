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
import net.rrm.ehour.report.reports.element.FlatReportElement;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

/**
 * PerMonth DAO impl using sql-queries  
 **/
@Repository("detailedReportDAO")
public class DetailedReportDAOHibernateImpl extends AbstractAnnotationDaoHibernateImpl implements DetailedReportDAO
{
	@SuppressWarnings("unchecked")
	public List<FlatReportElement> getHoursPerMonthPerAssignmentForUsers(List<Serializable> userIds, List<Serializable> projectIds, DateRange dateRange)
	{
		// FIXME, derby queries don't work
		Session session = this.getSession();
		
		Query query = session.getNamedQuery("Report.getHoursPerMonthPerAssignmentForUsersAndProjects")
						.setDate("dateStart", dateRange.getDateStart())
						.setDate("dateEnd", dateRange.getDateEnd())
						.setParameterList("userIdList", userIds)
						.setParameterList("projectIdList", projectIds)
						.setResultTransformer(Transformers.aliasToBean(FlatReportElement.class));

		return query.list();
	}

	/* (non-Javadoc)
	 * @see net.rrm.ehour.report.dao.ReportPerMonthDAO#getHoursPerMonthPerAssignmentForUsers(java.lang.Integer[], net.rrm.ehour.data.DateRange)
	 */
	@SuppressWarnings("unchecked")
	public List<FlatReportElement> getHoursPerMonthPerAssignmentForUsers(List<Serializable> userIds, DateRange dateRange)
	{
		// FIXME, derby queries don't work
		Session session = this.getSession();
		
		Query query = session.getNamedQuery("Report.getHoursPerMonthPerAssignmentForUsers")
						.setDate("dateStart", dateRange.getDateStart())
						.setDate("dateEnd", dateRange.getDateEnd())
						.setParameterList("userIdList", userIds)
						.setResultTransformer(Transformers.aliasToBean(FlatReportElement.class));

		return query.list();
	}
	
	/* (non-Javadoc)
	 * @see net.rrm.ehour.report.dao.ReportPerMonthDAO#getHoursPerMonthPerAssignmentForProjects(java.lang.Integer[], net.rrm.ehour.data.DateRange)
	 */
	@SuppressWarnings("unchecked")
	public List<FlatReportElement> getHoursPerMonthPerAssignmentForProjects(List<Serializable> projectIds, DateRange dateRange)
	{
		// FIXME, derby queries don't work		
		Session session = this.getSession();
		
		Query query = session.getNamedQuery("Report.getHoursPerMonthPerAssignmentForProjects")
						.setDate("dateStart", dateRange.getDateStart())
						.setDate("dateEnd", dateRange.getDateEnd())
						.setParameterList("projectIdList", projectIds)
						.setResultTransformer(Transformers.aliasToBean(FlatReportElement.class));

		return query.list();		
	}


	/* (non-Javadoc)
	 * @see net.rrm.ehour.report.dao.ReportPerMonthDAO#getHoursPerMonthPerAssignment(net.rrm.ehour.data.DateRange)
	 */
	@SuppressWarnings("unchecked")
	public List<FlatReportElement> getHoursPerMonthPerAssignment(DateRange dateRange)
	{
		// FIXME, derby queries don't work		
		Session session = this.getSession();
		
		Query query = session.getNamedQuery("Report.getHoursPerMonthPerAssignment")
						.setDate("dateStart", dateRange.getDateStart())
						.setDate("dateEnd", dateRange.getDateEnd())
						.setResultTransformer(Transformers.aliasToBean(FlatReportElement.class));

		return query.list();
	}	


	/**
	 * Get hours per day for assignments
	 * @param assignmentId
	 * @param dateRange
	 * @return
	 */	
	@SuppressWarnings("unchecked")
	public List<FlatReportElement> getHoursPerDayForAssignment(List<Serializable> assignmentIds, DateRange dateRange)
	{
		Session session = this.getSession();
		
		Query query = session.getNamedQuery("Report.getHoursPerDayForAssignment")
						.setDate("dateStart", dateRange.getDateStart())
						.setDate("dateEnd", dateRange.getDateEnd())
						.setParameterList("assignmentId", assignmentIds)
						.setResultTransformer(Transformers.aliasToBean(FlatReportElement.class));

		return query.list();
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.report.dao.DetailedReportDAO#getHoursPerDayForUsers(java.util.List, net.rrm.ehour.data.DateRange)
	 */
	@SuppressWarnings("unchecked")
	public List<FlatReportElement> getHoursPerDayForUsers(List<Serializable> userIds, DateRange dateRange)
	{
		Session session = this.getSession();
		
		Query query = session.getNamedQuery("Report.getHoursPerDayForUsers")
						.setDate("dateStart", dateRange.getDateStart())
						.setDate("dateEnd", dateRange.getDateEnd())
						.setParameterList("userIds", userIds)
						.setResultTransformer(Transformers.aliasToBean(FlatReportElement.class));

		return query.list();
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.report.dao.DetailedReportDAO#getHoursPerDayForProjects(java.util.List, net.rrm.ehour.data.DateRange)
	 */
	@SuppressWarnings("unchecked")
	public List<FlatReportElement> getHoursPerDayForProjects(List<Serializable> projectIds, DateRange dateRange)
	{
		Session session = this.getSession();
		
		Query query = session.getNamedQuery("Report.getHoursPerDayForProjects")
						.setDate("dateStart", dateRange.getDateStart())
						.setDate("dateEnd", dateRange.getDateEnd())
						.setParameterList("projectIds", projectIds)
						.setResultTransformer(Transformers.aliasToBean(FlatReportElement.class));

		return query.list();
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.report.dao.DetailedReportDAO#getHoursPerDayForProjectsAndUsers(java.util.List, java.util.List, net.rrm.ehour.data.DateRange)
	 */
	@SuppressWarnings("unchecked")
	public List<FlatReportElement> getHoursPerDayForProjectsAndUsers(List<Serializable> projectIds, List<Serializable> userIds, DateRange dateRange)
	{
		Session session = this.getSession();
		
		Query query = session.getNamedQuery("Report.getHoursPerDayForProjectsAndUsers")
						.setDate("dateStart", dateRange.getDateStart())
						.setDate("dateEnd", dateRange.getDateEnd())
						.setParameterList("projectIds", projectIds)
						.setParameterList("userIds", userIds)
						.setResultTransformer(Transformers.aliasToBean(FlatReportElement.class));

		return query.list();
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.report.dao.DetailedReportDAO#getHoursPerDay(net.rrm.ehour.data.DateRange)
	 */
	@SuppressWarnings("unchecked")
	public List<FlatReportElement> getHoursPerDay(DateRange dateRange)
	{
		Session session = this.getSession();
		
		Query query = session.getNamedQuery("Report.getHoursPerDay")
						.setDate("dateStart", dateRange.getDateStart())
						.setDate("dateEnd", dateRange.getDateEnd())
						.setResultTransformer(Transformers.aliasToBean(FlatReportElement.class));

		return query.list();	
	}
}
