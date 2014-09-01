package net.rrm.ehour.ui.chart

import net.rrm.ehour.report.reports.AggregateReportDataObjectMother
import net.rrm.ehour.ui.report.panel.aggregate.{AggregateReportChartGenerator, ChartContext}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfter, FunSuite, Matchers}

@RunWith(classOf[JUnitRunner])
class ProjectChartGeneratorTest extends FunSuite with Matchers with BeforeAndAfter {
  var chart: String = _

  before {
    val reportData = AggregateReportDataObjectMother.generateReportData

    chart = AggregateReportChartGenerator.generateProjectReportChart(ChartContext("container", reportData, "$"))
  }

  test("should have minimum height of 400px") {
    chart should include( """chart:{"renderTo":"container","defaultSeriesType":"bar","height":400}""")
  }

  test("should have x axis with projects") {
    chart should include( """xAxis:[{"categories":["Project A","Project B","Project C","Project D","Project E"]}]""")
  }

  test("should have one y axis with hours") {
    chart should include( """yAxis:[{"title":{"text":"Hours"}}]""")
  }

  test("should have series with booked hours") {
    chart should include( """series:[{"name":"Hours","data":[14.0,18.0,10.0,12.0,10.0],"yAxis":0}]""")
  }
}