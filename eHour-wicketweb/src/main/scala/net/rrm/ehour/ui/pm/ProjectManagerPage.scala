package net.rrm.ehour.ui.pm

import java.lang.Boolean
import java.util

import com.google.common.collect.Lists
import net.rrm.ehour.config.PmPrivilege
import net.rrm.ehour.domain.{Project, UserRole}
import net.rrm.ehour.project.service.ProjectService
import net.rrm.ehour.ui.common.border.GreyRoundedBorder
import net.rrm.ehour.ui.common.event.AjaxEvent
import net.rrm.ehour.ui.common.page.AbstractBasePage
import net.rrm.ehour.ui.common.panel.entryselector.EntrySelectorPanel.EntrySelectorBuilder
import net.rrm.ehour.ui.common.panel.entryselector.{EntrySelectorData, EntrySelectorPanel}
import net.rrm.ehour.ui.common.session.EhourWebSession
import net.rrm.ehour.ui.common.wicket.Container
import net.rrm.ehour.ui.manage.assignment.AssignmentAjaxEventType
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation
import org.apache.wicket.markup.head.{CssHeaderItem, IHeaderResponse}
import org.apache.wicket.markup.html.WebMarkupContainer
import org.apache.wicket.model.ResourceModel
import org.apache.wicket.request.resource.CssResourceReference
import org.apache.wicket.spring.injection.annot.SpringBean

@AuthorizeInstantiation(Array(UserRole.ROLE_PROJECTMANAGER))
class ProjectManagerPage extends AbstractBasePage[String](new ResourceModel("pmReport.title")) {

  val ContainerId = "contentContainer"
  val ContentId = "content"
  val StatusId = "status"
  val Self = this

  val Css = new CssResourceReference(classOf[ProjectManagerPage], "projectManagement.css")

  var selector:EntrySelectorPanel = _

  var statusContainer:WebMarkupContainer = _

  @SpringBean
  protected var projectService: ProjectService = _

  override def onInitialize() {
    super.onInitialize()

    val greyBorder = createEntrySelectorFrame
    addOrReplace(greyBorder)

    selector = buildEntrySelector().build()
    greyBorder.add(selector)

    val container = createContentContainer
    addOrReplace(container)
    container.add(createPlaceholderContainer(ContentId))
    statusContainer = new Container(StatusId)
    container.add(statusContainer)
  }

  protected def createPlaceholderContainer(id: String):WebMarkupContainer = new Container(id)

  protected def createContentContainer: WebMarkupContainer = new WebMarkupContainer(ContainerId)

  protected def createEntrySelectorFrame: GreyRoundedBorder = {
    new GreyRoundedBorder("entrySelectorFrame", new ResourceModel("admin.project.title"))
  }

  protected def buildEntrySelector(): EntrySelectorBuilder = {
    val clickHandler = new EntrySelectorPanel.ClickHandler {
      def onClick(row: EntrySelectorData.EntrySelectorRow, target: AjaxRequestTarget) {
        val id = row.getId.asInstanceOf[Integer]

        val project = projectService.getProject(id)

        onProjectSelected(id, project, target)
      }
    }

    EntrySelectorBuilder.startAs("projectSelector")
      .onClick(clickHandler)
      .withData(createSelectorData(projects))
      .withInactiveTooltip(new ResourceModel("admin.project.hideInactive"))

  }

  def onProjectSelected(id: Integer, project: Project, target: AjaxRequestTarget) {
    val container = findContentContainer
    updateContentPanel(project, target, container)
    updateStatusPanel(project, target, container)
  }

  def updateContentPanel(project: Project, target: AjaxRequestTarget, container: WebMarkupContainer): Unit = {
    if (getConfig.getPmPrivilege != PmPrivilege.NONE) {
      val projectInfoPanel = new ProjectManagerModifyPanel(ContentId, project)
      projectInfoPanel.setOutputMarkupId(true)
      container.addOrReplace(projectInfoPanel)
      target.add(projectInfoPanel)
    }
  }

  def updateStatusPanel(project: Project, target: AjaxRequestTarget, container: WebMarkupContainer): Unit = {
    val statusPanel = new ProjectManagerStatusPanel(StatusId, project)
    statusPanel.setOutputMarkupId(true)
    container.addOrReplace(statusPanel)
    target.add(statusPanel)
  }

  protected def findContentContainer: WebMarkupContainer = Self.get(ContainerId).asInstanceOf[WebMarkupContainer]

  def projects = projectService.getProjectManagerProjects(EhourWebSession.getUser)

  def createSelectorData(projects: util.List[Project]): EntrySelectorData = {
    val headers = Lists.newArrayList(new EntrySelectorData.Header("admin.project.customer"),
                                     new EntrySelectorData.Header("admin.project.code"),
                                     new EntrySelectorData.Header("admin.project.name"))

    import scala.collection.JavaConversions._
    val rows = for (project <- projects) yield {
      val cells = Lists.newArrayList(project.getCustomer.getFullName, project.getName, project.getProjectCode)
      new EntrySelectorData.EntrySelectorRow(cells,  project.getProjectId, project.isActive)
    }

    new EntrySelectorData(headers, rows)
  }

  override def ajaxEventReceived(ajaxEvent: AjaxEvent): Boolean = {
    if (ajaxEvent.getEventType == AssignmentAjaxEventType.ASSIGNMENT_UPDATED || ajaxEvent.getEventType == AssignmentAjaxEventType.ASSIGNMENT_DELETED) {
      ajaxEvent.getTarget.add(statusContainer)
    }

    true
  }

  override def onRenderHead(response: IHeaderResponse) {
    response.render(CssHeaderItem.forReference(Css))
  }
}
