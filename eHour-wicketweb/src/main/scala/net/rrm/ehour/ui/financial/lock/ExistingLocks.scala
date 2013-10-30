package net.rrm.ehour.ui.financial.lock

import net.rrm.ehour.ui.common.panel.AbstractAjaxPanel
import net.rrm.ehour.ui.common.border.{GreyBlueRoundedBorder, GreyRoundedBorder}
import org.apache.wicket.spring.injection.annot.SpringBean
import net.rrm.ehour.timesheet.service.{LockedTimesheet, TimesheetLockService}
import scala.collection.convert.WrapAsJava
import org.apache.wicket.markup.html.list.{ListItem, ListView}
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.event.IEvent

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

    val repeater = new ListView[LockedTimesheet](ExistingLocksPanel.LocksId, WrapAsJava.seqAsJavaList(timesheetLocks)) {
      override def populateItem(item: ListItem[LockedTimesheet]) = {
        val timesheet = item.getModelObject

        item.add(new Label("startDate", timesheet.startDate.toString("MM/dd/YYYY")))
        item.add(new Label("endDate", timesheet.endDate.toString("MM/dd/YYYY")))
      }
    }

    blueBorder.add(repeater)


  }

  override def onEvent(event: IEvent[_]) {
    Console.out.println(event.getPayload)
    Console.err.println(event.getPayload.getClass)
    event.getPayload match {
      case lockedAddedEvent: LockAddedEvent => lockedAddedEvent.refresh(this)
      case _ => Console.out.println("fefe")
    }
  }
}

object ExistingLocksPanel {
  val OuterBorderId = "outerBorder"
  val BlueBorderId = "innerBorder"
  val LocksId = "locks"
}

