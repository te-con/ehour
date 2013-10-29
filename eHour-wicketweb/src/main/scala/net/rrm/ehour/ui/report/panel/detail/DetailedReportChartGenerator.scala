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
    val highChart = generateDetailedChart(chartContext.reportData, _.getTotalHours.floatValue())

    highChart.copy(yAxis = axis("Hours"), title = title("Hours booked on customers per day"), tooltip = tooltip("hours")).build(chartContext.renderToId)
  }


  def generateTurnoverBasedDetailedChart(chartContext: ChartContext): String = {
    val highChart = generateDetailedChart(chartContext.reportData, _.getTotalTurnOver.floatValue())

    highChart.copy(yAxis = axis(chartContext.currencySymbol), title = title("Turnover booked on customers per day"), tooltip = tooltip(chartContext.currencySymbol)).build(chartContext.renderToId)
  }


  private def generateDetailedChart(reportData: ReportData, f: FlatReportElement => Float): HighChart = {
    val elements = WrapAsScala.asScalaBuffer(reportData.getReportElements).toSeq.asInstanceOf[Seq[FlatReportElement]]

    val reportRange = reportData.getReportRange

    val (categoryDataMap, categoryNameMap) = buildCategoryMap(elements, f)

    val series = (for (category <- categoryDataMap.keySet) yield {
      val values = categoryDataMap.get(category).get

      SparseDateSeries(name = categoryNameMap.get(category), data = values, dateStart = new DateTime(reportRange.getDateStart), dateEnd = new DateTime(reportRange.getDateEnd))
    }).toList

    val chart = new Chart(defaultSeriesType = SeriesType.column, zoomType = ZoomType.x)

    val seriesLength = if (series.length > 0) series.head.data.length else 1

    // for performance reasons, disable shadows when there's a lot of data
    val isBigDataSet = (series.length * seriesLength) > 150

    val plotOptions = PlotOptions(PlotOptionsSeries(shadow = !isBigDataSet, pointStart = Some(new DateTime(reportRange.getDateStart)), pointInterval = Some(PointInterval.DAY)), column = PlotOptionsColumn(pointWidth = 10))
    new HighChart(chart = chart,
      xAxis = Seq(Axis(axisType = AxisType.datetime, maxZoom = 3)),
      series = series,
      plotOptions = plotOptions
    )
  }

  private def buildCategoryMap(elements: Seq[FlatReportElement], f: FlatReportElement => Float) = {

    type NameMap = Map[Int, String]
    type DataMap = Map[Int, List[DateFloatValue]]

    def add(es: Seq[FlatReportElement], nameMap: NameMap = Map(), dataMap: DataMap = Map()): (DataMap, NameMap) = {
      if (es.isEmpty) {
        (dataMap, nameMap)
      } else {
        val e = es.head
        val id = e.getCustomerId.toInt
        val nameMap2 = nameMap + (id -> e.getCustomerName)

        val item = DateFloatValue(new DateTime(e.getDayDate), f(e))

        val d = (item :: dataMap.getOrElse(id, List()).reverse).reverse

        add(es.tail, nameMap2, dataMap + (id -> d))
      }
    }

    add(elements)
  }
}