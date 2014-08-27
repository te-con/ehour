package net.rrm.ehour.ui.manage.project

import net.rrm.ehour.ui.common.panel.AbstractBasePanel
import org.apache.wicket.model.IModel

class ProjectFormContainer(id: String, model: IModel[ProjectAdminBackingBean]) extends AbstractBasePanel(id, model) {
  val AssignedPanelId = "assignedUserPanel"

  override def onInitialize() {
    super.onInitialize()

    addOrReplace(new ProjectFormPanel("projectFormPanel", model))
  }
}
