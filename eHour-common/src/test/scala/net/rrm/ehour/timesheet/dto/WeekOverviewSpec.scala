package net.rrm.ehour.timesheet.dto

import org.scalatest.{Matchers, WordSpec}
import net.rrm.ehour.domain.{TimesheetEntry, TimesheetEntryObjectMother, ProjectAssignmentObjectMother}
import java.{util => ju}
import org.joda.time.LocalDate

class WeekOverviewSpec extends WordSpec with Matchers {
  "Week Overview" should {
    "have two assignments with 1 timesheet entry each" in {
      val entry1 = TimesheetEntryObjectMother.createTimesheetEntry(1, LocalDate.parse("20131108").toDate, 5f)
      val entry2 = TimesheetEntryObjectMother.createTimesheetEntry(2, LocalDate.parse("20131109").toDate, 3f)

      val overview = new WeekOverview(ju.Arrays.asList(entry1, entry2), ju.Arrays.asList())

      overview.getAssignmentMap should have size 2

      val entriesOnDate = overview.getAssignmentMap.get(entry1.getEntryId.getProjectAssignment)
      entriesOnDate should have size 1

      val entry: TimesheetEntry = entriesOnDate.get(overview.formatter.format(entry1.getEntryId.getEntryDate))
      entry.getHours should be (5f)
    }

    "have one assignments with 2 timesheet entries" in {
      val entry1 = TimesheetEntryObjectMother.createTimesheetEntry(1, LocalDate.parse("20131108").toDate, 5f)
      val entry2 = TimesheetEntryObjectMother.createTimesheetEntry(1, LocalDate.parse("20131109").toDate, 3f)

      val overview = new WeekOverview(ju.Arrays.asList(entry1, entry2), ju.Arrays.asList())

      overview.getAssignmentMap should have size 1

      val entriesOnDate = overview.getAssignmentMap.get(entry1.getEntryId.getProjectAssignment)
      entriesOnDate should have size 2

      val entry: TimesheetEntry = entriesOnDate.get(overview.formatter.format(entry2.getEntryId.getEntryDate))
      entry.getHours should be (3f)
    }


    "merge assignments without entries" in {
      val assignment = ProjectAssignmentObjectMother.createProjectAssignment(3)

      val entry1 = TimesheetEntryObjectMother.createTimesheetEntry(1, LocalDate.parse("20131108").toDate, 5f)
      val entry2 = TimesheetEntryObjectMother.createTimesheetEntry(2, LocalDate.parse("20131109").toDate, 3f)

      val overview = new WeekOverview(ju.Arrays.asList(entry1, entry2), ju.Arrays.asList(assignment))

      overview.getAssignmentMap should have size 3
    }
  }
}
