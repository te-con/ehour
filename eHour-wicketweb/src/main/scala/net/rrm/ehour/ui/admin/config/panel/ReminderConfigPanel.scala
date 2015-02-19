package net.rrm.ehour.ui.admin.config.panel

import net.rrm.ehour.appconfig.EhourSystemConfig
import net.rrm.ehour.ui.admin.config.MainConfigBackingBean
import net.rrm.ehour.ui.common.component.{AjaxFormComponentFeedbackIndicator, ValidatingFormComponentAjaxBehavior}
import net.rrm.ehour.ui.common.panel.AbstractBasePanel
import net.rrm.ehour.ui.common.wicket.Container
import org.apache.commons.lang.WordUtils
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox
import org.apache.wicket.markup.html.form._
import org.apache.wicket.model.{IModel, ResourceModel}
import org.apache.wicket.spring.injection.annot.SpringBean
import org.apache.wicket.validation.validator.StringValidator

class ReminderConfigPanel(id: String, model: IModel[MainConfigBackingBean]) extends AbstractBasePanel[MainConfigBackingBean](id, model) {
  @SpringBean
  var ehourSystemConfig: EhourSystemConfig = _

  import scala.collection.JavaConversions._
  override def onInitialize() {
    super.onInitialize()

    val form = new Form("reminderForm", model)
    addOrReplace(form)

    val disabled = new Container("mailDisabled")
    disabled.setVisible(!ehourSystemConfig.isEnableMail)
    form.add(disabled)

    val reminderMinHoursContainer = new ReminderContainer("reminderMinHoursContainer")
    form.add(reminderMinHoursContainer)

    val reminderMinHours = new TextField[Integer]("config.reminderMinimalHours")
    reminderMinHours.add(new UpdateBehavior)
    reminderMinHours.add(new ValidatingFormComponentAjaxBehavior)
    reminderMinHoursContainer.add(new AjaxFormComponentFeedbackIndicator("minHoursValidationError", reminderMinHours))
    reminderMinHoursContainer.add(reminderMinHours)

    val reminderMinHoursHelpContainer = new ReminderContainer("reminderMinHoursHelpContainer")
    form.add(reminderMinHoursHelpContainer)

    val reminderCcContainer = new ReminderContainer("reminderCcContainer")
    form.add(reminderCcContainer)

    val reminderCc = new TextField[String]("config.reminderCC")
    reminderCc.add(new UpdateBehavior)
    reminderCcContainer.add(reminderCc)

    val reminderCcHelpContainer = new ReminderContainer("reminderCcHelpContainer")
    form.add(reminderCcHelpContainer)

    val reminderBodyContainer = new ReminderContainer("reminderBodyContainer")
    form.add(reminderBodyContainer)
    
    val reminderBody = new TextArea[String]("config.reminderBody")
    reminderBody.add(new ValidatingFormComponentAjaxBehavior)
    reminderBody.add(StringValidator.maximumLength(4095))
    reminderBodyContainer.add(new AjaxFormComponentFeedbackIndicator("bodyValidationError", reminderBody))
    reminderBody.setLabel(new ResourceModel("admin.config.reminder.body"))
    reminderBody.add(new UpdateBehavior)
    reminderBody.setEscapeModelStrings(false)

    reminderBodyContainer.add(reminderBody)

    val reminderSubjectContainer = new ReminderContainer("reminderSubjectContainer")
    form.add(reminderSubjectContainer)

    val reminderSubject = new TextField[String]("config.reminderSubject")
    reminderSubject.add(new ValidatingFormComponentAjaxBehavior)
    reminderSubject.add(StringValidator.maximumLength(4095))
    reminderSubjectContainer.add(new AjaxFormComponentFeedbackIndicator("subjectValidationError", reminderBody))
    reminderSubject.setLabel(new ResourceModel("admin.config.reminder.subject"))
    reminderSubject.add(new UpdateBehavior)
    reminderSubjectContainer.add(reminderSubject)

    val reminderTimeContainer = new ReminderContainer("reminderTimeContainer")
    form.add(reminderTimeContainer)

    reminderTimeContainer.add(new DropDownChoice[String]("reminderDay", MainConfigBackingBean.VALID_REMINDER_DAYS, new IChoiceRenderer[String] {
      override def getDisplayValue(obj: String) = WordUtils.capitalizeFully(obj)

      override def getIdValue(obj: String, index: Int) = Integer.toString(index)
    }))

    reminderTimeContainer.add(new DropDownChoice[Integer]("reminderHour", (0 to 23).toList.asInstanceOf[List[java.lang.Integer]]))
    reminderTimeContainer.add(new DropDownChoice[Integer]("reminderMinute", (0 to 59).toList.asInstanceOf[List[java.lang.Integer]]))

    val reminderEnabledCheckbox = new AjaxCheckBox("config.reminderEnabled") {
      override protected def onUpdate(target: AjaxRequestTarget) {
        target.add(reminderTimeContainer, reminderSubjectContainer, reminderBodyContainer, reminderCcContainer, reminderCcHelpContainer, reminderMinHoursContainer, reminderMinHoursHelpContainer)
      }
    }
    reminderEnabledCheckbox.setMarkupId("reminderEnabled")
    form.add(reminderEnabledCheckbox)
  }

  private class ReminderContainer(id: String) extends Container(id) {
    setOutputMarkupPlaceholderTag(true)

    override def isVisible: Boolean = getPanelModelObject.getConfig.isReminderEnabled
  }

  private class UpdateBehavior extends AjaxFormComponentUpdatingBehavior("change") {
    protected def onUpdate(target: AjaxRequestTarget) {}
  }
}
