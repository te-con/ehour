package net.rrm.ehour.ui.admin.project

import org.apache.wicket.model.IModel
import net.rrm.ehour.ui.common.panel.AbstractBasePanel
import java.lang.Boolean

class ManageAssignmentsPanel(id: String, model: IModel[ProjectAdminBackingBean], onlyDeactivation:Boolean = false) extends AbstractBasePanel(id, model) {
  override def onInitialize() {
    super.onInitialize()

    addOrReplace(new AssignedUsersPanel("assignedUserPanel", model, onlyDeactivation = onlyDeactivation))
  }
}
