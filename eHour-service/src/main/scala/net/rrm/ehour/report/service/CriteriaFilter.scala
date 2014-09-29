package net.rrm.ehour.report.service

import java.{util => ju}

import net.rrm.ehour.domain.{Customer, Project, User}
import net.rrm.ehour.persistence.customer.dao.CustomerDao
import net.rrm.ehour.report.criteria.UserSelectedCriteria
import net.rrm.ehour.user.service.UserService
import net.rrm.ehour.util._
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import scala.collection.convert.WrapAsScala

@Service
class CustomerAndProjectCriteriaFilter @Autowired()(customerDao: CustomerDao) {
  def getAvailableCustomers(userSelectedCriteria: UserSelectedCriteria): (ju.List[Customer], ju.List[Project]) = {
    def fetchCustomers(userSelectedCriteria: UserSelectedCriteria): List[Customer] =
      toScala(if (userSelectedCriteria.isOnlyActiveCustomers) customerDao.findAllActive() else customerDao.findAll())

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
class UserAndDepartmentCriteriaFilter @Autowired() (userService: UserService) {
  import scala.collection.JavaConversions._
  def getAvailableUsers(userSelectedCriteria: UserSelectedCriteria): ju.List[User] = {
    val users = new ju.ArrayList[User]

    if (userSelectedCriteria.isCustomerReporter) {

      val allUsersAssignedToCustomers = userService.getAllUsersAssignedToCustomers(userSelectedCriteria.getCustomers, userSelectedCriteria.isOnlyActiveUsers)
      if (allUsersAssignedToCustomers != null && !allUsersAssignedToCustomers.isEmpty) {
        users.addAll(allUsersAssignedToCustomers)
      }

      users
    } else {
      val ldapUsers = userService.getLdapUsers("", true)

      ldapUsers filter (f => f.getUser.isActive) map (_.getUser)
    }
  }
}
