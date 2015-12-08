package net.rrm.ehour.ui.report.detailed

import java.util
import java.util.Date

import net.rrm.ehour.report.criteria.AggregateBy
import net.rrm.ehour.ui.common.session.EhourWebSession
import org.apache.wicket.model.ResourceModel
import org.joda.time.{DateTime, DateTimeZone, LocalDate}

case class DetailedReportResponse(pointStart: DateTime,
                                  `type`: Char,
                                  title: String,
                                  yAxis: String,
                                  series: util.List[JSparseDateSeries],
                                  hasReportRole: Boolean = EhourWebSession.getSession.isReporter)

object DetailedReportResponse {
  def apply(aggregateBy: AggregateBy,
            startDate: Date,
            aggregateByLabel: String,
            yAxis: String,
            series: util.List[JSparseDateSeries],
            hasReportRole: Boolean)
           (implicit weekStartsAt: Int):DetailedReportResponse = {
    val calculatedStartDate = calculateStartDate(aggregateBy, startDate)

    DetailedReportResponse(pointStart = calculatedStartDate.toDateTimeAtStartOfDay(DateTimeZone.UTC),
      `type` = aggregateBy.name().charAt(0).toUpper,
      title = new ResourceModel(s"report.chart.detailed.label.$aggregateByLabel").getObject,
      yAxis = "Hours",
      series = series,
      hasReportRole = hasReportRole)
  }

  private def calculateStartDate(aggregateBy: AggregateBy, date: Date)(implicit weekStartsAt: Int): LocalDate =
    aggregateBy match {
      case AggregateBy.WEEK => DateCalculator.toWeekStart(date)
      case AggregateBy.MONTH => DateCalculator.toMonthStart(date)
      case AggregateBy.QUARTER => DateCalculator.toQuarterStart(date)
      case AggregateBy.YEAR => DateCalculator.toYearStart(date)
      case _ => new LocalDate(date)
    }
}