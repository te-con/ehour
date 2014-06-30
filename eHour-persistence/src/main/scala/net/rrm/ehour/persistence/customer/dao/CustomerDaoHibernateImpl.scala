package net.rrm.ehour.persistence.customer.dao

import java.util

import net.rrm.ehour.domain.Customer
import net.rrm.ehour.persistence.dao.AbstractGenericDaoHibernateImpl
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

/**
 * Customer DAO
 */
@Repository("customerDao")
@Transactional(readOnly = true)
class CustomerDaoHibernateImpl extends AbstractGenericDaoHibernateImpl[Integer, Customer](classOf[Customer]) with CustomerDao {
  private final val CacheRegion = Some("query.Customer")

  override def findOnNameAndCode(name: String, code: String): Customer = {
    val keys = List("name", "code")
    val params = List(name.toLowerCase, code.toLowerCase)

    val results = findByNamedQuery("Customer.findByNameAndCode", keys, params, CacheRegion)

    if (results != null && results.size > 0) results.get(0) else null
  }

  override def findAllActive(): util.List[Customer] = findByNamedQuery("Customer.findAllWithActive", "active", java.lang.Boolean.TRUE, CacheRegion)
}


