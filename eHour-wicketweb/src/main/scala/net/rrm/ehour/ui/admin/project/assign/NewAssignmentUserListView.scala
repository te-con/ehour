package net.rrm.ehour.ui.admin.project.assign

import org.apache.wicket.model.{StringResourceModel, PropertyModel}
import net.rrm.ehour.ui.common.panel.AbstractBasePanel
import org.apache.wicket.spring.injection.annot.SpringBean
import net.rrm.ehour.user.service.UserService
import java.{util => ju}
import net.rrm.ehour.domain.User
import net.rrm.ehour.ui.common.border.GreyBlueRoundedBorder
import org.apache.wicket.markup.html.list.{ListItem, ListView}
import net.rrm.ehour.ui.common.wicket.{Container, NonEmptyLabel}
import net.rrm.ehour.ui.common.wicket.WicketDSL._
import org.apache.wicket.request.resource.JavaScriptResourceReference
import org.apache.wicket.markup.head.{OnDomReadyHeaderItem, JavaScriptHeaderItem, IHeaderResponse}
import org.apache.wicket.model.util.ListModel
import com.google.common.collect.Lists
import collection.mutable.{Map => MMap}
import org.apache.wicket.markup.html.WebMarkupContainer
import com.googlecode.wicket.jquery.ui.widget.tooltip.TooltipBehavior
import org.apache.wicket.AttributeModifier

class NewAssignmentUserListView(id: String) extends AbstractBasePanel[Unit](id) {
  val FilterJs = new JavaScriptResourceReference(classOf[CurrentAssignmentsListView], "listFilter.js")
  val HighlightJs = new JavaScriptResourceReference(classOf[CurrentAssignmentsListView], "listHighlight.js")

  val AffectedContainerId = "affectedContainer"
  val AffectedUsersListId = "affectedUsers"
  val AllUsersBorderId = "allBorder"

  val userToItemId: MMap[User, String] = MMap.empty

  @SpringBean
  var userService: UserService = _

  override def onInitialize() {
    super.onInitialize()

    userToItemId.clear()

    val users = userService.getActiveUsers
    ju.Collections.sort(users)

    val allBorder = new GreyBlueRoundedBorder(AllUsersBorderId)
    addOrReplace(allBorder)
    allBorder.addOrReplace(createAllUserView("users", users))

    val affectedContainer = new Container(AffectedContainerId)
    addOrReplace(affectedContainer)
    affectedContainer.setOutputMarkupId(true)
    affectedContainer.addOrReplace(createAffectedUserView(AffectedUsersListId, new ListModel[User](Lists.newArrayList())))

    val help = new WebMarkupContainer("help")
    help.add(new TooltipBehavior())
    help.add(AttributeModifier.replace("title", new StringResourceModel("admin.projects.assignments.add.help", null)))
    addOrReplace(help)
  }

  private[assign] def affectedContainer = get(AffectedContainerId)

  def selectedAffectedUsers = affectedContainer.get(AffectedUsersListId).getDefaultModel.asInstanceOf[ListModel[User]]

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

              ju.Collections.sort(users)

              userToItemId.put(user, markupId)
            }

            target.add(affectedContainer)
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

            target.add(affectedContainer)

            userToItemId.get(user) match {
              case Some(itemId) => target.appendJavaScript("listHighlight.deselect('%s')" format itemId)
              case None =>
            }
          }
        }))

        item.add(new NonEmptyLabel("name", new PropertyModel(itemModel, "fullName")))
      }
    }
  }

  override def renderHead(response: IHeaderResponse) {
    response.render(JavaScriptHeaderItem.forReference(HighlightJs))
    response.render(JavaScriptHeaderItem.forReference(FilterJs))

    response.render(OnDomReadyHeaderItem.forScript(applyJsFilter))
    response.render(OnDomReadyHeaderItem.forScript("$( document ).tooltip();"))

  }

  val applyJsFilter = "new ListFilter('#filterUserInput', '#allUsers');"
}
