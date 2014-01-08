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
import org.apache.wicket.model.util.ListModel
import net.rrm.ehour.domain.User
import com.google.common.collect.Lists

class ManageAssignmentsPanel(id: String, model: IModel[ProjectAdminBackingBean], onlyDeactivation: Boolean = false) extends AbstractAjaxPanel(id, model) {
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

    val greyBorder = new GreyRoundedBorder(BORDER_ID, new ResourceModel("admin.projects.assignments.header"))
    addOrReplace(greyBorder)

    greyBorder.add(createCurrentAssignmentsList)
    greyBorder.add(createAffectedUserContainer)
    greyBorder.add(createFormContainer)
  }

  def createAffectedUserContainer = new Container(AFFECTED_USER_ID)

  def createFormContainer = new Container(FORM_ID)

  def createCurrentAssignmentsList = {
    val view: CurrentAssignmentsListView = new CurrentAssignmentsListView(LIST_ID, model)
    view.setOutputMarkupId(true)
    view
  }

  // Wicket 6 event system
  override def onEvent(event: IEvent[_]) {
    event.getPayload match {
      case event: NewAssignmentEvent => initializeNewAssignment(event)
      case event: EditAssignmentEvent => initializeEditAssignment(event)
      case event: UserSelectedEvent => userSelected(event)
      case event: UserDeselectedEvent => userDeselected(event)
      case _ =>
    }
  }

  def userSelected(event: UserSelectedEvent) {
    val users = getAffectedUsers

    if (!users.contains(event.user))
      users.add(event.user)

    event.refresh(getAffectedUsersPanel)
  }

  def userDeselected(event: UserDeselectedEvent) {
    getAffectedUsers.remove(event.user)
    event.refresh(getAffectedUsersPanel)
  }

  def getAffectedUsers: ju.List[User] = getAffectedUsersPanel match {
    case affected: AffectedUsersPanel => affected.getPanelModelObject
    case _ => Lists.newArrayList()
  }

  def getAffectedUsersPanel = getBorderContainer.get(AFFECTED_USER_ID)

  private def initializeNewAssignment(event: NewAssignmentEvent) {
    val bean = AssignmentAdminBackingBean.createAssignmentAdminBackingBean(getPanelModelObject.getDomainObject)

    def replaceFormPanel: AssignmentFormPanel = {
      val model = new CompoundPropertyModel[AssignmentAdminBackingBean](bean)
      val formPanel = new AssignmentFormPanel(FORM_ID, model, ju.Arrays.asList(DisplayOption.SHOW_SAVE_BUTTON, DisplayOption.SHOW_DELETE_BUTTON, DisplayOption.NO_BORDER))
      formPanel.setOutputMarkupId(true)
      getBorderContainer.addOrReplace(formPanel)
      formPanel
    }

    def replaceUserListPanel: NewAssignmentUserListView = {
      val view = new NewAssignmentUserListView(LIST_ID)
      view.setOutputMarkupId(true)
      getBorderContainer.addOrReplace(view)
      view
    }

    def replaceAffectedUserPanel: AffectedUsersPanel = {
      val affectedUsersPanel = new AffectedUsersPanel(AFFECTED_USER_ID, new ListModel[User](Lists.newArrayList()))
      getBorderContainer.addOrReplace(affectedUsersPanel)
      affectedUsersPanel
    }

    event.refresh(replaceUserListPanel, replaceFormPanel, replaceAffectedUserPanel)
  }


  private def initializeEditAssignment(event: EditAssignmentEvent) {
    def replaceFormPanel: AssignmentFormPanel = {
      val model = new CompoundPropertyModel[AssignmentAdminBackingBean](new AssignmentAdminBackingBean(event.assignment))
      val formPanel = new AssignmentFormPanel(FORM_ID, model, ju.Arrays.asList(DisplayOption.SHOW_SAVE_BUTTON, DisplayOption.SHOW_DELETE_BUTTON, DisplayOption.NO_BORDER))
      formPanel.setOutputMarkupId(true)
      getBorderContainer.addOrReplace(formPanel)
      formPanel
    }
    def replaceAffectedUserPanel: AffectedUserLabel = {
      val affectedUserLabel = new AffectedUserLabel(AFFECTED_USER_ID, event.assignment.getUser)
      getBorderContainer.addOrReplace(affectedUserLabel)
      affectedUserLabel
    }

    event.refresh(replaceFormPanel, replaceAffectedUserPanel)
  }

  private[assign] def getBorderContainer = Self.get(BORDER_ID).asInstanceOf[Border].getBodyContainer

  // own legacy event system...
  override def ajaxEventReceived(ajaxEvent: AjaxEvent): Boolean = {
    def persistAssignment(backingBean: AssignmentAdminBackingBean) {
      val assignment = backingBean.getProjectAssignmentForSave

      if (assignment.isNew) {
        assignmentManagementService.assignUsersToProjects(getAffectedUsers, assignment)
      } else {
        assignmentManagementService.assignUserToProject(assignment)
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

    if (ajaxEvent.getEventType == AssignmentAjaxEventType.ASSIGNMENT_UPDATED || ajaxEvent.getEventType == AssignmentAjaxEventType.ASSIGNMENT_DELETED) {
      val backingBean = ajaxEvent.asInstanceOf[PayloadAjaxEvent[AdminBackingBean]].getPayload.asInstanceOf[AssignmentAdminBackingBean]

      if (ajaxEvent.getEventType == AssignmentAjaxEventType.ASSIGNMENT_UPDATED)
        persistAssignment(backingBean)
      else
        deleteAssignment(backingBean)

      replaceAssignmentsPanel()
      replaceForm()
    }

    true
  }

  override def renderHead(response: IHeaderResponse) {
    response.render(CssHeaderItem.forReference(Css))
  }
}

