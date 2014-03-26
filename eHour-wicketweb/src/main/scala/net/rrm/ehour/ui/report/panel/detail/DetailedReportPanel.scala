package net.rrm.ehour.ui.report.panel
package detail


import net.rrm.ehour.ui.common.report.DetailedReportConfig
import net.rrm.ehour.ui.report.panel.TreeReportDataPanel
import net.rrm.ehour.ui.report.trend.DetailedReportModel
import net.rrm.ehour.ui.chart.HighChartContainer
import org.apache.wicket.model.Model
import net.rrm.ehour.ui.report.{TreeReportData, TreeReportModel}
import org.apache.wicket.markup.html.WebMarkupContainer
import org.apache.wicket.ajax.AjaxRequestTarget
import net.rrm.ehour.ui.common.component.AjaxBehaviorComponent
import net.rrm.ehour.ui.common.panel.AbstractBasePanel
import net.rrm.ehour.ui.report.excel.DetailedReportExcel
import aggregate.ChartContext
import net.rrm.ehour.report.criteria.AggregateBy

class DetailedReportPanel(id: String, report: DetailedReportModel) extends AbstractBasePanel[DetailedReportModel](id) {

  setDefaultModel(report)
  setOutputMarkupId(true)

  val AggregateToConfigMap = Map(AggregateBy.DAY -> DetailedReportConfig.DETAILED_REPORT_BY_DAY,
              AggregateBy.MONTH -> DetailedReportConfig.DETAILED_REPORT_BY_MONTH)


  protected override def onBeforeRender() {
    val frame = new WebMarkupContainer("frame")
    addOrReplace(frame)

    val reportConfig = AggregateToConfigMap.getOrElse(report.getReportCriteria.getUserSelectedCriteria.getAggregateBy, DetailedReportConfig.DETAILED_REPORT_BY_DAY)

    val reportModel = getDefaultModel.asInstanceOf[TreeReportModel]
    frame.add(new TreeReportDataPanel("reportTable", report, reportConfig, DetailedReportExcel.getInstance()))

    val treeReportData = reportModel.getReportData.asInstanceOf[TreeReportData]
    val rawData = treeReportData.getRawReportData
    val chartContainer = new HighChartContainer("chart", new Model(rawData), DetailedReportChartGenerator.generateHourBasedDetailedChart)
    chartContainer.setVisible(!treeReportData.isEmpty)
    frame.add(chartContainer)

    val radioButton = (id: String, generateChart: (ChartContext) => String) => {
      new AjaxBehaviorComponent(id, "onclick", (target: AjaxRequestTarget) => {
        val chart = new HighChartContainer("chart", new Model(rawData), generateChart)
        frame.addOrReplace(chart)
        target.add(chart)
      })
    }

    val typeSelector = new WebMarkupContainer("typeSelector")
    frame.add(typeSelector)

    typeSelector.add(radioButton("turnover", DetailedReportChartGenerator.generateTurnoverBasedDetailedChart))
    typeSelector.add(radioButton("time", DetailedReportChartGenerator.generateHourBasedDetailedChart))

    typeSelector.setVisible(getEhourWebSession.isWithReportRole)

    super.onBeforeRender()
  }
}