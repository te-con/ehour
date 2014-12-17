package net.rrm.ehour.ui.report.detailed

import net.rrm.ehour.ui.report.panel.UpdateReportDataEvent
import org.apache.wicket.event.IEvent
import org.apache.wicket.markup.head.{IHeaderResponse, OnLoadHeaderItem}
import org.apache.wicket.markup.html.IHeaderContributor
import org.apache.wicket.markup.html.panel.Panel

class DetailedReportChartContainer(id: String, cacheKey: String) extends Panel(id) with IHeaderContributor {
  setOutputMarkupId(true)

  override def renderHead(response: IHeaderResponse) {
    response.render(new OnLoadHeaderItem(s"window.chart = new DetailedReportChart('$cacheKey', 'chart');"))

    response.render(new OnLoadHeaderItem("window.chart.init();"))
  }

  override def onEvent(event: IEvent[_]) {
    event.getPayload match {
      case event: UpdateReportDataEvent => {
        val cacheKey = event.getCacheKey
        event.target.appendJavaScript(s"window.chart.update('$cacheKey');")
      }
      case _ =>
    }
  }
}
