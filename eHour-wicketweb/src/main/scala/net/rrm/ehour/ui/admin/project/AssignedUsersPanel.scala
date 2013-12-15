package net.rrm.ehour.ui.admin.project

import net.rrm.ehour.ui.common.panel.AbstractBasePanel
import org.apache.wicket.model.{StringResourceModel, PropertyModel, IModel}
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
import org.apache.wicket.markup.html.form.{Form, CheckBox, TextField}
import net.rrm.ehour.ui.common.panel.datepicker.LocalizedDatePicker
import org.apache.wicket.markup.html.WebMarkupContainer
import org.apache.wicket.AttributeModifier
import net.rrm.ehour.ui.common.component.{AjaxFormComponentFeedbackIndicator, ValidatingFormComponentAjaxBehavior}
import org.apache.wicket.validation.validator.RangeValidator
import java.lang.{Float => JFloat}
import net.rrm.ehour.ui.common.validator.DateOverlapValidator
import net.rrm.ehour.ui.common.wicket.AjaxLink.LinkCallback
import java.{util => ju}

class AssignedUsersPanel(id: String, model: IModel[ProjectAdminBackingBean], onlyDeactivation:Boolean) extends AbstractBasePanel[ProjectAdminBackingBean](id, model) {
  def this(id: String, model: IModel[ProjectAdminBackingBean]) = this(id, model, false)

  val Self = this

  val Css = new CssResourceReference(classOf[AssignedUsersPanel], "projectAdmin.css")
  val Js = new JavaScriptResourceReference(classOf[AssignedUsersPanel], "projectAdmin.js")

  @SpringBean
  protected var assignmentService: ProjectAssignmentService = _

  @SpringBean
  protected var userService: UserService = _

  override def onInitialize() {
    val project = getPanelModelObject.getProject

    val assignments = sort(fetchProjectAssignmentsAndMergeWithModel(project))

    addOrReplace(createFilterOrHide("filterContainer", !assignments.isEmpty))

    val container = new Container("assignmentContainer")
    addOrReplace(container)
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
        Self.addOrReplace(filterOrHide)
        target.add(filterOrHide)

        target.add(container)
        target.appendJavaScript(applyJsFilter)

        def changeVisibility(buttonId: String, visibility: Boolean) {
          val addUsersButton = Self.get(buttonId)
          addUsersButton.setVisible(visibility)
          target.add(addUsersButton)
        }

        changeVisibility("addUsers", showUsersVisibility)
        changeVisibility("hideUsers", !showUsersVisibility)
      }


    val hideUsers = new AjaxLink("hideUsers", callBack(p => fetchProjectAssignmentsAndMergeWithModel(p), showUsersVisibility = true))
    addOrReplace(hideUsers)
    hideUsers.setOutputMarkupId(true)
    hideUsers.setOutputMarkupPlaceholderTag(true)
    hideUsers.setVisible(false)

    val addUsers = new AjaxLink("addUsers", callBack(p => joinWithDuplicates(fetchUsers(p), fetchProjectAssignmentsAndMergeWithModel(p)), showUsersVisibility = false))
    addOrReplace(addUsers)
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

        def createNameLabel = new NonEmptyLabel("name", new PropertyModel(itemModel, "user.fullName"))

        def createEditFragment(): Fragment = {
          def closeEditMode(target: AjaxRequestTarget) {
            val replacement = createShowFragment()
            item.addOrReplace(replacement)
            target.add(replacement)
          }

          val fragment = new Fragment(ContainerId, "inputRow", Self)
          fragment.setOutputMarkupId(true)

          val form = new Form[Unit]("editForm")
          fragment.add(form)

          form.add(new CheckBox("active", new PropertyModel[Boolean](itemModel, "active")))

          form.add(createNameLabel)

          val dateStart = new LocalizedDatePicker("startDate", new PropertyModel[ju.Date](itemModel, "dateStart"))
          dateStart.add(new ValidatingFormComponentAjaxBehavior)
          form.add(new AjaxFormComponentFeedbackIndicator("dateStartValidationError", dateStart))
          form.add(dateStart)

          val dateEnd = new LocalizedDatePicker("endDate", new PropertyModel[ju.Date](itemModel, "dateEnd"))
          dateEnd.add(new ValidatingFormComponentAjaxBehavior)
          form.add(new AjaxFormComponentFeedbackIndicator("dateEndValidationError", dateEnd))
          form.add(dateEnd)

          val rate = new TextField("rate", new PropertyModel[JFloat](itemModel, "hourlyRate"), classOf[JFloat])
          form.add(rate)

          rate.add(new ValidatingFormComponentAjaxBehavior)
          rate.add(RangeValidator.minimum[JFloat](0f))
          form.add(new AjaxFormComponentFeedbackIndicator("rateValidationError", rate))

          form.add(new DateOverlapValidator("dateStartDateEnd", dateStart, dateEnd))

          val submitButton = new WebMarkupContainer("submit")
          submitButton.add(ajaxSubmit(form, {
            (form, target) =>
              Self.getPanelModelObject.addAssignmentToQueue(itemModel.getObject)
              closeEditMode(target)
          }))

          form.add(submitButton)

          val cancelButton = new WebMarkupContainer("cancel")
          cancelButton.add(ajaxClick({
            target => closeEditMode(target)
          }))
          form.add(cancelButton)

          val deleteButton = new WebMarkupContainer("delete")
          deleteButton.setVisible(!(onlyDeactivation || !itemModel.getObject.isDeletable))
          deleteButton.add(ajaxClick({
            target =>
              Self.getPanelModelObject.deleteAssignment(itemModel.getObject)
              item.setVisible(false)
              target.add(item)
          }))
          form.add(deleteButton)

          fragment
        }

        def createShowFragment(): Fragment = {
          val container = new Fragment(ContainerId, "displayRow", Self)
          container.setOutputMarkupId(true)

          val activeAssignment = new WebMarkupContainer("activeAssignment")
          val assignment = itemModel.getObject

          def isUnassigned = assignment.getPK == null
          def isAssigned = !isUnassigned || getPanelModelObject.getAssignmentsQueue.contains(assignment)
          def isActive = assignment.isActive

          val (cssClass, title) =
            if (isAssigned) {
              if (isActive) ("ui-icon-bullet", "admin.projects.assignments.assigned")
              else ("ui-icon-radio-off", "admin.projects.assignments.inactive_assigned")
            } else
              ("ui-icon-radio-on", "admin.projects.assignments.not_assigned")

          activeAssignment.add(AttributeModifier.append("class", cssClass))
          activeAssignment.add(AttributeModifier.replace("title", new StringResourceModel(title, Self, null)))
          container.add(activeAssignment)

          container.add(createNameLabel)
          container.add(new DateLabel("startDate", new PropertyModel(itemModel, "dateStart")))
          container.add(new DateLabel("endDate", new PropertyModel(itemModel, "dateEnd")))
          container.add(new NonEmptyLabel("rate", new PropertyModel(itemModel, "hourlyRate")))

          container.add(ajaxClick({
            target => {
              val replacement = createEditFragment()
              item.addOrReplace(replacement)
              target.add(replacement)
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

  val applyJsFilter = "initFilter();"
}
