package net.rrm.ehour.report.service

import net.rrm.ehour.domain.{UserDepartment, User, Project, Customer}
import net.rrm.ehour.report.criteria.UserSelectedCriteria
import org.springframework.beans.factory.annotation.Autowired
import net.rrm.ehour.persistence.customer.dao.CustomerDao
import java.{util => ju}
import org.springframework.stereotype.Service
import net.rrm.ehour.persistence.user.dao.UserDao
import scala.collection.convert.WrapAsScala
import net.rrm.ehour.util._

@Service
class CustomerAndProjectCriteriaFilter @Autowired()(customerDao: CustomerDao) {
  def getAvailableCustomers(userSelectedCriteria: UserSelectedCriteria): (ju.List[Customer], ju.List[Project]) = {
    def fetchCustomers(userSelectedCriteria: UserSelectedCriteria): List[Customer] =
      toScala(if (userSelectedCriteria.isOnlyActiveCustomers) customerDao.findAllActive else customerDao.findAll)

    def filterBillable(xs: List[Project]): List[Project] =
      if (userSelectedCriteria.isOnlyBillableProjects) xs.filter(ProjectPredicate.billablePredicate) else xs

    def filterPm(xs: List[Project]): List[Project] =
      if (userSelectedCriteria.isForPm) xs.filter(ProjectPredicate.pmPredicate(userSelectedCriteria.getPm)) else xs

    def filterActive(xs: List[Project]): List[Project] =
      if (userSelectedCriteria.isOnlyActiveProjects) xs.filter(ProjectPredicate.activePredicate) else xs

    val customers = fetchCustomers(userSelectedCriteria)

    val filteredCustomers = if (userSelectedCriteria.getCustomers == null || userSelectedCriteria.getCustomers.isEmpty)
                              customers
                            else
                              customers.filter(userSelectedCriteria.getCustomers.contains)

    val projects = filteredCustomers.flatMap(c => toScala(c.getProjects))
    val billables = filterBillable(projects)
    val pm = filterPm(billables)
    val activeProjects = filterActive(pm).toSet.toList

    if (userSelectedCriteria.isForPm) {
      val customersForProjects = activeProjects.map(_.getCustomer).toSet.toList

      (toJava(customersForProjects), toJava(activeProjects))

    } else {
      (toJava(customers), toJava(activeProjects))
    }
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

@Service
class UserAndDepartmentCriteriaFilter @Autowired()(userDao: UserDao) {
  def getAvailableUsers(userSelectedCriteria: UserSelectedCriteria): (ju.List[UserDepartment], ju.List[User]) = {
    val users = toScala(if (userSelectedCriteria.isEmptyDepartments)
      userDao.findUsers(userSelectedCriteria.isOnlyActiveUsers)
    else
      userDao.findUsersForDepartments(userSelectedCriteria.getDepartments, userSelectedCriteria.isOnlyActiveUsers))

    val filteredUsers = if (userSelectedCriteria.isForPm) {
      users.filter(u => {
        val i = WrapAsScala.asScalaIterator(u.getProjectAssignments.iterator())
        !i.filter(p => userSelectedCriteria.getPm.equals(p.getProject.getProjectManager)).isEmpty
      })
    }
    else
      users

    val departments = filteredUsers.map(_.getUserDepartment).toSet.toList

    (toJava(departments), toJava(filteredUsers))
  }
}
