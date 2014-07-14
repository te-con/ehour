package net.rrm.ehour.ui.manage.lock

import java.util.Date

import net.rrm.ehour.domain.{TimesheetLock, User}
import net.rrm.ehour.ui.common.border.GreySquaredRoundedBorder
import net.rrm.ehour.ui.common.component._
import net.rrm.ehour.ui.common.decorator.LoadingSpinnerDecorator
import net.rrm.ehour.ui.common.panel.AbstractFormSubmittingPanel
import net.rrm.ehour.ui.common.panel.datepicker.LocalizedDatePicker
import net.rrm.ehour.ui.common.panel.multiselect.MultiUserSelect
import net.rrm.ehour.ui.common.session.EhourWebSession
import net.rrm.ehour.ui.common.util.WebGeo
import net.rrm.ehour.ui.common.validator.DateOverlapValidator
import net.rrm.ehour.ui.common.wicket.AjaxButton._
import net.rrm.ehour.ui.common.wicket.{Container, Event, NonDemoAjaxButton, NonDemoAjaxLink}
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior
import org.apache.wicket.ajax.markup.html.AjaxLink
import org.apache.wicket.event.Broadcast
import org.apache.wicket.markup.html.WebMarkupContainer
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.form.{Form, TextField}
import org.apache.wicket.model.{IModel, Model, PropertyModel, ResourceModel}

class LockFormPanel(id: String, model: IModel[LockAdminBackingBean]) extends AbstractFormSubmittingPanel[LockAdminBackingBean](id, model) {
  val self = this

  val ExcludedUsersId = "excludedUsers"

  override def onInitialize() {
    super.onInitialize()

    val modelObject = getPanelModelObject
    val outerBorder = new GreySquaredRoundedBorder(LockFormPanel.OuterBorderId, WebGeo.AUTO)
    addOrReplace(outerBorder)

    val form = new Form(LockFormPanel.FormId)
    outerBorder.add(form)

    form.add(createNameInputField())
    addDateInputFields(form)
    val excludedUserSelection = createUserSelection(ExcludedUsersId)
    form.add(excludedUserSelection)
    addAffectedUserPanel()
    form.add(new ServerMessageLabel(LockFormPanel.ServerMessageId, "formValidationError", new PropertyModel[String](model, "serverMessage")))
    form.add(createSubmitButton)
    form.add(createUnlockButton)

    def addAffectedUserPanel() {
      val link = new AjaxLink(LockFormPanel.ShowAffectedId) {
        override def onClick(target: AjaxRequestTarget) {
          toggleAffectedUsersPanel(form, target)
        }
      }

      form.add(link)
      form.add(new Container(LockFormPanel.AffectedContainerId))
    }

    def createSubmitButton: NonDemoAjaxButton = {
      val success: Callback = (target, form) => {
        val label = new Label(LockFormPanel.ServerMessageId, new Model("Locked"))
        label.setOutputMarkupPlaceholderTag(true)
        form.addOrReplace(label)
        target.add(label)

        val lock = getPanelModelObject.getLock
        val excludedUsers = excludedUserSelection.selectedUsers.getObject
        lock.setExcludedUsers(excludedUsers)

        val event = if (modelObject.isNew)
          LockAddedEvent(lock, target)
        else
          LockEditedEvent(lock, target)

        send(getPage, Broadcast.BREADTH, event)
      }

      val submitButton = NonDemoAjaxButton(LockFormPanel.SubmitId, form, success)
      submitButton
    }

    def createUnlockButton: NonDemoAjaxLink = {
      val unlockButton = NonDemoAjaxLink(LockFormPanel.UnlockId, (target) => {
        send(getPage, Broadcast.BREADTH, UnlockedEvent(getPanelModelObject.getLock, target))
      }, List(new JavaScriptConfirmation(new ResourceModel("general.deleteConfirmation"))))


      unlockButton.setVisible(!getPanelModelObject.isNew)
      unlockButton
    }
  }

  def toggleAffectedUsersPanel(form: Form[_], target: AjaxRequestTarget) {
    val replacement = if (isShowingAffectedUsersPanel(form))
      new Container(LockFormPanel.AffectedContainerId)
    else
      new LockAffectedUsersPanel(LockFormPanel.AffectedContainerId, model)

    updatePanel(form, target, replacement)
  }

  private def updatePanel(form: Form[_], target: AjaxRequestTarget, replacement: WebMarkupContainer) {
    replacement.setOutputMarkupId(true)
    form.addOrReplace(replacement)
    target.add(replacement)
  }

  private def createNameInputField(): TextField[String] = {
    val nameInputField = new TextField(LockFormPanel.NameId, new PropertyModel[String](model, "lock.name"))
    nameInputField.setOutputMarkupId(true)
    nameInputField
  }

  private def createUserSelection(id: String) = new MultiUserSelect(id)

  private def addDateInputFields(form: Form[_]) {
    val startDate = new LocalizedDatePicker("startDate", new PropertyModel[Date](model, "lock.dateStart"))
    def onDateChangeAjaxBehavior: OnChangeAjaxBehavior = new OnChangeAjaxBehavior {
      override def onUpdate(target: AjaxRequestTarget) {
        if (isShowingAffectedUsersPanel(form)) {
          target.add(getAffectedPanel(form))
        }

        val bean = self.getPanelModelObject
        bean.updateName(EhourWebSession.getEhourConfig.getFormattingLocale)
        target.add(form.get(LockFormPanel.NameId))
      }

      protected override def updateAjaxAttributes(attributes: AjaxRequestAttributes) {
        super.updateAjaxAttributes(attributes)
        attributes.getAjaxCallListeners.add(new LoadingSpinnerDecorator)
      }
    }

    startDate.add(onDateChangeAjaxBehavior)
    form.add(startDate)
    startDate.add(new ValidatingFormComponentAjaxBehavior)
    form.add(new AjaxFormComponentFeedbackIndicator("startDateValidationError", startDate))

    val endDate = new LocalizedDatePicker("endDate", new PropertyModel[Date](model, "lock.dateEnd"))
    endDate.add(onDateChangeAjaxBehavior)
    form.add(endDate)
    endDate.add(new ValidatingFormComponentAjaxBehavior)
    form.add(new AjaxFormComponentFeedbackIndicator("endDateValidationError", endDate))

    form.add(new DateOverlapValidator("dateStartDateEnd", startDate, endDate))
  }

  private def isShowingAffectedUsersPanel(form: Form[_]) = getAffectedPanel(form).isInstanceOf[LockAffectedUsersPanel]

  private def getAffectedPanel(form: Form[_]) = form.get(LockFormPanel.AffectedContainerId)
}

object LockFormPanel {
  val OuterBorderId = "outerBorder"
  val FormId = "lockForm"
  val NameId = "name"
  val ServerMessageId = "serverMessage"
  val AffectedUsersId = "affectedUsersPanel"
  val SubmitId = "submit"
  val UnlockId = "unlock"
  val ShowAffectedId = "showAffected"
  val AffectedContainerId = "affectedContainer"
}

case class LockAddedEvent(lock: TimesheetLock, override val target: AjaxRequestTarget) extends Event(target)

case class LockEditedEvent(lock: TimesheetLock, override val target: AjaxRequestTarget) extends Event(target)

case class UnlockedEvent(lock: TimesheetLock, override val target: AjaxRequestTarget) extends Event(target)
