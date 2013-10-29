package net.rrm.ehour.ui.financial.lock

import net.rrm.ehour.ui.common.panel.AbstractAjaxPanel
import net.rrm.ehour.ui.common.border.{GreyBlueRoundedBorder, GreyRoundedBorder}
import org.apache.wicket.spring.injection.annot.SpringBean
import net.rrm.ehour.timesheet.service.{LockedTimesheet, TimesheetLockService}
import scala.collection.convert.WrapAsJava
import org.apache.wicket.markup.html.list.{ListItem, ListView}
import org.apache.wicket.markup.html.basic.Label

class ExistingLocksPanel(id: String) extends AbstractAjaxPanel[Unit](id) {
  @SpringBean
  protected var lockService: TimesheetLockService = _

  override def onInitialize() {
    super.onInitialize()

    val greyBorder = new GreyRoundedBorder(ExistingLocksPanel.OuterBorderId, "Existing locks")
    add(greyBorder)

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
}

object ExistingLocksPanel {
  val OuterBorderId = "outerBorder"
  val BlueBorderId = "innerBorder"
  val LocksId = "locks"
}

