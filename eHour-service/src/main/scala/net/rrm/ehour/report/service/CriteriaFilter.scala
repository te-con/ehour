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
    val customers = fetchCustomers(userSelectedCriteria)

    def filterBillable: List[Customer] =
      if (userSelectedCriteria.isOnlyBillableProjects) ProjectFilter.filter(customers, ProjectFilter.billablePredicate) else customers

    def filterPm(xs: List[Customer]): List[Customer] =
      if (userSelectedCriteria.isForPm) ProjectFilter.filter(xs, ProjectFilter.pmPredicate(userSelectedCriteria.getPm)) else xs

    def filterActive(xs: List[Customer]): List[Customer] =
      if (userSelectedCriteria.isForPm) ProjectFilter.filter(xs, ProjectFilter.activePredicate) else xs

    filterActive(filterPm(filterBillable))
  }

  private def fetchCustomers(userSelectedCriteria: UserSelectedCriteria): List[Customer] =
    if (userSelectedCriteria.isOnlyActiveCustomers) customerDao.findAllActive else customerDao.findAll
}

@Service
class ProjectCriteriaFilter @Autowired()(projectDao: ProjectDao) {
  def getAvailableProjects(userSelectedCriteria: UserSelectedCriteria): ju.List[Project] = {
    val projects = fetchProjects(userSelectedCriteria)

    val filteredProjects = if (userSelectedCriteria.isOnlyBillableProjects) projects.filter(ProjectFilter.billablePredicate) else projects
    if (userSelectedCriteria.isForPm) filteredProjects.filter(ProjectFilter.pmPredicate(userSelectedCriteria.getPm)) else filteredProjects
  }

  private def fetchProjects(userSelectedCriteria: UserSelectedCriteria): List[Project] =
    if (userSelectedCriteria.isEmptyCustomers)
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
