package nl.tecon.highcharts.config

import org.joda.time.DateTime


object Alignment extends Enumeration {
  type Type = Value
  val left, center, right = Value
}

object VerticalAlignment extends Enumeration {
  type Type = Value
  val top, middle, bottom = Value
}

object Layout extends Enumeration {
  type Type = Value
  val horizontal, vertical = Value
}

object AxisType extends Enumeration {
  type Type = Value
  val linear, datetime = Value
}

object SeriesType extends Enumeration {
  type Type = Value
  val column, line, spline, area, bar = Value
}

object ZoomType extends Enumeration {
  type Type = Value
  val x, y, xy = Value
}

case class Labels(rotation: Option[Int] = None,
                    formatter: Option[JavascriptFunction] = None)

case class Legend(align: Option[Alignment.Type] = None,
                  verticalAlign: Option[VerticalAlignment.Type] = None,
                  y: Option[Int] = None,
                  floating: Option[Boolean] = None,
                  borderWidth: Option[Int] = None,
                  layout: Option[Layout.Type] = None)


case class Title(align: Option[Alignment.Type] = None,
                 floating: Option[Boolean] = None,
                 margin: Option[Int] = None,
                 text: Option[String] = None,
                 verticalAlign: Option[VerticalAlignment.Type] = None,
                 x: Option[Int] = None,
                 y: Option[Int] = None)

case class Axis(categories: Option[Array[String]] = None,
                title: Option[Title] = None,
                min: Option[Int] = None,
                axisType: Option[AxisType.Type] = None,
                maxZoom: Option[Int] = None,
                labels: Option[Labels] = None,
                tickInterval: Option[Int] = None,
                endOnTick: Option[Boolean] = None,
                startOnTick: Option[Boolean] = None,
                opposite: Option[Boolean] = None,
                allowDecimals: Option[Boolean] = None,
                max: Option[Int] = None)

case class Chart(renderTo: Option[String] = None,
                 defaultSeriesType: Option[SeriesType.Type] = None,
                 animation: Option[Boolean] = None,
                 zoomType: Option[ZoomType.Type] = None,
                 height: Option[Int] = None)

case class Credits(enabled: Boolean = false)

object PointInterval {
  val DAY = 24 * 3600 * 1000
  val WEEK = 7 * DAY
  val YEAR = 365 * DAY
}

case class PlotOptions(series: Option[PlotOptionsSeries] = None,
                        column: Option[PlotOptionsColumn] = None
                        )

case class PlotOptionsSeries(shadow: Option[Boolean] = None,
                             pointStart: Option[DateTime] = None,
                             pointInterval: Option[Int] = None,
                             marker: Option[Marker] = None,
                             pointWidth: Option[Int] = None,
                             column: Option[PlotOptionsColumn] = None)

case class PlotOptionsColumn(stacking: String = "normal",
                             pointWidth: Option[Int] = None,
                             lineWidth: Option[Int] = None,
                              dataLabels: Option[PlotOptionsColumnDataLabels] = None
                              )

case class PlotOptionsColumnDataLabels(enabled: Boolean = false)

case class Marker(enabled: Boolean = true)

case class Tooltip(shared: Option[Boolean] = None,
                    formatter: Option[JavascriptFunction] = None)

case class JavascriptFunction(function: String)


