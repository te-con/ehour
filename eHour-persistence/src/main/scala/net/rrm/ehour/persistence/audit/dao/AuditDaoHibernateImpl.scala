package net.rrm.ehour.persistence.audit.dao

import java.util

import net.rrm.ehour.data.{AuditReportRequest, DateRange}
import net.rrm.ehour.domain.Audit
import net.rrm.ehour.persistence.dao.AbstractGenericDaoHibernateImpl
import net.rrm.ehour.persistence.retry.ExponentialBackoffRetryPolicy
import org.apache.commons.lang.StringUtils
import org.hibernate.Criteria
import org.hibernate.criterion.{Order, Projections, Restrictions}
import org.springframework.stereotype.Repository

@Repository("auditDao")
class AuditDaoHibernateImpl extends AbstractGenericDaoHibernateImpl[Number, Audit](classOf[Audit]) with AuditDao {

  override def findAudits(request: AuditReportRequest): util.List[Audit] = {
    val criteria = buildCriteria(request)
    criteria.addOrder(Order.asc("date"))

    ExponentialBackoffRetryPolicy.retry(criteria.list.asInstanceOf[util.List[Audit]])
  }

  override def findAudits(request: AuditReportRequest, offset: Int, max: Int): util.List[Audit] = {
    val criteria: Criteria = buildCriteria(request)
    criteria.setFirstResult(offset)
    criteria.setMaxResults(max)
    criteria.addOrder(Order.asc("date"))

    ExponentialBackoffRetryPolicy.retry(criteria.list.asInstanceOf[util.List[Audit]])
  }

  override def count(request: AuditReportRequest): Number = {
    val criteria = buildCriteria(request)
    criteria.setProjection(Projections.rowCount)

    ExponentialBackoffRetryPolicy.retry(criteria.uniqueResult).asInstanceOf[Number]
  }

  private def buildCriteria(request: AuditReportRequest): Criteria = {
    val criteria = getSession.createCriteria(classOf[Audit])
    if (!StringUtils.isBlank(request.getAction)) {
      criteria.add(Restrictions.like("action", "%" + request.getAction.toLowerCase + "%").ignoreCase)
    }

    if (!StringUtils.isBlank(request.getName)) {
      criteria.add(Restrictions.like("userFullName", "%" + request.getName.toLowerCase + "%").ignoreCase)
    }

    val reportRange: DateRange = request.getReportRange
    if (!request.isInfiniteStartDate && reportRange.getDateStart != null) {
      criteria.add(Restrictions.ge("date", reportRange.getDateStart))
    }

    if (!request.isInfiniteEndDate && reportRange.getDateEnd != null) {
      criteria.add(Restrictions.le("date", reportRange.getDateEnd))
    }

    criteria
  }
}

