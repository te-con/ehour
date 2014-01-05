package net.rrm.ehour.ui.admin.project.assign

import org.apache.wicket.model.{ResourceModel, CompoundPropertyModel, IModel}
import net.rrm.ehour.ui.common.panel.AbstractAjaxPanel
import java.lang.Boolean
import org.apache.wicket.event.IEvent
import net.rrm.ehour.ui.common.wicket.Container
import net.rrm.ehour.ui.admin.assignment.{AssignmentAjaxEventType, AssignmentAdminBackingBean, AssignmentFormPanel}
import net.rrm.ehour.ui.common.event.AjaxEvent
import net.rrm.ehour.ui.admin.assignment.form.AssignmentFormComponentContainerPanel.DisplayOption
import org.apache.wicket.request.resource.CssResourceReference
import org.apache.wicket.spring.injection.annot.SpringBean
import net.rrm.ehour.project.service.ProjectAssignmentService
import net.rrm.ehour.ui.common.border.GreyRoundedBorder
import org.apache.wicket.markup.head.{CssHeaderItem, IHeaderResponse}
import net.rrm.ehour.ui.admin.project.ProjectAdminBackingBean
import org.apache.wicket.markup.html.border.Border
import java.util

class ManageAssignmentsPanel(id: String, model: IModel[ProjectAdminBackingBean], onlyDeactivation:Boolean = false) extends AbstractAjaxPanel(id, model) {
  def this(id: String, model: IModel[ProjectAdminBackingBean]) = this(id, model, false)

  val BORDER_ID= "border"
  val ASSIGNED_USER_ID = "assignedUserPanel"
  val FORM_ID = "assignmentFormPanel"

  val Self = this

  val Css = new CssResourceReference(classOf[ManageAssignmentsPanel], "manageAssignments.css")

  @SpringBean
  protected var assignmentService: ProjectAssignmentService = _

  setOutputMarkupId(true)

  override def onInitialize() {
    super.onInitialize()

    val greyBorder = new GreyRoundedBorder(BORDER_ID, new ResourceModel("admin.projects.assignments.header"))
    addOrReplace(greyBorder)

    greyBorder.add(createCurrentAssignmentsList)
    greyBorder.add(createFormContainer)
  }

  def createFormContainer = new Container(FORM_ID)

  def createCurrentAssignmentsList = {
    val view: CurrentAssignmentsListView = new CurrentAssignmentsListView(ASSIGNED_USER_ID, model)
    view.setOutputMarkupId(true)
    view

  }

  // Wicket 6 event system
  override def onEvent(event: IEvent[_]) {
    event.getPayload match {
      case event: EditAssignmentEvent => editAssignment(event)
      case event: NewAssignmentEvent => newAssignment(event)
      case _ =>
    }
  }

  def newAssignment(event: NewAssignmentEvent) {
/*
    val bean = AssignmentAdminBackingBean.createAssignmentAdminBackingBean(getPanelModelObject.getDomainObject)

    val model = new CompoundPropertyModel[AssignmentAdminBackingBean](bean)
    val formPanel = new AssignmentFormPanel(FORM_ID, model, util.Arrays.asList(DisplayOption.SHOW_SAVE_BUTTON, DisplayOption.SHOW_DELETE_BUTTON, DisplayOption.NO_BORDER))
    formPanel.setOutputMarkupId(true)
    getBorderContainer.addOrReplace(formPanel)
*/

    val view = new NewAssignmentUserListView(ASSIGNED_USER_ID)
    view.setOutputMarkupId(true)
    getBorderContainer.addOrReplace(view)

    event.refresh(view)
  }


  def editAssignment(event: EditAssignmentEvent) {
    val model = new CompoundPropertyModel[AssignmentAdminBackingBean](new AssignmentAdminBackingBean(event.assignment))
    val formPanel = new AssignmentFormPanel(FORM_ID, model, util.Arrays.asList(DisplayOption.SHOW_SAVE_BUTTON, DisplayOption.SHOW_DELETE_BUTTON, DisplayOption.NO_BORDER))
    formPanel.setOutputMarkupId(true)
    getBorderContainer.addOrReplace(formPanel)
    event.refresh(formPanel)
  }

  private def getBorderContainer = Self.get(BORDER_ID).asInstanceOf[Border].getBodyContainer

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

  override def renderHead(response: IHeaderResponse) {
    response.render(CssHeaderItem.forReference(Css))
  }
}

