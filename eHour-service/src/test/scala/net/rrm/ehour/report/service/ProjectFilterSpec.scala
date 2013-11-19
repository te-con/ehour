  package net.rrm.ehour.report.service

import org.scalatest.{WordSpec, Matchers}
import net.rrm.ehour.domain.{UserObjectMother, CustomerObjectMother, ProjectObjectMother}

  class ProjectFilterSpec extends WordSpec with Matchers
{
  "Project filter" must {
    val inactiveCustomer = CustomerObjectMother.createCustomer(1)
    val inactiveProject = inactiveCustomer.getProjects.iterator().next
    inactiveProject.setActive(false)
    inactiveProject.setBillable(false)

    val billableCustomer = CustomerObjectMother.createCustomer(2)
    billableCustomer.getProjects.iterator().next.setBillable(true)

    val pm = UserObjectMother.createUser()
    val pmCustomer = CustomerObjectMother.createCustomer(3)
    val pmProject = pmCustomer.getProjects.iterator().next
    pmProject.setProjectManager(pm)
    pmProject.setBillable(false)

    val activeCustomer = CustomerObjectMother.createCustomer(4)
    activeCustomer.getProjects.iterator().next.setBillable(false)

    val inactiveButBillableSecondProject = ProjectObjectMother.createProject(10)
    inactiveButBillableSecondProject.setActive(false)
    inactiveButBillableSecondProject.setBillable(true)
    activeCustomer.addProject(inactiveButBillableSecondProject)

    "filter customers with only active projects" in {
      val customers = List(inactiveCustomer, billableCustomer, pmCustomer)

      val filtered = ProjectFilter.filter(customers, ProjectFilter.activePredicate)

      filtered should have size 2

      val iterator = filtered.iterator
      iterator.next() should be (billableCustomer)
      iterator.next() should be (pmCustomer)
    }

    "filter customers with billable projects" in {
      val customers = List(inactiveCustomer, billableCustomer, pmCustomer, activeCustomer)

      val filtered = ProjectFilter.filter(customers, ProjectFilter.billablePredicate)

      filtered should have size 2

      val iterator = filtered.iterator
      iterator.next() should be (billableCustomer)
      iterator.next() should be (activeCustomer)
    }

    "filter customers with pm projects" in {
      val customers = List(inactiveCustomer, billableCustomer, pmCustomer, activeCustomer)

      val filtered = ProjectFilter.filter(customers, ProjectFilter.pmPredicate(pm))

      filtered should have size 1

      val iterator = filtered.iterator
      iterator.next() should be (pmCustomer)
    }
  }
}
