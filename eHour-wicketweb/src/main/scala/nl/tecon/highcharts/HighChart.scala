package nl.tecon.highcharts

import config._
import json._
import collection.mutable.ListBuffer
import net.liftweb.json.Serialization.write
import net.liftweb.json.ext.EnumNameSerializer
import net.liftweb.json._

case class HighChart(chart: Option[Chart] = None,
                     title: Option[Title] = None,
                     xAxis: Option[Seq[Axis]] = None,
                     yAxis: Option[Seq[Axis]] = None,
                     legend: Option[Legend] = None,
                     plotOptions: Option[PlotOptions] = None,
                     tooltip: Option[Tooltip] = Some(new Tooltip(Some(true))),
                     credits: Option[Credits] = Some(new Credits()),
                     series: Option[List[_ <: AbstractSeries[_]]] = None,
                     marginLeft: Option[Int] = None) {

  implicit val formats = DefaultFormats + new NumericValueSerializer + new DateNumericValueSerializer + new EnumNameSerializer(Alignment) + new JavascriptFunctionSerializer() + new DateTimeSerializer()

  def build(renderTo: String): String = {
    val targetedChart = chart.get.copy(renderTo = Some(renderTo))

    val serialized = List(serialize("legend", legend),
      serialize("title", title),
      serializePlotOptions(plotOptions),
      serialize("xAxis", xAxis),
      serialize("credits", credits),
      serialize("tooltip", tooltip))

    val list = filterDefined(serialized) map (_.get)

    val json = new StringBuilder()

    json append serialize("chart", targetedChart)
    json append ","
    json append list.mkString(",")
    json append ","

    yAxis match {
      case Some(f) => {
        json append serialize("yAxis", f)
        json append ","
      }
      case _ =>
    }

    json append serializeSeries(series)

    postProcess(json.toString())
  }

  def serializePlotOptions(plotOptions: Option[PlotOptions]) = if (plotOptions.isDefined) Some(unquoteDate(serialize("plotOptions", plotOptions.get))) else None

  def serializeSeries(series: Option[List[_ <: AbstractSeries[_]]])(implicit formats: Formats) = {
    val processedSeries = series match {
      case Some(s) => {
        for (serie <- series.get) yield {
          serie.preProcess()
        }
      }
      case _ => List()
    }

    unquoteDate(serialize("series", processedSeries))
  }

  private def unquoteDate(json: String) = json.replaceAll("\"Date", "Date").replaceAll("\\)\"", "\\)")

  private def postProcess(json: String) = unquoteJavascriptFunction(renameAxisType(json))

  private[highcharts] def unquoteJavascriptFunction(json: String) = json.replaceAllLiterally("\"${JSF}$", "").replaceAllLiterally("${/JSF}$\"", "")

  private def renameAxisType(json: String) = json.replace("\"axisType\"", "\"type\"")

  private def filterDefined(serialized: List[Option[String]]) = serialized.filter(_.isDefined)

  private def serialize(name: String, obj: Option[AnyRef])(implicit formats: Formats): Option[String] = if (obj.isDefined) Some(serialize(name, obj.get)) else None

  private def serialize(name: String, obj: AnyRef): String = {
    val toSerialize = if (obj.isInstanceOf[ListBuffer[_]]) listOrFirstElement(obj) else obj

    "%s:%s".format(name, write(toSerialize))
  }

  private def listOrFirstElement(obj: AnyRef): AnyRef = {
    val list = obj.asInstanceOf[ListBuffer[AnyRef]]
    if (list.size > 1) list else list(0)
  }
}

