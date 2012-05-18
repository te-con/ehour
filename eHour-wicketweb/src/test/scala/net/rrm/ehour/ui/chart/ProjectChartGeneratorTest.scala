package net.rrm.ehour.ui.chart

import net.rrm.ehour.report.reports.AggregateReportDataObjectMother
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{BeforeAndAfter, FunSuite}
import net.rrm.ehour.config.EhourConfigStub

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
    chart should include("""xAxis:[{"categories":["Project A","Project B","Project C","Project D","Project E","Project B"]}]""")
  }

  test("should have two y axises with hours and formatted turnover") {
    chart should include("""yAxis:[{"title":{"text":"Hours"},"opposite":true},{"title":{"text":"""")

    chart should include(""""},"labels":{"formatter":function() { return this.value.toLocaleString();}}}]""")
  }

  test("should have series with booked hours") {
    chart should include("""series:[{"name":"Booked hours","data":[14,8,10,12,10,10],"yAxis":0}""")
  }

  test("should have series with turnover") {
    chart should include("""{"name":"Turnover","data":[140.0,200.0,350.0,60.0,350.0,350.0],"yAxis":1}]""")
  }
}