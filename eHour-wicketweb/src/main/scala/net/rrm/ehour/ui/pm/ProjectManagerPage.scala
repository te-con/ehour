package net.rrm.ehour.ui.pm

import java.lang.Boolean
import java.{util => ju}

import net.rrm.ehour.config.PmPrivilege
import net.rrm.ehour.domain.{Project, UserRole}
import net.rrm.ehour.project.service.ProjectService
import net.rrm.ehour.ui.common.border.GreyRoundedBorder
import net.rrm.ehour.ui.common.event.AjaxEvent
import net.rrm.ehour.ui.common.page.AbstractBasePage
import net.rrm.ehour.ui.common.panel.entryselector.{EntrySelectorListView, EntrySelectorPanel}
import net.rrm.ehour.ui.common.session.EhourWebSession
import net.rrm.ehour.ui.common.wicket.Container
import net.rrm.ehour.ui.manage.assignment.AssignmentAjaxEventType
import org.apache.wicket.AttributeModifier
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation
import org.apache.wicket.markup.head.{CssHeaderItem, IHeaderResponse}
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.list.ListItem
import org.apache.wicket.markup.html.panel.Fragment
import org.apache.wicket.model.{IModel, ResourceModel}
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


  override def ajaxEventReceived(ajaxEvent: AjaxEvent): Boolean = {
    if (ajaxEvent.getEventType == AssignmentAjaxEventType.ASSIGNMENT_UPDATED || ajaxEvent.getEventType == AssignmentAjaxEventType.ASSIGNMENT_DELETED) {
      ajaxEvent.getTarget.add(get(StatusId))
    }

    true
  }

  override def onInitialize() {
    super.onInitialize()

    val greyBorder = new GreyRoundedBorder("entrySelectorFrame", new ResourceModel("admin.project.title"))
    addOrReplace(greyBorder)

    greyBorder.add(initializeProjectSelector())

    addOrReplace(new Container(ContainerId))
    addOrReplace(new Container(StatusId))
  }

  def initializeProjectSelector() = {
    val projects = projectService.getProjectManagerProjects(EhourWebSession.getUser)
    val projectListHolder = createProjectListHolder(projects)

    new EntrySelectorPanel("projectSelector", projectListHolder, new ResourceModel("admin.project.hideInactive"))
  }

  private def createProjectListHolder(projects: ju.List[Project]): Fragment = {
    val fragment = new Fragment("itemListHolder", "itemListHolder", ProjectManagerPage.this)
    fragment.setOutputMarkupId(true)

    val projectListView = new EntrySelectorListView[Project]("itemList", projects) {
      protected def onPopulate(item: ListItem[Project], itemModel: IModel[Project]) {
        val project: Project = itemModel.getObject
        if (!project.isActive) {
          item.add(AttributeModifier.append("class", "inactive"))
        }
        item.add(new Label("name", project.getName))
        item.add(new Label("code", project.getProjectCode))
      }

      protected def onClick(item: ListItem[Project], target: AjaxRequestTarget) {
        val project = item.getModelObject

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

    fragment.add(projectListView)
    fragment
  }

  override def renderHead(response: IHeaderResponse) {
    response.render(CssHeaderItem.forReference(Css))
  }
}
