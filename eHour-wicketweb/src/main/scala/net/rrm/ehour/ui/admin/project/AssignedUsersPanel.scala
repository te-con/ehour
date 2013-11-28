package net.rrm.ehour.ui.admin.project

import net.rrm.ehour.ui.common.panel.AbstractBasePanel
import org.apache.wicket.model.{StringResourceModel, PropertyModel, IModel}
import org.apache.wicket.spring.injection.annot.SpringBean
import net.rrm.ehour.project.service.{ProjectAssignmentManagementService, ProjectAssignmentService}
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
import java.util.Date
import org.apache.wicket.markup.html.WebMarkupContainer
import org.apache.wicket.AttributeModifier
import net.rrm.ehour.ui.common.component.{AjaxFormComponentFeedbackIndicator, ValidatingFormComponentAjaxBehavior}
import org.apache.wicket.validation.validator.RangeValidator
import java.lang.{Float => JFloat}
import net.rrm.ehour.ui.common.validator.DateOverlapValidator
import net.rrm.ehour.ui.common.wicket.AjaxLink.LinkCallback


class AssignedUsersPanel(id: String, model: IModel[ProjectAdminBackingBean]) extends AbstractBasePanel[ProjectAdminBackingBean](id, model) {

  val Self = this

  val Css = new CssResourceReference(classOf[AssignedUsersPanel], "projectAdmin.css")
  val Js = new JavaScriptResourceReference(classOf[AssignedUsersPanel], "projectAdmin.js")

  @SpringBean
  protected var assignmentService: ProjectAssignmentService = _

  @SpringBean
  protected var assignmentManagementService: ProjectAssignmentManagementService = _

  @SpringBean
  protected var userService: UserService = _

  override def onInitialize() {
    def joinWithDuplicates(notAssigned: List[ProjectAssignment], assigned: List[ProjectAssignment]) = filter(notAssigned, assigned) ++ assigned

    def filter(notAssigned: List[ProjectAssignment], assigned: List[ProjectAssignment]) = notAssigned.filterNot(p => assigned.exists(a => a.getUser.equals(p.getUser)))

    super.onInitialize()

    val project = getPanelModelObject.getProject

    val assignments = sort(fetchProjectAssignments(project))


    val container = new Container("assignmentContainer")
    addOrReplace(container)
    container.addOrReplace(createAssignmentListView(assignments))

    def callBack(fetchAssignments:(Project) => List[ProjectAssignment], showUsersVisibility: Boolean):LinkCallback =
      target => {
        val assignments = sort(fetchAssignments(getPanelModelObject.getProject))

        val view = createAssignmentListView(assignments)
        container.addOrReplace(view)
        target.add(container)
        target.appendJavaScript(applyJsFilter)

        val addUsersButton = Self.get("addUsers")
        addUsersButton.setVisible(showUsersVisibility)
        target.add(addUsersButton)

        val hideUsersButton = Self.get("hideUsers")
        hideUsersButton.setVisible(!showUsersVisibility)
        target.add(hideUsersButton)
      }


    val hideUsers = new AjaxLink("hideUsers", callBack(p => fetchProjectAssignments(p), showUsersVisibility = true))
    addOrReplace(hideUsers)
    hideUsers.setOutputMarkupId(true)
    hideUsers.setOutputMarkupPlaceholderTag(true)
    hideUsers.setVisible(false)

    val addUsers = new AjaxLink("addUsers", callBack(p => joinWithDuplicates(fetchUsers, fetchProjectAssignments(p)), showUsersVisibility = false))
    addOrReplace(addUsers)
    addUsers.setOutputMarkupId(true)
    addUsers.setOutputMarkupPlaceholderTag(true)
  }

  private def sort(assignments: List[ProjectAssignment]) = assignments.sortWith((a, b) => a.getUser.compareTo(b.getUser) < 0)

  import WicketDSL._

