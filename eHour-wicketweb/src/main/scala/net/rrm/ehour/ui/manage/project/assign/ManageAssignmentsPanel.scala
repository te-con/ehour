package net.rrm.ehour.ui.manage.project.assign

import java.lang.Boolean
import java.{util => ju}

import com.google.common.collect.Lists
import net.rrm.ehour.domain.{Project, ProjectAssignment, User}
import net.rrm.ehour.project.service.{ProjectAssignmentManagementService, ProjectAssignmentService}
import net.rrm.ehour.ui.common.border.GreyRoundedBorder
import net.rrm.ehour.ui.common.event.{AjaxEvent, PayloadAjaxEvent}
import net.rrm.ehour.ui.common.model.{AdminBackingBean, DateModel}
import net.rrm.ehour.ui.common.panel.AbstractAjaxPanel
import net.rrm.ehour.ui.common.panel.entryselector.EntrySelectorData
import net.rrm.ehour.ui.common.panel.entryselector.EntrySelectorData.{ColumnType, EntrySelectorRow}
import net.rrm.ehour.ui.common.panel.entryselector.EntrySelectorPanel.ClickHandler
import net.rrm.ehour.ui.common.panel.multiselect.MultiUserSelect
import net.rrm.ehour.ui.common.session.EhourWebSession
import net.rrm.ehour.ui.common.wicket.Container
import net.rrm.ehour.ui.manage.assignment.form.AssignmentFormComponentContainerPanel.DisplayOption
import net.rrm.ehour.ui.manage.assignment.{AssignmentAdminBackingBean, AssignmentAjaxEventType, AssignmentFormPanel}
import net.rrm.ehour.ui.manage.project.ProjectAdminBackingBean
import net.rrm.ehour.util._
import org.apache.wicket.MarkupContainer
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.event.{Broadcast, IEvent}
import org.apache.wicket.markup.head.{CssHeaderItem, IHeaderResponse}
import org.apache.wicket.markup.html.border.Border
import org.apache.wicket.model.{CompoundPropertyModel, IModel, ResourceModel}
import org.apache.wicket.request.resource.CssResourceReference
import org.apache.wicket.spring.injection.annot.SpringBean

class ManageAssignmentsPanel[T <: ProjectAdminBackingBean](id: String, model: IModel[T], panelConfig: ManagementPanelConfig = ManagementPanelConfig(onlyDeactivation = false, borderless = false, wide = false)) extends AbstractAjaxPanel(id, model) {
  val BorderId = "border"
  val ListId = "list"
  val FormId = "form"
  val AffectedUserId = "affectedUser"

  val Self = this

  val Css = new CssResourceReference(classOf[ManageAssignmentsPanel[T]], "manageAssignments.css")

  @SpringBean
  protected var assignmentService: ProjectAssignmentService = _

  @SpringBean
  protected var assignmentManagementService: ProjectAssignmentManagementService = _

  setOutputMarkupId(true)

  override def onInitialize() {
    super.onInitialize()

    val greyBorder = if (panelConfig.borderless) new Container(BorderId) else new GreyRoundedBorder(BorderId, new ResourceModel("admin.projects.assignments.header"))
    addOrReplace(greyBorder)

    greyBorder.add(createCurrentAssignmentsList)
    greyBorder.add(createAffectedUserContainer)
    greyBorder.add(createFormContainer)
  }

  def createAffectedUserContainer = new Container(AffectedUserId)

  def createFormContainer = new Container(FormId)

  def createCurrentAssignmentsList = {
    val p = model.getObject.getProject
    val xs = sort(fetchProjectAssignments(p))

    val ch = new ClickHandler {
      override def onClick(row: EntrySelectorRow, target: AjaxRequestTarget) = {

        val assignmentId = row.getId.asInstanceOf[Integer]
        val assignment = assignmentService.getProjectAssignment(assignmentId)

        send(Self, Broadcast.BUBBLE, EditAssignmentEvent(assignment, target))
      }
    }

    val selector = new AssignmentSelector(ListId, createSelectorData(xs), clickHandler = ch, onlyDeactivate = panelConfig.onlyDeactivation, wide = true)
    selector.setOutputMarkupId(true)
    selector
  }

