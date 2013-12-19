package net.rrm.ehour.ui.financial.lock

import net.rrm.ehour.ui.common.panel.AbstractAjaxPanel
import net.rrm.ehour.ui.common.border.GreyRoundedBorder
import org.apache.wicket.markup.html.form.{TextField, Form}
import org.apache.wicket.model.{IModel, PropertyModel}
import net.rrm.ehour.ui.common.wicket._
import net.rrm.ehour.ui.common.wicket.WicketDSL._

import net.rrm.ehour.ui.common.wicket.AjaxButton._
import org.apache.wicket.spring.injection.annot.SpringBean
import net.rrm.ehour.timesheet.service.{LockedTimesheet, TimesheetLockService}
import net.rrm.ehour.ui.common.panel.datepicker.LocalizedDatePicker
import java.util.Date
import org.joda.time.LocalDate
import scala.language.implicitConversions
import net.rrm.ehour.ui.common.component.PlaceholderPanel
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.event.{IEvent, Broadcast}
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior
import net.rrm.ehour.ui.common.event.Event
import net.rrm.ehour.util.DateUtil

class LockDetailsPanel(id: String, lockModel: IModel[LockModel]) extends AbstractAjaxPanel[LockModel](id, lockModel) {
  def this(id: String) {
    this(id, Model(LockModel.apply()))
  }

  @SpringBean
  protected var lockService: TimesheetLockService = _

  implicit private def dateToLocalDate(date: Date): LocalDate = LocalDate.fromDateFields(date)

  setOutputMarkupId(true)

  val self = this

  override def onBeforeRender() {
    super.onBeforeRender()

    val model = getPanelModelObject
    val outerBorder = new GreyRoundedBorder(LockDetailsPanel.OuterBorderId, "Timesheet lock details")
    addOrReplace(outerBorder)

    val affectedUsersPanel = new AffectedUsersPanel(LockDetailsPanel.AffectedUsersId, getPanelModel)
    affectedUsersPanel.setOutputMarkupId(true)
    outerBorder.addOrReplace(affectedUsersPanel)

    val form = new Form[Unit](LockDetailsPanel.FormId)
    outerBorder.add(form)

    form.add(new TextField("name", new PropertyModel[String](model, "name")))

    val startDate = new LocalizedDatePicker("startDate", new PropertyModel[Date](model, "startDate"))
    startDate.add(new OnChangeAjaxBehavior {
      def onUpdate(target: AjaxRequestTarget) {
        target.add(affectedUsersPanel)
      }
    })
    form.add(startDate)

    val endDate = new LocalizedDatePicker("endDate", new PropertyModel[Date](model, "endDate"))
    endDate.add(new OnChangeAjaxBehavior {
      def onUpdate(target: AjaxRequestTarget) {
        target.add(affectedUsersPanel)
      }
    })
    form.add(endDate)

    form.add(new PlaceholderPanel(LockDetailsPanel.SaveConfirmId))

    val success: Callback = (target, form) => {
      model.id match {
        case Some(lockId) => lockService.updateExisting(lockId, model.startDate, model.endDate, model.name)
        case None => lockService.createNew(model.startDate, model.endDate)
      }

      val label = createNotificationLabel(Model("Locked"))
      form.addOrReplace(label)
      target.add(label)

      send(self.getParent, Broadcast.BREADTH, LockModifiedEvent(target))
    }

    val submitButton = AjaxButton("submit", form, success)
    form.add(submitButton)

    val unlockButton = AjaxButton("unlock", form, (target, form) => {
      lockService.deleteLock(getPanelModelObject.id.get)

      send(self.getParent, Broadcast.BREADTH, LockModifiedEvent(target))

      val replacement = new LockDetailsPanel(self.getId)
      self.replaceWith(replacement)
      target.add(replacement)
    })

    unlockButton.setVisible(getPanelModelObject.id.isDefined)
    form.add(unlockButton)

  }


  def createNotificationLabel(model: IModel[String]): Label = {
    val label = new Label(LockDetailsPanel.SaveConfirmId, model)
    label.setOutputMarkupId(true)
    label
  }

  override def onEvent(event: IEvent[_]) {
    def editTimesheet(event: EditLockEvent) {
      lockService.find(event.id) match {
        case Some(lockedTimesheet) => setPanelModelObject(LockModel(lockedTimesheet))
        case None => setPanelModelObject(LockModel())
      }

      event.refresh(this)
    }

    event.getPayload match {
      case event: EditLockEvent => editTimesheet(event)
      case _ =>
    }
  }
}

object LockDetailsPanel {
  val OuterBorderId = "outerBorder"
  val FormId = "lockForm"
  val SaveConfirmId = "saveConfirm"
  val AffectedUsersId = "affectedUsersPanel"
}

class LockModel(val id: Option[Int] = None, var name: String = "", var startDate: Date = new Date(), var endDate: Date = new Date()) extends Serializable

object LockModel {
  def apply(lockedTimesheet: LockedTimesheet): LockModel = new LockModel(lockedTimesheet.id, lockedTimesheet.name.getOrElse(""), lockedTimesheet.dateStart.toDate, lockedTimesheet.dateStart.toDate)

  def apply(): LockModel = {
    val month = DateUtil.getDateRangeForMonth(new Date())
    new LockModel(startDate = month.getDateStart, endDate = month.getDateEnd)
  }
}

case class LockModifiedEvent(target: AjaxRequestTarget) extends Event(target)
