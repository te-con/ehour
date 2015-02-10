package net.rrm.ehour.report.reports.element

import java.util.{Calendar, GregorianCalendar}

import net.rrm.ehour.domain.{ProjectAssignmentType, ProjectAssignment}
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class AssignmentAggregateReportElementTest extends FunSuite {
  test("should initialize") {
    val element = new AssignmentAggregateReportElement()
    !element.getProgressPercentage.isPresent
    null == element.getAvailableHours
    0 == element.getTurnOver.floatValue()
  }

  test("should get 50% progress for allotted fixed") {
    val assignment = new ProjectAssignment()
    assignment.setAssignmentType(ProjectAssignmentType.newAssignmentTimeAllottedFixed())
    assignment.setAllottedHours(20f)

    assert(50 == new AssignmentAggregateReportElement(assignment, 10).getProgressPercentage.get())
  }

  test("should get 50% progress for allotted flex") {
    val assignment = new ProjectAssignment()
    assignment.setAssignmentType(ProjectAssignmentType.newAssignmentTimeAllottedFlex())
    assignment.setAllottedHours(20f)
    assignment.setAllowedOverrun(10f)

    val e = new AssignmentAggregateReportElement(assignment, 10)
    assert(50 == e.getProgressPercentage.get)
  }

  test("should calculate available hours for allotted flex") {
    val assignment = new ProjectAssignment()
    assignment.setAssignmentType(ProjectAssignmentType.newAssignmentTimeAllottedFlex())
    assignment.setAllottedHours(20f)
    assignment.setAllowedOverrun(10f)

    val e = new AssignmentAggregateReportElement(assignment, 10)
    assert(20 == e.getAvailableHours.get().toInt)
  }

  test("should calculate available hours for allotted fixed") {
    val assignment = new ProjectAssignment()
    assignment.setAssignmentType(ProjectAssignmentType.newAssignmentTimeAllottedFixed())
    assignment.setAllottedHours(20f)

    assert(10 == new AssignmentAggregateReportElement(assignment, 10).getAvailableHours.get.toInt)
  }

  test("should get 50% progress for date assignment when we're in the middle") {
    val assignment = new ProjectAssignment()
    assignment.setAssignmentType(ProjectAssignmentType.newAssignmentDate())

    val start = new GregorianCalendar()
    start.add(Calendar.DAY_OF_YEAR, -1)
    assignment.setDateStart(start.getTime)

    val end= new GregorianCalendar()
    end.add(Calendar.DAY_OF_YEAR, 1)
    assignment.setDateEnd(end.getTime)

    val e = new AssignmentAggregateReportElement(assignment, 10)
    assert(50 == e.getProgressPercentage.get)
  }

  test("should get turnover") {
    val assignment = new ProjectAssignment()
    assignment.setHourlyRate(15f)

    val e = new AssignmentAggregateReportElement(assignment, 10)

    assert(150 == e.getTurnOver.intValue())
  }
}
