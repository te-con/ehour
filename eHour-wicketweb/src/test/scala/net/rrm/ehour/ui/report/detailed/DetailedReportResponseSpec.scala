package net.rrm.ehour.ui.report.detailed

import com.google.common.collect.Lists
import net.rrm.ehour.AbstractSpec
import net.rrm.ehour.report.criteria.AggregateBy
import org.joda.time.{DateTimeConstants, LocalDate}

class DetailedReportResponseSpec extends AbstractSpec {
  "Detailed Report Response" should {
    val noData = Lists.newArrayList[JSparseDateSeries]()

    implicit val weekStartsAt = DateTimeConstants.MONDAY

    "calculated start date for day aggregation should be the day provided" in {
      val start = new LocalDate(2014, DateTimeConstants.MAY, 21)

      val response  = DetailedReportResponse.apply(AggregateBy.DAY,
        start.toDate,
        "day",
        "hour",
        noData,
        hasReportRole = true)

      response.pointStart.toLocalDate should equal (new LocalDate(2014, DateTimeConstants.MAY, 21))
    }

    "calculated start date for week aggregation should be the first day of the week" in {
      val start = new LocalDate(2014, DateTimeConstants.MAY, 21)

      val response  = DetailedReportResponse.apply(AggregateBy.WEEK,
                                                  start.toDate,
                                                  "week",
                                                  "hour",
                                                  noData,
                                                  hasReportRole = true)

      response.pointStart.toLocalDate should equal (new LocalDate(2014, DateTimeConstants.MAY, 19))
    }

    "calculated start date for month aggregation should be the first day of the month" in {
      val start = new LocalDate(2014, DateTimeConstants.MAY, 21)

      val response  = DetailedReportResponse.apply(AggregateBy.MONTH,
        start.toDate,
        "month",
        "hour",
        noData,
        hasReportRole = true)

      response.pointStart.toLocalDate should equal (new LocalDate(2014, DateTimeConstants.MAY, 1))
    }

    "calculated start date for quarter aggregation should be the first day of the quarter" in {
      val start = new LocalDate(2014, DateTimeConstants.MAY, 21)

      val response  = DetailedReportResponse.apply(AggregateBy.QUARTER,
        start.toDate,
        "quarter",
        "hour",
        noData,
        hasReportRole = true)

      response.pointStart.toLocalDate should equal (new LocalDate(2014, DateTimeConstants.APRIL, 1))
    }

    "calculated start date for year aggregation should be the first day of the quarter" in {
      val start = new LocalDate(2014, DateTimeConstants.MAY, 21)

      val response  = DetailedReportResponse.apply(AggregateBy.YEAR,
        start.toDate,
        "year",
        "hour",
        noData,
        hasReportRole = true)

      response.pointStart.toLocalDate should equal (new LocalDate(2014, DateTimeConstants.JANUARY, 1))
    }
  }
}
