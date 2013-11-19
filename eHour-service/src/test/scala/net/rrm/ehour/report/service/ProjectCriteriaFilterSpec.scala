package net.rrm.ehour.report.service

import org.scalatest.{BeforeAndAfterEach, Matchers, WordSpec}
import org.scalatest.mock.MockitoSugar
import org.mockito.Mockito._
import net.rrm.ehour.report.service.ReportFilterFixture._
import net.rrm.ehour.report.criteria.UserSelectedCriteria
import net.rrm.ehour.persistence.project.dao.ProjectDao
import net.rrm.ehour.report.criteria.UserSelectedCriteria.ReportType

class ProjectCriteriaFilterSpec extends WordSpec with MockitoSugar with Matchers with BeforeAndAfterEach {
  val dao = mock[ProjectDao]
  val subject = new ProjectCriteriaFilter(dao)

  override protected def beforeEach() = reset(dao)

  "Project Criteria Filter" must {
    "find all projects when no customers are defined" in {
      when(dao.findAll()).thenReturn(List(pmProject))

      val criteria = new UserSelectedCriteria
      criteria.setOnlyActiveProjects(false)
      val projects = subject.getAvailableProjects(criteria)

      verify(dao).findAll()

      projects should have size 1
    }

    "find all active projects when no customers are defined" in {
      when(dao.findAllActive()).thenReturn(List(pmProject))

      val criteria = new UserSelectedCriteria
      criteria.setOnlyActiveProjects(true)

      val projects = subject.getAvailableProjects(criteria)
      projects should have size 1
    }

    "find all projects when customers are defined" in {
      val reqCustomers = List(activeWithInactiveBillableCustomer, pmCustomer)
      when(dao.findProjectForCustomers(reqCustomers, false)).thenReturn(List(pmProject, inactiveProject))

      val criteria = new UserSelectedCriteria
      criteria.setCustomers(reqCustomers)
      criteria.setOnlyActiveProjects(false)
      val projects = subject.getAvailableProjects(criteria)

      projects should have size 2
    }

    "find all active projects when customers are defined" in {
      val reqCustomers = List(activeWithInactiveBillableCustomer, pmCustomer)
      when(dao.findProjectForCustomers(reqCustomers, true)).thenReturn(List(pmProject))

      val criteria = new UserSelectedCriteria
      criteria.setCustomers(reqCustomers)
      criteria.setOnlyActiveProjects(true)
      val projects = subject.getAvailableProjects(criteria)

      projects should have size 1
    }

    "find all active, billable projects" in {
      when(dao.findAllActive()).thenReturn(List(billableProject, pmProject))

      val criteria = new UserSelectedCriteria
      criteria.setOnlyActiveProjects(true)
      criteria.setOnlyBillableProjects(true)

      val projects = subject.getAvailableProjects(criteria)
      projects should have size 1
      projects(0) should be(billableProject)
    }

    "find all projects for a PM" in {
      when(dao.findAllActive()).thenReturn(List(pmProject, otherPmProject))

      val criteria = new UserSelectedCriteria
      criteria.setOnlyActiveProjects(true)
      criteria.addReportType(ReportType.PM)
      criteria.setPm(pm)

      val projects = subject.getAvailableProjects(criteria)
      projects should have size 1
      projects(0) should be(pmProject)
    }
  }
}
