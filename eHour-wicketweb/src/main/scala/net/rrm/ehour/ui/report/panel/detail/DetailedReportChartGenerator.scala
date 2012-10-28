package net.rrm.ehour.ui.report.panel.detail

import net.rrm.ehour.report.reports.ReportData
import net.rrm.ehour.config.EhourConfig
import scalaj.collection.Imports._
import net.rrm.ehour.report.reports.element.FlatReportElement
import nl.tecon.highcharts.HighChart
import nl.tecon.highcharts.config._
import collection.Seq
import java.lang.String
import org.joda.time.DateTime
import nl.tecon.highcharts.config.Conversions._

object DetailedReportChartGenerator {
  val axis = (title: String) => Seq(Axis(title = Title(text = title)))
  val title = (t: String) => Title(text = t)
  val tooltip = (t: String) => Tooltip(formatter = Some(JavascriptFunction("function() { return new Date(this.x).toLocaleDateString() + '<br />' + this.series.name + ': ' + this.y.toLocaleString() + ' " + t + "' } ")))


  def generateHourBasedDetailedChart(renderToId: String, reportData: ReportData, config: EhourConfig): String = {
    val highChart = generateDetailedChart(reportData, config, _.getTotalHours.floatValue())

    highChart.copy(yAxis = axis("Hours"), title = title("Hours booked on customers per day"), tooltip = tooltip("hours")).build(renderToId)
  }

  def generateTurnoverBasedDetailedChart(renderToId: String, reportData: ReportData, config: EhourConfig): String = {
    val highChart = generateDetailedChart(reportData, config, _.getTotalTurnOver.floatValue())

    highChart.copy(yAxis = axis(config.getCurrencySymbol), title = title("Turnover booked on customers per day"), tooltip = tooltip(config.getCurrencySymbol)).build(renderToId)
  }

  private def generateDetailedChart(reportData: ReportData, config: EhourConfig, f: FlatReportElement => Float): HighChart = {
    val elements = reportData.getReportElements.asScala.asInstanceOf[Seq[FlatReportElement]]

    val reportRange = reportData.getReportRange

    val (categoryDataMap, categoryNameMap) = buildCategoryMap(elements, f)

    val series = (for (category <- categoryDataMap.keySet) yield {
      val values = categoryDataMap.get(category).get

      SparseDateSeries(name = categoryNameMap.get(category), data = values, dateStart = new DateTime(reportRange.getDateStart), dateEnd = new DateTime(reportRange.getDateEnd))
    }).toList

    val chart = new Chart(defaultSeriesType = SeriesType.Column, zoomType = ZoomType.X)

    val seriesLength = if (series.length > 0) series.head.data.length else 1

    // for performance reasons, disable shadows when there's a lot of data
    val isBigDataSet = (series.length * seriesLength) > 150

    val plotOptions = PlotOptions(PlotOptionsSeries(shadow = !isBigDataSet, pointStart = Some(new DateTime(reportRange.getDateStart)), pointInterval = Some(PointInterval.DAY)), column = PlotOptionsColumn(pointWidth = 10))
    new HighChart(chart = chart,
      xAxis = Seq(Axis(axisType = AxisType.Datetime, maxZoom = 3)),
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