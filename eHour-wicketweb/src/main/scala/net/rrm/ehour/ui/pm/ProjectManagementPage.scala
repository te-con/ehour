package net.rrm.ehour.ui.pm

import net.rrm.ehour.ui.common.page.AbstractBasePage
import org.apache.wicket.model.{IModel, ResourceModel}
import net.rrm.ehour.domain.{Project, UserRole}
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation
import org.apache.wicket.markup.html.panel.Fragment
import org.apache.wicket.AttributeModifier
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.markup.html.list.ListItem
import net.rrm.ehour.ui.common.panel.entryselector.{EntrySelectorPanel, EntrySelectorListView}
import java.{util => ju}
import org.apache.wicket.spring.injection.annot.SpringBean
import net.rrm.ehour.project.service.ProjectService
import net.rrm.ehour.ui.common.border.GreyRoundedBorder

@AuthorizeInstantiation(Array(UserRole.ROLE_PROJECTMANAGER))
class ProjectManagementPage extends AbstractBasePage[String](new ResourceModel("pmReport.title")) {

  @SpringBean
  protected var projectService: ProjectService = _


  override def onInitialize()  {
    super.onInitialize()

    val projects = projectService.getProjectManagerProjects(getEhourWebSession.getUser)
    val projectListHolder = createProjectListHolder(projects)

    val greyBorder = new GreyRoundedBorder("entrySelectorFrame", new ResourceModel("admin.project.title"))
    addOrReplace(greyBorder)

    val entrySelectorPanel = new EntrySelectorPanel("projectSelector", projectListHolder, new ResourceModel("admin.project.hideInactive"))
    greyBorder.addOrReplace(entrySelectorPanel)
  }

  private def createProjectListHolder(projects: ju.List[Project]): Fragment = {
    val fragment= new Fragment("itemListHolder", "itemListHolder", ProjectManagementPage.this)
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
        val projectId: Integer = item.getModelObject.getProjectId
      }
    }
    fragment.add(projectListView)
    return fragment
  }

}
