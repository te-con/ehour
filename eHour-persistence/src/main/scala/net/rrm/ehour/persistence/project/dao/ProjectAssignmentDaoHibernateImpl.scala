package net.rrm.ehour.persistence.project.dao

import java.util

import net.rrm.ehour.data.DateRange
import net.rrm.ehour.domain._
import net.rrm.ehour.persistence.dao.AbstractGenericDaoHibernateImpl
import net.rrm.ehour.persistence.retry.ExponentialBackoffRetryPolicy
import org.hibernate.criterion.Restrictions
import org.springframework.stereotype.Repository

/**
 * CRUD stuff on PA do
 */
@Repository("projectAssignmentDao")
class ProjectAssignmentDaoHibernateImpl extends AbstractGenericDaoHibernateImpl[Integer, ProjectAssignment](classOf[ProjectAssignment]) with ProjectAssignmentDao {
  private final val CacheRegion = Some("query.ProjectAssignment")

  override def findProjectAssignmentsForUser(userId: Integer, range: DateRange): util.List[ProjectAssignment] = {
    val keys = List("dateStart", "dateEnd", "userId")
    val params = List(range.getDateStart, range.getDateEnd, userId)
    findByNamedQuery("ProjectAssignment.findProjectAssignmentsForUserInRange", keys, params, CacheRegion)
  }

  override def findProjectAssignmentForUser(projectId: Integer, userId: Integer): util.List[ProjectAssignment] = {
    val keys = List("projectId", "userId")
    val params  = List(projectId, userId)
    findByNamedQuery("ProjectAssignment.findProjectAssignmentsForUserForProject", keys, params, CacheRegion)
  }

  override def findProjectAssignmentsForUser(user: User): util.List[ProjectAssignment] =
    findByNamedQuery("ProjectAssignment.findProjectAssignmentsForUser", "user", user, CacheRegion)

  override def findProjectAssignmentsForProject(project: Project, range: DateRange): util.List[ProjectAssignment] = {
    val keys = List("dateStart", "dateEnd", "project")
    val params = List(range.getDateStart, range.getDateEnd, project)
    findByNamedQuery("ProjectAssignment.findProjectAssignmentsForProjectInRange", keys, params, CacheRegion)
  }

  override def findAllProjectAssignmentsForProject(project: Project): util.List[ProjectAssignment] = findProjectAssignmentsForProject(project, onlyActive = false)

  override def findAllActiveProjectAssignmentsForProject(project: Project): util.List[ProjectAssignment] = findProjectAssignmentsForProject(project, onlyActive = true)

  private def findProjectAssignmentsForProject(project: Project, onlyActive: Boolean): util.List[ProjectAssignment] = {
    val crit = getSession.createCriteria(classOf[ProjectAssignment])

    if (onlyActive) {
      crit.add(Restrictions.eq("active", true))
    }

    crit.add(Restrictions.eq("project", project))

    ExponentialBackoffRetryPolicy retry crit.list.asInstanceOf[util.List[ProjectAssignment]]
  }

  override def findProjectAssignmentTypes(): util.List[ProjectAssignmentType] =
    ExponentialBackoffRetryPolicy retry getSession.createCriteria(classOf[ProjectAssignmentType]).list.asInstanceOf[util.List[ProjectAssignmentType]]

  override def findProjectAssignmentsForCustomer(customer: Customer, range: DateRange): util.List[ProjectAssignment] = {
    val keys = List("dateStart", "dateEnd", "customer")
    val params = List(range.getDateStart, range.getDateEnd, customer)
    findByNamedQuery("ProjectAssignment.findProjectAssignmentsForCustomerInRange", keys, params, CacheRegion)
  }
}
