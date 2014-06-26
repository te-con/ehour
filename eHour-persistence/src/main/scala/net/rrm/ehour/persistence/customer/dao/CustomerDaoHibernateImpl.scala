package net.rrm.ehour.persistence.customer.dao

import java.util

import net.rrm.ehour.domain.Customer
import net.rrm.ehour.persistence.dao.AbstractGenericDaoHibernateScalaImpl
import net.rrm.ehour.persistence.retry.ExponentialBackoffRetryPolicy
import org.springframework.stereotype.Repository

/**
 * Customer DAO
 */
object CustomerDaoHibernateImpl {
  private final val CacheRegion = Some("query.Customer")
}

@Repository("customerDao")
class CustomerDaoHibernateImpl extends AbstractGenericDaoHibernateScalaImpl[Integer, Customer](classOf[Customer]) with CustomerDao {
  override def findOnNameAndCode(name: String, code: String): Customer = {
    val keys = List("name", "code")
    val params = List(name.toLowerCase, code.toLowerCase)

    def op = () => findByNamedQueryAndNamedParametersOrCache("Customer.findByNameAndCode", keys, params, CustomerDaoHibernateImpl.CacheRegion)

    val results = ExponentialBackoffRetryPolicy.retry(op)

    if (results != null && results.size > 0) results.get(0) else null
  }

  override def findAllActive: util.List[Customer] = {
    def op = () => findByNamedQueryAndNamedParam("Customer.findAllWithActive", "active", "true", CustomerDaoHibernateImpl.CacheRegion)
    ExponentialBackoffRetryPolicy.retry(op)
  }
}


