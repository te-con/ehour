package net.rrm.ehour.report.service

import net.rrm.ehour.domain.{UserDepartmentObjectMother, ProjectObjectMother, UserObjectMother, CustomerObjectMother}


object ReportFilterFixture {
  val inactiveCustomer = CustomerObjectMother.createCustomer(1)
  inactiveCustomer.setActive(false)
  val inactiveProject = inactiveCustomer.getProjects.iterator().next
  inactiveProject.setActive(false)
  inactiveProject.setBillable(false)
  inactiveProject.setProjectId(1)

  val billableCustomer = CustomerObjectMother.createCustomer(2)
  val billableProject = billableCustomer.getProjects.iterator().next
  billableProject.setBillable(true)
  billableProject.setProjectId(2)

  val pm = UserObjectMother.createUser()
  val pmCustomer = CustomerObjectMother.createCustomer(3)
  val pmProject = pmCustomer.getProjects.iterator().next
  pmProject.setProjectManager(pm)
  pmProject.setBillable(false)
  pmProject.setProjectId(3)

  val otherPm = UserObjectMother.createUser()
  otherPm.setUserId(2)
  otherPm.setFirstName("other")
  val otherPmCustomer = CustomerObjectMother.createCustomer(5)
  val otherPmProject = otherPmCustomer.getProjects.iterator().next
  otherPmProject.setProjectManager(otherPm)
  otherPmProject.setBillable(false)
  otherPmProject.setProjectId(4)

  val activeWithInactiveBillableCustomer = CustomerObjectMother.createCustomer(4)
  private val notBillableProject = activeWithInactiveBillableCustomer.getProjects.iterator().next
  notBillableProject.setBillable(false)
  notBillableProject.setProjectId(5)

  val inactiveButBillableSecondProject = ProjectObjectMother.createProject(6, activeWithInactiveBillableCustomer)
  inactiveButBillableSecondProject.setActive(false)
  inactiveButBillableSecondProject.setBillable(true)

  val department = UserDepartmentObjectMother.createUserDepartment()
}
