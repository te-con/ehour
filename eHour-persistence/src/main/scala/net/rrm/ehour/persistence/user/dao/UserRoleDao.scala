package net.rrm.ehour.persistence.user.dao

import net.rrm.ehour.domain.UserRole
import net.rrm.ehour.persistence.dao.{AbstractGenericDaoHibernateImpl, GenericDao}
import org.springframework.stereotype.Repository

/**
 * CRUD on UserRole domain object
 */
trait UserRoleDao extends GenericDao[String, UserRole]

@Repository("userRoleDao")
class UserRoleDaoHibernateImpl extends AbstractGenericDaoHibernateImpl[String, UserRole](classOf[UserRole]) with UserRoleDao

