package net.rrm.ehour.ui.admin.project.assign

import net.rrm.ehour.ui.common.panel.AbstractBasePanel
import net.rrm.ehour.ui.common.border.GreyBlueRoundedBorder
import net.rrm.ehour.domain.{ProjectAssignment, Project}
import org.apache.wicket.spring.injection.annot.SpringBean
import net.rrm.ehour.project.service.ProjectAssignmentService
import org.apache.wicket.model.{PropertyModel, IModel}
import net.rrm.ehour.ui.admin.project.ProjectAdminBackingBean
import java.lang.Boolean
import net.rrm.ehour.util._
import org.apache.wicket.markup.html.panel.Fragment
import org.apache.wicket.markup.html.list.{ListItem, ListView}
import net.rrm.ehour.ui.common.wicket.{DateLabel, NonEmptyLabel}
import org.apache.wicket.{Component, AttributeModifier}
import net.rrm.ehour.ui.common.wicket.WicketDSL._
import org.apache.wicket.event.Broadcast
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.markup.head.{OnDomReadyHeaderItem, JavaScriptHeaderItem, IHeaderResponse}
import org.apache.wicket.request.resource.JavaScriptResourceReference

class CurrentAssignmentsListView(id: String, model: IModel[ProjectAdminBackingBean]) extends AbstractBasePanel[ProjectAdminBackingBean](id, model) {
  val Self = this
  val Js = new JavaScriptResourceReference(classOf[CurrentAssignmentsListView], "assignmentsList.js")

  @SpringBean
  protected var assignmentService: ProjectAssignmentService = _

  override def onInitialize() {
    super.onInitialize()


    val project = getPanelModelObject.getProject

    val assignments = sort(fetchProjectAssignments(project))

    addOrReplace(createFilter("filterContainer", !assignments.isEmpty))

    val border = new GreyBlueRoundedBorder("border")
    addOrReplace(border)
    border.addOrReplace(createAssignmentListView("assignments", assignments))
  }

  def createAssignmentListView(id: String, assignments: List[ProjectAssignment]): ListView[ProjectAssignment] = {
    new ListView[ProjectAssignment](id, toJava(assignments)) {
      override def populateItem(item: ListItem[ProjectAssignment]) {
        val itemModel = item.getModel

        item.add(new NonEmptyLabel("name", new PropertyModel(itemModel, "user.fullName")))
        item.add(new NonEmptyLabel("role", new PropertyModel(itemModel, "role")))
        item.add(new DateLabel("startDate", new PropertyModel(itemModel, "dateStart")))
        item.add(new DateLabel("endDate", new PropertyModel(itemModel, "dateEnd")))
        item.add(new NonEmptyLabel("rate", new PropertyModel(itemModel, "hourlyRate")))


        item.add(ajaxClick({
          target => {
            send(Self, Broadcast.BUBBLE, EditAssignmentEvent(itemModel.getObject, target))
          }
        }))

        if (itemModel.getObject.isActive) {
          item.add(AttributeModifier.append("style", "#6e9fcc !important"))
        }
      }
    }
  }

  private def createFilter(id: String, show: Boolean) = {
    val f = new Fragment(id, if (show) "filterInput" else "noAssignments", this)
    f.setOutputMarkupId(true)
    f
  }

  private def fetchProjectAssignments(project: Project) = if (project.getPK == null) List() else toScala(assignmentService.getProjectAssignmentsAndCheckDeletability(project))

  private def sort(assignments: List[ProjectAssignment]) = assignments.sortWith((a, b) => a.getUser.compareTo(b.getUser) < 0)

  override def renderHead(response: IHeaderResponse) {
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
