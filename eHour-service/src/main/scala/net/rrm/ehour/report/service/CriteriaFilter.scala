package net.rrm.ehour.report.service

import net.rrm.ehour.domain.{User, Project, Customer}
import net.rrm.ehour.report.criteria.UserSelectedCriteria
import org.springframework.beans.factory.annotation.Autowired
import net.rrm.ehour.persistence.customer.dao.CustomerDao
import java.{util => ju}
import org.springframework.stereotype.Service
import net.rrm.ehour.persistence.project.dao.ProjectDao
import net.rrm.ehour.persistence.user.dao.UserDao
import scala.collection.convert.WrapAsScala


@Service
class CustomerCriteriaFilter @Autowired()(customerDao: CustomerDao) {
  def getAvailableCustomers(userSelectedCriteria: UserSelectedCriteria): ju.List[Customer] = {
    def fetchCustomers(userSelectedCriteria: UserSelectedCriteria): List[Customer] =
      if (userSelectedCriteria.isOnlyActiveCustomers) customerDao.findAllActive else customerDao.findAll

    def filterBillable(xs: List[Project]): List[Project] =
      if (userSelectedCriteria.isOnlyBillableProjects) xs.filter(ProjectPredicate.billablePredicate) else xs

    def filterPm(xs: List[Project]): List[Project] =
      if (userSelectedCriteria.isForPm) xs.filter(ProjectPredicate.pmPredicate(userSelectedCriteria.getPm)) else xs

    def filterActive(xs: List[Project]): List[Project] =
      if (userSelectedCriteria.isOnlyActiveProjects) xs.filter(ProjectPredicate.activePredicate) else xs

    val customers = fetchCustomers(userSelectedCriteria)
    val projects = toScala(customers).flatMap(c => toScala(c.getProjects))
    filterActive(filterPm(filterBillable(projects))).map(_.getCustomer)
  }
}

@Service
class ProjectCriteriaFilter @Autowired()(projectDao: ProjectDao) {
  def getAvailableProjects(userSelectedCriteria: UserSelectedCriteria): ju.List[Project] = {
    val projects = fetchProjects(userSelectedCriteria)

    val filteredProjects = if (userSelectedCriteria.isOnlyBillableProjects) projects.filter(ProjectPredicate.billablePredicate) else projects
    if (userSelectedCriteria.isForPm) filteredProjects.filter(ProjectPredicate.pmPredicate(userSelectedCriteria.getPm)) else filteredProjects
  }

  private def fetchProjects(userSelectedCriteria: UserSelectedCriteria): List[Project] =
    if (!userSelectedCriteria.isEmptyCustomers)
      projectDao.findProjectForCustomers(userSelectedCriteria.getCustomers, userSelectedCriteria.isOnlyActiveProjects)
    else if (userSelectedCriteria.isOnlyActiveProjects)
      projectDao.findAllActive
    else
      projectDao.findAll
}

@Service
class UserCriteriaFilter @Autowired()(userDao: UserDao) {
  def getAvailableUsers(userSelectedCriteria: UserSelectedCriteria): ju.Set[User] = {
    val users = (if (userSelectedCriteria.isEmptyDepartments)
      userDao.findUsers(userSelectedCriteria.isOnlyActiveUsers)
    else
      userDao.findUsersForDepartments(userSelectedCriteria.getUserFilter, userSelectedCriteria.getDepartments, userSelectedCriteria.isOnlyActiveUsers)).toSet

    if (userSelectedCriteria.isForPm) {
      users.filter(u => {
        val i = WrapAsScala.asScalaIterator(u.getProjectAssignments.iterator())
        !i.filter(p => p.getProject.getProjectManager.equals(userSelectedCriteria.getPm)).isEmpty
      })
    }
    else
      users
  }
}

private object ProjectPredicate {
  type ProjectPredicate = (Project) => Boolean

  val activePredicate: ProjectPredicate = (p: Project) => p.isActive

  val billablePredicate: ProjectPredicate = (p: Project) => p.isBillable

  def pmPredicate(pm: User): ProjectPredicate = (p: Project) => pm.equals(p.getProjectManager)

  def filter(customers: List[Customer], predicate: ProjectPredicate): List[Customer] = {
    val projects = customers.flatMap(c => WrapAsScala.asScalaSet(c.getProjects))
    projects.filter(predicate)

    val filteredProjects = projects.filter(predicate)

    filteredProjects.map(p => p.getCustomer)
  }
}
