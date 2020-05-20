package nl.tecon.highcharts

import config._

import config.Conversions._
import scala.Predef._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{WordSpec, BeforeAndAfter}
import org.joda.time.DateTime
import org.scalatest.Matchers

@RunWith(classOf[JUnitRunner])
class HighChartTest extends WordSpec with Matchers with BeforeAndAfter {
  val chart = new Chart(defaultSeriesType = SeriesType.column)
  val title = new Title(text = "chart")

  val xAxis = new Axis(categories = Array("jan", "feb"))
  val yAxis = new Axis(title = new Title(text = "load"))

  var highChart: HighChart = _

  before {
    highChart = new HighChart(chart = chart,
      title = title,
      xAxis = Seq(xAxis),
      yAxis = Seq(yAxis))
  }

  "HighCharts" should {
    " have formatted labels on the axis" in {
      val serializedChart = highChart.copy(yAxis = Seq(new Axis(labels = Labels(formatter = JavascriptFunction(function = "function() { return this.value +'km';}"))))).build("container")

      serializedChart should include( """yAxis:[{"labels":{"formatter":function() { return this.value +'km';}""")
    }

    " unquote multiple JavascriptFunctions properly" in {
      val unquoted = highChart.unquoteJavascriptFunction( """ "${JSF}$first block${/JSF}$", "2", "${JSF}$second block${/JSF}$" """)

      unquoted should equal( """ first block, "2", second block """)
    }

    " generate" in {
      val serializedChart = highChart.build("container")

      serializedChart should equal( """chart:{"renderTo":"container","defaultSeriesType":"column"},title:{"text":"chart"},xAxis:[{"categories":["jan","feb"]}],credits:{"enabled":false},tooltip:{"shared":true},yAxis:[{"title":{"text":"load"}}],series:[]""")
    }

    " build chart config with legend" in {
      val legend = Legend(align = Alignment.left, verticalAlign = VerticalAlignment.top, y = 20, floating = true, borderWidth = 0)

      val serializedChart = highChart.copy(legend = legend).build("container")

      serializedChart should include( """legend:{"align":"left","verticalAlign":"top","y":20,"floating":true,"borderWidth":0}""")
    }

    " build chart config with series plotOptions" in {
      val plotOptions = PlotOptions(PlotOptionsSeries(marker = Marker(enabled = false)))

      val serializedChart = highChart.copy(plotOptions = plotOptions).build("container")

      serializedChart should include( """plotOptions:{"series":{"marker":{"enabled":false}}}""")
    }

    " build chart config with column plotOptions" in {
      val plotOptions = PlotOptions(column = PlotOptionsColumn(pointWidth = 10))

      val serializedChart = highChart.copy(plotOptions = plotOptions).build("container")

      serializedChart should include( """plotOptions:{"column":{"stacking":"normal","pointWidth":10}}""")
    }


    "build chart config with tooltip" in {
      val serializedChart = highChart.copy(tooltip = Tooltip(true)).build("container")

      serializedChart should include( """tooltip:{"shared":true}""")
    }

    "build chart config with multiple axis" in {
      val serializedChart = highChart.copy(yAxis = Seq(yAxis, Axis(title = new Title(text = "B")))).build("container")

      serializedChart should include( """yAxis:[{"title":{"text":"load"}},{"title":{"text":"B"}}]""")
    }

    " build chart with number series" in {
      val serializedChart = highChart.copy(series = List(Series[Int](data = List(1, 2)))).build("container")

      serializedChart should include( """series:[{"data":[1,2]}]""")
    }

    " build chart with numeric double series" in {
      val serializedChart = highChart.copy(series = List(Series[Double](data = List(1.0, 2.2)))).build("container")

      serializedChart should include( """series:[{"data":[1.0,2.2]}]""")
    }

    " build chart with numeric double series and reversed axis" in {
      val data: Series[Double] = Series[Double](data = List(1.0, 2.2))
      val serializedChart = highChart.copy(series = List(data)).build("container")

      serializedChart should include( """series:[{"data":[1.0,2.2]}]""")
    }

    " build chart with named datetime integer series" in {
      val now = new DateTime(2011, 2, 20, 13, 0, 0, 0)
      val chart = highChart.copy(series = List(Series[DateFloatValue](data = List(DateFloatValue(now, 1), DateFloatValue(now.plusDays(1), 2)), name = "s1")))

      val serializedChart = chart.build("container")

      serializedChart should include( """series:[{"name":"s1","data":[[Date.UTC(2011,1,20),1.0],[Date.UTC(2011,1,21),2.0]]}]""")
    }

    " build chart with tooltip function unescaped" in {
      val chart = highChart.copy(tooltip = Tooltip(formatter = JavascriptFunction("function() { return 'dont escape me'}")))

      chart.build("container") should include( """tooltip:{"formatter":function() { return 'dont escape me'}}""")
    }

    " build chart when nothing is provided" in {
      val chart = highChart.copy(xAxis = None, yAxis = None)

      assert(!chart.build("container").isEmpty)
    }

    " have height set" in {
      val serializedChart = highChart.copy(chart = chart.copy(height = 300)).build("container")

      serializedChart should include( """"height":300""")
    }

    " treat gaps in sparse date series as zero" in {
      val now = new DateTime(2011, 2, 20, 13, 0, 0, 0)
      val chart = highChart.copy(xAxis = Seq(Axis(axisType = AxisType.datetime)),
        series = List(SparseDateSeries(data = List(DateFloatValue(now, 1), DateFloatValue(now.plusDays(2), 2), DateFloatValue(now.plusDays(5), 3)), name = "s1")))

      chart.build("container") should include( """series:[{"name":"s1","data":[1.0,0.0,2.0,0.0,0.0,3.0]""")
    }

    " treat gaps in sparse date series with an explicit start date as zero" in {
      val now = new DateTime(2011, 2, 20, 13, 0, 0, 0)

      val data = List(DateFloatValue(now.plusDays(2), 1), DateFloatValue(now.plusDays(4), 2))
      val sparseSeries = SparseDateSeries(data = data, dateStart = now, name = "s1")

      val chart = highChart.copy(xAxis = Seq(Axis(axisType = AxisType.datetime)), series = List(sparseSeries))

      chart.build("container") should include( """series:[{"name":"s1","data":[0.0,0.0,1.0,0.0,2.0]""")
    }

    " treat gaps in sparse date series with an explicit start and end date as zero" in {
      val now = new DateTime(2011, 2, 20, 13, 0, 0, 0)

      val data = List(DateFloatValue(now.plusDays(1), 1), DateFloatValue(now.plusDays(3), 2))
      val sparseSeries = SparseDateSeries(data = data, dateStart = now, dateEnd = now.plusDays(4), name = "s1")

      val chart = highChart.copy(xAxis = Seq(Axis(axisType = AxisType.datetime)), series = List(sparseSeries))

      chart.build("container") should include( """series:[{"name":"s1","data":[0.0,1.0,0.0,2.0,0.0]""")
    }

    " unquoted point start and interval set" in {
      val now = new DateTime(2011, 2, 20, 13, 0, 0, 0)
      val chart = highChart.copy(plotOptions = PlotOptions(PlotOptionsSeries(pointStart = now, pointInterval = PointInterval.DAY)))

      chart.build("container") should include( """plotOptions:{"series":{"pointStart":Date.UTC(2011,1, 20),"pointInterval":86400000}}""")
    }
  }
}
