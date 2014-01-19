package net.rrm.ehour.ui.common.wicket

import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.Component

class Event(val target: AjaxRequestTarget) {
  def refresh(components: Component*) {
    target.add(components: _*)
  }
}
