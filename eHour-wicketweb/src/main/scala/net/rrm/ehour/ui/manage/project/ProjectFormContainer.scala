package net.rrm.ehour.ui.manage.project

import net.rrm.ehour.ui.common.panel.AbstractBasePanel
import net.rrm.ehour.ui.common.wicket.Container
import net.rrm.ehour.ui.manage.project.assign.ManageAssignmentsPanel
import org.apache.wicket.model.IModel

class ProjectFormContainer[T <: ProjectAdminBackingBean] (id: String, model: IModel[T]) extends AbstractBasePanel(id, model) {
  val AssignedPanelId = "assignedUserPanel"

  override def onInitialize() {
    super.onInitialize()

    addOrReplace(createFormPanel("projectFormPanel"))

    if (model.getObject.isNew)
      addOrReplace(new Container(AssignedPanelId))
    else
      addOrReplace(new ManageAssignmentsPanel(AssignedPanelId, model))
  }

  protected def createFormPanel(id: String): ProjectFormPanel[T] = new ProjectFormPanel[T](id, model)
}
