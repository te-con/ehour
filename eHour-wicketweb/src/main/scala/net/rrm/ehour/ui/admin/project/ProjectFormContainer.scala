package net.rrm.ehour.ui.admin.project

import org.apache.wicket.model.IModel
import net.rrm.ehour.ui.common.panel.AbstractBasePanel

class ProjectFormContainer(id: String, model: IModel[ProjectAdminBackingBean]) extends AbstractBasePanel(id, model) {
  override def onInitialize() {
    super.onInitialize()

    addOrReplace(new ProjectFormPanel("projectFormPanel", model))
    addOrReplace(new ManageAssignmentsPanel("assignedUserPanel", model, onlyDeactivation = false))
  }
}
