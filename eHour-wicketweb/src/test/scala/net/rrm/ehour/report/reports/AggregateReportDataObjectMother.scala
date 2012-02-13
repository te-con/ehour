package net.rrm.ehour.report.reports

import element.{AssignmentAggregateReportElementMother, AssignmentAggregateReportElement}
import net.rrm.ehour.report.criteria.{ReportCriteria, AvailableCriteria}
import scalaj.collection.Imports._
import net.rrm.ehour.domain._
import net.rrm.ehour.data.DateRange

object AggregateReportDataObjectMother {
  def generateReportData:ReportData = {
    val availCriteria: AvailableCriteria = new AvailableCriteria

    val customers = List(new Customer(1))
    availCriteria.setCustomers(customers.asJava)
    val projects = List(new Project(2))
    availCriteria.setProjects(projects.asJava)

    val depts = List(new UserDepartment(2))
    availCriteria.setUserDepartments(depts.asJava)
    val users = List(new User(2, "Thies", "Edeling"))
    availCriteria.setUsers(users.asJava)

    val reportCriteria = new ReportCriteria(availCriteria)

    val projectA: Project = new Project(1, new Customer(1).setName("Customer")).setName("Project A")
    val assignmentA = new ProjectAssignment(new User(1, "Rosalie", "Edeling"), projectA)
    assignmentA.setHourlyRate(10)
    val reportElementA = new AssignmentAggregateReportElement(assignmentA, 14)

    val projectB: Project = new Project(1, new Customer(1).setName("Customer")).setName("Project B")
    val assignmentB = new ProjectAssignment(new User(1, "Rosalie", "Edeling"), projectB)
    assignmentB.setHourlyRate(25)
    val reportElementB = new AssignmentAggregateReportElement(assignmentB, 8)

    new ReportData(List(reportElementA, reportElementB).asJava, reportCriteria.getReportRange)
  }

  def getAssignmentAggregateReportElements = {
    val pagA = AssignmentAggregateReportElementMother.createProjectAssignmentAggregate(1, 1, 1)
    pagA.getProjectAssignment.getProject.setProjectId(1)

    val pagB = AssignmentAggregateReportElementMother.createProjectAssignmentAggregate(20, 1, 2)
    pagB.getProjectAssignment.getProject.setProjectId(2)

    val pagC = AssignmentAggregateReportElementMother.createProjectAssignmentAggregate(3, 2, 1)
    val pagD = AssignmentAggregateReportElementMother.createProjectAssignmentAggregate(4, 2, 2)
    val pagE = AssignmentAggregateReportElementMother.createProjectAssignmentAggregate(5, 3, 3)

    val pagF = AssignmentAggregateReportElementMother.createProjectAssignmentAggregate(30, 1, 3)
    pagF.getProjectAssignment.getProject.setProjectId(2)

    List(pagE, pagD, pagB, pagC, pagA, pagF).asJava
  }

  def getAssignmentReportData: ReportData = new ReportData(getAssignmentAggregateReportElements, new DateRange)
}