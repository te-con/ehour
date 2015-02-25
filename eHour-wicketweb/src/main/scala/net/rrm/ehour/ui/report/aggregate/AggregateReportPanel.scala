package net.rrm.ehour.ui.report.aggregate

import net.rrm.ehour.report.reports.ReportData
import net.rrm.ehour.ui.chart.HighChartContainer
import net.rrm.ehour.ui.common.panel.AbstractBasePanel
import net.rrm.ehour.ui.common.report.{AbstractExcelReport, ReportConfig}
import net.rrm.ehour.ui.common.session.EhourWebSession
import net.rrm.ehour.ui.report.model.{TreeReportData, TreeReportModel}
import net.rrm.ehour.ui.report.panel.TreeReportDataPanel
import org.apache.wicket.markup.html.panel.Panel
import org.apache.wicket.model.Model


abstract class AggregateReportPanel(id: String, reportModel: TreeReportModel, reportConfig: ReportConfig, generateChart: (ChartContext) => String, excelReport: AbstractExcelReport) extends AbstractBasePanel[TreeReportModel](id) {
  setDefaultModel(reportModel)
  setOutputMarkupId(true)

  val ReportTableId = "reportTable"

  protected override def onBeforeRender() {

    val reportModel = getDefaultModel.asInstanceOf[TreeReportModel]

    addOrReplace(new TreeReportDataPanel(ReportTableId, reportModel, reportConfig, excelReport))

    val reportData: ReportData = reportModel.getReportData
    val chartPanel = addCharts(reportData)
    chartPanel.setVisible(!reportData.isEmpty)

    addOrReplace(chartPanel)

    super.onBeforeRender()
  }

  private def addCharts(data: ReportData): Panel = {
    val rawData: ReportData = data.asInstanceOf[TreeReportData].getRawReportData
    implicit val withTurnover = EhourWebSession.getSession.isReporter

    new HighChartContainer("chart", new Model(rawData), generateChart)
  }
}