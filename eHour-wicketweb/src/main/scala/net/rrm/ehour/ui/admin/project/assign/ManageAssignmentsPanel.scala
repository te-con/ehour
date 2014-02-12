package net.rrm.ehour.ui.admin.project.assign

import org.apache.wicket.model.{ResourceModel, CompoundPropertyModel, IModel}
import net.rrm.ehour.ui.common.panel.AbstractAjaxPanel
import java.lang.Boolean
import org.apache.wicket.event.IEvent
import net.rrm.ehour.ui.common.wicket.Container
import net.rrm.ehour.ui.admin.assignment.{AssignmentAjaxEventType, AssignmentAdminBackingBean, AssignmentFormPanel}
import net.rrm.ehour.ui.common.event.{PayloadAjaxEvent, AjaxEvent}
import net.rrm.ehour.ui.admin.assignment.form.AssignmentFormComponentContainerPanel.DisplayOption
import org.apache.wicket.request.resource.CssResourceReference
import org.apache.wicket.spring.injection.annot.SpringBean
import net.rrm.ehour.project.service.{ProjectAssignmentManagementService, ProjectAssignmentService}
import net.rrm.ehour.ui.common.border.GreyRoundedBorder
import org.apache.wicket.markup.head.{CssHeaderItem, IHeaderResponse}
import net.rrm.ehour.ui.admin.project.ProjectAdminBackingBean
import org.apache.wicket.markup.html.border.Border
import java.{util => ju}
import net.rrm.ehour.ui.common.model.AdminBackingBean
import net.rrm.ehour.domain.User
import com.google.common.collect.Lists
import org.apache.wicket.MarkupContainer

class ManageAssignmentsPanel(id: String, model: IModel[ProjectAdminBackingBean], panelConfig: ManagementPanelConfig = ManagementPanelConfig(onlyDeactivation = false, borderless = false)) extends AbstractAjaxPanel(id, model) {
  val BORDER_ID = "border"
  val LIST_ID = "list"
  val FORM_ID = "form"
  val AFFECTED_USER_ID = "affectedUser"

  val Self = this

  val Css = new CssResourceReference(classOf[ManageAssignmentsPanel], "manageAssignments.css")

  @SpringBean
  protected var assignmentService: ProjectAssignmentService = _

  @SpringBean
  protected var assignmentManagementService: ProjectAssignmentManagementService = _

  setOutputMarkupId(true)

  override def onInitialize() {
    super.onInitialize()

    val greyBorder = if (panelConfig.borderless) new Container(BORDER_ID) else new GreyRoundedBorder(BORDER_ID, new ResourceModel("admin.projects.assignments.header"))
    addOrReplace(greyBorder)

    greyBorder.add(createCurrentAssignmentsList)
    greyBorder.add(createAffectedUserContainer)
    greyBorder.add(createFormContainer)
  }

  def createAffectedUserContainer = new Container(AFFECTED_USER_ID)

  def createFormContainer = new Container(FORM_ID)

  def createCurrentAssignmentsList = {
    val view: CurrentAssignmentsListView = new CurrentAssignmentsListView(LIST_ID, model, panelConfig.onlyDeactivation)
    view.setOutputMarkupId(true)
    view
  }

  // Wicket 6 event system
  override def onEvent(event: IEvent[_]) {
    event.getPayload match {
      case event: NewAssignmentEvent => initializeNewAssignment(event)
      case event: EditAssignmentEvent => initializeEditAssignment(event)
      case _ =>
    }
  }

  def selectedAffectedUsers: ju.List[User] = findListPanel match {
    case newUserList: NewAssignmentUserListView => newUserList.selectedAffectedUsers.getObject
    case _ => Lists.newArrayList()
  }

  def findListPanel = getBorderContainer.get(LIST_ID)

  private def initializeNewAssignment(event: NewAssignmentEvent) {
    val bean = AssignmentAdminBackingBean.createAssignmentAdminBackingBean(getPanelModelObject.getDomainObject)

    def replaceFormPanel: AssignmentFormPanel = {
      val model = new CompoundPropertyModel[AssignmentAdminBackingBean](bean)
      val formPanel = createAssignmentFormPanel(model)
      getBorderContainer.addOrReplace(formPanel)
      formPanel
    }

    def replaceUserListPanel: NewAssignmentUserListView = {
      val view = new NewAssignmentUserListView(LIST_ID)
      view.setOutputMarkupId(true)
      getBorderContainer.addOrReplace(view)
      view
    }

    event.refresh(replaceUserListPanel, replaceFormPanel, replaceAffectedUserPanel)
  }

