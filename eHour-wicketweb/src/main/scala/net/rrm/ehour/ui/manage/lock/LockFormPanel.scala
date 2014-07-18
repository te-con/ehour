package net.rrm.ehour.ui.manage.lock

import java.util.Date

import net.rrm.ehour.domain.{TimesheetLock, User}
import net.rrm.ehour.ui.common.border.GreySquaredRoundedBorder
import net.rrm.ehour.ui.common.component._
import net.rrm.ehour.ui.common.decorator.LoadingSpinnerDecorator
import net.rrm.ehour.ui.common.panel.AbstractFormSubmittingPanel
import net.rrm.ehour.ui.common.panel.datepicker.LocalizedDatePicker
import net.rrm.ehour.ui.common.panel.multiselect.{MultiUserSelect, SelectionUpdatedEvent}
import net.rrm.ehour.ui.common.session.EhourWebSession
import net.rrm.ehour.ui.common.util.WebGeo
import net.rrm.ehour.ui.common.validator.DateOverlapValidator
import net.rrm.ehour.ui.common.wicket.AjaxButton._
import net.rrm.ehour.ui.common.wicket.AjaxLink.LinkCallback
import net.rrm.ehour.ui.common.wicket._
import org.apache.wicket.Component
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior
import org.apache.wicket.event.{Broadcast, IEvent}
import org.apache.wicket.markup.html.WebMarkupContainer
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.border.Border
import org.apache.wicket.markup.html.form.{Form, TextField}
import org.apache.wicket.markup.html.list.{ListItem, ListView}
import org.apache.wicket.markup.html.panel.Fragment
import org.apache.wicket.model.{IModel, Model, PropertyModel, ResourceModel}

class LockFormPanel(id: String, model: IModel[LockAdminBackingBean]) extends AbstractFormSubmittingPanel[LockAdminBackingBean](id, model) {
  val self = this

  var affectedUsersShown = false

