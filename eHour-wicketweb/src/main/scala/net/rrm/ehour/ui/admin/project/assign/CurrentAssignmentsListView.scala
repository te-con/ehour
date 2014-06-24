package net.rrm.ehour.ui.admin.project.assign

import java.lang.Boolean

import net.rrm.ehour.domain.{Project, ProjectAssignment}
import net.rrm.ehour.project.service.ProjectAssignmentService
import net.rrm.ehour.ui.admin.project.ProjectAdminBackingBean
import net.rrm.ehour.ui.common.border.GreyBlueRoundedBorder
import net.rrm.ehour.ui.common.panel.AbstractBasePanel
import net.rrm.ehour.ui.common.wicket.AjaxLink._
import net.rrm.ehour.ui.common.wicket.WicketDSL._
import net.rrm.ehour.ui.common.wicket.{AjaxLink, DateLabel, Event, NonEmptyLabel}
import net.rrm.ehour.util._
import org.apache.wicket.AttributeModifier
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.event.Broadcast
import org.apache.wicket.markup.head.{IHeaderResponse, JavaScriptHeaderItem, OnDomReadyHeaderItem}
import org.apache.wicket.markup.html.WebMarkupContainer
import org.apache.wicket.markup.html.list.{ListItem, ListView}
import org.apache.wicket.markup.html.panel.Fragment
import org.apache.wicket.model.{IModel, PropertyModel}
import org.apache.wicket.request.resource.JavaScriptResourceReference
import org.apache.wicket.spring.injection.annot.SpringBean

class CurrentAssignmentsListView(id: String, model: IModel[ProjectAdminBackingBean], onlyDeactivate: Boolean = false) extends AbstractBasePanel[ProjectAdminBackingBean](id, model) {
  val Self = this
  val HighlightJs = new JavaScriptResourceReference(classOf[CurrentAssignmentsListView], "listHighlight.js")
  val FilterJs = new JavaScriptResourceReference(classOf[CurrentAssignmentsListView], "listFilter.js")

  @SpringBean
  protected var assignmentService: ProjectAssignmentService = _

  override def onInitialize() {
    super.onInitialize()

    val project = getPanelModelObject.getProject

    val assignments = sort(fetchProjectAssignments(project))

    addOrReplace(createFilter("filterContainer", assignments.nonEmpty))

    addOrReplace(createAssignmentListView("assignments", assignments))

    val linkCallback: LinkCallback = target => send(this, Broadcast.BUBBLE, NewAssignmentEvent(target))
    val link = new AjaxLink("addUsers", linkCallback)
    link.setVisible(!onlyDeactivate)
    addOrReplace(link)
  }

  def createAssignmentListView(id: String, assignments: List[ProjectAssignment]): Fragment = {
    val component = if (assignments.isEmpty)
      new Fragment(id, "noAssignments", this)
    else {
      val f = new Fragment(id, "assignmentFragment", this)

      val border = new GreyBlueRoundedBorder("border")
      f.add(border)

      val assignmentList = new ListView[ProjectAssignment]("row", toJava(assignments)) {
        override def populateItem(item: ListItem[ProjectAssignment]) {
          val itemModel = item.getModel

          item.add(new NonEmptyLabel("name", new PropertyModel(itemModel, "user.fullName")))
          item.add(new NonEmptyLabel("role", new PropertyModel(itemModel, "role")))
          item.add(new DateLabel("startDate", new PropertyModel(itemModel, "dateStart")))
          item.add(new DateLabel("endDate", new PropertyModel(itemModel, "dateEnd")))
          item.add(new NonEmptyLabel("rate", new PropertyModel(itemModel, "hourlyRate")))

          item.add(ajaxClick({
            target => {
              target.appendJavaScript("listHighlight.selectAndDeselectRest('%s')" format item.getMarkupId)
              send(Self, Broadcast.BUBBLE, EditAssignmentEvent(itemModel.getObject, target))
            }
          }))

          if (!itemModel.getObject.isActive) {
            item.add(AttributeModifier.append("style", "#6e9fcc !important"))
          }

          item.setOutputMarkupId(true)
        }
      }

      border.add(assignmentList)
      f
    }

    component.setOutputMarkupId(true)
    component
  }

  private def createFilter(id: String, show: Boolean) = {
    val f = if (show) new Fragment(id, "filterInput", this) else new WebMarkupContainer(id)
    f.setOutputMarkupId(true)
    f
  }

  private def fetchProjectAssignments(project: Project) = if (project.getPK == null) List() else toScala(assignmentService.getProjectAssignmentsAndCheckDeletability(project))

  private def sort(assignments: List[ProjectAssignment]) = assignments.sortWith((a, b) => a.getUser.compareTo(b.getUser) < 0)

  override def renderHead(response: IHeaderResponse) {
    response.render(JavaScriptHeaderItem.forReference(HighlightJs))
    response.render(JavaScriptHeaderItem.forReference(FilterJs))

    response.render(OnDomReadyHeaderItem.forScript(applyJsFilter))
  }

  val applyJsFilter = "new ListFilter('#filterAssignmentInput', '.assignmentList');"
}

case class EditAssignmentEvent(assignment: ProjectAssignment, override val target: AjaxRequestTarget) extends Event(target)

case class NewAssignmentEvent(override val target: AjaxRequestTarget) extends Event(target)

