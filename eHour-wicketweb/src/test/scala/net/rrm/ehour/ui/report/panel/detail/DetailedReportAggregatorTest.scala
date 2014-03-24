package net.rrm.ehour.ui.report.panel.detail

import org.scalatest.{Matchers, WordSpec}
import net.rrm.ehour.report.reports.element.FlatReportElement
import org.joda.time.LocalDate

class DetailedReportAggregatorTest extends WordSpec with Matchers {
  "Detailed Report Aggregator" should {
    val baseDate = new LocalDate(2014, 1, 1)

    "aggregate 0 bookings to nothing" in {
      val data = List()

      val aggregate = DetailedReportAggregator.aggregate(data, DetailedReportAggregator.ByMonth)

      aggregate should be('empty)
    }

    "by month: aggregate 2 bookings on 1 assignment in 1 month into 1 aggregated element" in {
      val data = List(buildElement(1, baseDate, 10), buildElement(1, baseDate.plusWeeks(3), 5))

      val aggregate = DetailedReportAggregator.aggregate(data, DetailedReportAggregator.ByMonth)

      aggregate.size should be(1)
      aggregate.head.getTotalHours should be(15)
    }

    "by month: aggregate 2 bookings on 1 assignment in 1 month into 1 aggregated element and leave the other assignment alone" in {
      val data = List(buildElement(1, baseDate, 10), buildElement(1, baseDate.plusWeeks(3), 5),
        buildElement(2, baseDate, 10))

      val aggregate = DetailedReportAggregator.aggregate(data, DetailedReportAggregator.ByMonth)

      aggregate.size should be(2)
    }

    "by month: do not aggregate 2 bookings when they're in different months" in {
      val data = List(buildElement(1, baseDate, 10), buildElement(1, baseDate.plusMonths(1), 5))

      val aggregate = DetailedReportAggregator.aggregate(data, DetailedReportAggregator.ByMonth)

      aggregate.size should be(2)
    }

    "by week: aggregate 2 bookings on 1 assignment in 1 week into 1 aggregated element" in {
      val data = List(buildElement(1, baseDate, 10), buildElement(1, baseDate.plusDays(1), 5))

      val aggregate = DetailedReportAggregator.aggregate(data, DetailedReportAggregator.ByWeek)

      aggregate.size should be(1)
      aggregate.head.getTotalHours should be(15)
    }

    "by week: aggregate 2 bookings on 1 assignment in 1 week  into 1 aggregated element and leave the other assignment alone" in {
      val data = List(buildElement(1, baseDate, 10), buildElement(1, baseDate.plusDays(1), 5),
        buildElement(2, baseDate, 10))

      val aggregate = DetailedReportAggregator.aggregate(data, DetailedReportAggregator.ByWeek)

      aggregate.size should be(2)
    }

    "by week: do not aggregate 2 bookings when they're in different weeks" in {
      val data = List(buildElement(1, baseDate, 10), buildElement(1, baseDate.plusWeeks(2), 5))

      val aggregate = DetailedReportAggregator.aggregate(data, DetailedReportAggregator.ByWeek)

      aggregate.size should be(2)
    }


    "by quarter: aggregate 2 bookings on 1 assignment in 1 quarter into 1 aggregated element" in {
      val data = List(buildElement(1, baseDate, 10), buildElement(1, baseDate.plusMonths(1), 5))

      val aggregate = DetailedReportAggregator.aggregate(data, DetailedReportAggregator.ByQuarter)

      aggregate.size should be(1)
      aggregate.head.getTotalHours should be(15)
    }

    "by quarter: aggregate 2 bookings on 1 assignment in 1 quarter into 1 aggregated element and leave the other assignment alone" in {
      val data = List(buildElement(1, baseDate, 10), buildElement(1, baseDate.plusMonths(2), 5),
        buildElement(2, baseDate, 10))

      val aggregate = DetailedReportAggregator.aggregate(data, DetailedReportAggregator.ByQuarter)

      aggregate.size should be(2)
    }

    "by quarter: do not aggregate 2 bookings when they're in different quarters" in {
      val data = List(buildElement(1, baseDate, 10), buildElement(1, baseDate.plusMonths(3), 5))

      val aggregate = DetailedReportAggregator.aggregate(data, DetailedReportAggregator.ByQuarter)

      aggregate.size should be(2)
    }
  }

  def buildElement(assignmentId: Int, date: LocalDate, hours: Float) = {
    val element = new FlatReportElement()
    element.setAssignmentId(assignmentId)
    element.setDayDate(date.toDate)
    element.setTotalHours(hours)
    element
  }

}
