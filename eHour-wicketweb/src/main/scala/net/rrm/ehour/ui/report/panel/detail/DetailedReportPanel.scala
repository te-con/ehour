package net.rrm.ehour.ui.report.panel
package detail

import net.rrm.ehour.ui.common.report.{ReportConfig, DetailedReportConfig}
import net.rrm.ehour.ui.report.panel.TreeReportDataPanel
import net.rrm.ehour.ui.report.trend.DetailedReportModel
import org.apache.wicket.model.PropertyModel
import net.rrm.ehour.ui.report.{TreeReportData, TreeReportModel}
import org.apache.wicket.markup.html.WebMarkupContainer
import net.rrm.ehour.ui.common.panel.AbstractBasePanel
import net.rrm.ehour.ui.report.excel.DetailedReportExcel
import net.rrm.ehour.report.criteria.{UserSelectedCriteria, AggregateBy}
import org.apache.wicket.spring.injection.annot.SpringBean
import net.rrm.ehour.ui.report.cache.ReportCacheService
import org.apache.wicket.markup.html.panel.Panel
import org.apache.wicket.markup.html.form.DropDownChoice
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.event.Broadcast
import net.rrm.ehour.ui.common.wicket.Event

object DetailedReportPanel {
  val AggregateToConfigMap = Map(AggregateBy.DAY -> DetailedReportConfig.DETAILED_REPORT_BY_DAY,
    AggregateBy.WEEK -> DetailedReportConfig.DETAILED_REPORT_BY_WEEK,
    AggregateBy.MONTH -> DetailedReportConfig.DETAILED_REPORT_BY_MONTH)

}

class DetailedReportPanel(id: String, report: DetailedReportModel) extends AbstractBasePanel[DetailedReportModel](id) {

  setDefaultModel(report)
  setOutputMarkupId(true)

  @SpringBean
  var reportCacheService: ReportCacheService = _


  protected override def onBeforeRender() {
    val frame = new WebMarkupContainer("frame")
    addOrReplace(frame)

    val reportConfig = DetailedReportPanel.AggregateToConfigMap.getOrElse(report.getReportCriteria.getUserSelectedCriteria.getAggregateBy, DetailedReportConfig.DETAILED_REPORT_BY_DAY)

    val reportModel = getDefaultModel.asInstanceOf[TreeReportModel]
    frame.add(new TreeReportDataPanel("reportTable", report, reportConfig, DetailedReportExcel.getInstance()) {
      protected override def createAdditionalOptions(id: String): WebMarkupContainer = new AggregateByDatePanel(id, report.getReportCriteria.getUserSelectedCriteria)
    })

    val treeReportData = reportModel.getReportData.asInstanceOf[TreeReportData]
    val rawData = treeReportData.getRawReportData
    val cacheKey = reportCacheService.storeReportData(rawData)

    val chartContainer = new DetailedReportChartContainer("chart", cacheKey)
    chartContainer.setVisible(!treeReportData.isEmpty)
    frame.add(chartContainer)

    super.onBeforeRender()
  }
}

import net.rrm.ehour.util._

class AggregateByDatePanel(id: String, criteria: UserSelectedCriteria) extends Panel(id) {
  val Self = this

  override def onInitialize() {
    super.onInitialize()

    val options = toJava(AggregateBy.values().toList)

    val aggregateSelect = new DropDownChoice[AggregateBy]("aggregateBy", new PropertyModel[AggregateBy](criteria, "aggregateBy"), options)

    aggregateSelect.add(new AjaxFormComponentUpdatingBehavior("change") {
      override def onUpdate(target: AjaxRequestTarget) {
        val aggregateBy = aggregateSelect.getModelObject

        send(Self, Broadcast.BUBBLE, new AggregateByChangedEvent(target, DetailedReportPanel.AggregateToConfigMap(aggregateBy)))
      }
    })

    addOrReplace(aggregateSelect)
  }
}

case class AggregateByChangedEvent(t: AjaxRequestTarget, reportConfig: ReportConfig) extends Event(t)
