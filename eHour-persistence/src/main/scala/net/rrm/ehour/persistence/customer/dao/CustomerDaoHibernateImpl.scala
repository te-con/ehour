package net.rrm.ehour.persistence.customer.dao

import java.util

import net.rrm.ehour.domain.Customer
import net.rrm.ehour.persistence.dao.AbstractGenericDaoHibernateImpl
import net.rrm.ehour.persistence.retry.ExponentialBackoffRetryPolicy
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

/**
 * Customer DAO
 */
@Repository("customerDao")
class CustomerDaoHibernateImpl extends AbstractGenericDaoHibernateImpl[Integer, Customer](classOf[Customer]) with CustomerDao {
  private final val CacheRegion = Some("query.Customer")

  @Transactional(readOnly = true)
  override def findOnNameAndCode(name: String, code: String): Customer = {
    val keys = List("name", "code")
    val params = List(name.toLowerCase, code.toLowerCase)

    val op = () => findByNamedQueryAndNamedParams("Customer.findByNameAndCode", keys, params, CacheRegion)

    val results = ExponentialBackoffRetryPolicy.retry(op)

    if (results != null && results.size > 0) results.get(0) else null
  }

  @Transactional(readOnly = true)
  override def findAllActive(): util.List[Customer] = () => findByNamedQueryAndNamedParam("Customer.findAllWithActive", "active", java.lang.Boolean.TRUE, CacheRegion)
}


