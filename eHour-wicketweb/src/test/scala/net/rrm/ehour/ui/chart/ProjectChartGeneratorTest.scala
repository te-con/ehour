package net.rrm.ehour.ui.chart

import net.rrm.ehour.report.reports.AggregateReportDataObjectMother
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{BeforeAndAfter, FunSuite}
import net.rrm.ehour.config.EhourConfigStub
import net.rrm.ehour.ui.report.panel.aggregate.AggregateReportChartGenerator

@RunWith(classOf[JUnitRunner])
class ProjectChartGeneratorTest extends FunSuite with ShouldMatchers with BeforeAndAfter {
  var chart: String = _

  before {
    val reportData = AggregateReportDataObjectMother.generateReportData

    chart = AggregateReportChartGenerator.generateProjectReportChart("container", reportData, new EhourConfigStub())
  }

  test("should have minimum height of 400px") {
    chart should include("""chart:{"renderTo":"container","defaultSeriesType":"bar","height":400}""")
  }

  test("should have x axis with projects") {
    chart should include("""xAxis:[{"categories":["Project A","Project B","Project C","Project D","Project E"]}]""")
  }

  test("should have two y axises with hours and formatted turnover") {
    chart should include("""yAxis:[{"title":{"text":"Hours"},"opposite":true},{"title":{"text":"""")

    chart should include(""""},"labels":{"formatter":function() { return this.value.toLocaleString();}}}]""")
  }

  test("should have series with booked hours") {
    chart should include("""series:[{"name":"Booked hours","data":[14,18,10,12,10],"yAxis":0}""")
  }

  test("should have series with turnover") {
    chart should include("""{"name":"Turnover","data":[140,550,350,60,350],"yAxis":1}]""")
  }
}