package net.rrm.ehour.ui
package chart

import net.rrm.ehour.report.reports.AggregateReportDataObjectMother
import net.rrm.ehour.ui.report.panel.aggregate.{AggregateReportChartGenerator, ChartContext}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfter, FunSuite, Matchers}

@RunWith(classOf[JUnitRunner])
class EmployeeChartGeneratorTest extends FunSuite with Matchers with BeforeAndAfter {
  var chart: String = _

  before {
    val reportData = AggregateReportDataObjectMother.generateReportData

    chart = AggregateReportChartGenerator.generateEmployeeReportChart(ChartContext("container", reportData, "$"))
  }

  test("should have minimum height of 400px") {
    chart should include("""chart:{"renderTo":"container","defaultSeriesType":"bar","height":400}""")
  }

  test("should have x axis with employees") {
    chart should include("""xAxis:[{"categories":["Rosalie Edeling","Thies Edeling"]}]""")
  }

  test("should have one axises with hour") {
    chart should include("""yAxis:[{"title":{"text":"Hours"}}]""")
  }

  test("should have series with booked hours") {
    chart should include("""series:[{"name":"Hours","data":[22.0,42.0],"yAxis":0}""")
  }
}