  override def onInitialize() {
    super.onInitialize()

    val modelObject = getPanelModelObject
    val outerBorder = new GreySquaredRoundedBorder(LockFormPanel.OuterBorderId, WebGeo.AUTO)
    addOrReplace(outerBorder)

    val form = new Form(LockFormPanel.FormId)
    outerBorder.add(form)

    form.add(createNameInputField())
    addDateInputFields(form)
    addUserSelection(form)
    createAffectedUserPanel(LockFormPanel.AffectedId)
    form.add(new ServerMessageLabel(LockFormPanel.ServerMessageId, "formValidationError", new PropertyModel[String](model, "serverMessage")))
    form.add(createSubmitButton)
    form.add(createUnlockButton)

    def createAffectedUserPanel(id: String) {
      getPage match {
        case p: LockManagePage => if (p.affectedUsersShown) showAffectedUserPanel(id) else hideAffectedUserPanel(id)
        case _ => if (affectedUsersShown) showAffectedUserPanel(id) else hideAffectedUserPanel(id)
      }
    }

    def showAffectedUserPanel(id: String) {
      displayAffectedUserPanel(id, "showHours", showAfterClick = false, new LockAffectedUsersPanel(LockFormPanel.AffectedContainerId, model))
    }

    def hideAffectedUserPanel(id: String) {
      displayAffectedUserPanel(id, "hideHours", showAfterClick = true, new Container(LockFormPanel.AffectedContainerId))
    }

    def displayAffectedUserPanel(id: String, fragmentId: String, showAfterClick: Boolean, panel: WebMarkupContainer) {
      val f = new Fragment(id, fragmentId, this)
      f.setOutputMarkupId(true)

      val showAffectedCallback: LinkCallback = target => toggleAffectedUsersPanel(id, target, show = showAfterClick)
      val link = new AjaxLink("affectedLinkToggle", showAffectedCallback)
      f.add(link)
      form.addOrReplace(f)

     panel.setOutputMarkupId(true)
     form.addOrReplace(panel)
    }

    def toggleAffectedUsersPanel(id: String, target: AjaxRequestTarget, show: Boolean) {
      getPage match {
        case p: LockManagePage => p.affectedUsersShown = show
        case _ => affectedUsersShown = show
      }

      createAffectedUserPanel(id)

      target.add(form.get(LockFormPanel.AffectedId))
      target.add(form.get(LockFormPanel.AffectedContainerId))
    }

    def createSubmitButton: NonDemoAjaxButton = {
      val success: Callback = (target, form) => {
        val label = new Label(LockFormPanel.ServerMessageId, new Model("Locked"))
        label.setOutputMarkupPlaceholderTag(true)
        form.addOrReplace(label)
        target.add(label)

        val lock = getPanelModelObject.getLock

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

  private def isShowingAffectedUsersPanel(form: Form[_]) = getAffectedPanel(form).isInstanceOf[LockAffectedUsersPanel]

  private def getAffectedPanel(form: Form[_]) = form.get(LockFormPanel.AffectedContainerId)

  override def onEvent(wrappedEvent: IEvent[_]) {
    wrappedEvent.getPayload match {
      case event: SelectionUpdatedEvent =>
        val component = get(LockFormPanel.OuterBorderId).asInstanceOf[Border].getBodyContainer
        val form = component.get(LockFormPanel.FormId).asInstanceOf[Form[_]]

        if (isShowingAffectedUsersPanel(form)) {
          updatePanel(form, event.target, getAffectedPanel(form))
        }
      case _ =>
    }
  }

  private def updatePanel(form: Form[_], target: AjaxRequestTarget, replacement: Component) {
    replacement.setOutputMarkupId(true)
    form.addOrReplace(replacement)
    target.add(replacement)
  }

  private def createNameInputField(): TextField[String] = {
    val nameInputField = new TextField(LockFormPanel.NameId, new PropertyModel[String](model, "lock.name"))
    nameInputField.setOutputMarkupId(true)
    nameInputField
  }

  // ugly..
  private def addUserSelection(form: Form[_]): Fragment = {
    val users = getPanelModelObject.getLock.getExcludedUsers

    val fragment: Fragment = if (users.isEmpty) {
      new Fragment(LockFormPanel.ExcludedUsersId, "noExcludedUsers", self)
    } else {
      createReadOnlyExcludedUsersView()
    }

    fragment.setOutputMarkupId(true)

    val linkCallback: LinkCallback = target => {
      val f = new Fragment(LockFormPanel.ExcludedUsersId, "selectUsersToExclude", self)
      f.setOutputMarkupId(true)

      f.add(new MultiUserSelect("userSelect", new PropertyModel(getPanelModel, "lock.excludedUsers")))
      f.setOutputMarkupId(true)
      target.add(f)
      form.addOrReplace(f)

      val cb: LinkCallback = target => {
        val replacement = addUserSelection(form)
        target.add(replacement)
      }

      val link = new AjaxLink("hide", cb)
      f.add(link)

    }
    val link = new AjaxLink("modify", linkCallback)
    fragment.add(link)

    form.addOrReplace(fragment)

    fragment
  }

  def createReadOnlyExcludedUsersView(): Fragment = {
    val f = new Fragment(LockFormPanel.ExcludedUsersId, "readOnlyExcludedUsers", self)

    f.setOutputMarkupId(true)

    f.add(new ListView[User]("excluded", new PropertyModel(model, "lock.excludedUsers")) {
      override def populateItem(item: ListItem[User]) {
        val userFullName = item.getModelObject.getFullName

        val labelText = if (item.getIndex < getList.size() - 1) s"$userFullName, " else userFullName

        item.add(new Label("name", labelText))
      }
    })
    f
  }

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
}

object LockFormPanel {
  val OuterBorderId = "outerBorder"
  val FormId = "lockForm"
  val NameId = "name"
  val ServerMessageId = "serverMessage"
  val AffectedUsersId = "affectedUsersPanel"
  val SubmitId = "submit"
  val UnlockId = "unlock"
  val AffectedId = "affected"
  val AffectedContainerId = "affectedContainer"
  val ExcludedUsersId = "excludedUsers"
}

case class LockAddedEvent(lock: TimesheetLock, override val target: AjaxRequestTarget) extends Event(target)

case class LockEditedEvent(lock: TimesheetLock, override val target: AjaxRequestTarget) extends Event(target)

case class UnlockedEvent(lock: TimesheetLock, override val target: AjaxRequestTarget) extends Event(target)
