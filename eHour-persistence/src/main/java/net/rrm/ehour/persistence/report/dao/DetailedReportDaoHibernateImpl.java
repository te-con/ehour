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
import net.rrm.ehour.persistence.dao.AbstractAnnotationDaoHibernate4Impl;
import net.rrm.ehour.report.reports.element.FlatReportElement;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

/**
 * PerMonth DAO impl using sql-queries
 */
@Repository("detailedReportDao")
public class DetailedReportDaoHibernateImpl extends AbstractAnnotationDaoHibernate4Impl implements DetailedReportDao {
    @SuppressWarnings("unchecked")
    public List<FlatReportElement> getHoursPerDayForAssignment(List<? extends Serializable> assignmentIds, DateRange dateRange) {
        Session session = this.getSession();

        Query query = session.getNamedQuery("Report.getHoursPerDayForAssignment")
                .setDate("dateStart", dateRange.getDateStart())
                .setDate("dateEnd", dateRange.getDateEnd())
                .setParameterList("assignmentId", assignmentIds)
                .setResultTransformer(Transformers.aliasToBean(FlatReportElement.class));

        return query.list();
    }

    @SuppressWarnings("unchecked")
    public List<FlatReportElement> getHoursPerDayForUsers(List<? extends Serializable> userIds, DateRange dateRange) {
        Session session = this.getSession();

        Query query = session.getNamedQuery("Report.getHoursPerDayForUsers")
                .setDate("dateStart", dateRange.getDateStart())
                .setDate("dateEnd", dateRange.getDateEnd())
                .setParameterList("userIds", userIds)
                .setResultTransformer(Transformers.aliasToBean(FlatReportElement.class));

        return query.list();
    }

    @SuppressWarnings("unchecked")
    public List<FlatReportElement> getHoursPerDayForProjects(List<? extends Serializable> projectIds, DateRange dateRange) {
        Session session = this.getSession();

        Query query = session.getNamedQuery("Report.getHoursPerDayForProjects")
                .setDate("dateStart", dateRange.getDateStart())
                .setDate("dateEnd", dateRange.getDateEnd())
                .setParameterList("projectIds", projectIds)
                .setResultTransformer(Transformers.aliasToBean(FlatReportElement.class));

        return query.list();
    }

    @SuppressWarnings("unchecked")
    public List<FlatReportElement> getHoursPerDayForProjectsAndUsers(List<? extends Serializable> projectIds, List<? extends Serializable> userIds, DateRange dateRange) {
        Session session = this.getSession();

        Query query = session.getNamedQuery("Report.getHoursPerDayForProjectsAndUsers")
                .setDate("dateStart", dateRange.getDateStart())
                .setDate("dateEnd", dateRange.getDateEnd())
                .setParameterList("projectIds", projectIds)
                .setParameterList("userIds", userIds)
                .setResultTransformer(Transformers.aliasToBean(FlatReportElement.class));

        return query.list();
    }

    @SuppressWarnings("unchecked")
    public List<FlatReportElement> getHoursPerDay(DateRange dateRange) {
        Session session = this.getSession();

        Query query = session.getNamedQuery("Report.getHoursPerDay")
                .setDate("dateStart", dateRange.getDateStart())
                .setDate("dateEnd", dateRange.getDateEnd())
                .setResultTransformer(Transformers.aliasToBean(FlatReportElement.class));

        return query.list();
    }
}
