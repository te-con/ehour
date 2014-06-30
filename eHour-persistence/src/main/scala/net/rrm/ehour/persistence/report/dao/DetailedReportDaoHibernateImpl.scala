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
package net.rrm.ehour.persistence.report.dao

import java.util

import net.rrm.ehour.data.DateRange
import net.rrm.ehour.persistence.dao.AbstractAnnotationDaoHibernate4Impl
import net.rrm.ehour.persistence.retry.ExponentialBackoffRetryPolicy
import net.rrm.ehour.report.reports.element.FlatReportElement
import org.hibernate.transform.Transformers
import org.springframework.stereotype.Repository

/**
 * PerMonth DAO impl using sql-queries
 */
@Repository("detailedReportDao")
class DetailedReportDaoHibernateImpl extends AbstractAnnotationDaoHibernate4Impl with DetailedReportDao {
  override def getHoursPerDayForAssignment(assignmentIds: util.List[Integer], dateRange: DateRange): util.List[FlatReportElement] = {
    val session = this.getSession
    val query = session.getNamedQuery("Report.getHoursPerDayForAssignment")
      .setDate("dateStart", dateRange.getDateStart)
      .setDate("dateEnd", dateRange.getDateEnd)
      .setParameterList("assignmentId", assignmentIds)
      .setResultTransformer(Transformers.aliasToBean(classOf[FlatReportElement]))
    ExponentialBackoffRetryPolicy retry query.list.asInstanceOf[util.List[FlatReportElement]]
  }

  override def getHoursPerDayForUsers(userIds: util.List[Integer], dateRange: DateRange): util.List[FlatReportElement] = {
    val session = this.getSession
    val query = session.getNamedQuery("Report.getHoursPerDayForUsers").setDate("dateStart", dateRange.getDateStart).setDate("dateEnd", dateRange.getDateEnd).setParameterList("userIds", userIds).setResultTransformer(Transformers.aliasToBean(classOf[FlatReportElement]))
    ExponentialBackoffRetryPolicy retry query.list.asInstanceOf[util.List[FlatReportElement]]
  }

  override def getHoursPerDayForProjects(projectIds: util.List[Integer], dateRange: DateRange): util.List[FlatReportElement] = {
    val session = this.getSession
    val query = session.getNamedQuery("Report.getHoursPerDayForProjects")
      .setDate("dateStart", dateRange.getDateStart)
      .setDate("dateEnd", dateRange.getDateEnd)
      .setParameterList("projectIds", projectIds)
      .setResultTransformer(Transformers.aliasToBean(classOf[FlatReportElement]))
    ExponentialBackoffRetryPolicy retry query.list.asInstanceOf[util.List[FlatReportElement]]
  }

  override def getHoursPerDayForProjectsAndUsers(projectIds: util.List[Integer], userIds: util.List[Integer], dateRange: DateRange): util.List[FlatReportElement] = {
    val session = this.getSession
    val query = session.getNamedQuery("Report.getHoursPerDayForProjectsAndUsers")
      .setDate("dateStart", dateRange.getDateStart)
      .setDate("dateEnd", dateRange.getDateEnd)
      .setParameterList("projectIds", projectIds)
      .setParameterList("userIds", userIds)
      .setResultTransformer(Transformers.aliasToBean(classOf[FlatReportElement]))
    ExponentialBackoffRetryPolicy retry query.list.asInstanceOf[util.List[FlatReportElement]]
  }

  override def getHoursPerDay(dateRange: DateRange): util.List[FlatReportElement] = {
    val session = this.getSession
    val query = session.getNamedQuery("Report.getHoursPerDay")
      .setDate("dateStart", dateRange.getDateStart)
      .setDate("dateEnd", dateRange.getDateEnd)
      .setResultTransformer(Transformers.aliasToBean(classOf[FlatReportElement]))
    ExponentialBackoffRetryPolicy retry query.list.asInstanceOf[util.List[FlatReportElement]]
  }
}

