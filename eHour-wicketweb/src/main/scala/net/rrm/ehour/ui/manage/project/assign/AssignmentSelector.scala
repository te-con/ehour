package net.rrm.ehour.ui.manage.project.assign

import net.rrm.ehour.ui.common.panel.entryselector.{EntrySelectorData, EntrySelectorPanel}
import net.rrm.ehour.ui.common.wicket.AjaxLink
import net.rrm.ehour.ui.common.wicket.AjaxLink._
import org.apache.wicket.event.Broadcast
import org.apache.wicket.markup.html.WebMarkupContainer

class AssignmentSelector(id: String, entrySelectorData: EntrySelectorData, clickHandler: EntrySelectorPanel.ClickHandler, onlyDeactivate: Boolean = false, wide: Boolean)
  extends EntrySelectorPanel(id, entrySelectorData, clickHandler, null, wide)
{
  override protected def onFiltersCreated(filterInputContainer: WebMarkupContainer) = {
    val linkCallback: LinkCallback = target => send(this, Broadcast.BUBBLE, NewAssignmentEvent(target))
    val link = new AjaxLink("addUsers", linkCallback)
    link.setVisible(!onlyDeactivate)
    filterInputContainer.addOrReplace(link)
  }
}
