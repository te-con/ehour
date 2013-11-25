package net.rrm.ehour.ui.common.wicket

import org.apache.wicket.ajax.markup.html.form.{AjaxButton => WicketAjaxButton}
import org.apache.wicket.markup.html.form.Form
import org.apache.wicket.ajax.AjaxRequestTarget
import net.rrm.ehour.ui.common.wicket.AjaxButton.Callback
import org.apache.wicket.ajax.markup.html.{AjaxLink => WicketAjaxLink}
import net.rrm.ehour.ui.common.wicket.AjaxLink.LinkCallback
import org.apache.wicket.markup.html.WebMarkupContainer
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.{ComponentTag, MarkupStream}
import org.apache.commons.lang.StringUtils
import org.apache.wicket.util.convert.IConverter
import org.apache.wicket.model.IModel
import java.util.Date
import net.rrm.ehour.ui.common.converter.DateConverter


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

class AlwaysOnLabel[T <: java.io.Serializable](id: String, label: IModel[T], converter: Option[IConverter[T]] = None) extends Label(id, label) {
  override def getConverter[T](`type`: Class[T]): IConverter[T] = converter match {
    case Some(c) => c.asInstanceOf[IConverter[T]]
    case None => super.getConverter(`type`)
  }

  override def onComponentTagBody(markupStream: MarkupStream, openTag: ComponentTag) = replaceComponentTagBody(markupStream, openTag, if (StringUtils.isBlank(getDefaultModelObjectAsString)) "&nbsp;" else getDefaultModelObjectAsString)
}

class DateLabel(id: String, model: IModel[Date]) extends AlwaysOnLabel(id, model, Some(new DateConverter))


