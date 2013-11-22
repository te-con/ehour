package net.rrm.ehour.ui.admin.project

import net.rrm.ehour.ui.common.panel.AbstractBasePanel
import net.rrm.ehour.ui.common.border.GreyRoundedBorder
import org.apache.wicket.model.{Model, IModel}
import org.apache.wicket.spring.injection.annot.SpringBean
import net.rrm.ehour.project.service.ProjectAssignmentService
import net.rrm.ehour.domain.{Project, User}
import net.rrm.ehour.util._
import org.apache.wicket.markup.html.list.{ListItem, ListView}
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox
import org.apache.wicket.ajax.AjaxRequestTarget
import java.lang.Boolean
import org.apache.wicket.markup.head.{IHeaderResponse, CssHeaderItem}
import org.apache.wicket.request.resource.CssResourceReference
import net.rrm.ehour.user.service.UserService
import java.{util => ju}
import net.rrm.ehour.ui.common.wicket.Container

class AssignedUsersPanel(id: String, model: IModel[ProjectAdminBackingBean]) extends AbstractBasePanel[ProjectAdminBackingBean](id, model) {

  val Css = new CssResourceReference(classOf[AssignedUsersPanel], "projectAdmin.css")

  @SpringBean
  protected var assignmentService: ProjectAssignmentService = _

  @SpringBean
  protected var userService: UserService = _


  override def onInitialize() {
    super.onInitialize()

    val border = new GreyRoundedBorder("border")
    addOrReplace(border)

    val project = getPanelModelObject.getProject

    val assignments = fetchProjectAssignments(project)

    val container = new Container("assignmentContainer")
    border.addOrReplace(container)
    container.addOrReplace(createAssignmentListView(assignments))

    border.add(new AjaxCheckBox("toggleAll", new Model[Boolean]()) {
      override def onUpdate(target: AjaxRequestTarget) {
        val assignments = fetchUsers ++ fetchProjectAssignments(getPanelModelObject.getProject).sortWith((a, b) => a.user.compareTo(b.user) < 0)
        val view = createAssignmentListView(assignments)
        container.addOrReplace(view)
        target.add(container)
      }
    })
  }


  def createAssignmentListView(assignments: List[Assignment]): ListView[Assignment] = {
    new ListView[Assignment]("assignments", toJava(assignments)) {
      setOutputMarkupId(true)

      override def populateItem(item: ListItem[Assignment]) {
        val assignment = item.getModelObject

        item.add(new Label("name", assignment.user.getFullName))

        item.add(new AjaxCheckBox("active", new Model[Boolean]()) {
          override def onUpdate(target: AjaxRequestTarget) {
            ???
          }
        })
      }
    }
  }

  def fetchUsers = {
    val users = toScala(userService.getActiveUsers)
    users.map(u => Assignment(None, u))
  }

  def fetchProjectAssignments(project: Project): List[Assignment] = {
    val projectAssignments = toScala(assignmentService.getProjectAssignments(project, true))
    projectAssignments.map(a => Assignment(Some(a.getAssignmentId), a.getUser))
  }

  override def renderHead(response: IHeaderResponse) {
    response.render(CssHeaderItem.forReference(Css))
  }
}

case class Assignment(id: Option[Int], user: User)
