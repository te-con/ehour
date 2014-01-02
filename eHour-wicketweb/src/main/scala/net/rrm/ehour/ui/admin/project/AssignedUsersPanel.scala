package net.rrm.ehour.ui.admin.project

import net.rrm.ehour.ui.common.panel.AbstractBasePanel
import org.apache.wicket.model.{PropertyModel, IModel}
import org.apache.wicket.spring.injection.annot.SpringBean
import net.rrm.ehour.project.service.ProjectAssignmentService
import net.rrm.ehour.domain.{ProjectAssignment, Project}
import net.rrm.ehour.util._
import org.apache.wicket.markup.html.list.{ListItem, ListView}
import org.apache.wicket.ajax.AjaxRequestTarget
import java.lang.Boolean
import org.apache.wicket.markup.head.{OnDomReadyHeaderItem, JavaScriptHeaderItem, IHeaderResponse, CssHeaderItem}
import org.apache.wicket.request.resource.{JavaScriptResourceReference, CssResourceReference}
import net.rrm.ehour.user.service.UserService
import net.rrm.ehour.ui.common.wicket._
import org.apache.wicket.markup.html.panel.Fragment
import java.lang.{Float => JFloat}
import net.rrm.ehour.ui.common.wicket.AjaxLink.LinkCallback
import java.{util => ju}
import net.rrm.ehour.ui.common.border.GreyRoundedBorder
import org.apache.wicket.event.Broadcast
import org.apache.wicket.Component

class AssignedUsersPanel(id: String, model: IModel[ProjectAdminBackingBean], onlyDeactivation:Boolean) extends AbstractBasePanel[ProjectAdminBackingBean](id, model) {
  def this(id: String, model: IModel[ProjectAdminBackingBean]) = this(id, model, false)

  val Self = this

  val Css = new CssResourceReference(classOf[AssignedUsersPanel], "assignedUsersPanel.css")
  val Js = new JavaScriptResourceReference(classOf[AssignedUsersPanel], "assignedUsersPanel.js")

  setOutputMarkupId(true)

  @SpringBean
  protected var assignmentService: ProjectAssignmentService = _

  @SpringBean
  protected var userService: UserService = _

  override def onInitialize() {
    val project = getPanelModelObject.getProject

    val border = new GreyRoundedBorder("border")
    addOrReplace(border)

    val assignments = sort(fetchProjectAssignmentsAndMergeWithModel(project))

    border.addOrReplace(createFilterOrHide("filterContainer", !assignments.isEmpty))

    val container = new Container("assignmentContainer")
    border.addOrReplace(container)
    container.addOrReplace(createAssignmentListView(assignments))
    container.setVisible(!assignments.isEmpty)
    container.setOutputMarkupPlaceholderTag(true)

    def callBack(fetchAssignments: (Project) => List[ProjectAssignment], showUsersVisibility: Boolean): LinkCallback =
      target => {
        val assignments = sort(fetchAssignments(getPanelModelObject.getProject))

        val view = createAssignmentListView(assignments)
        container.addOrReplace(view)
        container.setVisible(!assignments.isEmpty)

        val filterOrHide = createFilterOrHide("filterContainer", !assignments.isEmpty)
        border.addOrReplace(filterOrHide)
        target.add(filterOrHide)

        target.add(container)
        target.appendJavaScript(applyJsFilter)

        def changeVisibility(buttonId: String, visibility: Boolean) {
          val button = border.getBodyContainer.get(buttonId)
          button.setVisible(visibility)
          target.add(button)
        }

        changeVisibility("addUsers", showUsersVisibility)
        changeVisibility("hideUsers", !showUsersVisibility)
      }


    val hideUsers = new AjaxLink("hideUsers", callBack(p => fetchProjectAssignmentsAndMergeWithModel(p), showUsersVisibility = true))
    border.addOrReplace(hideUsers)
    hideUsers.setOutputMarkupId(true)
    hideUsers.setOutputMarkupPlaceholderTag(true)
    hideUsers.setVisible(false)

    val addUsers = new AjaxLink("addUsers", callBack(p => joinWithDuplicates(fetchUsers(p), fetchProjectAssignmentsAndMergeWithModel(p)), showUsersVisibility = false))
    border.addOrReplace(addUsers)
    addUsers.setOutputMarkupId(true)
    addUsers.setOutputMarkupPlaceholderTag(true)
    addUsers.setVisible(!onlyDeactivation)

    super.onInitialize()
  }

