package net.rrm.ehour.ui.admin.project

import net.rrm.ehour.ui.common.panel.AbstractBasePanel
import net.rrm.ehour.ui.common.border.GreyRoundedBorder
import org.apache.wicket.model.{PropertyModel, Model, IModel}
import org.apache.wicket.spring.injection.annot.SpringBean
import net.rrm.ehour.project.service.ProjectAssignmentService
import net.rrm.ehour.domain.{ProjectAssignment, Project}
import net.rrm.ehour.util._
import org.apache.wicket.markup.html.list.{ListItem, ListView}
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox
import org.apache.wicket.ajax.AjaxRequestTarget
import java.lang.Boolean
import org.apache.wicket.markup.head.{IHeaderResponse, CssHeaderItem}
import org.apache.wicket.request.resource.CssResourceReference
import net.rrm.ehour.user.service.UserService
import net.rrm.ehour.ui.common.wicket.{DateLabel, AlwaysOnLabel, Container}
import org.apache.wicket.markup.html.panel.Fragment

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
        val assignments =  if (getModelObject) {
          (fetchUsers ++ fetchProjectAssignments(getPanelModelObject.getProject)).sortWith((a, b) => a.getUser.compareTo(b.getUser) < 0)
        } else {
          fetchProjectAssignments(getPanelModelObject.getProject)
        }

        val view = createAssignmentListView(assignments)
        container.addOrReplace(view)
        target.add(container)
      }
    })
  }


  def createAssignmentListView(assignments: List[ProjectAssignment]): ListView[ProjectAssignment] = {
    new ListView[ProjectAssignment]("assignments", toJava(assignments)) {
      setOutputMarkupId(true)

      override def populateItem(item: ListItem[ProjectAssignment]) {
        def createNameLabel =  new AlwaysOnLabel("name", new PropertyModel(item.getModel, "user.fullName"))
        def createStartDateLabel = new DateLabel("startDate", new PropertyModel(item.getModel, "dateStart"))
        def createEndDateLabel = new DateLabel("endDate", new PropertyModel(item.getModel, "dateEnd"))

        val container = new Container("container")
        item.add(container)

        container.add(createNameLabel)

        container.add(new AjaxCheckBox("active", new PropertyModel(item.getModel, "active")) {
          override def onUpdate(target: AjaxRequestTarget) {
            val replacement = new Fragment("container", "inputRow", getParent)
            replacement.setOutputMarkupId(true)
            replacement.add(createNameLabel)
            replacement.add(createStartDateLabel)
            replacement.add(createEndDateLabel)

            item.addOrReplace(replacement)
            target.add(replacement)
          }
        })

        container.add(createStartDateLabel)
        container.add(createEndDateLabel)
        container.add(new AlwaysOnLabel("rate", new PropertyModel(item.getModel, "hourlyRate")))

        container.setOutputMarkupId(true)
      }
    }
  }



  def fetchUsers = {
    val project = getPanelModelObject.getProject

    val users = toScala(userService.getActiveUsers)
    users.map(new ProjectAssignment(_, project))
  }

  def fetchProjectAssignments(project: Project): List[ProjectAssignment] = {
    if (project.getProjectId == null) {
      List()
    } else {
      toScala(assignmentService.getProjectAssignments(project, true))
    }
  }

  override def renderHead(response: IHeaderResponse) {
    response.render(CssHeaderItem.forReference(Css))
  }
}
