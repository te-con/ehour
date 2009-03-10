/**
 * Created on Feb 4, 2007
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
 *
 * This Software is copyright TE-CON 2007. This Software is not open source by definition. The source of the Software is available for educational purposes.
 * TE-CON holds all the ownership rights on the Software.
 * TE-CON freely grants the right to use the Software. Any reproduction or modification of this Software, whether for commercial use or open source,
 * is subject to obtaining the prior express authorization of TE-CON.
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.report.dao;

import java.io.Serializable;
import java.util.List;

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.report.reports.element.FlatReportElement;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * PerMonth DAO impl using sql-queries  
 **/

public class DetailedReportDAOHibernateImpl extends HibernateDaoSupport implements DetailedReportDAO
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