  private def createSelectorData(assignments: List[ProjectAssignment]): EntrySelectorData = {
    val headers = Lists.newArrayList(new EntrySelectorData.Header("admin.project.assignments.user"),
      new EntrySelectorData.Header("admin.project.assignments.role"),
      new EntrySelectorData.Header("admin.project.assignments.date", ColumnType.HTML),
      new EntrySelectorData.Header("admin.project.assignments.rate", ColumnType.NUMERIC)
    )

    val rows = for (assignment <- assignments) yield {
      val start = new DateModel(assignment.getDateStart, EhourWebSession.getEhourConfig, DateModel.DATESTYLE_FULL).getObject
      val end = new DateModel(assignment.getDateEnd, EhourWebSession.getEhourConfig, DateModel.DATESTYLE_FULL).getObject


      val cells = Lists.newArrayList(assignment.getUser.getFullName, assignment.getRole, s"$start - $end", assignment.getHourlyRate)
      new EntrySelectorData.EntrySelectorRow(cells, assignment.getPK)
    }

    import scala.collection.JavaConversions._
    new EntrySelectorData(headers, rows)
  }

  private def fetchProjectAssignments(project: Project) = if (project.getPK == null) List() else toScala(assignmentService.getProjectAssignmentsAndCheckDeletability(project))

  private def sort(assignments: List[ProjectAssignment]) = assignments.sortWith((a, b) => a.getUser.compareTo(b.getUser) < 0)


  // Wicket 6 event system
  override def onEvent(event: IEvent[_]) {
    event.getPayload match {
      case event: NewAssignmentEvent => initializeNewAssignment(event)
      case event: EditAssignmentEvent => initializeEditAssignment(event)
      case _ =>
    }
  }

  def selectedAffectedUsers: ju.List[User] = findListPanel match {
    case newUserList: MultiUserSelect => newUserList.selectedUsers.getObject
    case _ => Lists.newArrayList()
  }

  def findListPanel = getBorderContainer.get(ListId)

  private def initializeNewAssignment(event: NewAssignmentEvent) {
    val bean = AssignmentAdminBackingBean.createAssignmentAdminBackingBean(getPanelModelObject.getDomainObject)

    def replaceFormPanel: AssignmentFormPanel = {
      val model = new CompoundPropertyModel[AssignmentAdminBackingBean](bean)
      val formPanel = createAssignmentFormPanel(model)
      getBorderContainer.addOrReplace(formPanel)
      formPanel
    }

    def replaceUserListPanel: MultiUserSelect = {
      val view = new MultiUserSelect(ListId)
      view.setOutputMarkupId(true)
      getBorderContainer.addOrReplace(view)
      view
    }

    event.refresh(replaceUserListPanel, replaceFormPanel, replaceAffectedUserPanel)
  }

  def createAssignmentFormPanel(model: CompoundPropertyModel[AssignmentAdminBackingBean]): AssignmentFormPanel = {
    val formPanel = new AssignmentFormPanel(FormId, model, ju.Arrays.asList(DisplayOption.NO_BORDER, DisplayOption.SHOW_CANCEL_BUTTON))
    formPanel.setOutputMarkupId(true)
    formPanel
  }

  def replaceAffectedUserPanel = {
    val affectedUsersPanel = new Container(AffectedUserId)
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
      val affectedUserLabel = new AffectedUserPanel(AffectedUserId, event.assignment.getUser)
      getBorderContainer.addOrReplace(affectedUserLabel)
      affectedUserLabel
    }

    event.refresh(replaceFormPanel, replaceAffectedUserPanel)
  }

  private[assign] def getBorderContainer:MarkupContainer = if (panelConfig.borderless) get(BorderId).asInstanceOf[Container] else get(BorderId).asInstanceOf[Border].getBodyContainer

  // own legacy event system...
  override def ajaxEventReceived(ajaxEvent: AjaxEvent): Boolean = {
    def persistAssignment(backingBean: AssignmentAdminBackingBean) {
      val assignment = backingBean.getProjectAssignmentForSave

      if (backingBean.isNewAssignment) {
        assignmentManagementService.assignUsersToProjects(selectedAffectedUsers, assignment)
      } else {
        assignmentManagementService.persistNewProjectAssignment(assignment)
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

case class ManagementPanelConfig(onlyDeactivation: Boolean = false, borderless: Boolean = false, wide: Boolean = false)

