package net.rrm.ehour.ui.report.panel.detail

import net.rrm.ehour.report.reports.ReportData
import net.rrm.ehour.report.reports.element.FlatReportElement
import collection.Seq
import java.lang.String
import org.joda.time.DateTime
import scala.collection.convert.WrapAsScala
import net.rrm.ehour.data.DateRange
import net.rrm.ehour.ui.common.chart.{DateFloatValue, SparseDateSeries}

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

    (for (category <- dateValueMap.keySet) yield {
      val values = dateValueMap.get(category).get

      SparseDateSeries(name = customerNameMap.getOrElse(category, ""), data = values, dateStart = new DateTime(reportRange.getDateStart), dateEnd = new DateTime(reportRange.getDateEnd))
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