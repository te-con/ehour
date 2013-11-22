package net.rrm.ehour.report.service

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfterEach, WordSpec, Matchers}
import org.scalatest.mock.MockitoSugar
import net.rrm.ehour.persistence.customer.dao.CustomerDao
import org.mockito.Mockito._
import net.rrm.ehour.report.criteria.UserSelectedCriteria
import ReportFilterFixture._
import net.rrm.ehour.report.criteria.UserSelectedCriteria.ReportType
import net.rrm.ehour.util._

@RunWith(classOf[JUnitRunner])
class CustomerCriteriaFilterSpec extends WordSpec with MockitoSugar with Matchers with BeforeAndAfterEach {
  val dao = mock[CustomerDao]
  val subject = new CustomerAndProjectCriteriaFilter(dao)

  override protected def beforeEach() = reset(dao)

  "Customer Criteria Filter" must {
    "find all customers" in {
      when(dao.findAll()).thenReturn(toJava(List(billableCustomer)))

      val criteria = new UserSelectedCriteria
      criteria.setOnlyActiveCustomers(false)
      val (customers, projects) = subject.getAvailableCustomers(criteria)

      customers should have size 1
    }

    "find only active customers" in {
      when(dao.findAllActive()).thenReturn(toJava(List(activeWithInactiveBillableCustomer, inactiveCustomer, billableCustomer)))

      val (customers, projects) = subject.getAvailableCustomers(new UserSelectedCriteria)

      customers should have size 2
    }

    "find only billable customers" in {
      when(dao.findAllActive()).thenReturn(toJava(List(activeWithInactiveBillableCustomer)))

      val criteria = new UserSelectedCriteria
      criteria.setOnlyBillableProjects(true)
      criteria.setOnlyActiveProjects(true)
      val (customers, projects) = subject.getAvailableCustomers(criteria)

      customers should be ('empty)
    }

    "find only customers which have this PM" in {
      when(dao.findAllActive()).thenReturn(toJava(List(billableCustomer, pmCustomer, otherPmCustomer)))

      val criteria = new UserSelectedCriteria
      criteria.addReportType(ReportType.PM)
      criteria.setPm(pm)
      val (customers, projects) = subject.getAvailableCustomers(criteria)

      customers should have size 1
      customers.get(0) should be (pmCustomer)
    }
  }
}
