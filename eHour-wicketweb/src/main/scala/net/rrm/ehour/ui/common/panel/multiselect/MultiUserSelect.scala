package net.rrm.ehour.ui.common.panel.multiselect

import java.{util => ju}

import com.google.common.collect.Lists
import net.rrm.ehour.domain.User
import net.rrm.ehour.ui.common.border.GreyBlueRoundedBorder
import net.rrm.ehour.ui.common.panel.AbstractBasePanel
import net.rrm.ehour.ui.common.wicket.WicketDSL._
import net.rrm.ehour.ui.common.wicket.{Container, Event, NonEmptyLabel}
import net.rrm.ehour.user.service.UserService
import org.apache.wicket.AttributeModifier
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.event.Broadcast
import org.apache.wicket.markup.head.{CssHeaderItem, IHeaderResponse}
import org.apache.wicket.markup.html.list.{ListItem, ListView}
import org.apache.wicket.model.util.ListModel
import org.apache.wicket.model.{IModel, PropertyModel}
import org.apache.wicket.request.resource.CssResourceReference
import org.apache.wicket.spring.injection.annot.SpringBean

import scala.collection.mutable.{Map => MMap}

class MultiUserSelect(id: String, model: IModel[ju.List[User]] = new ListModel[User](Lists.newArrayList())) extends AbstractBasePanel(id, model) with Filterable with Highlights {
  val Css = new CssResourceReference(classOf[MultiUserSelect], "multiUserSelect.css")

  val SelectedContainerId = "selectedContainer"
  val SelectedUsersListId = "selectedUsers"
  val AllUsersBorderId = "allBorder"

  val userToItemId: MMap[User, String] = MMap.empty

  val self = this

  override def listFilterId = "#filterUserInput"
  override def listId = "#allUsers"

  @SpringBean
  var userService: UserService = _

  override def onInitialize() {
    super.onInitialize()

    userToItemId.clear()

    val users = userService.getActiveUsers
    ju.Collections.sort(users)

    val allBorder = new GreyBlueRoundedBorder(AllUsersBorderId)
    addOrReplace(allBorder)

    val selectedContainer = new Container(SelectedContainerId)
    addOrReplace(selectedContainer)
    selectedContainer.setOutputMarkupId(true)
    selectedContainer.addOrReplace(createSelectedUserView(SelectedUsersListId, model))

    allBorder.addOrReplace(createAllUserView("users", users))
  }

  private def selectedContainer =  get(SelectedContainerId)

  def selectedUsers = selectedContainer.get(SelectedUsersListId).getDefaultModel.asInstanceOf[IModel[ju.List[User]]]

  def createAllUserView(id: String, users: ju.List[User]): ListView[User] = {
    val selected = selectedUsers.getObject

    new ListView[User](id, users) {
      override def populateItem(item: ListItem[User]) {
        val itemModel = item.getModel
        val user = itemModel.getObject

        item.add(ajaxClick({
          target => {
            val users = selectedUsers.getObject
            val markupId = item.getMarkupId

            if (users.contains(user)) {
              target.appendJavaScript("listHighlight.deselect('%s')" format markupId)
              users.remove(user)
            } else {
              target.appendJavaScript("listHighlight.select('%s')" format markupId)
              users.add(user)

              ju.Collections.sort(users)

              userToItemId.put(user, markupId)
            }

            target.add(selectedContainer)

            sendEvent(target)
          }
        }))

        if (selected.contains(user)) {
          item.add(AttributeModifier.append("class", "selected"))
        }

        item.add(new NonEmptyLabel("name", new PropertyModel(itemModel, "fullName")))
      }
    }
  }

  def createSelectedUserView(id: String, users: IModel[ju.List[User]]): ListView[User] = {
    new ListView[User](id, users) {
      override def populateItem(item: ListItem[User]) {
        val itemModel = item.getModel

        item.add(ajaxClick({
          target => {
            val users = selectedUsers.getObject
            val user = itemModel.getObject
            users.remove(user)

            target.add(selectedContainer)

            userToItemId.get(user) match {
              case Some(itemId) => target.appendJavaScript("listHighlight.deselect('%s')" format itemId)
              case None =>
            }

            sendEvent(target)
          }
        }))

        item.add(new NonEmptyLabel("name", new PropertyModel(itemModel, "fullName")))
      }

    }
  }

  def sendEvent(target: AjaxRequestTarget) {
    send(self, Broadcast.BUBBLE, SelectionUpdatedEvent(target))
  }

  override def renderHead(response: IHeaderResponse) {
    super.renderHead(response)
    response.render(CssHeaderItem.forReference(Css))
  }
}

case class SelectionUpdatedEvent(override val target: AjaxRequestTarget) extends Event(target)