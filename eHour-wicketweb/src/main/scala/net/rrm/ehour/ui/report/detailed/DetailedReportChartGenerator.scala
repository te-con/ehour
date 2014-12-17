package net.rrm.ehour.ui.report.detailed

import net.rrm.ehour.data.DateRange
import net.rrm.ehour.report.criteria.AggregateBy
import net.rrm.ehour.report.reports.ReportData
import net.rrm.ehour.report.reports.element.FlatReportElement
import net.rrm.ehour.ui.common.chart.{DateFloatValue, SparseDateSeries}
import org.joda.time.DateTime

import scala.collection.Seq
import scala.collection.convert.WrapAsScala

object DetailedReportChartGenerator {
  def generateHourBasedDetailedChartData(reportData: ReportData): List[SparseDateSeries] = {
    val reportRange = reportData.getReportRange

    buildSeries(reportData, reportRange, f => if (f.getTotalHours == null) 0 else f.getTotalHours.floatValue())
  }

  def generateTurnoverBasedDetailedChartData(reportData: ReportData): List[SparseDateSeries] = {
    val reportRange = reportData.getReportRange

    buildSeries(reportData, reportRange, _.getTotalTurnOver.floatValue())
  }

  private def buildSeries(reportData: ReportData, reportRange: DateRange, f: FlatReportElement => Float) = {
    val elements = WrapAsScala.asScalaBuffer(reportData.getReportElements).toSeq.asInstanceOf[Seq[FlatReportElement]]

    val (dateValueMap, customerNameMap) = buildCustomerAndValueMap(elements, f)

    type f = DateTime => DateTime

    val (dateIncreaseMethod: (DateTime => DateTime), converter: AggregateConverter) = reportData.getCriteria.getAggregateBy match {
      case AggregateBy.WEEK=> ((d: DateTime) => d.plusWeeks(1), new ByWeek)
      case AggregateBy.MONTH => ((d: DateTime) => d.plusMonths(1), new ByMonth)
      case AggregateBy.QUARTER => ((d: DateTime) => d.plusMonths(3), new ByQuarter)
      case AggregateBy.YEAR=> ((d: DateTime) => d.plusYears(1), new ByYear)
      case _ => ((d: DateTime) => d.plusDays(1), new ByDay)
    }

    (for (category <- dateValueMap.keySet) yield {
      val values = dateValueMap.get(category).get

      SparseDateSeries(name = customerNameMap.getOrElse(category, ""),
                       data = values,
                       dateStart = new DateTime(converter.toDate(reportRange.getDateStart)),
                       dateEnd = new DateTime(converter.toDate(reportRange.getDateEnd)),
                       yAxis = None,
                       dateIncrease = dateIncreaseMethod)
    }).toList
  }

  private def buildCustomerAndValueMap(elements: Seq[FlatReportElement], f: FlatReportElement => Float) = {

    type NameMap = Map[Int, String]
    type DataMap = Map[Int, List[DateFloatValue]]

    def add(es: Seq[FlatReportElement], customerIdToNameMap: NameMap = Map(), dateValueMap: DataMap = Map()): (DataMap, NameMap) = {
      if (es.isEmpty) {
        (dateValueMap, customerIdToNameMap)
      } else {
        val element = es.head
        val customerId = element.getCustomerId.toInt
        
        val updatedCustomerIdToNameMap = customerIdToNameMap + (customerId -> element.getCustomerName)

        val dateValue = DateFloatValue(new DateTime(element.getDayDate), if (f == null) 0 else f(element))

        val d = (dateValue :: dateValueMap.getOrElse(customerId, List()).reverse).reverse

        add(es.tail, updatedCustomerIdToNameMap, dateValueMap + (customerId -> d))
      }
    }

    add(elements)
  }
}