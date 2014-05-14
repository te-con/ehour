package net.rrm.ehour.ui.report.panel.detail

import org.apache.wicket.markup.html.panel.Panel
import org.apache.wicket.markup.html.IHeaderContributor
import org.apache.wicket.markup.head.{OnLoadHeaderItem, IHeaderResponse}
import org.apache.wicket.event.IEvent
import net.rrm.ehour.ui.report.panel.UpdateReportDataEvent

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
