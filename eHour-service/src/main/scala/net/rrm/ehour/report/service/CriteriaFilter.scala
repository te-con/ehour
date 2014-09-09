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

    def filterProjectsOnBillable(xs: List[Project]): List[Project] =
      if (userSelectedCriteria.isOnlyBillableProjects) xs.filter(ProjectPredicate.billablePredicate) else xs

    def filterProjectsOnPm(xs: List[Project]): List[Project] =
      if (userSelectedCriteria.isForPm) xs.filter(ProjectPredicate.pmPredicate(userSelectedCriteria.getPm)) else xs

    def filterProjectsOnActive(xs: List[Project]): List[Project] =
      if (userSelectedCriteria.isOnlyActiveProjects) xs.filter(ProjectPredicate.activePredicate) else xs

    def retrieveProjectsFromCustomers(customers: List[Customer]): List[Project] = {
      val allProjects = customers.flatMap(c => toScala(c.getProjects))
      filterProjectsOnBillable(filterProjectsOnPm(filterProjectsOnActive(allProjects)))
    }

    def discardInvalidCustomerFilter(customers: List[Customer]) {
      if (isWithCustomerFilter && applyCustomerFilter(customers).isEmpty) {
        userSelectedCriteria.getCustomers.clear()
      }
    }

    def isWithCustomerFilter = !(userSelectedCriteria.getCustomers == null || userSelectedCriteria.getCustomers.isEmpty)

    def applyCustomerFilter(customers: List[Customer]): List[Customer] = {
      if (isWithCustomerFilter)
        customers.filter(userSelectedCriteria.getCustomers.contains)
      else
        customers
    }

    def extractCustomers(xs: List[Project]): List[Customer] = xs.map(_.getCustomer).toSet.toList

    val allCustomers = fetchCustomers(userSelectedCriteria)

    val allProjects = retrieveProjectsFromCustomers(allCustomers)
    val allAvailableCustomers = extractCustomers(allProjects)

    discardInvalidCustomerFilter(allAvailableCustomers)

    val filteredCustomers = applyCustomerFilter(allCustomers)
    val filteredProjects = retrieveProjectsFromCustomers(filteredCustomers)

    (toJava(allAvailableCustomers), toJava(filteredProjects))
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
    val users = toScala(if (userSelectedCriteria.isOnlyActiveUsers)
      userDao.findActiveUsers()
    else
      userDao.findAll())

    val filteredUsers = if (userSelectedCriteria.isForPm) {
      users.filter(u => {
        val i = WrapAsScala.asScalaIterator(u.getProjectAssignments.iterator())
        !i.filter(p => userSelectedCriteria.getPm.equals(p.getProject.getProjectManager)).isEmpty
      })
    }
    else
      users

    val departments = filteredUsers.map(_.getUserDepartment).toSet.toList

    val xs = if (userSelectedCriteria.isEmptyDepartments)
      filteredUsers
    else
     filteredUsers.filter(u => userSelectedCriteria.getDepartments.contains(u.getUserDepartment))

    (toJava(departments), toJava(xs))
  }
}
