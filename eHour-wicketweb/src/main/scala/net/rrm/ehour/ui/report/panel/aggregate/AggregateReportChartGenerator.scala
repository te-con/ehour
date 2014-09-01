package net.rrm.ehour.ui.report.panel.aggregate

import net.rrm.ehour.report.reports.ReportData
import net.rrm.ehour.report.reports.element.ActivityAggregateReportElement
import nl.tecon.highcharts.HighChart
import nl.tecon.highcharts.config._

import scala.collection.Seq
import scala.collection.convert.WrapAsScala

case class ChartContext(renderToId: String, reportData: ReportData, currencySymbol: String)

object AggregateReportChartGenerator {

  def generateEmployeeReportChart(chartContext: ChartContext): String =
    generateReportChart(chartContext, _.getActivity.getAssignedUser.getFullName, "Users in hours")

  def generateCustomerReportChart(chartContext: ChartContext): String =
    generateReportChart(chartContext, _.getActivity.getProject.getCustomer.getFullName, "Customers in hours")

  def generateProjectReportChart(chartContext: ChartContext): String =
    generateReportChart(chartContext, _.getActivity.getProject.getFullName, "Projects in hours")

  private def generateReportChart(chartContext: ChartContext, findCategory: (ActivityAggregateReportElement) => String, chartTitle: String): String = {
    import nl.tecon.highcharts.config.Conversions.valueToOption

    val elements = WrapAsScala.asScalaBuffer(chartContext.reportData.getReportElements).toSeq.asInstanceOf[Seq[ActivityAggregateReportElement]]

    val categoryData = extractCategoryData(elements, findCategory)

    val categories = categoryData map (_._1)
    val hourSeries = Series(name = "Hours", data = categoryData map (_._2), yAxis = 0)
    // not winning a beauty contest with this..
    val series: Option[List[Series[Float]]] = Some(List(hourSeries))

    val hourAxis = Axis(title = Title(text = "Hours"))

    val yAxis: Option[Seq[Axis]] = Some(Seq(hourAxis))

    val chartTitleText = chartTitle

    val height = (categories.size * 35) + 110
    val chart = Chart(defaultSeriesType = SeriesType.bar, height = if (height < 400) 400 else height)

    HighChart(chart = chart,
      xAxis = Seq(Axis(Some(categories.toArray))),
      yAxis = yAxis,
      series = series,
      title = Title(text = chartTitleText),
      tooltip = Tooltip(shared = true, formatter = Some(JavascriptFunction("""function() { var s = '<b>'+ this.x +'</b>'; $.each(this.points.reverse(), function(i, point) { s += '<br/>'+ point.series.name +': ' + point.y.toLocaleString(); }); return s; }"""))),
      plotOptions = PlotOptions(PlotOptionsSeries(shadow = false))
    ).build(chartContext.renderToId)
  }

  private def extractCategoryData(elements: Seq[ActivityAggregateReportElement], findCategory: (ActivityAggregateReportElement) => String): List[(String, Float, Float)] = {
    val categories = (elements map findCategory).toSet

    val aggregator = (id: String, valueOf: (ActivityAggregateReportElement) => Float) => elements.foldLeft(0f)((total, element) => if (findCategory(element) == id) total + valueOf(element) else total)

    val hourAggregator = (id: String) => aggregator(id, _.getHours.floatValue())
    val turnOverAggregator = (id: String) => aggregator(id, _.getTurnOver.floatValue())

    val categoryTuples = categories map (category => (category, hourAggregator(category), turnOverAggregator(category)))

    categoryTuples.toList.sortBy(_._1)
  }
}