package net.rrm.ehour.ui.report.aggregate

import net.rrm.ehour.report.reports.ReportData
import net.rrm.ehour.report.reports.element.AssignmentAggregateReportElement
import nl.tecon.highcharts.HighChart
import nl.tecon.highcharts.config._
import org.apache.wicket.model.ResourceModel

import scala.collection.Seq
import scala.collection.convert.WrapAsScala

case class ChartContext(renderToId: String, reportData: ReportData, currencySymbol: String, withTurnover: Boolean)

object AggregateReportChartGenerator {

  def generateUserReportChart(chartContext: ChartContext): String =
    generateReportChart(chartContext, _.getProjectAssignment.getUser.getFullName, "report.chart.label.users")

  def generateCustomerReportChart(chartContext: ChartContext): String =
    generateReportChart(chartContext, _.getProjectAssignment.getProject.getCustomer.getFullName, "report.chart.label.customers")

  def generateProjectReportChart(chartContext: ChartContext): String =
    generateReportChart(chartContext, _.getProjectAssignment.getFullName, "report.chart.label.projects")

  private def generateReportChart(chartContext: ChartContext, findCategory: (AssignmentAggregateReportElement) => String, baseChartTitleResourceKey: String): String = {
    import nl.tecon.highcharts.config.Conversions.valueToOption

    val elements = WrapAsScala.asScalaBuffer(chartContext.reportData.getReportElements).toSeq.asInstanceOf[Seq[AssignmentAggregateReportElement]]

    val categoryData = extractCategoryData(elements, findCategory)

    val categories = categoryData map (_._1)
    val hourSeries = Series(name = lookupResource("report.chart.label.hours"), data = categoryData map (_._2), yAxis = 0)
    val legend = Labels(formatter = JavascriptFunction("function() { return this.value.toLocaleString();}"))

    // not winning a beauty contest with this..
    val series: Option[List[Series[Float]]] = Some(if (chartContext.withTurnover) {
      val turnoverSeries = Series(name = lookupResource("report.chart.label.turnover"), data = categoryData map (_._3), yAxis = 1)
      List(hourSeries, turnoverSeries)
    } else {
      List(hourSeries)
    })

    val hourAxis = Axis(title = Title(text = lookupResource("report.chart.label.hours")))

    val yAxis: Option[Seq[Axis]] = Some(if (chartContext.withTurnover) {
      val turnOverAxis = Axis(title = Title(text = chartContext.currencySymbol), labels = legend, opposite = true)
      Seq(hourAxis, turnOverAxis)
    } else {
      Seq(hourAxis)
    })

    val chartTitleResourceKey = baseChartTitleResourceKey + (if (chartContext.withTurnover) ".turnover" else ".hours")

    val chartTitle = lookupResource(chartTitleResourceKey)

    val height = (categories.size * 35) + 110
    val chart = Chart(defaultSeriesType = SeriesType.bar, height = if (height < 400) 400 else height)

    HighChart(chart = chart,
      xAxis = Seq(Axis(Some(categories.toArray))),
      yAxis = yAxis,
      series = series,
      title = Title(text = chartTitle),
      tooltip = Tooltip(shared = true, formatter = Some(JavascriptFunction("""function() { var s = '<b>'+ this.x +'</b>'; $.each(this.points.reverse(), function(i, point) { s += '<br/>'+ point.series.name +': ' + point.y.toLocaleString(); }); return s; }"""))),
      plotOptions = PlotOptions(PlotOptionsSeries(shadow = false))
    ).build(chartContext.renderToId)
  }

  private def lookupResource(resourceKey: String) = new ResourceModel(resourceKey).getObject

  private def extractCategoryData(elements: Seq[AssignmentAggregateReportElement], findCategory: (AssignmentAggregateReportElement) => String): List[(String, Float, Float)] = {
    val categories = (elements map findCategory).toSet

    val aggregator = (id: String, valueOf: (AssignmentAggregateReportElement) => Float) => elements.foldLeft(0f)((total, element) => if (findCategory(element) == id) total + valueOf(element) else total)

    val hourAggregator = (id: String) => aggregator(id, _.getHours.floatValue())
    val turnOverAggregator = (id: String) => aggregator(id, _.getTurnOver.floatValue())

    val categoryTuples = categories map (category => (category, hourAggregator(category), turnOverAggregator(category)))

    categoryTuples.toList.sortBy(_._1)
  }
}