package net.rrm.ehour.ui.financial.lock

import net.rrm.ehour.ui.common.panel.AbstractFormSubmittingPanel
import org.apache.wicket.model.{Model, ResourceModel, PropertyModel, IModel}
import net.rrm.ehour.ui.common.border.GreySquaredRoundedBorder
import org.apache.wicket.markup.html.form.{TextField, Form}
import net.rrm.ehour.ui.common.panel.datepicker.LocalizedDatePicker
import java.util.Date
import net.rrm.ehour.ui.common.component.{JavaScriptConfirmation, PlaceholderPanel}
import net.rrm.ehour.ui.common.wicket.AjaxButton._
import net.rrm.ehour.ui.common.wicket.{NonDemoAjaxButton, NonDemoAjaxLink}
import org.apache.wicket.event.Broadcast
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.ajax.AjaxRequestTarget
import net.rrm.ehour.ui.common.event.Event
import net.rrm.ehour.ui.common.util.WebGeo

class LockFormPanel(id: String, model: IModel[LockAdminBackingBean]) extends AbstractFormSubmittingPanel[LockAdminBackingBean](id, model) {
  override def onInitialize() {
    super.onInitialize()

    val modelObject: LockAdminBackingBean = getPanelModelObject
    val outerBorder = new GreySquaredRoundedBorder(LockFormPanel.OuterBorderId, WebGeo.AUTO)
    addOrReplace(outerBorder)

    val form = new Form[Unit](LockFormPanel.FormId)
    outerBorder.add(form)

    form.add(new TextField("name", new PropertyModel[String](model, "lock.name")))

    val startDate = new LocalizedDatePicker("startDate", new PropertyModel[Date](model, "lock.dateStart"))
    //    startDate.add(new OnChangeAjaxBehavior {
    //      def onUpdate(target: AjaxRequestTarget) {
    //        target.add(affectedUsersPanel)
    //      }
    //    })
    form.add(startDate)

    val endDate = new LocalizedDatePicker("endDate", new PropertyModel[Date](model, "lock.dateEnd"))
    //    endDate.add(new OnChangeAjaxBehavior {
    //      def onUpdate(target: AjaxRequestTarget) {
    //        target.add(affectedUsersPanel)
    //      }
    //    })
    form.add(endDate)

    form.add(new PlaceholderPanel(LockFormPanel.SaveConfirmId))

    val success: Callback = (target, form) => {
      val label = createNotificationLabel(new Model("Locked"))
      form.addOrReplace(label)
      target.add(label)

      val event = if (modelObject.isNew) LockAddedEvent(getPanelModelObject, target) else LockModifiedEvent(getPanelModelObject, target)

      send(getParent, Broadcast.BREADTH, event)
    }

    val submitButton = NonDemoAjaxButton("submit", form, success)
    form.add(submitButton)

    val unlockButton = NonDemoAjaxLink("unlock", (target) => {
      send(getParent, Broadcast.BREADTH, LockDeletedEvent(getPanelModelObject, target))
      //
      //      val replacement = new LockDetailsPanel(self.getId)
      //      self.replaceWith(replacement)
      //      target.add(replacement)
    })

    unlockButton.add(new JavaScriptConfirmation("onclick", new ResourceModel("general.deleteConfirmation")))

    unlockButton.setVisible(!getPanelModelObject.isNew)
    form.add(unlockButton)
  }

  def createNotificationLabel(model: IModel[String]): Label = {
    val label = new Label(LockFormPanel.SaveConfirmId, model)
    label.setOutputMarkupId(true)
    label
  }
}

object LockFormPanel {
  val OuterBorderId = "outerBorder"
  val FormId = "lockForm"
  val SaveConfirmId = "saveConfirm"
  val AffectedUsersId = "affectedUsersPanel"
}

case class LockAddedEvent(bean: LockAdminBackingBean, target: AjaxRequestTarget) extends Event(target)

case class LockModifiedEvent(bean: LockAdminBackingBean, target: AjaxRequestTarget) extends Event(target)

case class LockDeletedEvent(bean: LockAdminBackingBean, target: AjaxRequestTarget) extends Event(target)
