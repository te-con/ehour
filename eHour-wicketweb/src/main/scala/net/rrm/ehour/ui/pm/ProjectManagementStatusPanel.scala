package net.rrm.ehour.ui.pm

import net.rrm.ehour.domain.Project
import net.rrm.ehour.ui.common.panel.AbstractBasePanel
import net.rrm.ehour.ui.common.border.GreyRoundedBorder

class ProjectManagementStatusPanel (id: String, project: Project) extends AbstractBasePanel(id) {
  override def onInitialize() {
    super.onInitialize()

    val border = new GreyRoundedBorder("border", project.getName)
    addOrReplace(border)
  }
}
