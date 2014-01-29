package net.rrm.ehour.ui.financial.lock

import net.rrm.ehour.ui.common.panel.AbstractFormSubmittingPanel
import org.apache.wicket.model.{Model, ResourceModel, PropertyModel, IModel}
import net.rrm.ehour.ui.common.border.GreySquaredRoundedBorder
import org.apache.wicket.markup.html.form.{TextField, Form}
import net.rrm.ehour.ui.common.panel.datepicker.LocalizedDatePicker
import java.util.Date
import net.rrm.ehour.ui.common.component._
import net.rrm.ehour.ui.common.wicket.AjaxButton._
import net.rrm.ehour.ui.common.wicket.Event
import org.apache.wicket.event.Broadcast
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.ajax.AjaxRequestTarget
import net.rrm.ehour.ui.common.util.WebGeo
import net.rrm.ehour.ui.common.validator.DateOverlapValidator
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior
import net.rrm.ehour.ui.common.decorator.LoadingSpinnerDecorator
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes
import net.rrm.ehour.ui.common.wicket.NonDemoAjaxLink
import net.rrm.ehour.ui.common.wicket.NonDemoAjaxButton
import net.rrm.ehour.ui.common.session.EhourWebSession

class LockFormPanel(id: String, model: IModel[LockAdminBackingBean]) extends AbstractFormSubmittingPanel[LockAdminBackingBean](id, model) {
  val self = this

  override def onInitialize() {
    super.onInitialize()

    val modelObject: LockAdminBackingBean = getPanelModelObject
    val outerBorder = new GreySquaredRoundedBorder(LockFormPanel.OuterBorderId, WebGeo.AUTO)
    addOrReplace(outerBorder)

    val form = new Form[Unit](LockFormPanel.FormId)
    outerBorder.add(form)

    val nameInputField = new TextField("name", new PropertyModel[String](model, "lock.name"))
    nameInputField.setOutputMarkupId(true)
    form.add(nameInputField)

    val startDate = new LocalizedDatePicker("startDate", new PropertyModel[Date](model, "lock.dateStart"))
    def onChangeAjaxBehavior: OnChangeAjaxBehavior = new OnChangeAjaxBehavior {
      override def onUpdate(target: AjaxRequestTarget) {
        send(getPage, Broadcast.BREADTH, DateChangeEvent(getPanelModelObject, target))

        val bean = self.getPanelModelObject
        bean.updateName(EhourWebSession.getEhourConfig.getFormattingLocale)
        target.add(nameInputField)
      }

      protected override def updateAjaxAttributes(attributes: AjaxRequestAttributes) {
        super.updateAjaxAttributes(attributes)
        attributes.getAjaxCallListeners.add(new LoadingSpinnerDecorator)
      }
    }
    startDate.add(onChangeAjaxBehavior)
    form.add(startDate)
    startDate.add(new ValidatingFormComponentAjaxBehavior)
    form.add(new AjaxFormComponentFeedbackIndicator("startDateValidationError", startDate))

    val endDate = new LocalizedDatePicker("endDate", new PropertyModel[Date](model, "lock.dateEnd"))
    endDate.add(onChangeAjaxBehavior)
    form.add(endDate)
    endDate.add(new ValidatingFormComponentAjaxBehavior)
    form.add(new AjaxFormComponentFeedbackIndicator("endDateValidationError", endDate))

    form.add(new DateOverlapValidator("dateStartDateEnd", startDate, endDate))

    form.add(new ServerMessageLabel(LockFormPanel.ServerMessageId, "formValidationError", new PropertyModel[String](model, "serverMessage")))

    val success: Callback = (target, form) => {
      val label = createNotificationLabel(new Model("Locked"))
      form.addOrReplace(label)
      target.add(label)

      val event = if (modelObject.isNew)
        LockAddedEvent(getPanelModelObject, target)
      else LockEditedEvent(getPanelModelObject, target)

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
    val label = new Label(LockFormPanel.ServerMessageId, model)
    label.setOutputMarkupId(true)
    label
  }
}

object LockFormPanel {
  val OuterBorderId = "outerBorder"
  val FormId = "lockForm"
  val ServerMessageId = "serverMessage"
  val AffectedUsersId = "affectedUsersPanel"
  val SubmitId = "submit"
  val UnlockId = "unlock"
}

case class LockAddedEvent(bean: LockAdminBackingBean, override val target: AjaxRequestTarget) extends Event(target)

case class LockEditedEvent(bean: LockAdminBackingBean, override val target: AjaxRequestTarget) extends Event(target)

case class UnlockedEvent(bean: LockAdminBackingBean, override val target: AjaxRequestTarget) extends Event(target)

case class DateChangeEvent(bean: LockAdminBackingBean, override val target: AjaxRequestTarget) extends Event(target)
