package net.rrm.ehour.ui.admin.project

import net.rrm.ehour.ui.common.panel.AbstractBasePanel
import org.apache.wicket.model.IModel
import net.rrm.ehour.ui.common.border.GreySquaredRoundedBorder
import net.rrm.ehour.ui.common.util.WebGeo

class ProjectAdminFormContainer(id: String, model: IModel[ProjectAdminBackingBean]) extends AbstractBasePanel[Unit](id) {

  override def onInitialize() {
    super.onInitialize()

    val border = new GreySquaredRoundedBorder("border", WebGeo.AUTO)
    addOrReplace(border)

    border.addOrReplace(new ProjectFormPanel("form", model))
    border.addOrReplace(new AssignedUsersPanel("assignedUsers", model))
  }
}
