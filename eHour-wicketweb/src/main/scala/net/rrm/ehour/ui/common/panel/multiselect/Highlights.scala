package net.rrm.ehour.ui.common.panel.multiselect

import org.apache.wicket.markup.head.{IHeaderResponse, JavaScriptHeaderItem}
import org.apache.wicket.markup.html.IHeaderContributor
import org.apache.wicket.request.resource.JavaScriptResourceReference

trait Highlights extends IHeaderContributor {
  val HighlightJs = new JavaScriptResourceReference(classOf[Highlights], "listHighlight.js")

  abstract override def renderHead(response: IHeaderResponse) {
    super.renderHead(response)
    response.render(JavaScriptHeaderItem.forReference(HighlightJs))
  }
}

