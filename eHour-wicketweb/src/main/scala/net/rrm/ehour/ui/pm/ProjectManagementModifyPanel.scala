package net.rrm.ehour.ui.pm

import net.rrm.ehour.ui.common.panel.AbstractBasePanel
import net.rrm.ehour.domain.Project
import net.rrm.ehour.ui.admin.project.{ManageAssignmentsPanel, ProjectAdminBackingBean}
import org.apache.wicket.model.{ResourceModel, Model}
import net.rrm.ehour.ui.common.border.GreyRoundedBorder
import net.rrm.ehour.ui.common.wicket.AjaxLink
import net.rrm.ehour.project.service.ProjectAssignmentManagementService
import org.apache.wicket.spring.injection.annot.SpringBean
import org.apache.wicket.markup.html.basic.Label
import net.rrm.ehour.config.PmPrivilege

class ProjectManagementModifyPanel(id: String, project: Project) extends AbstractBasePanel(id) {
  @SpringBean
  protected var assignmentMgmtService: ProjectAssignmentManagementService = _

  override def onInitialize() = {
    super.onInitialize()

    val border = new GreyRoundedBorder("border", project.getName)
    addOrReplace(border)

    val adminBackingBean = new ProjectAdminBackingBean(project)
    val privilege = getConfig.getPmPrivilege

    val assignedUsersPanel = new ManageAssignmentsPanel("assignments", new Model(adminBackingBean), onlyDeactivation = privilege == PmPrivilege.DEACTIVATE_ONLY)
    border.add(assignedUsersPanel)

    val placeholderLabel = new Label("serverMessage", "")
    placeholderLabel.setOutputMarkupPlaceholderTag(true)
    placeholderLabel.setOutputMarkupId(true)
    placeholderLabel.setVisible(false)
    border.add(placeholderLabel)

    border.add(new AjaxLink("submitButton", target => {
//      toScala(assignedUsersPanel.getPanelModelObject.getAssignmentsQueue).map(assignmentMgmtService.updateProjectAssignment)
//      toScala(assignedUsersPanel.getPanelModelObject.getRemovalQueue).map(assignmentMgmtService.deleteProjectAssignment)
//      assignedUsersPanel.getPanelModelObject.clearQueue

      // TODO use new panel

      val label = new Label("serverMessage", new ResourceModel("pm.admin.assignments.saved"))
      placeholderLabel.setOutputMarkupId(true)
      border.addOrReplace(label)

      target.add(label)
    }))
  }
}
