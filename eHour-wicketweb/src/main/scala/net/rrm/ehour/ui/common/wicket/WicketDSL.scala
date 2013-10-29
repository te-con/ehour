package net.rrm.ehour.ui.common.wicket

import org.apache.wicket.ajax.markup.html.form.{AjaxButton => WicketAjaxButton}
import org.apache.wicket.markup.html.form.Form
import org.apache.wicket.ajax.AjaxRequestTarget
import net.rrm.ehour.ui.common.wicket.AjaxButton.Callback


class AjaxButton(id: String, form: Form[_], success: Callback, error: Callback = (a, f) => {}) extends WicketAjaxButton(id, form) {
  override def onSubmit(target: AjaxRequestTarget, form: Form[_]) {
    success(target, form)
  }
}

object AjaxButton {
  type Callback =  (AjaxRequestTarget, Form[_]) => Unit
}
