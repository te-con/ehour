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

    chart = AggregateReportChartGenerator.generateProjectReportChart(ChartContext("container", reportData, "$", withTurnover = true))
  }

  test("should have minimum height of 400px") {
    chart should include( """chart:{"renderTo":"container","defaultSeriesType":"bar","height":400}""")
  }

  test("should have x axis with projects") {
    chart should include( """xAxis:[{"categories":["aa50","aa40","aa200","aa30","aa10","aa300"]}]""")
  }

  test("should have one y axis with hours") {
    chart should include( """yAxis:[{"title":{"text":"Hours"},"opposite":true}""")
  }

  test("should have series with booked hours") {
    chart should include( """series:[{"name":"Booked hours","data":[5,4,20,3,1,30],"yAxis":0}""")
  }
}