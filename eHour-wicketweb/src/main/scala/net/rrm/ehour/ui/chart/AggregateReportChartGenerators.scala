package net.rrm.ehour.ui.chart

import net.rrm.ehour.report.reports.ReportData
import scalaj.collection.Imports._
import nl.tecon.highcharts.HighChart
import net.rrm.ehour.report.reports.element.AssignmentAggregateReportElement
import nl.tecon.highcharts.config._
import java.lang.String
import net.rrm.ehour.config.EhourConfig
import collection.Seq


object AggregateReportChartGenerators {

  def generateEmployeeReportChart(renderToId: String, reportData: ReportData, config: EhourConfig): String = {
    generateReportChart(renderToId, reportData, config, _.getProjectAssignment.getUser.getFullName, "Employees in hours and turnover")
  }

  def generateCustomerReportChart(renderToId: String, reportData: ReportData, config: EhourConfig): String = {
    generateReportChart(renderToId, reportData, config, _.getProjectAssignment.getProject.getCustomer.getFullName, "Customers in hours and turnover")
  }

  private def generateReportChart(renderToId: String, reportData: ReportData, config: EhourConfig, findCategory: (AssignmentAggregateReportElement) => String, chartTitle: String): String = {
    import nl.tecon.highcharts.config.Conversions.valueToOption

    val elements: Seq[AssignmentAggregateReportElement] = reportData.getReportElements.asScala.asInstanceOf[Seq[AssignmentAggregateReportElement]]

    val categoryData: Set[(String, Int, Int)] = extractCategoryData(elements, findCategory)

    val categories: Array[String] = (categoryData map (_._1)).toArray
    val hourSeries = Series(name = "Booked hours", data = categoryData map (_._2), yAxis = 0)
    val turnoverSeries = Series(name = "Turnover", data = categoryData map (_._3), yAxis = 1)

    val height: Int = (categories.size * 35) + 110
    val chart = new Chart(defaultSeriesType = SeriesType.Bar, height = if (height < 400) 400 else height)

    val legend = Labels(formatter = JavascriptFunction("function() { return this.value.toLocaleString();}"))

    val highChart = new HighChart(chart = chart,
      xAxis = Seq(Axis(Some(categories))),
      yAxis = Seq(Axis(title = Title(text = "Hours"), opposite = true), Axis(title = Title(text = config.getCurrencySymbol), labels = legend)),
      series = List(hourSeries, turnoverSeries),
      title = Title(text = chartTitle),
      tooltip = Tooltip(formatter = Some(JavascriptFunction("function() { return this.series.name + ': ' + this.y.toLocaleString() } ")))
    )

    highChart.build(renderToId)
  }

  private def extractCategoryData(elements: Seq[AssignmentAggregateReportElement], findCategory: (AssignmentAggregateReportElement) => String): Set[(String, Int, Int)] = {
    val categories: Set[String] = (for (element <- elements) yield findCategory(element)).toSet

    val aggregator = (id: String, valueOf: (AssignmentAggregateReportElement) => Int) =>
            elements.foldLeft(0)((total, element) => if (findCategory(element) == id) total + valueOf(element) else total)
    
    val hourAggregator = (id: String) => aggregator(id, _.getHours.intValue())
    val turnOverAggregator = (id: String) => aggregator(id, _.getTurnOver.intValue())

    for (category <- categories) yield {
      (category, hourAggregator(category), turnOverAggregator(category))
    }
  }

  def generateProjectReportChart(renderToId: String, reportData: ReportData, config: EhourConfig): String = {
    import nl.tecon.highcharts.config.Conversions.valueToOption

    val elements: Seq[AssignmentAggregateReportElement] = reportData.getReportElements.asScala.asInstanceOf[Seq[AssignmentAggregateReportElement]]

    val hourTuples = for (element <- elements) yield (element, element.getProjectAssignment.getFullName)
    val hourSeries = Series(name = "Booked hours", data = hourTuples map (_._1.getHours), yAxis = 0)
    val turnoverSeries = Series(name = "Turnover", data = hourTuples map (_._1.getTurnOver), yAxis = 1)
    val categories = (hourTuples map (_._2)).toArray

    val height: Int = (categories.size * 35) + 110
    val chart = new Chart(defaultSeriesType = SeriesType.Bar, height = if (height < 400) 400 else height)

    val legend = Labels(formatter = JavascriptFunction("function() { return this.value.toLocaleString();}"))

    val highChart = new HighChart(chart = chart,
      xAxis = Seq(Axis(Some(categories))),
      yAxis = Seq(Axis(title = Title(text = "Hours"), opposite = true), Axis(title = Title(text = config.getCurrencySymbol), labels = legend)),
      series = List(hourSeries, turnoverSeries),
      title = Title(text = "Projects in hours and turnover"),
      tooltip = Tooltip(formatter = Some(JavascriptFunction("function() { return this.series.name + ': ' + this.y.toLocaleString() } ")))
    )

    highChart.build(renderToId)
  }
}