  def createAssignmentListView(assignments: List[ProjectAssignment]): ListView[ProjectAssignment] = {
    new ListView[ProjectAssignment]("assignments", toJava(assignments)) {
      setOutputMarkupId(true)

      override def populateItem(item: ListItem[ProjectAssignment]) {
        val itemModel = item.getModel

        def createNameLabel = new AlwaysOnLabel("name", new PropertyModel(itemModel, "user.fullName"))

        def createEditFragment: Fragment = {
          def closeEditMode(target: AjaxRequestTarget) {
            val replacement = createShowFragment
            item.addOrReplace(replacement)
            target.add(replacement)
          }

          val fragment = new Fragment("container", "inputRow", Self)
          fragment.setOutputMarkupId(true)

          val form = new Form[Unit]("editForm")
          fragment.add(form)

          form.add(new CheckBox("active", new PropertyModel[Boolean](itemModel, "active")))

          form.add(createNameLabel)

          val dateStart = new LocalizedDatePicker("startDate", new PropertyModel[Date](itemModel, "dateStart"))
          dateStart.add(new ValidatingFormComponentAjaxBehavior)
          form.add(new AjaxFormComponentFeedbackIndicator("dateStartValidationError", dateStart))
          form.add(dateStart)

          val dateEnd = new LocalizedDatePicker("endDate", new PropertyModel[Date](itemModel, "dateEnd"))
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
              assignmentManagementService.updateProjectAssignment(itemModel.getObject)
              closeEditMode(target)
          }))

          form.add(submitButton)

          val cancelButton = new WebMarkupContainer("cancel")
          cancelButton.add(ajaxClick({
            target => closeEditMode(target)
          }))
          form.add(cancelButton)

          fragment
        }

        def createShowFragment: Fragment = {
          val container = new Fragment("container", "displayRow", Self)
          container.setOutputMarkupId(true)

          val activeAssignment = new WebMarkupContainer("activeAssignment")
          val assignment = itemModel.getObject
          val (cssClass, title) = if (assignment.getPK == null) ("ui-icon-radio-on", "admin.projects.assignments.not_assigned")
          else if (assignment.isActive) ("ui-icon-bullet", "admin.projects.assignments.assigned")
          else ("ui-icon-radio-off", "admin.projects.assignments.assigned")
          activeAssignment.add(AttributeModifier.append("class", cssClass))
          activeAssignment.add(AttributeModifier.replace("title", new StringResourceModel(title, Self, null)))
          container.add(activeAssignment)

          container.add(createNameLabel)
          container.add(new DateLabel("startDate", new PropertyModel(itemModel, "dateStart")))
          container.add(new DateLabel("endDate", new PropertyModel(itemModel, "dateEnd")))
          container.add(new AlwaysOnLabel("rate", new PropertyModel(itemModel, "hourlyRate")))

          container.add(ajaxClick({
            target => {
              val replacement = createEditFragment
              item.addOrReplace(replacement)
              target.add(replacement)
            }
          }))

          container
        }

        val container = createShowFragment
        item.add(container)
      }
    }
  }

  def fetchUsers = {
    val project = getPanelModelObject.getProject

    val users = toScala(userService.getActiveUsers)
    users.map(u => {
      val assignment = new ProjectAssignment(u, project)
      assignment.setActive(true)
      assignment.setAssignmentType(EhourConstants.ASSIGNMENT_TYPE_DATE)
      assignment
    })
  }

  def fetchProjectAssignments(project: Project): List[ProjectAssignment] = {
    if (project.getProjectId == null) {
      List()
    } else {
      toScala(assignmentService.getProjectAssignments(project))
    }
  }

  override def renderHead(response: IHeaderResponse) {
    response.render(CssHeaderItem.forReference(Css))
    response.render(JavaScriptHeaderItem.forReference(Js))
    response.render(OnDomReadyHeaderItem.forScript(applyJsFilter))
  }

  val applyJsFilter = "initFilter();"
}
