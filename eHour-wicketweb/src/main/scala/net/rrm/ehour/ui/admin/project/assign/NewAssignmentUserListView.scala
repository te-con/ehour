package net.rrm.ehour.ui.admin.project.assign

import org.apache.wicket.model.PropertyModel
import net.rrm.ehour.ui.common.panel.AbstractBasePanel
import org.apache.wicket.spring.injection.annot.SpringBean
import net.rrm.ehour.user.service.UserService
import java.{util => ju}
import net.rrm.ehour.domain.User
import net.rrm.ehour.ui.common.border.GreyBlueRoundedBorder
import org.apache.wicket.markup.html.list.{ListItem, ListView}
import net.rrm.ehour.ui.common.wicket.{Event, NonEmptyLabel}
import org.apache.wicket.ajax.AjaxRequestTarget
import net.rrm.ehour.ui.common.wicket.WicketDSL._
import org.apache.wicket.request.resource.JavaScriptResourceReference
import org.apache.wicket.markup.head.{OnDomReadyHeaderItem, JavaScriptHeaderItem, IHeaderResponse}

class NewAssignmentUserListView(id: String) extends AbstractBasePanel[Unit](id) {
  val FilterJs = new JavaScriptResourceReference(classOf[CurrentAssignmentsListView], "listFilter.js")

  @SpringBean
  var userService: UserService = _

  override def onInitialize() {
    super.onInitialize()

    val users = userService.getActiveUsers

    val allBorder = new GreyBlueRoundedBorder("allBorder")
    addOrReplace(allBorder)
    allBorder.addOrReplace(createAllUserView("users", users))

    val affectedBorder = new GreyBlueRoundedBorder("affectedBorder")
    addOrReplace(affectedBorder)
//    affectedBorder.addOrReplace(createAssignmentListView("users", users))

  }

  def createAllUserView(id: String, users: ju.List[User]): ListView[User] = {
    new ListView[User](id, users) {
      override def populateItem(item: ListItem[User]) {
        val itemModel = item.getModel

        item.add(ajaxClick({
          target => {
            target.appendJavaScript("listFilter.highlight('%s')" format (item.getMarkupId))
//            send(Self, Broadcast.BUBBLE, EditAssignmentEvent(itemModel.getObject, target))
          }
        }))

//        val checkbox = new AjaxCheckBox("selected", new Model) {
//          def onUpdate(target: AjaxRequestTarget) {
//            send(getPage, Broadcast.BREADTH, if (getModelObject) UserSelectedEvent(item.getModelObject, target) else UserDeselectedEvent(item.getModelObject, target))
//          }
//        }
//        item.add(checkbox)
        item.add(new NonEmptyLabel("name", new PropertyModel(itemModel, "fullName")))
      }
    }
  }


  override def renderHead(response: IHeaderResponse) {
    response.render(JavaScriptHeaderItem.forReference(FilterJs))

    response.render(OnDomReadyHeaderItem.forScript(applyJsFilter))
  }

  val applyJsFilter = "new ListFilter('#filterUserInput', '#allUsers');"
}

case class UserSelectedEvent(user: User, target: AjaxRequestTarget) extends Event(target)
case class UserDeselectedEvent(user: User, target: AjaxRequestTarget) extends Event(target)
