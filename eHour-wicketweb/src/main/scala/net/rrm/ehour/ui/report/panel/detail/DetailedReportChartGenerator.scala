package net.rrm.ehour.ui.report.panel.detail

import net.rrm.ehour.report.reports.ReportData
import net.rrm.ehour.report.reports.element.FlatReportElement
import nl.tecon.highcharts.HighChart
import nl.tecon.highcharts.config._
import collection.Seq
import java.lang.String
import org.joda.time.DateTime
import nl.tecon.highcharts.config.Conversions._
import net.rrm.ehour.ui.report.panel.aggregate.ChartContext
import scala.collection.convert.WrapAsScala

object DetailedReportChartGenerator {
  val axis = (title: String) => Seq(Axis(title = Title(text = title)))
  val title = (t: String) => Title(text = t)
  val tooltip = (t: String) => Tooltip(formatter = Some(JavascriptFunction("function() { return new Date(this.x).toLocaleDateString() + '<br />' + this.series.name + ': ' + this.y.toLocaleString() + ' " + t + "' } ")))


  def generateHourBasedDetailedChart(chartContext: ChartContext): String = {
    val highChart = generateDetailedChart(chartContext.reportData, f => if (f.getTotalHours == null) 0 else f.getTotalHours.floatValue())

    highChart.copy(yAxis = axis("Hours"), title = title("Hours booked on customers per day"), tooltip = tooltip("hours")).build(chartContext.renderToId)
  }


  def generateTurnoverBasedDetailedChart(chartContext: ChartContext): String = {
    val highChart = generateDetailedChart(chartContext.reportData, _.getTotalTurnOver.floatValue())

    highChart.copy(yAxis = axis(chartContext.currencySymbol), title = title("Turnover booked on customers per day"), tooltip = tooltip(chartContext.currencySymbol)).build(chartContext.renderToId)
  }


  private def generateDetailedChart(reportData: ReportData, f: FlatReportElement => Float): HighChart = {
    val elements = WrapAsScala.asScalaBuffer(reportData.getReportElements).toSeq.asInstanceOf[Seq[FlatReportElement]]

    val reportRange = reportData.getReportRange

    val (dateValueMap, customerNameMap) = buildCustomerAndValueMap(elements, f)

    val series = (for (category <- dateValueMap.keySet) yield {
      val values = dateValueMap.get(category).get

      SparseDateSeries(name = customerNameMap.get(category), data = values, dateStart = new DateTime(reportRange.getDateStart), dateEnd = new DateTime(reportRange.getDateEnd))
    }).toList

    val chart = new Chart(defaultSeriesType = SeriesType.column, zoomType = ZoomType.x)

    val seriesLength = if (series.length > 0) series.head.data.length else 1

    // for performance reasons, disable shadows when there's a lot of data
    val isBigDataSet = (series.length * seriesLength) > 150

    val plotOptions = PlotOptions(PlotOptionsSeries(shadow = !isBigDataSet, pointStart = Some(new DateTime(reportRange.getDateStart)), pointInterval = Some(PointInterval.WEEK)), column = PlotOptionsColumn(pointWidth = 10))
    new HighChart(chart = chart,
      xAxis = Seq(Axis(axisType = AxisType.datetime, maxZoom = 3)),
      series = series,
      plotOptions = plotOptions
    )
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