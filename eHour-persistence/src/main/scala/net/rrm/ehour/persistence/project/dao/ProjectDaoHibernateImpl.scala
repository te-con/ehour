package net.rrm.ehour.persistence.project.dao

import java.util

import net.rrm.ehour.domain.{Customer, Project, User}
import net.rrm.ehour.persistence.dao.AbstractGenericDaoHibernateImpl
import org.hibernate.FetchMode
import org.hibernate.criterion.Restrictions
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository("projectDao")
class ProjectDaoHibernateImpl extends AbstractGenericDaoHibernateImpl[Integer, Project](classOf[Project]) with ProjectDao {
  private final val CacheRegion = Some("query.Project")

  @Transactional(readOnly = true)
  override def findAllActive(): util.List[Project] = () => findByNamedQuery("Project.findAllActive", CacheRegion)

  @Transactional(readOnly = true)
  override def findDefaultProjects(): util.List[Project] = () => findByNamedQuery("Project.findAllActiveDefault", CacheRegion)

  @Transactional(readOnly = true)
  override def findProjectForCustomers(customers: util.List[Customer], onlyActive: Boolean): util.List[Project] = {
    val hqlName = if (onlyActive) "Project.findActiveProjectsForCustomers" else "Project.findAllProjectsForCustomers"
    () => findByNamedQueryAndNamedParam(hqlName, "customers", customers, CacheRegion)
  }

  @Transactional(readOnly = true)
  override def findActiveProjectsWhereUserIsPM(user: User): util.List[Project] = () => findByNamedQueryAndNamedParam("Project.findActiveProjectsWhereUserIsPM", "user", user, CacheRegion)

  @Transactional(readOnly = true)
  override def findAllProjectsWithPmSet(): util.List[Project] = {
    val criteria = getSession.createCriteria(classOf[Project])
    criteria.add(Restrictions.isNotNull("projectManager"))
    criteria.setFetchMode("projectManager.userRoles", FetchMode.JOIN)
    () => criteria.list.asInstanceOf[util.List[Project]]
  }
}