  private def createFilterOrHide(id: String, show: Boolean) = {
    val f = new Fragment(id, if (show) "filterInput" else "noAssignments", Self)
    f.setOutputMarkupId(true)
    f
  }


  private def sort(assignments: List[ProjectAssignment]) = assignments.sortWith((a, b) => a.getUser.compareTo(b.getUser) < 0)

  import WicketDSL._

  def createAssignmentListView(assignments: List[ProjectAssignment]): ListView[ProjectAssignment] = {
    val ContainerId = "container"

    new ListView[ProjectAssignment]("assignments", toJava(assignments)) {
      setOutputMarkupId(true)

      override def populateItem(item: ListItem[ProjectAssignment]) {
        val itemModel = item.getModel
        item.setOutputMarkupId(true)

        def createShowFragment(): Fragment = {
          val container = new Fragment(ContainerId, "displayRow", Self)
          container.setOutputMarkupId(true)

          container.add(new NonEmptyLabel("name", new PropertyModel(itemModel, "user.fullName")))
          container.add(new NonEmptyLabel("role", new PropertyModel(itemModel, "role")))
          container.add(new DateLabel("startDate", new PropertyModel(itemModel, "dateStart")))
          container.add(new DateLabel("endDate", new PropertyModel(itemModel, "dateEnd")))
          container.add(new NonEmptyLabel("rate", new PropertyModel(itemModel, "hourlyRate")))

          container.add(ajaxClick({
            target => {
              send(Self.getParent, Broadcast.BREADTH, EditAssignmentEvent(itemModel.getObject, target))
            }
          }))

          container.setVisible(!Self.getPanelModelObject.isDeleted(itemModel.getObject))

          container
        }

        val container = createShowFragment()
        item.add(container)
      }
    }
  }

  private def fetchUsers(project: Project) = {
    val users = toScala(userService.getActiveUsers)
    users.map(u => {
      val assignment = new ProjectAssignment(u, project)
      assignment.setActive(true)
      assignment.setAssignmentType(EhourConstants.ASSIGNMENT_TYPE_DATE)
      assignment.setDeletable(true)
      assignment
    })
  }

  private def fetchProjectAssignmentsAndMergeWithModel(project: Project) = {
    val assignmentSource = if (project.getProjectId == null) List() else toScala(assignmentService.getProjectAssignmentsAndCheckDeletability(project))

    getPanelModelObject.mergeOriginalAssignmentsWithQueue(assignmentSource)
  }

  private def joinWithDuplicates(notAssigned: List[ProjectAssignment], assigned: List[ProjectAssignment]) = {
    def filter(notAssigned: List[ProjectAssignment], assigned: List[ProjectAssignment]) = notAssigned.filterNot(p => assigned.exists(a => a.getUser.equals(p.getUser)))

    filter(notAssigned, assigned) ++ assigned
  }

  override def renderHead(response: IHeaderResponse) {
    response.render(CssHeaderItem.forReference(Css))
    response.render(JavaScriptHeaderItem.forReference(Js))
    response.render(OnDomReadyHeaderItem.forScript(applyJsFilter))
  }

  val applyJsFilter = "initAssignmentFilter();"
}

case class EditAssignmentEvent(assignment: ProjectAssignment, target: AjaxRequestTarget) {
  def refresh(components: Component*) {
    target.add(components: _*)
  }
}