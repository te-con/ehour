package net.rrm.ehour.ui.manage.lock

import net.rrm.ehour.domain.Project
import net.rrm.ehour.timesheet.service.{AffectedUser, TimesheetLockService}
import net.rrm.ehour.ui.common.border.GreyBlueRoundedBorder
import net.rrm.ehour.ui.common.panel.AbstractAjaxPanel
import net.rrm.ehour.ui.common.wicket.WicketDSL._
import net.rrm.ehour.ui.common.wicket.{Container, Model}
import net.rrm.ehour.util._
import org.apache.wicket.markup.head.{IHeaderResponse, JavaScriptHeaderItem}
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.list.{ListItem, ListView}
import org.apache.wicket.model.{IModel, PropertyModel}
import org.apache.wicket.request.resource.JavaScriptResourceReference
import org.apache.wicket.spring.injection.annot.SpringBean

class LockAffectedUsersPanel(id: String, lockModel: IModel[LockAdminBackingBean]) extends AbstractAjaxPanel[LockAdminBackingBean](id, lockModel) {
  @SpringBean
  protected var lockService: TimesheetLockService = _

  val HighlightJs = new JavaScriptResourceReference(classOf[LockAffectedUsersPanel], "affectedUsers.js")

  setOutputMarkupId(true)

  val self = this

  import scala.collection.JavaConversions._
  override def onBeforeRender() {
    super.onBeforeRender()

    val blueBorder = new GreyBlueRoundedBorder(LockAffectedUsersPanel.BorderId)
    blueBorder.setOutputMarkupId(true)
    addOrReplace(blueBorder)

    val domainObject = getPanelModelObject.getDomainObject
    val affectedUsers = lockService.findAffectedUsers(domainObject.getDateStart, domainObject.getDateEnd, domainObject.getExcludedUsers)

    val affectedUserDetailsModel = Model(AffectedUser())

    val details = createProjectList(affectedUserDetailsModel)
    addOrReplace(details)

    val repeater = new ListView[AffectedUser](LockAffectedUsersPanel.AffectedUsersId, toJava(affectedUsers)) {
      def populateItem(item: ListItem[AffectedUser]) {
        val affectedUser = item.getModelObject

        item.add(new Label("user", affectedUser.user.getFullName))
        item.add(new Label("hours", affectedUser.hoursBooked))

        item.add(ajaxClick({
          target => {
            affectedUserDetailsModel.setObject(affectedUser)
            target.add(details)
            target.appendJavaScript("affectedListHighlight.selectAndDeselectRest('%s')" format item.getMarkupId)
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

    val container = new Container("projectContainer") {
      override def isVisible = model.getObject.user != null
    }
    container.setOutputMarkupPlaceholderTag(true)
    container.add(repeater)

    container
  }

  override def renderHead(response: IHeaderResponse) {
    response.render(JavaScriptHeaderItem.forReference(HighlightJs))
  }
}

object LockAffectedUsersPanel {
  val AffectedUsersId = "affectedUsers"
  val GreyBorderId = "greyBorder"
  val BorderId = "border"
}