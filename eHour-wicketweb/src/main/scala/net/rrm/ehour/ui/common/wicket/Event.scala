package net.rrm.ehour.ui.common.wicket

import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.Component

class Event(t: AjaxRequestTarget) {
  def refresh(components: Component*) {
    t.add(components: _*)
  }
}
