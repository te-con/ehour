package net.rrm.ehour.ui.chart

import net.rrm.ehour.report.reports.ReportData
import scalaj.collection.Imports._
import nl.tecon.highcharts.HighChart
import net.rrm.ehour.report.reports.element.AssignmentAggregateReportElement
import nl.tecon.highcharts.config._
import java.lang.String
import net.rrm.ehour.config.EhourConfig
import collection.Seq

object AggregateReportChartGenerator {

  def generateEmployeeReportChart(renderToId: String, reportData: ReportData, config: EhourConfig): String = {
    generateReportChart(renderToId, reportData, config, _.getProjectAssignment.getUser.getFullName, "Employees in hours and turnover")
  }

  def generateCustomerReportChart(renderToId: String, reportData: ReportData, config: EhourConfig): String = {
    generateReportChart(renderToId, reportData, config, _.getProjectAssignment.getProject.getCustomer.getFullName, "Customers in hours and turnover")
  }

  def generateProjectReportChart(renderToId: String, reportData: ReportData, config: EhourConfig): String = {
    generateReportChart(renderToId, reportData, config, _.getProjectAssignment.getFullName, "Projects in hours and turnover")
  }

  private def generateReportChart(renderToId: String, reportData: ReportData, config: EhourConfig, findCategory: (AssignmentAggregateReportElement) => String, chartTitle: String): String = {
    import nl.tecon.highcharts.config.Conversions.valueToOption

    val elements = reportData.getReportElements.asScala.asInstanceOf[Seq[AssignmentAggregateReportElement]]

    val categoryData = extractCategoryData(elements, findCategory)

    val categories = categoryData map (_._1)
    val hourSeries = Series(name = "Booked hours", data = categoryData map (_._2), yAxis = 0)
    val turnoverSeries = Series(name = "Turnover", data = categoryData map (_._3), yAxis = 1)

    val height = (categories.size * 35) + 110
    val chart = Chart(defaultSeriesType = SeriesType.Bar, height = if (height < 400) 400 else height)

    val legend = Labels(formatter = JavascriptFunction("function() { return this.value.toLocaleString();}"))

    HighChart(chart = chart,
              xAxis = Seq(Axis(Some(categories.toArray))),
              yAxis = Seq(Axis(title = Title(text = "Hours"), opposite = true), Axis(title = Title(text = config.getCurrencySymbol), labels = legend)),
              series = List(hourSeries, turnoverSeries),
              title = Title(text = chartTitle),
              tooltip = Tooltip(formatter = Some(JavascriptFunction("function() { return this.series.name + ': ' + this.y.toLocaleString() } ")))
            ).build(renderToId)
  }

  private def extractCategoryData(elements: Seq[AssignmentAggregateReportElement], findCategory: (AssignmentAggregateReportElement) => String): List[(String, Int, Int)] = {
    val categories = (elements map (findCategory(_))).toSet

    val aggregator = (id: String, valueOf: (AssignmentAggregateReportElement) => Int) => elements.foldLeft(0)((total, element) => if (findCategory(element) == id) total + valueOf(element) else total)
    
    val hourAggregator = (id: String) => aggregator(id, _.getHours.intValue())
    val turnOverAggregator = (id: String) => aggregator(id, _.getTurnOver.intValue())

    val categoryTuples = categories map (category => (category, hourAggregator(category), turnOverAggregator(category)))

    categoryTuples.toList.sortBy(_._1)
  }
}