package net.rrm.ehour.ui.admin.project.assign

import org.apache.wicket.model.{Model, PropertyModel}
import net.rrm.ehour.ui.common.panel.AbstractBasePanel
import org.apache.wicket.spring.injection.annot.SpringBean
import net.rrm.ehour.user.service.UserService
import java.{util => ju}
import net.rrm.ehour.domain.User
import net.rrm.ehour.ui.common.border.GreyBlueRoundedBorder
import org.apache.wicket.markup.html.list.{ListItem, ListView}
import net.rrm.ehour.ui.common.wicket.{Event, NonEmptyLabel}
import org.apache.wicket.event.Broadcast
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox
import org.apache.wicket.ajax.AjaxRequestTarget

class NewAssignmentUserListView(id: String) extends AbstractBasePanel[Unit](id) {
  val self = this

  @SpringBean
  var userService: UserService = _

  override def onInitialize() {
    super.onInitialize()

    val users = userService.getActiveUsers

    val border = new GreyBlueRoundedBorder("border")
    addOrReplace(border)
    border.addOrReplace(createAssignmentListView("users", users))
  }

  def createAssignmentListView(id: String, users: ju.List[User]): ListView[User] = {
    new ListView[User](id, users) {
      override def populateItem(item: ListItem[User]) {
        val itemModel = item.getModel

        val checkbox = new AjaxCheckBox("selected", new Model) {
          def onUpdate(target: AjaxRequestTarget) {
            send(self, Broadcast.BUBBLE, if (getModelObject) UserSelectedEvent(item.getModelObject, target) else UserDeselectedEvent(item.getModelObject, target))
          }
        }
        item.add(checkbox)
        item.add(new NonEmptyLabel("name", new PropertyModel(itemModel, "fullName")))
      }
    }
  }
}

case class UserSelectedEvent(user: User, target: AjaxRequestTarget) extends Event(target)
case class UserDeselectedEvent(user: User, target: AjaxRequestTarget) extends Event(target)