  def createAssignmentFormPanel(model: CompoundPropertyModel[AssignmentAdminBackingBean]): AssignmentFormPanel = {
    val formPanel = new AssignmentFormPanel(FORM_ID, model, ju.Arrays.asList(DisplayOption.NO_BORDER, DisplayOption.SHOW_CANCEL_BUTTON))
    formPanel.setOutputMarkupId(true)
    formPanel
  }

  def replaceAffectedUserPanel = {
    val affectedUsersPanel = new Container(AFFECTED_USER_ID)
    getBorderContainer.addOrReplace(affectedUsersPanel)
    affectedUsersPanel
  }

  private def initializeEditAssignment(event: EditAssignmentEvent) {
    def replaceFormPanel: AssignmentFormPanel = {
      val model = new CompoundPropertyModel[AssignmentAdminBackingBean](new AssignmentAdminBackingBean(event.assignment))
      val formPanel = createAssignmentFormPanel(model)
      getBorderContainer.addOrReplace(formPanel)
      formPanel
    }
    def replaceAffectedUserPanel: AffectedUserPanel = {
      val affectedUserLabel = new AffectedUserPanel(AFFECTED_USER_ID, event.assignment.getUser)
      getBorderContainer.addOrReplace(affectedUserLabel)
      affectedUserLabel
    }

    event.refresh(replaceFormPanel, replaceAffectedUserPanel)
  }

  private[assign] def getBorderContainer:MarkupContainer = if (panelConfig.borderless) get(BORDER_ID).asInstanceOf[Container] else get(BORDER_ID).asInstanceOf[Border].getBodyContainer

  // own legacy event system...
  override def ajaxEventReceived(ajaxEvent: AjaxEvent): Boolean = {
    def persistAssignment(backingBean: AssignmentAdminBackingBean) {
      val assignment = backingBean.getProjectAssignmentForSave

      if (backingBean.isNewAssignment) {
        assignmentManagementService.assignUsersToProjects(selectedAffectedUsers, assignment)
      } else {
        assignmentManagementService.persist(assignment)
      }
    }

    def deleteAssignment(backingBean: AssignmentAdminBackingBean) {
      assignmentManagementService.deleteProjectAssignment(backingBean.getProjectAssignment)
    }

    def replaceForm() {
      val container = createFormContainer
      getBorderContainer.addOrReplace(container)
      ajaxEvent.getTarget.add(container)
    }

    def replaceAssignmentsPanel() {
      val replacement = createCurrentAssignmentsList
      getBorderContainer.addOrReplace(replacement)
      ajaxEvent.getTarget.add(replacement)
    }

    def showExistingAssignments() {
      replaceAssignmentsPanel()
      replaceForm()
      ajaxEvent.getTarget.add(replaceAffectedUserPanel)
    }

    if (ajaxEvent.getEventType == AssignmentAjaxEventType.ASSIGNMENT_UPDATED || ajaxEvent.getEventType == AssignmentAjaxEventType.ASSIGNMENT_DELETED) {
      val backingBean = ajaxEvent.asInstanceOf[PayloadAjaxEvent[AdminBackingBean[_]]].getPayload.asInstanceOf[AssignmentAdminBackingBean]

      if (ajaxEvent.getEventType == AssignmentAjaxEventType.ASSIGNMENT_UPDATED)
        persistAssignment(backingBean)
      else
        deleteAssignment(backingBean)

      showExistingAssignments()
    } else if (ajaxEvent.getEventType == AssignmentAjaxEventType.ASSIGNMENT_CANCELLED) {
      showExistingAssignments()
    }

    true
  }

  override def renderHead(response: IHeaderResponse) {
    response.render(CssHeaderItem.forReference(Css))
  }
}

case class ManagementPanelConfig(onlyDeactivation: Boolean = false, borderless: Boolean = false)

