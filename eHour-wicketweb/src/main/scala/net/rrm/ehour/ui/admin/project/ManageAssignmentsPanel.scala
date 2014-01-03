package net.rrm.ehour.ui.admin.project

import org.apache.wicket.model.{CompoundPropertyModel, IModel}
import net.rrm.ehour.ui.common.panel.AbstractAjaxPanel
import java.lang.Boolean
import org.apache.wicket.event.IEvent
import net.rrm.ehour.ui.common.wicket.Container
import net.rrm.ehour.ui.admin.assignment.{AssignmentAjaxEventType, AssignmentAdminBackingBean, AssignmentFormPanel}
import net.rrm.ehour.ui.common.event.AjaxEvent
import net.rrm.ehour.ui.admin.assignment.form.AssignmentFormComponentContainerPanel.DisplayOption

class ManageAssignmentsPanel(id: String, model: IModel[ProjectAdminBackingBean], onlyDeactivation:Boolean = false) extends AbstractAjaxPanel(id, model) {
  override def onInitialize() {
    super.onInitialize()

    addOrReplace(createrAssignedUsersPanel)

    addOrReplace(createFormContainer)
  }

  def createFormContainer: Container = {
    new Container(ManageAssignmentsPanel.FORM_ID)
  }

  def createrAssignedUsersPanel: AssignedUsersPanel = {
    new AssignedUsersPanel(ManageAssignmentsPanel.ASSIGNED_USER_ID, model, onlyDeactivation = onlyDeactivation)
  }

  // Wicket 6 event system
  override def onEvent(event: IEvent[_]): Unit = {
    event.getPayload match {
      case event: EditAssignmentEvent => editAssignment(event)
      case _ =>
    }
  }


  // own legacy event system...
  override def ajaxEventReceived(ajaxEvent: AjaxEvent): Boolean = {
    if (ajaxEvent.getEventType == AssignmentAjaxEventType.ASSIGNMENT_UPDATED || ajaxEvent.getEventType == AssignmentAjaxEventType.ASSIGNMENT_DELETED) {
      val replacement = createrAssignedUsersPanel
      addOrReplace(replacement)
      ajaxEvent.getTarget.add(replacement)

      val container = createFormContainer
      addOrReplace(container)
      ajaxEvent.getTarget.add(container)
    }

    true
  }

  def editAssignment(event: EditAssignmentEvent) {
    val model = new CompoundPropertyModel[AssignmentAdminBackingBean](new AssignmentAdminBackingBean(event.assignment))
    val formPanel = new AssignmentFormPanel(ManageAssignmentsPanel.FORM_ID, model, DisplayOption.SHOW_SAVE_BUTTON, DisplayOption.SHOW_DELETE_BUTTON)
    formPanel.setOutputMarkupId(true)
    addOrReplace(formPanel)
    event.refresh(formPanel)
  }
}

object ManageAssignmentsPanel {
  val ASSIGNED_USER_ID = "assignedUserPanel"
  val FORM_ID = "assignmentFormPanel"
}
