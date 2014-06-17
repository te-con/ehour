package net.rrm.ehour.ui.common.wicket

import org.apache.wicket.Component
import org.apache.wicket.ajax.AjaxRequestTarget

class component(val target: AjaxRequestTarget) {
  def refresh(components: Component*) {
    target.add(components: _*)
  }

  def refresh(components: Component) {
    target.add(components)
  }
}
