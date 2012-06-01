package net.rrm.ehour.report.reports.element

import net.rrm.ehour.domain.ProjectAssignment
import net.rrm.ehour.util.EhourConstants
import org.junit.Test
import static org.junit.Assert.assertEquals

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 * Created on: 12/7/10 - 12:35 AM
 */
class AssignmentAggregateReportElementTest
{

  @Test
  void shouldTestOnNull()
  {
    def element = new AssignmentAggregateReportElement()
    assertEquals 0, element.getProgressPercentage(), 0.01
    assertEquals null, element.getAvailableHours()
    assertEquals 0, element.getTurnOver().floatValue(), 0.01
  }

  @Test
  void shouldGetPercentageOf50ForAllottedFixed()
  {
    def assignment = new ProjectAssignment(assignmentType: EhourConstants.ASSIGNMENT_TYPE_TIME_ALLOTTED_FIXED, allottedHours: 20)
    def element = new AssignmentAggregateReportElement(projectAssignment: assignment, hours: 10)

    assertEquals 50, element.getProgressPercentage(), 0.01
  }

  @Test
  void shouldGetPercentageOf50ForAllottedFlex()
  {
    def assignment = new ProjectAssignment(assignmentType: EhourConstants.ASSIGNMENT_TYPE_TIME_ALLOTTED_FLEX, allottedHours: 20, allowedOverrun: 10)
    def element = new AssignmentAggregateReportElement(projectAssignment: assignment, hours: 10)

    assertEquals 50, element.getProgressPercentage(), 0.01
    assertEquals 20, element.getAvailableHours(), 0.01
  }

  @Test
  void shouldGetAvailableHoursForAllottedFixed()
  {
    def assignment = new ProjectAssignment(assignmentType: EhourConstants.ASSIGNMENT_TYPE_TIME_ALLOTTED_FIXED, allottedHours: 20)
    def element = new AssignmentAggregateReportElement(projectAssignment: assignment, hours: 10)

    assertEquals 10, element.getAvailableHours(), 0.01
  }

  @Test
  void shouldGetAvailableHoursForAllottedFlex()
  {
    def assignment = new ProjectAssignment(assignmentType: EhourConstants.ASSIGNMENT_TYPE_TIME_ALLOTTED_FLEX, allottedHours: 20, allowedOverrun: 10)
    def element = new AssignmentAggregateReportElement(projectAssignment: assignment, hours: 10)

    assertEquals 20, element.getAvailableHours(), 0.01
  }


  @Test
  void shouldGetPercentageOf50ForDate()
  {
    def assignment = new ProjectAssignment(assignmentType: EhourConstants.ASSIGNMENT_TYPE_DATE, dateStart:  new Date() - 1, dateEnd: new Date() + 1)
    def element = new AssignmentAggregateReportElement(projectAssignment: assignment, hours: 10)

    assertEquals 50, element.getProgressPercentage(), 2
  }

    @Test
  void shouldGetTurnOver()
  {
    def assignment = new ProjectAssignment(hourlyRate: 15)
    def element = new AssignmentAggregateReportElement(projectAssignment: assignment, hours: 10)

    assertEquals 150, element.getTurnOver(), 0.01
  }


}
