package net.rrm.ehour.persistence.user.dao

import java.util

import net.rrm.ehour.domain.{User, UserDepartment}
import net.rrm.ehour.persistence.dao.AbstractGenericDaoHibernateImpl
import net.rrm.ehour.persistence.retry.ExponentialBackoffRetryPolicy
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository("userDao")
class UserDaoHibernateImpl extends AbstractGenericDaoHibernateImpl[Integer, User](classOf[User]) with UserDao {
  private final val CacheRegion = Some("query.User")

  @Transactional(readOnly = true)
  override def findByUsername(username: String): User = {
    val results = ExponentialBackoffRetryPolicy retry findByNamedQuery("User.findByUsername", "username", username, CacheRegion)

    if (results.size > 0) results.get(0) else null
  }

  @Transactional(readOnly = true)
  override def findUsers(onlyActive: Boolean): util.List[User] = {
    if (onlyActive)
      findByNamedQuery("User.findActiveUsers", CacheRegion)
    else
      findAll()
  }

  @Transactional(readOnly = true)
  override def findActiveUsers(): util.List[User] = findUsers(onlyActive = true)

  @Transactional(readOnly = true)
  override def findUsersForDepartments(departments: util.List[UserDepartment], onlyActive: Boolean): util.List[User] = {
    val hql = if (onlyActive) "User.findActiveForDepartment" else "User.findForDepartment"
    findByNamedQuery(hql, "departments", departments, CacheRegion)
  }

  @Transactional(readOnly = true)
  override def findAllActiveUsersWithEmailSet(): util.List[User] = findByNamedQuery("User.findAllActiveUsersWithEmailSet", CacheRegion)

  @Transactional
  override def deletePmWithoutProject() {
    ExponentialBackoffRetryPolicy retry getSession.getNamedQuery("User.deletePMsWithoutProject").executeUpdate
  }
}