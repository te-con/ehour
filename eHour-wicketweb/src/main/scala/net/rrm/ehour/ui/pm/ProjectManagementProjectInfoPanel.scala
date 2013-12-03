package net.rrm.ehour.ui.pm

import net.rrm.ehour.ui.common.panel.AbstractBasePanel
import net.rrm.ehour.domain.Project
import net.rrm.ehour.ui.admin.project.{AssignedUsersPanel, ProjectAdminBackingBean}
import org.apache.wicket.model.Model
import net.rrm.ehour.ui.common.border.GreyRoundedBorder

class ProjectManagementProjectInfoPanel(id: String, project: Project) extends AbstractBasePanel(id) {
  override def onInitialize() = {
    super.onInitialize()

    val border = new GreyRoundedBorder("border", project.getName)
    addOrReplace(border)

    val adminBackingBean = new ProjectAdminBackingBean(project)
    border.add(new AssignedUsersPanel("assignments", new Model(adminBackingBean), onlyDeactivation = true))
  }
}
