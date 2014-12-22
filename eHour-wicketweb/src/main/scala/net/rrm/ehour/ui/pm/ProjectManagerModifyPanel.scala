package net.rrm.ehour.ui.pm

import net.rrm.ehour.config.PmPrivilege
import net.rrm.ehour.domain.Project
import net.rrm.ehour.project.service.ProjectAssignmentManagementService
import net.rrm.ehour.ui.common.border.GreyRoundedBorder
import net.rrm.ehour.ui.common.panel.AbstractBasePanel
import net.rrm.ehour.ui.manage.project.ProjectAdminBackingBean
import net.rrm.ehour.ui.manage.project.assign.{ManageAssignmentsPanel, ManagementPanelConfig}
import org.apache.wicket.model.Model
import org.apache.wicket.spring.injection.annot.SpringBean

class ProjectManagerModifyPanel(id: String, project: Project) extends AbstractBasePanel(id) {
  @SpringBean
  protected var assignmentMgmtService: ProjectAssignmentManagementService = _

  override def onInitialize() = {
    super.onInitialize()

    val border = new GreyRoundedBorder("border", project.getName)
    addOrReplace(border)

    val adminBackingBean = new ProjectAdminBackingBean(project)
    val privilege = getConfig.getPmPrivilege

    val panelConfig = ManagementPanelConfig(onlyDeactivation = privilege == PmPrivilege.DEACTIVATE_ONLY, borderless = true, wide = false)
    val assignedUsersPanel = new ManageAssignmentsPanel("assignments", new Model(adminBackingBean), panelConfig)
    border.add(assignedUsersPanel)
  }
}
