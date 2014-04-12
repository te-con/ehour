package net.rrm.ehour.ui.report.panel.detail

import org.wicketstuff.rest.resource.gson.{GsonSerialDeserial, GsonRestResource}
import org.wicketstuff.rest.annotations.MethodMapping
import org.apache.wicket.spring.injection.annot.SpringBean
import net.rrm.ehour.ui.report.cache.ReportCacheService
import net.rrm.ehour.ui.common.util.WebUtils
import net.rrm.ehour.util._
import net.rrm.ehour.ui.common.chart.{PointInterval, GsonSerializer, SparseDateSeries}
import scala.Some
import scala.collection.convert.WrapAsJava
import org.apache.log4j.Logger
import java.util
import org.joda.time.DateTime
import net.rrm.ehour.ui.common.session.EhourWebSession

object DetailedReportRESTResource {
  def apply: DetailedReportRESTResource = new DetailedReportRESTResource(new GsonSerialDeserial(GsonSerializer.create))

  private final val LOG = Logger.getLogger(classOf[DetailedReportRESTResource])
}


class DetailedReportRESTResource(serializer: GsonSerialDeserial) extends GsonRestResource(serializer) {
  @SpringBean
  var reportCacheService: ReportCacheService = _

  @MethodMapping("/hour/{cacheKey}")
  def getHourlyData(cacheKey: String): DetailedReportResponse = {
    cacheService.retrieveReportData(cacheKey) match {
      case Some(data) =>
        val reportRange = data.getReportRange
        val unprocessedSeries = DetailedReportChartGenerator.generateHourBasedDetailedChartData(data)

        DetailedReportResponse(pointStart = new DateTime(reportRange.getDateStart),
                               pointInterval = PointInterval.DAY,
                               title = "Hours booked on customers per day",
                               yAxis = "Hours",
                               series = toJava(unprocessedSeries.map(JSparseDateSeries(_))))
      case None =>
        DetailedReportRESTResource.LOG.warn(s"no data found for key $cacheKey")
        throw new IllegalArgumentException(s"no data found for key $cacheKey")
    }
  }

  @MethodMapping("/turnover/{cacheKey}")
  def getTurnoverData(cacheKey: String): DetailedReportResponse = {
    cacheService.retrieveReportData(cacheKey) match {
      case Some(data) =>
        val reportRange = data.getReportRange
        val unprocessedSeries = DetailedReportChartGenerator.generateTurnoverBasedDetailedChartData(data)

        DetailedReportResponse(pointStart = new DateTime(reportRange.getDateStart),
                              pointInterval = PointInterval.DAY,
                              title = "Turnover booked on customers per day",
                              yAxis = "Turnover",
                              series = toJava(unprocessedSeries.map(JSparseDateSeries(_))))
      case None =>
        DetailedReportRESTResource.LOG.warn(s"no data found for key $cacheKey")
        throw new IllegalArgumentException(s"no data found for key $cacheKey")
    }
  }

  private def cacheService = {
    if (reportCacheService == null) WebUtils.springInjection(this)

    reportCacheService
  }
}

case class DetailedReportResponse(pointStart: DateTime,
                                  pointInterval: Long,
                                  title: String,
                                  yAxis: String,
                                  series: util.List[JSparseDateSeries],
                                  hasReportRole: Boolean = EhourWebSession.getSession.isWithReportRole)

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