package net.rrm.ehour.ui.financial.lock

import net.rrm.ehour.ui.common.panel.AbstractFormSubmittingPanel
import org.apache.wicket.model.{Model, ResourceModel, PropertyModel, IModel}
import net.rrm.ehour.ui.common.border.GreySquaredRoundedBorder
import org.apache.wicket.markup.html.form.{TextField, Form}
import net.rrm.ehour.ui.common.panel.datepicker.LocalizedDatePicker
import java.util.Date
import net.rrm.ehour.ui.common.component.{AjaxFormComponentFeedbackIndicator, ValidatingFormComponentAjaxBehavior, JavaScriptConfirmation, PlaceholderPanel}
import net.rrm.ehour.ui.common.wicket.AjaxButton._
import net.rrm.ehour.ui.common.wicket.{Event, NonDemoAjaxButton, NonDemoAjaxLink}
import org.apache.wicket.event.Broadcast
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.ajax.AjaxRequestTarget
import net.rrm.ehour.ui.common.util.WebGeo
import net.rrm.ehour.ui.common.validator.DateOverlapValidator

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
    startDate.add(new ValidatingFormComponentAjaxBehavior)
    form.add(new AjaxFormComponentFeedbackIndicator("startDateValidationError", startDate))

    val endDate = new LocalizedDatePicker("endDate", new PropertyModel[Date](model, "lock.dateEnd"))
    //    endDate.add(new OnChangeAjaxBehavior {
    //      def onUpdate(target: AjaxRequestTarget) {
    //        target.add(affectedUsersPanel)
    //      }
    //    })
    form.add(endDate)
    endDate.add(new ValidatingFormComponentAjaxBehavior)
    form.add(new AjaxFormComponentFeedbackIndicator("endDateValidationError", endDate))

    form.add(new DateOverlapValidator("dateStartDateEnd", startDate, endDate))

    form.add(new PlaceholderPanel(LockFormPanel.SaveConfirmId))

    val success: Callback = (target, form) => {
      val label = createNotificationLabel(new Model("Locked"))
      form.addOrReplace(label)
      target.add(label)

      val event = if (modelObject.isNew)
        LockAddedEvent(getPanelModelObject, target) else LockEditedEvent(getPanelModelObject, target)

      send(getPage, Broadcast.BREADTH, event)
    }

    val submitButton = NonDemoAjaxButton(LockFormPanel.SubmitId, form, success)
    form.add(submitButton)

    val unlockButton = NonDemoAjaxLink(LockFormPanel.UnlockId, (target) => {
      send(getPage, Broadcast.BREADTH, UnlockedEvent(getPanelModelObject, target))
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
  val SubmitId = "submit"
  val UnlockId = "unlock"

}

case class LockAddedEvent(bean: LockAdminBackingBean, override val target: AjaxRequestTarget) extends Event(target)

case class LockEditedEvent(bean: LockAdminBackingBean, override val target: AjaxRequestTarget) extends Event(target)

case class UnlockedEvent(bean: LockAdminBackingBean, override val target: AjaxRequestTarget) extends Event(target)
