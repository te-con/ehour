package net.rrm.ehour.timesheet.dto

import java.{util => ju}

import net.rrm.ehour.domain.{ActivityMother, TimesheetEntry, TimesheetEntryObjectMother}
import org.joda.time.LocalDate
import org.scalatest.{Matchers, WordSpec}

class WeekOverviewSpec extends WordSpec with Matchers {
  "Week Overview" should {
    "have two assignments with 1 timesheet entry each" in {
      val entry1 = TimesheetEntryObjectMother.createTimesheetEntry(1, LocalDate.parse("20131108").toDate, 5f)
      val entry2 = TimesheetEntryObjectMother.createTimesheetEntry(2, LocalDate.parse("20131109").toDate, 3f)

      val overview = new WeekOverview(ju.Arrays.asList(entry1, entry2), ju.Arrays.asList())

      overview.getActivityMap should have size 2

      val entriesOnDate = overview.getActivityMap.get(entry1.getEntryId.getActivity)
      entriesOnDate should have size 1

      val entry: TimesheetEntry = entriesOnDate.get(overview.formatter.format(entry1.getEntryId.getEntryDate))
      entry.getHours should be (5f)
    }

    "have one assignments with 2 timesheet entries" in {
      val entry1 = TimesheetEntryObjectMother.createTimesheetEntry(1, LocalDate.parse("20131108").toDate, 5f)
      val entry2 = TimesheetEntryObjectMother.createTimesheetEntry(1, LocalDate.parse("20131109").toDate, 3f)

      val overview = new WeekOverview(ju.Arrays.asList(entry1, entry2), ju.Arrays.asList())

      overview.getActivityMap should have size 1

      val entriesOnDate = overview.getActivityMap.get(entry1.getEntryId.getActivity)
      entriesOnDate should have size 2

      val entry: TimesheetEntry = entriesOnDate.get(overview.formatter.format(entry2.getEntryId.getEntryDate))
      entry.getHours should be (3f)
    }


    "merge activities without entries" in {
      val activity = ActivityMother.createActivity(3)

      val entry1 = TimesheetEntryObjectMother.createTimesheetEntry(1, LocalDate.parse("20131108").toDate, 5f)
      val entry2 = TimesheetEntryObjectMother.createTimesheetEntry(2, LocalDate.parse("20131109").toDate, 3f)

      val overview = new WeekOverview(ju.Arrays.asList(entry1, entry2), ju.Arrays.asList(activity))

      overview.getActivityMap should have size 3
    }
  }
}
