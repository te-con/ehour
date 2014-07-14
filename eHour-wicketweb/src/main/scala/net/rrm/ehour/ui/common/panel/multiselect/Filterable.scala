package net.rrm.ehour.ui.common.panel.multiselect

import org.apache.wicket.markup.head.{IHeaderResponse, JavaScriptHeaderItem, OnDomReadyHeaderItem}
import org.apache.wicket.markup.html.IHeaderContributor
import org.apache.wicket.request.resource.JavaScriptResourceReference

trait Filterable extends IHeaderContributor {
  val FilterJs = new JavaScriptResourceReference(classOf[Filterable], "listFilter.js")

  def listFilterId = "#filterAssignmentInput"
  def listId = ".assignmentList"

  val applyJsFilter = s"new ListFilter('$listFilterId', '$listId');"

  abstract override def renderHead(response: IHeaderResponse) {
    super.renderHead(response)

    response.render(JavaScriptHeaderItem.forReference(FilterJs))

    response.render(OnDomReadyHeaderItem.forScript(applyJsFilter))
  }
}
