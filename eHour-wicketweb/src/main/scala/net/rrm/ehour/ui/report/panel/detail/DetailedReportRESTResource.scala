package net.rrm.ehour.ui.report.panel.detail

import org.wicketstuff.rest.resource.gson.{GsonSerialDeserial, GsonRestResource}
import org.wicketstuff.rest.annotations.MethodMapping
import org.apache.wicket.spring.injection.annot.SpringBean
import net.rrm.ehour.ui.report.cache.ReportCacheService
import net.rrm.ehour.ui.common.util.WebUtils
import net.rrm.ehour.util._
import java.util
import com.google.gson._
import java.lang.reflect.Type
import org.joda.time.DateTime
import net.rrm.ehour.ui.common.chart.SparseDateSeries
import scala.Some
import scala.collection.convert.WrapAsJava

object DetailedReportRESTResource {
  def apply: DetailedReportRESTResource = {
    val gson = new GsonBuilder()
      .registerTypeAdapter(classOf[DateTime], new DateTimeSerializer)
      .create()
    new DetailedReportRESTResource(new GsonSerialDeserial(gson))
  }
}

class DetailedReportRESTResource(serializer: GsonSerialDeserial) extends GsonRestResource(serializer) {
  @SpringBean
  var reportCacheService: ReportCacheService = _

  @MethodMapping("/hour/{cacheKey}")
  def findReport(cacheKey: String): util.List[JSparseDateSeries] = {
    Console.println("they're hitting me!")
    cacheService.retrieveReportData(cacheKey) match {
      case Some(data) =>
        val unprocessedSeries = DetailedReportChartGenerator.generateHourBasedDetailedChartData(data)
        toJava(unprocessedSeries.map(JSparseDateSeries(_)))
      case None =>
        Console.println(s"no data found for key $cacheKey")

        throw new IllegalArgumentException(s"no data found for key $cacheKey")
    }
  }

  private def cacheService = {
    if (reportCacheService == null) WebUtils.springInjection(this)

    reportCacheService
  }
}

class DateTimeSerializer extends JsonSerializer[DateTime] {
  override def serialize(d: DateTime, typeOfSrc: Type, context: JsonSerializationContext): JsonElement = new JsonPrimitive("Date.UTC(%d,%d, %d)" format(d.getYear, d.getMonthOfYear - 1, d.getDayOfMonth))
}

import java.util

case class JSparseDateSeries(name: String,
                             data: util.List[Float],
                             yAxis: Integer = null)

object JSparseDateSeries {
  def apply(series: SparseDateSeries): JSparseDateSeries = {
    val processedSeries = series.preProcess()

    processedSeries.yAxis match {
      case Some(axis) => JSparseDateSeries(processedSeries.name, WrapAsJava.bufferAsJavaList(processedSeries.data.toBuffer), axis)
      case None => JSparseDateSeries(processedSeries.name, WrapAsJava.bufferAsJavaList(processedSeries.data.toBuffer))
    }
  }
}