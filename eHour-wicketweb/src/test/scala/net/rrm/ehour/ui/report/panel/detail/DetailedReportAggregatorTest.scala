package net.rrm.ehour.ui.report.panel.detail

import org.scalatest.{Matchers, WordSpec}
import java.{util => ju}
import net.rrm.ehour.report.reports.element.FlatReportElement
import org.joda.time.LocalDate

class DetailedReportAggregatorTest extends WordSpec with Matchers {
  "Detailed Report Aggregator" should {
    val baseDate = new LocalDate(2014, 1, 1)

    "aggregate 0 bookings to nothing" in {
      val data = List()

      val aggregate = DetailedReportAggregator.aggregate(data)

      aggregate should be ('empty)
    }

    "aggregate 2 bookings on 1 assignment in 1 month into 1 aggregated element" in {
      val data = List(buildElement(1, baseDate, 10), buildElement(1, baseDate.plusDays(1), 5))

      val aggregate = DetailedReportAggregator.aggregate(data)

      aggregate.size should be (1)

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
