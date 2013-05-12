package net.rrm.ehour.ui
package chart

import net.rrm.ehour.report.reports.AggregateReportDataObjectMother
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{BeforeAndAfter, FunSuite}
import report.panel.aggregate.{ChartContext, AggregateReportChartGenerator}

@RunWith(classOf[JUnitRunner])
class EmployeeChartGeneratorTest extends FunSuite with ShouldMatchers with BeforeAndAfter {
  var chart: String = _

  before {
    val reportData = AggregateReportDataObjectMother.generateReportData

    chart = AggregateReportChartGenerator.generateEmployeeReportChart(ChartContext("container", reportData, "$", withTurnover = true))
  }

  test("should have minimum height of 400px") {
    chart should include("""chart:{"renderTo":"container","defaultSeriesType":"bar","height":400}""")
  }

  test("should have x axis with employees") {
    chart should include("""xAxis:[{"categories":["Edeling, Rosalie","Edeling, Thies"]}]""")
  }

  test("should have two y axises with hours and formatted turnover") {
    chart should include("""yAxis:[{"title":{"text":"Hours"},"opposite":true},{"title":{"text":"""")

    chart should include(""""},"labels":{"formatter":function() { return this.value.toLocaleString();}}}]""")
  }

  test("should have series with booked hours") {
    chart should include("""series:[{"name":"Booked hours","data":[22.0,42.0],"yAxis":0}""")
  }

  test("should have series with turnover") {
    chart should include("""{"name":"Turnover","data":[340.0,1110.0],"yAxis":1}]""")
  }
}