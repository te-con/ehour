package net.rrm.ehour.persistence.user.dao

import net.rrm.ehour.domain.UserDepartment
import net.rrm.ehour.persistence.dao.AbstractGenericDaoHibernateImpl
import net.rrm.ehour.persistence.retry.ExponentialBackoffRetryPolicy
import org.springframework.stereotype.Repository

@Repository("userDepartmentDao")
class UserDepartmentDaoHibernateImpl extends AbstractGenericDaoHibernateImpl[Integer, UserDepartment](classOf[UserDepartment]) with UserDepartmentDao {
  private final val CacheRegion = Some("query.Department")

  override def findOnNameAndCode(name: String, code: String): UserDepartment = {
    val keys = List("name", "code")
    val params = List(name.toLowerCase, code.toLowerCase)
    val results = ExponentialBackoffRetryPolicy retry findByNamedQuery("UserDepartment.findByNameAndCode", keys, params, CacheRegion)

    if (results.size > 0) results.get(0) else null
  }
}
