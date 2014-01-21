package net.rrm.ehour.ui.financial.lock

import org.apache.wicket.model.IModel
import net.rrm.ehour.ui.common.panel.AbstractAjaxPanel
import org.apache.wicket.spring.injection.annot.SpringBean
import net.rrm.ehour.timesheet.service.{AffectedUser, TimesheetLockService}
import net.rrm.ehour.ui.common.border.GreyBlueRoundedBorder
import org.apache.wicket.markup.html.list.{ListItem, ListView}
import org.apache.wicket.markup.html.basic.Label
import net.rrm.ehour.util._

class AffectedUsersPanel(id: String, lockModel: IModel[LockAdminBackingBean]) extends AbstractAjaxPanel[LockAdminBackingBean](id, lockModel) {
  @SpringBean
  protected var lockService: TimesheetLockService = _

  override def onBeforeRender(): Unit = {
    super.onBeforeRender()

    val blueBorder = new GreyBlueRoundedBorder(AffectedUsersPanel.BorderId)
    blueBorder.setOutputMarkupId(true)
    addOrReplace(blueBorder)

    val domainObject = getPanelModelObject.getDomainObject
    val affectedUsers = lockService.findAffectedUsers(domainObject.getDateStart, domainObject.getDateEnd)

    val repeater = new ListView[AffectedUser](AffectedUsersPanel.AffectedUsersId, toJava(affectedUsers)) {
      def populateItem(item: ListItem[AffectedUser]) {
        val affectedUser = item.getModelObject

        item.add(new Label("user", affectedUser.user.getFullName))
        item.add(new Label("hours", affectedUser.hoursBooked))
      }
    }
    
    blueBorder.add(repeater)
  }
}

object AffectedUsersPanel {
  val AffectedUsersId = "affectedUsers"
  val BorderId = "border"
}
