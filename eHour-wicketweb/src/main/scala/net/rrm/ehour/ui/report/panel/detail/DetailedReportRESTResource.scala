package net.rrm.ehour.ui.report.panel.detail

import java.util

import net.rrm.ehour.ui.common.chart.{GsonSerializer, SparseDateSeries}
import net.rrm.ehour.ui.common.session.EhourWebSession
import net.rrm.ehour.ui.common.util.WebUtils
import net.rrm.ehour.ui.report.cache.ReportCacheService
import net.rrm.ehour.util._
import org.apache.log4j.Logger
import org.apache.wicket.model.StringResourceModel
import org.apache.wicket.spring.injection.annot.SpringBean
import org.wicketstuff.rest.annotations.MethodMapping
import org.wicketstuff.rest.resource.gson.{GsonRestResource, GsonSerialDeserial}

import scala.collection.convert.WrapAsJava

@SuppressWarnings(Array("deprecation"))
object DetailedReportRESTResource {
  def apply: DetailedReportRESTResource = new DetailedReportRESTResource(new GsonSerialDeserial(GsonSerializer.create))

  private final val LOG = Logger.getLogger(classOf[DetailedReportRESTResource])
}

@SuppressWarnings(Array("deprecation"))
class DetailedReportRESTResource(serializer: GsonSerialDeserial) extends GsonRestResource(serializer) {
  @SpringBean
  var reportCacheService: ReportCacheService = _

  implicit def weekStartsAt = DateUtil.fromCalendarToJodaTimeDayInWeek(EhourWebSession.getEhourConfig.getFirstDayOfWeek)


  @MethodMapping("/hour/{cacheKey}")
  def getHourlyData(cacheKey: String): DetailedReportResponse = {
    cacheService.retrieveReportData(cacheKey) match {
      case Some(data) =>
        val aggregateBy = data.getCriteria.getAggregateBy
        val reportRange = data.getReportRange
        val unprocessedSeries = DetailedReportChartGenerator.generateHourBasedDetailedChartData(data)

        val model = new StringResourceModel("userReport.report." + aggregateBy.name().toLowerCase, null)

        DetailedReportResponse(aggregateBy = aggregateBy,
                                startDate = reportRange.getDateStart,
                                aggregateByLabel =  model.getString.toLowerCase,
                                yAxis = "Hours",
                                series = toJava(unprocessedSeries.map(JSparseDateSeries(_))),
                                hasReportRole =  EhourWebSession.getSession.isWithReportRole)
      case None =>
        val errorMsg = s"no data found for key $cacheKey"
        DetailedReportRESTResource.LOG.warn(errorMsg)
        throw new IllegalArgumentException(errorMsg)
    }
  }


  @MethodMapping("/turnover/{cacheKey}")
  def getTurnoverData(cacheKey: String): DetailedReportResponse = {
    cacheService.retrieveReportData(cacheKey) match {
      case Some(data) =>
        val aggregateBy = data.getCriteria.getAggregateBy
        val reportRange = data.getReportRange
        val unprocessedSeries = DetailedReportChartGenerator.generateTurnoverBasedDetailedChartData(data)

        val model = new StringResourceModel("userReport.report." + aggregateBy.name().toLowerCase, null)

        DetailedReportResponse(aggregateBy = aggregateBy,
                              startDate = reportRange.getDateStart,
                              aggregateByLabel = model.getString.toLowerCase,
                              yAxis = "Turnover",
                              series = toJava(unprocessedSeries.map(JSparseDateSeries(_))),
                              hasReportRole =  EhourWebSession.getSession.isWithReportRole)
      case None =>
        val errorMsg = s"no data found for key $cacheKey"
        DetailedReportRESTResource.LOG.warn(errorMsg)
        throw new IllegalArgumentException(errorMsg)
    }
  }

  private def cacheService = {
    if (reportCacheService == null) WebUtils.springInjection(this)

    reportCacheService
  }
}

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