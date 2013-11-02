package net.rrm.ehour.ui.financial.lock

import net.rrm.ehour.ui.common.panel.AbstractAjaxPanel
import net.rrm.ehour.ui.common.border.GreyRoundedBorder
import org.apache.wicket.markup.html.form.{TextField, Form}
import org.apache.wicket.model.{Model, PropertyModel}
import net.rrm.ehour.ui.common.wicket.AjaxButton
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
import org.apache.wicket.Component

class LockDetailsPanel(id: String) extends AbstractAjaxPanel[LockModel](id, new Model[LockModel](new LockModel())) {
  @SpringBean
  protected var lockService: TimesheetLockService = _

  implicit private def dateToLocalDate(date: Date): LocalDate = LocalDate.fromDateFields(date)

  setOutputMarkupId(true)

  override def onBeforeRender() {
    super.onBeforeRender()

    val model = getPanelModelObject
    val outerBorder = new GreyRoundedBorder(LockDetailsPanel.OuterBorderId, "Timesheet lock details")
    addOrReplace(outerBorder)

    val form = new Form[Unit](LockDetailsPanel.FormId)
    outerBorder.add(form)

    form.add(new TextField("name", new PropertyModel[String](model, "name")))

    val startDate = new LocalizedDatePicker("startDate", new PropertyModel[Date](model, "startDate"))
    form.add(startDate)

    val endDate = new LocalizedDatePicker("endDate", new PropertyModel[Date](model, "endDate"))
    form.add(endDate)

    form.add(new PlaceholderPanel(LockDetailsPanel.SaveConfirmId))

    val success: Callback = (target, form) => {
      lockService.createNew(model.startDate, model.endDate)

      val label = new Label(LockDetailsPanel.SaveConfirmId, "Locked")
      label.setOutputMarkupId(true)
      form.addOrReplace(label)
      target.add(label)

      send(this, Broadcast.BREADTH, LockAddedEvent(target))
    }

    val submitButton = new AjaxButton("submit", form, success)
    form.add(submitButton)
  }

  override def onEvent(event: IEvent[_]) {
    def editTimesheet(event: EditLockEvent) {
      lockService.find(event.id) match {
        case Some(lockedTimesheet) => setPanelModelObject(LockModel(lockedTimesheet))
        case None =>
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
}

class LockModel(var id: Option[Int] = None, var name: String = "", var startDate: Date = new Date(), var endDate: Date = new Date()) extends Serializable

object LockModel {
  def apply(lockedTimesheet: LockedTimesheet): LockModel = new LockModel(lockedTimesheet.id, lockedTimesheet.name.getOrElse(""), lockedTimesheet.dateStart.toDateMidnight.toDate, lockedTimesheet.dateStart.toDateMidnight.toDate)
}



case class LockAddedEvent(target: AjaxRequestTarget) {
  def refresh(components: Component*) {
    target.add(components: _*)
  }
}