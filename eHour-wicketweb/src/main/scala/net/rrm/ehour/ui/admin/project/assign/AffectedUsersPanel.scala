package net.rrm.ehour.ui.admin.project.assign

import net.rrm.ehour.ui.common.panel.AbstractBasePanel
import org.apache.wicket.event.IEvent
import net.rrm.ehour.domain.User
import org.apache.wicket.model.util.ListModel
import java.{util => ju}
import org.apache.wicket.markup.html.list.{ListItem, ListView}
import org.apache.wicket.markup.html.basic.Label
import com.google.common.collect.Lists

class AffectedUsersPanel(id: String) extends AbstractBasePanel[ju.List[User]](id, new ListModel[User](Lists.newArrayList())) {
  val Id = "users"

  setOutputMarkupId(true)

  override def onInitialize() {
    super.onInitialize()

    val view = new ListView[User](Id, getPanelModel) {
      def populateItem(item: ListItem[User]) {
        item.add(new Label("user", item.getModelObject.getFullName))
      }
    }

    add(view)
  }

  def userSelected(event: UserSelectedEvent) {
    val users = getPanelModelObject

    if (!users.contains(event.user))
      users.add(event.user)

    event.refresh(this)
  }

  def userDeselected(event: UserDeselectedEvent) {
    val users = getPanelModelObject

    users.remove(event.user)
    event.refresh(this)
  }

  override def onEvent(event: IEvent[_]) {
    event.getPayload match {
      case event: UserSelectedEvent => userSelected(event)
      case event: UserDeselectedEvent => userDeselected(event)
      case _ =>
    }
  }
}
