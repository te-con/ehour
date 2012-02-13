package net.rrm.ehour.ui.common.component

import org.apache.wicket.markup.html.WebComponent
import net.rrm.ehour.ui.common.decorator.LoadingSpinnerDecorator
import org.apache.wicket.ajax.{IAjaxCallDecorator, AjaxRequestTarget, AjaxEventBehavior}

class AjaxBehaviorComponent(id: String, event: String, behavior: AjaxRequestTarget => Unit) extends WebComponent(id) {
  setMarkupId(id)
  setOutputMarkupId(true)

  add(new AjaxEventBehavior(event) {
    def onEvent(target: AjaxRequestTarget) {
      behavior(target)
    }

    override def getAjaxCallDecorator: IAjaxCallDecorator = new LoadingSpinnerDecorator
  })
}