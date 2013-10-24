package net.rrm.ehour.ui.financial.lock

import net.rrm.ehour.ui.common.panel.AbstractAjaxPanel
import net.rrm.ehour.ui.common.border.GreyRoundedBorder

class LockDetailsPanel(id: String) extends AbstractAjaxPanel[Unit](id) {
  override def onInitialize() {
    super.onInitialize()

    val greyBorder = new GreyRoundedBorder(LockDetailsPanel.BorderId)
    add(greyBorder)
  }
}

object LockDetailsPanel {
  val BorderId = "border"
}
