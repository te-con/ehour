package net.rrm.ehour.ui.admin.project

import net.rrm.ehour.ui.common.panel.AbstractBasePanel
import org.apache.wicket.model.IModel

class ProjectAdminContainer(id: String, model: IModel[ProjectAdminBackingBean]) extends AbstractBasePanel[Unit](id) {

  override def onInitialize() {
    super.onInitialize()

    addOrReplace(new ProjectFormPanel("form", model))
    addOrReplace(new AssignedUsersPanel("assignedUsers", model))
  }
}
