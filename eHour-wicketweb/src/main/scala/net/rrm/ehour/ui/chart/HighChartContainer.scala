package net.rrm.ehour.ui.chart

import org.apache.wicket.markup.html.panel.Panel
import org.apache.wicket.markup.html.{IHeaderResponse, IHeaderContributor}
import net.rrm.ehour.report.reports.ReportData
import org.apache.wicket.model.IModel
import net.rrm.ehour.ui.common.session.EhourWebSession
import net.rrm.ehour.config.EhourConfig

class HighChartContainer(id: String, reportModel: IModel[ReportData], generateChart: (String, ReportData, EhourConfig) => String) extends Panel(id, reportModel) with IHeaderContributor {
  setOutputMarkupId(true)
  
  def renderHead(response: IHeaderResponse) {
    val config = EhourWebSession.getSession.getEhourConfig

    val chart = generateChart(getMarkupId, getDefaultModelObject.asInstanceOf[ReportData], config)
    val javascript = "new Highcharts.Chart({%s});\n" format chart
    
    response.renderOnLoadJavascript(javascript)
  }
}