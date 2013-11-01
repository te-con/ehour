package net.rrm.ehour.ui.financial.lock

import net.rrm.ehour.ui.common.panel.AbstractAjaxPanel
import net.rrm.ehour.ui.common.border.{GreyBlueRoundedBorder, GreyRoundedBorder}
import org.apache.wicket.spring.injection.annot.SpringBean
import net.rrm.ehour.timesheet.service.{LockedTimesheet, TimesheetLockService}
import scala.collection.convert.WrapAsJava
import org.apache.wicket.markup.html.list.{ListItem, ListView}
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.event.IEvent
import org.apache.wicket.AttributeModifier

class ExistingLocksPanel(id: String) extends AbstractAjaxPanel[Unit](id) {
  @SpringBean
  protected var lockService: TimesheetLockService = _

  override def onInitialize() {
    super.onInitialize()

    setOutputMarkupId(true)
  }

  override def onBeforeRender() {
    super.onBeforeRender()

    val greyBorder = new GreyRoundedBorder(ExistingLocksPanel.OuterBorderId, "Existing locks")
    addOrReplace(greyBorder)

    val blueBorder = new GreyBlueRoundedBorder(ExistingLocksPanel.BlueBorderId)
    blueBorder.setOutputMarkupId(true)
    greyBorder.add(blueBorder)

    val timesheetLocks = lockService.findAll()

    implicit val locale = getEhourWebSession.getEhourConfig.getFormattingLocale

    val repeater = new ListView[LockedTimesheet](ExistingLocksPanel.LocksId, WrapAsJava.seqAsJavaList(timesheetLocks)) {
      override def populateItem(item: ListItem[LockedTimesheet]) = {
        val timesheet = item.getModelObject

        if (item.getIndex == 0) {
          item.add(AttributeModifier.append("class", "firstRow"))
        } else if (item.getIndex % 2 == 1) {
          item.add(AttributeModifier.append("class", "oddRow"))
        }

        item.add(new Label("startDate", timesheet.dateStart.toString("MM/dd/YYYY")))
        item.add(new Label("endDate", timesheet.dateEnd.toString("MM/dd/YYYY")))
        item.add(new Label("name", timesheet.lockName))
      }
    }

    blueBorder.add(repeater)
  }

  override def onEvent(event: IEvent[_]) {
    event.getPayload match {
      case lockedAddedEvent: LockAddedEvent => lockedAddedEvent.refresh(this)
      case _ =>
    }
  }
}

object ExistingLocksPanel {
  val OuterBorderId = "outerBorder"
  val BlueBorderId = "innerBorder"
  val LocksId = "locks"
}

