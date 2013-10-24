package net.rrm.ehour.ui.financial.lock

import net.rrm.ehour.ui.common.panel.AbstractAjaxPanel
import net.rrm.ehour.ui.common.border.GreyBlueRoundedBorder

class ExistingLocksPanel(id: String) extends AbstractAjaxPanel[Unit](id) {
  override def onInitialize() {
    super.onInitialize()


    val greyBorder = new GreyBlueRoundedBorder(ExistingLocksPanel.BorderId)
    add(greyBorder)
  }
}

object ExistingLocksPanel {
  val BorderId = "border"
}
