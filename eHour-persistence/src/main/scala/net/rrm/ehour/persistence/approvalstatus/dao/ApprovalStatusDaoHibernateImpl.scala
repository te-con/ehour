package net.rrm.ehour.persistence.approvalstatus.dao

import java.util

import net.rrm.ehour.data.DateRange
import net.rrm.ehour.domain.{ApprovalStatus, Customer, User}
import net.rrm.ehour.persistence.dao.AbstractGenericDaoHibernateImpl

class ApprovalStatusDaoHibernateImpl extends AbstractGenericDaoHibernateImpl[Integer, ApprovalStatus](classOf[ApprovalStatus]) with ApprovalStatusDao {
  final val CacheRegion = Some("query.ApprovalStatus")

  override def findApprovalStatusesForUserWorkingForCustomer(user: User, customer: Customer, dateRange: DateRange): util.List[ApprovalStatus] = {
    val keys  = List("user", "customer", "dateStart", "dateEnd")
    val params  = List(user, customer, dateRange.getDateStart, dateRange.getDateEnd)

    findByNamedQuery("ApprovalStatus.findApprovalStatusesForUserWorkingForCustomer", keys, params, CacheRegion)
  }

  override def findApprovalStatusesForCustomers(customers: util.List[Customer], dateRange: DateRange): util.List[ApprovalStatus] = {
    val keys  = List("customers", "dateStart", "dateEnd")
    val params  = List(customers, dateRange.getDateStart, dateRange.getDateEnd)

    findByNamedQuery("ApprovalStatus.findApprovalStatusesForCustomers", keys, params, CacheRegion)
  }
}
