package net.rrm.ehour.ui.common.event

import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.Component

class Event(target: AjaxRequestTarget) {
  def refresh(components: Component*) {
    target.add(components: _*)
  }
}