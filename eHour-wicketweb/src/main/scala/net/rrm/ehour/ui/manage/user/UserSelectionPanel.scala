package net.rrm.ehour.ui.manage.user

import java.util
import java.util.Collections

import com.google.common.collect.Lists
import net.rrm.ehour.domain.User
import net.rrm.ehour.sort.UserComparator
import net.rrm.ehour.ui.common.border.GreyRoundedBorder
import net.rrm.ehour.ui.common.panel.AbstractBasePanel
import net.rrm.ehour.ui.common.panel.entryselector._
import net.rrm.ehour.ui.common.session.EhourWebSession
import net.rrm.ehour.ui.common.wicket.Event
import net.rrm.ehour.user.service.UserService
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.event.{Broadcast, IEvent}
import org.apache.wicket.model.ResourceModel
import org.apache.wicket.spring.injection.annot.SpringBean

class UserSelectionPanel(id: String,
                        showInactiveToggle: Boolean,
                         titleResourceKey: Option[String],
                         filterUsers: (util.List[User]) => util.List[User]) extends AbstractBasePanel[UserManageBackingBean](id) {
  def this(id: String, titleResourceKey: Option[String]) = this(id, true, titleResourceKey, xs => xs)

  val Self = this

  var entrySelectorPanel:EntrySelectorPanel = _

  @SpringBean
  protected var userService: UserService = _

  override def onInitialize() {
    super.onInitialize()

    val greyBorder = titleResourceKey match {
      case Some(resourceKey) => new GreyRoundedBorder("border", new ResourceModel(resourceKey))
      case None => new GreyRoundedBorder("border")
    }

    addOrReplace(greyBorder)

    val clickHandler = new EntrySelectorPanel.ClickHandler {
      def onClick(row: EntrySelectorData.EntrySelectorRow, target: AjaxRequestTarget) {
        val id = row.getId.asInstanceOf[Integer]

        send(Self.getPage, Broadcast.BREADTH, EntrySelectedEvent(id, target))
      }
    }

    entrySelectorPanel = new EntrySelectorPanel("entrySelectorFrame",
                                                createSelectorData(users(EhourWebSession.getSession.getHideInactiveSelections)),
                                                clickHandler,
                                                if (showInactiveToggle) new ResourceModel("admin.user.hideInactive") else null)

    greyBorder.add(entrySelectorPanel)
  }

  private def createSelectorData(users: util.List[User]): EntrySelectorData = {
    val headers = Lists.newArrayList(new EntrySelectorData.Header("admin.user.lastName"),
                                    new EntrySelectorData.Header("admin.user.firstName"),
                                    new EntrySelectorData.Header("admin.user.username"))

    import scala.collection.JavaConversions._
    val rows = for (user <- users) yield {
      val cells = Lists.newArrayList(user.getLastName, user.getFirstName, user.getUsername)
      new EntrySelectorData.EntrySelectorRow(cells, user.getUserId, user.isActive)
    }

    new EntrySelectorData(headers, rows)
  }

  override def onEvent(event: IEvent[_]) {
    def refresh(event: Event) {
      entrySelectorPanel.updateData(createSelectorData(users(EhourWebSession.getSession.getHideInactiveSelections)))
      entrySelectorPanel.reRender(event.target)
    }

    event.getPayload match {
      case event: EntryListUpdatedEvent => refresh(event)
      case event: InactiveFilterChangedEvent => refresh(event)
      case _ =>
    }
  }

  private def users(hideInactive: Boolean): util.List[User] = {
    val users: util.List[User] = filterUsers(if (!showInactiveToggle || hideInactive) userService.getActiveUsers else userService.getUsers)
    Collections.sort(users, new UserComparator(false))
    users
  }
}
