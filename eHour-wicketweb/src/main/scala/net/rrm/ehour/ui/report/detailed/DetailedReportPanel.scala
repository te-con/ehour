package net.rrm.ehour.ui.report.detailed

import com.googlecode.wicket.jquery.ui.resource.JQueryUIResourceReference
import net.rrm.ehour.report.criteria.{AggregateBy, ReportCriteria, UserSelectedCriteria}
import net.rrm.ehour.report.reports.ReportData
import net.rrm.ehour.ui.common.panel.AbstractBasePanel
import net.rrm.ehour.ui.common.renderers.LocalizedResourceRenderer
import net.rrm.ehour.ui.common.report.{DetailedReportConfig, ReportConfig}
import net.rrm.ehour.ui.common.wicket.Event
import net.rrm.ehour.ui.report.cache.ReportCacheService
import net.rrm.ehour.ui.report.model.{TreeReportData, TreeReportModel}
import net.rrm.ehour.ui.report.panel.{TreeReportDataPanel, UpdateReportDataEvent}
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior
import org.apache.wicket.event.{Broadcast, IEvent}
import org.apache.wicket.markup.head.{IHeaderResponse, JavaScriptHeaderItem}
import org.apache.wicket.markup.html.WebMarkupContainer
import org.apache.wicket.markup.html.form.DropDownChoice
import org.apache.wicket.markup.html.panel.Panel
import org.apache.wicket.model.PropertyModel
import org.apache.wicket.request.resource.JavaScriptResourceReference
import org.apache.wicket.spring.injection.annot.SpringBean

object DetailedReportPanel {
  val AggregateToConfigMap = Map(AggregateBy.DAY -> DetailedReportConfig.DETAILED_REPORT_BY_DAY,
    AggregateBy.WEEK -> DetailedReportConfig.DETAILED_REPORT_BY_WEEK,
    AggregateBy.MONTH -> DetailedReportConfig.DETAILED_REPORT_BY_MONTH,
    AggregateBy.QUARTER -> DetailedReportConfig.DETAILED_REPORT_BY_QUARTER,
    AggregateBy.YEAR -> DetailedReportConfig.DETAILED_REPORT_BY_YEAR)
}

class DetailedReportPanel(id: String, report: DetailedReportModel) extends AbstractBasePanel[DetailedReportModel](id) {
  val Self = this

  setDefaultModel(report)
  setOutputMarkupId(true)

  @SpringBean
  var reportCacheService: ReportCacheService = _

  protected override def onBeforeRender() {
    val reportConfig = DetailedReportPanel.AggregateToConfigMap.getOrElse(report.getReportCriteria.getUserSelectedCriteria.getAggregateBy, DetailedReportConfig.DETAILED_REPORT_BY_DAY)

    val excel = new DetailedReportExcel(new PropertyModel[ReportCriteria](report, "reportCriteria"))

    addOrReplace(new TreeReportDataPanel("reportTable", report, reportConfig, excel) {
      protected override def createAdditionalOptions(id: String): WebMarkupContainer = new AggregateByDatePanel(id, report.getReportCriteria.getUserSelectedCriteria)
    })

    val reportData: ReportData = rawReportData()
    val cacheKey = storeReportData(reportData)

    val chartContainer = new DetailedReportChartContainer("chart", cacheKey)
    chartContainer.setVisible(!reportData.isEmpty)
    addOrReplace(chartContainer)

    super.onBeforeRender()
  }

  val JqueryUi = new JavaScriptResourceReference(classOf[JQueryUIResourceReference], "jquery-ui.js")
  
  override def renderHead(response: IHeaderResponse) {
    response.render(JavaScriptHeaderItem.forReference(JqueryUi))
    
  }

  private def rawReportData():ReportData = {
    val reportModel = getDefaultModel.asInstanceOf[TreeReportModel]
    val treeReportData = reportModel.getReportData.asInstanceOf[TreeReportData]
    treeReportData.getRawReportData
  }

  private def storeReportData(data: ReportData) = reportCacheService.storeReportData(data)

  override def onEvent(event: IEvent[_]) = {
    event.getPayload match {
      case aggregateByChangedEvent: AggregateByChangedEvent =>
        val cacheKey = storeReportData(rawReportData())

        val reportDataEvent = new UpdateReportDataEvent(aggregateByChangedEvent.target, cacheKey, aggregateByChangedEvent.reportConfig)

        send(Self, Broadcast.BREADTH, reportDataEvent)
      case _ =>
    }
  }
}

import net.rrm.ehour.util._

class AggregateByDatePanel(id: String, criteria: UserSelectedCriteria) extends Panel(id) {
  val Self = this

  override def onInitialize() {
    super.onInitialize()

    val options = toJava(AggregateBy.values().toList)

    val aggregateSelect = new DropDownChoice[AggregateBy]("aggregateBy", new PropertyModel[AggregateBy](criteria, "aggregateBy"), options, new LocalizedResourceRenderer[AggregateBy]() {
      override protected def getResourceKey(o: AggregateBy): String = "userReport.report." + o.name.toLowerCase
    })

    aggregateSelect.add(new AjaxFormComponentUpdatingBehavior("change") {
      override def onUpdate(target: AjaxRequestTarget) {
        val aggregateBy = aggregateSelect.getModelObject

        send(Self.getPage, Broadcast.DEPTH, new AggregateByChangedEvent(target, DetailedReportPanel.AggregateToConfigMap(aggregateBy)))
      }
    })

    addOrReplace(aggregateSelect)
  }
}

case class AggregateByChangedEvent(t: AjaxRequestTarget, reportConfig: ReportConfig) extends Event(t)
