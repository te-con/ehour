package net.rrm.ehour.persistence.user.dao

import java.util

import net.rrm.ehour.domain.{User, UserDepartment}
import net.rrm.ehour.persistence.dao.AbstractGenericDaoHibernateImpl
import net.rrm.ehour.persistence.retry.ExponentialBackoffRetryPolicy
import org.springframework.stereotype.Repository

@Repository("userDao") class UserDaoHibernateImpl extends AbstractGenericDaoHibernateImpl[Integer, User](classOf[User]) with UserDao {
  private final val CacheRegion = Some("query.User")

  override def findByUsername(username: String): User = {
    val operation = () => findByNamedQueryAndNamedParam("User.findByUsername", "username", username, CacheRegion)

    val results = ExponentialBackoffRetryPolicy retry operation

    if (results.size > 0) results.get(0) else null
  }

  override def findUsers(onlyActive: Boolean): util.List[User] = {
    if (onlyActive)
      () => findByNamedQuery("User.findActiveUsers", CacheRegion)
    else
      findAll
  }

  override def findActiveUsers(): util.List[User] = findUsers(onlyActive = true)

  override def findUsersForDepartments(departments: util.List[UserDepartment], onlyActive: Boolean): util.List[User] = {
    val hql = if (onlyActive) "User.findActiveForDepartment" else "User.findForDepartment"
    () => findByNamedQueryAndNamedParam(hql, "departments", departments, CacheRegion)
  }

  override def findAllActiveUsersWithEmailSet(): util.List[User] = () => findByNamedQuery("User.findAllActiveUsersWithEmailSet", CacheRegion)

  override def deletePmWithoutProject() {
    () => getSession.getNamedQuery("User.deletePMsWithoutProject").executeUpdate
  }
}