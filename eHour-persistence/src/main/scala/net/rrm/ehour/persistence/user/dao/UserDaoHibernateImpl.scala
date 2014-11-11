package net.rrm.ehour.persistence.user.dao

import java.util

import com.google.common.collect.Lists
import net.rrm.ehour.data.LegacyUserDepartment
import net.rrm.ehour.domain.User
import net.rrm.ehour.persistence.dao.AbstractGenericDaoHibernateImpl
import net.rrm.ehour.persistence.retry.ExponentialBackoffRetryPolicy
import org.hibernate.exception.SQLGrammarException
import org.hibernate.transform.Transformers
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
  override def findAllActiveUsersWithEmailSet(): util.List[User] = findByNamedQuery("User.findAllActiveUsersWithEmailSet", CacheRegion)

  @Transactional
  override def deletePmWithoutProject() {
    ExponentialBackoffRetryPolicy retry getSession.getNamedQuery("User.deletePMsWithoutProject").executeUpdate
  }

  @Transactional
  override def findLegacyUserDepartments(): util.List[LegacyUserDepartment] = {
    try {
      getSession.createSQLQuery("SELECT USER_ID as userId, DEPARTMENT_ID as departmentId FROM USERS WHERE DEPARTMENT_ID IS NOT NULL")
        .addScalar("userId")
        .addScalar("departmentId")
        .setResultTransformer(Transformers.aliasToBean(classOf[LegacyUserDepartment]))
        .list()
        .asInstanceOf[util.List[LegacyUserDepartment]]
    } catch {
      case grammar: SQLGrammarException => Lists.newArrayList()
    }
  }
}