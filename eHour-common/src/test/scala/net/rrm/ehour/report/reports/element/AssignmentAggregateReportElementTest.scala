package net.rrm.ehour.report.reports.element

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import net.rrm.ehour.domain.ProjectAssignment
import net.rrm.ehour.util.EhourConstants
import java.util.{Calendar, GregorianCalendar}

@RunWith(classOf[JUnitRunner])
class AssignmentAggregateReportElementTest extends FunSuite {
  test("should initialize") {
    val element = new AssignmentAggregateReportElement()
    0 == element.getProgressPercentage
    null == element.getAvailableHours
    0 == element.getTurnOver.floatValue()
  }

  test("should get 50% progress for allotted fixed") {
    val assignment = new ProjectAssignment()
    assignment.setAssignmentType(EhourConstants.ASSIGNMENT_TYPE_TIME_ALLOTTED_FIXED)
    assignment.setAllottedHours(20)

    50 == new AssignmentAggregateReportElement(assignment, 10).getProgressPercentage
  }

  test("should get 50% progress for allotted flex") {
    val assignment = new ProjectAssignment()
    assignment.setAssignmentType(EhourConstants.ASSIGNMENT_TYPE_TIME_ALLOTTED_FLEX)
    assignment.setAllottedHours(20)
    assignment.setAllowedOverrun(10)

    val e = new AssignmentAggregateReportElement(assignment, 10)
    50 == e.getProgressPercentage
  }

  test("should calculate available hours for allotted flex") {
    val assignment = new ProjectAssignment()
    assignment.setAssignmentType(EhourConstants.ASSIGNMENT_TYPE_TIME_ALLOTTED_FLEX)
    assignment.setAllottedHours(20)
    assignment.setAllowedOverrun(10)

    val e = new AssignmentAggregateReportElement(assignment, 10)
    20 == e.getAvailableHours.toInt
  }

  test("should calculate available hours for allotted fixed") {
    val assignment = new ProjectAssignment()
    assignment.setAssignmentType(EhourConstants.ASSIGNMENT_TYPE_TIME_ALLOTTED_FIXED)
    assignment.setAllottedHours(20)

    10 == new AssignmentAggregateReportElement(assignment, 10).getAvailableHours.toInt
  }

  test("should get 50% progress for date assignment when we're in the middle") {
    val assignment = new ProjectAssignment()
    assignment.setAssignmentType(EhourConstants.ASSIGNMENT_TYPE_DATE)

    val now = new GregorianCalendar()
    now.add(Calendar.DAY_OF_YEAR, -1)

    assignment.setDateStart(now.getTime)
    now.add(Calendar.DAY_OF_YEAR, 2)
    assignment.setDateEnd(now.getTime)

    val e = new AssignmentAggregateReportElement(assignment, 10)
    50 == e.getProgressPercentage
  }

  test("should get turnover") {
    val assignment = new ProjectAssignment()
    assignment.setHourlyRate(15)

    val e = new AssignmentAggregateReportElement(assignment, 10)

    150 == e.getTurnOver.intValue()
  }
}
