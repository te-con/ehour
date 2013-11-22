package net.rrm.ehour.ui.common.wicket

import org.apache.wicket.ajax.markup.html.form.{AjaxButton => WicketAjaxButton}
import org.apache.wicket.markup.html.form.Form
import org.apache.wicket.ajax.AjaxRequestTarget
import net.rrm.ehour.ui.common.wicket.AjaxButton.Callback
import org.apache.wicket.ajax.markup.html.{AjaxLink => WicketAjaxLink}
import net.rrm.ehour.ui.common.wicket.AjaxLink.LinkCallback
import org.apache.wicket.markup.html.WebMarkupContainer


class AjaxButton(id: String, form: Form[_], success: Callback, error: Callback = (a, f) => {}) extends WicketAjaxButton(id, form) {
  override def onSubmit(target: AjaxRequestTarget, form: Form[_]) {
    success(target, form)
  }

  override def onError(target: AjaxRequestTarget, form: Form[_]) {
    error(target, form)
  }
}

object AjaxButton {
  type Callback =  (AjaxRequestTarget, Form[_]) => Unit
}

class AjaxLink(id: String, success: LinkCallback) extends WicketAjaxLink(id){
  override def onClick(target: AjaxRequestTarget) {
    success(target)
  }
}

object AjaxLink {
  type LinkCallback = AjaxRequestTarget => Unit
}

class Container(id: String) extends WebMarkupContainer(id) {
  setOutputMarkupId(true)
}
