package net.rrm.ehour.ui.admin.project

import org.apache.wicket.model.{ResourceModel, CompoundPropertyModel, IModel}
import net.rrm.ehour.ui.common.panel.AbstractAjaxPanel
import java.lang.Boolean
import org.apache.wicket.event.IEvent
import net.rrm.ehour.ui.common.wicket.Container
import net.rrm.ehour.ui.admin.assignment.{AssignmentAjaxEventType, AssignmentAdminBackingBean, AssignmentFormPanel}
import net.rrm.ehour.ui.common.event.AjaxEvent
import net.rrm.ehour.ui.admin.assignment.form.AssignmentFormComponentContainerPanel.DisplayOption
import net.rrm.ehour.ui.admin.project.assign.{CurrentAssignmentsListView, EditAssignmentEvent}
import org.apache.wicket.request.resource.CssResourceReference
import org.apache.wicket.spring.injection.annot.SpringBean
import net.rrm.ehour.project.service.ProjectAssignmentService
import net.rrm.ehour.ui.common.border.GreyRoundedBorder
import org.apache.wicket.markup.head.{CssHeaderItem, IHeaderResponse}

class ManageAssignmentsPanel(id: String, model: IModel[ProjectAdminBackingBean], onlyDeactivation:Boolean = false) extends AbstractAjaxPanel(id, model) {
  def this(id: String, model: IModel[ProjectAdminBackingBean]) = this(id, model, false)

  val Self = this

  val Css = new CssResourceReference(classOf[ManageAssignmentsPanel], "manageAssignments.css")

  @SpringBean
  protected var assignmentService: ProjectAssignmentService = _

  setOutputMarkupId(true)

  override def onInitialize() {
    super.onInitialize()

    val greyBorder = new GreyRoundedBorder("border", new ResourceModel("admin.dept.title"))
    addOrReplace(greyBorder)

    greyBorder.add(createCurrentAssignmentsList)
    greyBorder.add(createFormContainer)
  }

  def createFormContainer = new Container(ManageAssignmentsPanel.FORM_ID)

  def createCurrentAssignmentsList = new CurrentAssignmentsListView("currentAssignments", model, onlyDeactivation)

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
      val replacement = createCurrentAssignmentsList
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

  override def renderHead(response: IHeaderResponse) {
    response.render(CssHeaderItem.forReference(Css))
  }
}

object ManageAssignmentsPanel {
  val ASSIGNED_USER_ID = "assignedUserPanel"
  val FORM_ID = "assignmentFormPanel"
}
