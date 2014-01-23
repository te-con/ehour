package net.rrm.ehour.ui.financial.lock

import org.apache.wicket.model.{PropertyModel, ResourceModel, IModel}
import net.rrm.ehour.ui.common.panel.AbstractAjaxPanel
import org.apache.wicket.spring.injection.annot.SpringBean
import net.rrm.ehour.timesheet.service.TimesheetLockService
import net.rrm.ehour.ui.common.border.{GreyRoundedBorder, GreyBlueRoundedBorder}
import org.apache.wicket.markup.html.list.{ListItem, ListView}
import org.apache.wicket.markup.html.basic.Label
import net.rrm.ehour.util._
import net.rrm.ehour.domain.Project
import net.rrm.ehour.ui.common.wicket.WicketDSL._
import net.rrm.ehour.timesheet.service.AffectedUser
import scala.Float
import net.rrm.ehour.ui.common.wicket.{Model, Container}

class LockAffectedUsersPanel(id: String, lockModel: IModel[LockAdminBackingBean]) extends AbstractAjaxPanel[LockAdminBackingBean](id, lockModel) {
  @SpringBean
  protected var lockService: TimesheetLockService = _

  val self = this

  override def onBeforeRender(): Unit = {
    super.onBeforeRender()

    val greyBorder = new GreyRoundedBorder(LockAffectedUsersPanel.GreyBorderId, new ResourceModel("op.lock.admin.affectedUsers.headers"))
    addOrReplace(greyBorder)

    val blueBorder = new GreyBlueRoundedBorder(LockAffectedUsersPanel.BorderId)
    blueBorder.setOutputMarkupId(true)
    greyBorder.addOrReplace(blueBorder)

    val domainObject = getPanelModelObject.getDomainObject
    val affectedUsers = lockService.findAffectedUsers(domainObject.getDateStart, domainObject.getDateEnd)

    val affectedUserDetailsModel = Model(AffectedUser())

    val details: Container = createProjectList(affectedUserDetailsModel)
    greyBorder.addOrReplace(details)

    val repeater = new ListView[AffectedUser](LockAffectedUsersPanel.AffectedUsersId, toJava(affectedUsers)) {
      def populateItem(item: ListItem[AffectedUser]) {
        val affectedUser = item.getModelObject

        item.add(new Label("user", affectedUser.user.getFullName))
        item.add(new Label("hours", affectedUser.hoursBooked))

        item.add(ajaxClick({
          target => {
            affectedUserDetailsModel.setObject(affectedUser)
            target.add(details)
          }
        }))
      }
    }

    blueBorder.add(repeater)
  }

  private def createProjectList(model: IModel[AffectedUser]) = {
    val repeater = new ListView[(Project, Float)]("projects", new PropertyModel(model, "javaProjects")) {
      def populateItem(item: ListItem[(Project, Float)]): Unit = {
        val (project, hours) = item.getModelObject

        item.add(new Label("project", project.getFullName))
        item.add(new Label("hours", hours))
      }
    }

    val container = new Container("projectContainer")
    container.add(repeater)

    val projectsForLabel = new Label("projectsFor", new PropertyModel(model, "user.fullName")) {
      override def isVisible: Boolean = model.getObject.user != null
    }
    container.add(projectsForLabel)

    container
  }
}

object LockAffectedUsersPanel {
  val AffectedUsersId = "affectedUsers"
  val GreyBorderId = "greyBorder"
  val BorderId = "border"
}
