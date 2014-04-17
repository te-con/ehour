package net.rrm.ehour.report.reports

import element.{AssignmentAggregateReportElementMother, AssignmentAggregateReportElement}
import net.rrm.ehour.domain._
import net.rrm.ehour.data.DateRange
import net.rrm.ehour.util.DateUtil
import java.util.Calendar
import scala.collection.convert.WrapAsJava
import scala.collection.mutable
import com.google.common.collect.Lists
import net.rrm.ehour.report.criteria.UserSelectedCriteria

object AggregateReportDataObjectMother {
  def generateReportData: ReportData = {
    val customerA = new Customer(1).setName("Customer")
    val customerB = new Customer(2).setName("TECON")
    val customerC = new Customer(3).setName("CECON")
    val customerD = new Customer(4).setName("DECON")

    val projectA = new Project(1, customerA).setName("Project A")
    val projectB = new Project(2, customerA).setName("Project B")
    val projectC = new Project(3, customerB).setName("Project C")
    val projectD = new Project(4, customerC).setName("Project D")
    val projectE = new Project(5, customerD).setName("Project E")

    val userR = new User(1, "Rosalie", "Edeling")
    val userT = new User(2, "Thies", "Edeling")

    val reportElementA = new AssignmentAggregateReportElement(new ProjectAssignment(userR, projectA, 10), 14)
    val reportElementB = new AssignmentAggregateReportElement(new ProjectAssignment(userR, projectB, 25), 8)
    val reportElementC = new AssignmentAggregateReportElement(new ProjectAssignment(userT, projectC, 35), 10)
    val reportElementD = new AssignmentAggregateReportElement(new ProjectAssignment(userT, projectD, 5), 12)
    val reportElementE = new AssignmentAggregateReportElement(new ProjectAssignment(userT, projectE, 35), 10)
    val reportElementF = new AssignmentAggregateReportElement(new ProjectAssignment(userT, projectB, 35), 10)

    val elements = mutable.Buffer(reportElementA, reportElementB, reportElementC, reportElementD, reportElementE, reportElementF)
    new ReportData(Lists.newArrayList(), WrapAsJava.bufferAsJavaList(elements), DateUtil.getDateRangeForMonth(Calendar.getInstance()), new UserSelectedCriteria())
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

    WrapAsJava.bufferAsJavaList(mutable.Buffer(pagE, pagD, pagB, pagC, pagA, pagF))
  }

  def getAssignmentReportData: ReportData = new ReportData(Lists.newArrayList(), getAssignmentAggregateReportElements, new DateRange, new UserSelectedCriteria())
}