package net.rrm.ehour.ui.chart

import net.rrm.ehour.report.reports.ReportData
import net.rrm.ehour.ui.common.session.EhourWebSession
import net.rrm.ehour.ui.report.aggregate.ChartContext
import org.apache.wicket.markup.head.{OnDomReadyHeaderItem, IHeaderResponse, OnLoadHeaderItem}
import org.apache.wicket.markup.html.IHeaderContributor
import org.apache.wicket.markup.html.panel.Panel
import org.apache.wicket.model.IModel

class HighChartContainer(id: String, reportModel: IModel[ReportData], generateChart: (ChartContext) => String) extends Panel(id, reportModel) with IHeaderContributor {
  setOutputMarkupId(true)

  override def renderHead(response: IHeaderResponse) {
    val session = EhourWebSession.getSession
    val config = EhourWebSession.getEhourConfig

    val showTurnover = config.isShowTurnover || session.isReporter

    val chart = generateChart(ChartContext(getMarkupId, getDefaultModelObject.asInstanceOf[ReportData], config.getCurrencySymbol, withTurnover = showTurnover))
    val javascript = "new Highcharts.Chart({%s});\n" format chart

    response.render(OnDomReadyHeaderItem.forScript(javascript))
  }
}