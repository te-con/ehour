package net.rrm.ehour.ui.admin.project

import org.apache.wicket.model.IModel
import net.rrm.ehour.ui.common.panel.AbstractBasePanel
import net.rrm.ehour.ui.admin.project.assign.ManageAssignmentsPanel
import net.rrm.ehour.ui.common.wicket.Container

class ProjectFormContainer(id: String, model: IModel[ProjectAdminBackingBean]) extends AbstractBasePanel(id, model) {
  val AssignedPanelId = "assignedUserPanel"

  override def onInitialize() {
    super.onInitialize()

    addOrReplace(new ProjectFormPanel("projectFormPanel", model))

    if (model.getObject.isNew)
      addOrReplace(new Container(AssignedPanelId))
    else
      addOrReplace(new ManageAssignmentsPanel(AssignedPanelId, model))
  }
}
