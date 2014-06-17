package net.rrm.ehour.ui.admin.user

import java.util
import java.util.Collections

import net.rrm.ehour.domain.User
import net.rrm.ehour.sort.UserComparator
import net.rrm.ehour.ui.common.border.GreyRoundedBorder
import net.rrm.ehour.ui.common.panel.AbstractBasePanel
import net.rrm.ehour.ui.common.panel.entryselector.{EntrySelectedEvent, EntrySelectorFilter, EntrySelectorListView, EntrySelectorPanel}
import net.rrm.ehour.user.service.UserService
import org.apache.wicket.AttributeModifier
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.event.Broadcast
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.list.ListItem
import org.apache.wicket.markup.html.panel.Fragment
import org.apache.wicket.model.{IModel, ResourceModel}
import org.apache.wicket.spring.injection.annot.SpringBean

class UserSelectionPanel(id: String) extends AbstractBasePanel[UserAdminBackingBean](id) {
  val Self = this

  @SpringBean
  protected var userService: UserService = _

  override def onInitialize() {
    super.onInitialize()

    val greyBorder = new GreyRoundedBorder("border", new ResourceModel("admin.user.title"))
    addOrReplace(greyBorder)

    val container = createListView()

    val selectorPanel = new EntrySelectorPanel("entrySelectorFrame", container, new ResourceModel("admin.user.hideInactive"))

    greyBorder.add(selectorPanel)
  }

  def createListView() = {
    val currentFilter = new EntrySelectorFilter()

    val users = getUsers(currentFilter)

    val userListView = new EntrySelectorListView[User]("itemList", users) {
      protected def onPopulate(item: ListItem[User], itemModel: IModel[User]) {
        val user = item.getModelObject

        if (!user.isActive) {
          item.add(AttributeModifier.append("class", "inactive"))
        }

        item.add(new Label("firstName", user.getFirstName))
        item.add(new Label("lastName", user.getLastName))
        item.add(new Label("userName", user.getUsername))
      }

      protected def onClick(item: ListItem[User], target: AjaxRequestTarget) {
        val userId: Integer = item.getModelObject.getUserId

        send(Self.getPage, Broadcast.DEPTH, EntrySelectedEvent(userId, target))
        //                getTabbedPanel.setEditBackingBean(new UserAdminBackingBean(userService.getUserAndCheckDeletability(userId)))
        //                getTabbedPanel.switchTabOnAjaxTarget(target, AddEditTabbedPanel.TABPOS_EDIT)
      }
    }

    val listHolder = new Fragment(EntrySelectorPanel.ITEM_LIST_HOLDER_ID, "userSelection", UserSelectionPanel.this)
    listHolder.add(userListView)
    listHolder
  }

  private def getUsers(currentFilter: EntrySelectorFilter): util.List[User] = {
    val users: util.List[User] = if (currentFilter == null || currentFilter.isFilterToggle) userService.getActiveUsers else userService.getUsers
    Collections.sort(users, new UserComparator(false))
    users
  }
}
