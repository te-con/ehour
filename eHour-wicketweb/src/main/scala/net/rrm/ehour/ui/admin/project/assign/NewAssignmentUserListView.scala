package net.rrm.ehour.ui.admin.project.assign

import org.apache.wicket.model.PropertyModel
import net.rrm.ehour.ui.common.panel.AbstractBasePanel
import org.apache.wicket.spring.injection.annot.SpringBean
import net.rrm.ehour.user.service.UserService
import java.{util => ju}
import net.rrm.ehour.domain.User
import net.rrm.ehour.ui.common.border.GreyBlueRoundedBorder
import org.apache.wicket.markup.html.list.{ListItem, ListView}
import net.rrm.ehour.ui.common.wicket.{Container, Event, NonEmptyLabel}
import org.apache.wicket.ajax.AjaxRequestTarget
import net.rrm.ehour.ui.common.wicket.WicketDSL._
import org.apache.wicket.request.resource.JavaScriptResourceReference
import org.apache.wicket.markup.head.{OnDomReadyHeaderItem, JavaScriptHeaderItem, IHeaderResponse}
import org.apache.wicket.model.util.ListModel
import com.google.common.collect.Lists
import collection.mutable.{Map => MMap}

class NewAssignmentUserListView(id: String) extends AbstractBasePanel[Unit](id) {
  val FilterJs = new JavaScriptResourceReference(classOf[CurrentAssignmentsListView], "listFilter.js")
  val HighlightJs = new JavaScriptResourceReference(classOf[CurrentAssignmentsListView], "listHighlight.js")

  val AffectedBorderId = "affectedBorder"
  val AffectedUsersListId = "affectedUsers"

  val userToItemId: MMap[User, String] = MMap.empty

  @SpringBean
  var userService: UserService = _

  override def onInitialize() {
    super.onInitialize()

    userToItemId.clear()

    val users = userService.getActiveUsers

    val allBorder = new GreyBlueRoundedBorder("allBorder")
    addOrReplace(allBorder)
    allBorder.addOrReplace(createAllUserView("users", users))

    val affectedBorder = new Container(AffectedBorderId)
    addOrReplace(affectedBorder)
    affectedBorder.setOutputMarkupId(true)
    affectedBorder.addOrReplace(createAffectedUserView(AffectedUsersListId, new ListModel[User](Lists.newArrayList())))
  }

  def affectedBorder = get(AffectedBorderId)

  def selectedAffectedUsers = affectedBorder.get(AffectedUsersListId).getDefaultModel.asInstanceOf[ListModel[User]]

  def createAllUserView(id: String, users: ju.List[User]): ListView[User] = {
    new ListView[User](id, users) {
      override def populateItem(item: ListItem[User]) {
        val itemModel = item.getModel

        item.add(ajaxClick({
          target => {
            val user = itemModel.getObject

            val users = selectedAffectedUsers.getObject
            val markupId = item.getMarkupId

            if (users.contains(user)) {
              target.appendJavaScript("listHighlight.deselect('%s')" format markupId)
              users.remove(user)
            } else {
              target.appendJavaScript("listHighlight.select('%s')" format markupId)
              users.add(user)
              userToItemId.put(user, markupId)
            }

            target.add(affectedBorder)
          }
        }))

        item.add(new NonEmptyLabel("name", new PropertyModel(itemModel, "fullName")))
      }
    }
  }


  def createAffectedUserView(id: String, users: ListModel[User]): ListView[User] = {
    new ListView[User](id, users) {
      override def populateItem(item: ListItem[User]) {
        val itemModel = item.getModel

        item.add(ajaxClick({
          target => {
            val users = selectedAffectedUsers.getObject
            val user = itemModel.getObject
            users.remove(user)

            target.add(affectedBorder)

            userToItemId.get(user) match {
              case Some(itemId) => target.appendJavaScript("listHighlight.deselect('%s')" format itemId)
              case None =>
            }
          }
        }))
//            val user = itemModel.getObject
//
//            val users = selectedUserModel.getObject
//            if (users.contains(user)) {
//              target.appendJavaScript("listHighlight.deselect('%s')" format item.getMarkupId)
//              users.remove(user)
//            } else {
//              target.appendJavaScript("listHighlight.select('%s')" format item.getMarkupId)
//              users.add(user)
//
//            }
//
//            //            send(Self, Broadcast.BUBBLE, EditAssignmentEvent(itemModel.getObject, target))
//          }
//        }))
//
//        //        val checkbox = new AjaxCheckBox("selected", new Model) {
//        //          def onUpdate(target: AjaxRequestTarget) {
//        //            send(getPage, Broadcast.BREADTH, if (getModelObject) UserSelectedEvent(item.getModelObject, target) else UserDeselectedEvent(item.getModelObject, target))
//        //          }
//        //        }
//        //        item.add(checkbox)
        item.add(new NonEmptyLabel("name", new PropertyModel(itemModel, "fullName")))
      }
    }
  }



  override def renderHead(response: IHeaderResponse) {
    response.render(JavaScriptHeaderItem.forReference(HighlightJs))
    response.render(JavaScriptHeaderItem.forReference(FilterJs))

    response.render(OnDomReadyHeaderItem.forScript(applyJsFilter))
  }

  val applyJsFilter = "new ListFilter('#filterUserInput', '#allUsers');"
}

case class UserSelectedEvent(user: User, target: AjaxRequestTarget) extends Event(target)
case class UserDeselectedEvent(user: User, target: AjaxRequestTarget) extends Event(target)
