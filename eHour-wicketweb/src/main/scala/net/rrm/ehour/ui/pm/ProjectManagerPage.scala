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
import net.rrm.ehour.ui.common.panel.entryselector.{EntrySelectorData, EntrySelectorPanel}
import net.rrm.ehour.ui.common.session.EhourWebSession
import net.rrm.ehour.ui.common.wicket.Container
import net.rrm.ehour.ui.manage.assignment.AssignmentAjaxEventType
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation
import org.apache.wicket.markup.head.{CssHeaderItem, IHeaderResponse}
import org.apache.wicket.model.ResourceModel
import org.apache.wicket.request.resource.CssResourceReference
import org.apache.wicket.spring.injection.annot.SpringBean

@AuthorizeInstantiation(Array(UserRole.ROLE_PROJECTMANAGER))
class ProjectManagerPage extends AbstractBasePage[String](new ResourceModel("pmReport.title")) {

  val ContainerId = "content"
  val StatusId = "status"
  val Self = this

  val Css = new CssResourceReference(classOf[ProjectManagerPage], "projectManagement.css")

  @SpringBean
  protected var projectService: ProjectService = _

  override def onInitialize() {
    super.onInitialize()

    val greyBorder = new GreyRoundedBorder("entrySelectorFrame", new ResourceModel("admin.project.title"))
    addOrReplace(greyBorder)

    val clickHandler = new EntrySelectorPanel.ClickHandler {
      def onClick(row: EntrySelectorData.EntrySelectorRow, target: AjaxRequestTarget) {
        val id = row.getId.asInstanceOf[Integer]

        val project = projectService.getProject(id)

        if (getConfig.getPmPrivilege != PmPrivilege.NONE) {
          val projectInfoPanel = new ProjectManagerModifyPanel(ContainerId, project)
          projectInfoPanel.setOutputMarkupId(true)
          Self.addOrReplace(projectInfoPanel)
          target.add(projectInfoPanel)
        }

        val statusPanel = new ProjectManagerStatusPanel(StatusId, project)
        statusPanel.setOutputMarkupId(true)
        Self.addOrReplace(statusPanel)
        target.add(statusPanel)
      }
    }

    val entrySelectorPanel = new EntrySelectorPanel("entrySelectorFrame",
      createSelectorData(projects),
      clickHandler,
      new ResourceModel("admin.user.hideInactive"))


    greyBorder.add(entrySelectorPanel)

    addOrReplace(new Container(ContainerId))
    addOrReplace(new Container(StatusId))
  }

  def projects = projectService.getProjectManagerProjects(EhourWebSession.getUser)

  def createSelectorData(projects: util.List[Project]): EntrySelectorData = {
    val headers = Lists.newArrayList(new EntrySelectorData.Header("admin.project.code.short"),
                                     new EntrySelectorData.Header("admin.project.name"))

    import scala.collection.JavaConversions._
    val rows = for (project <- projects) yield {
      val cells = Lists.newArrayList(project.getName, project.getProjectCode)
      new EntrySelectorData.EntrySelectorRow(cells, project.getProjectId, project.isActive)
    }

    new EntrySelectorData(headers, rows)
  }

  override def ajaxEventReceived(ajaxEvent: AjaxEvent): Boolean = {
    if (ajaxEvent.getEventType == AssignmentAjaxEventType.ASSIGNMENT_UPDATED || ajaxEvent.getEventType == AssignmentAjaxEventType.ASSIGNMENT_DELETED) {
      ajaxEvent.getTarget.add(get(StatusId))
    }

    true
  }

  override def renderHead(response: IHeaderResponse) {
    response.render(CssHeaderItem.forReference(Css))
  }
}
