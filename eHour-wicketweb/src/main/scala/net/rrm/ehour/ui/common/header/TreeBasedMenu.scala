package net.rrm.ehour.ui.common.header

import java.util.{List => JList}

import net.rrm.ehour.domain.UserRole
import net.rrm.ehour.ui.common.session.EhourWebSession
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation
import org.apache.wicket.markup.html.WebPage
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.link.Link
import org.apache.wicket.markup.html.list.{ListItem, ListView}
import org.apache.wicket.markup.html.panel.{Fragment, Panel}
import org.apache.wicket.model.ResourceModel
import org.apache.wicket.request.mapper.parameter.PageParameters

import scala.collection.convert.WrapAsScala
import scala.collection.mutable
import scala.language.existentials

sealed abstract class MenuItem {
  def isVisibleForLoggedInUser: Boolean
}

case class DropdownMenu(menuTitle: String, items: JList[LinkItem]) extends MenuItem {
  def isVisibleForLoggedInUser = WrapAsScala.asScalaBuffer(items).foldLeft(false)((total, item) => total || item.isVisibleForLoggedInUser)
}

case class LinkItem(menuTitle: String, responsePageClass: Class[_ <: WebPage], pageParameters: Option[PageParameters] = None) extends MenuItem {
  override def isVisibleForLoggedInUser = LinkItem.isUserAuthorizedForPage(responsePageClass, WrapAsScala.asScalaSet(EhourWebSession.getUser.getUserRoles))
}

object LinkItem {
  private[header] def isUserAuthorizedForPage(pageClass: Class[_ <: WebPage], roles: mutable.Set[UserRole]): Boolean = {
    if (pageClass.isAnnotationPresent(classOf[AuthorizeInstantiation])) {
      if (roles != null) {
        val roleNames = roles.map(_.getRole)
        val authorizedRoles = pageClass.getAnnotation(classOf[AuthorizeInstantiation])
        authorizedRoles.value().toList.exists(roleNames.contains)
      } else false
    }
    else
      true
  }
}


class TreeBasedMenu(id: String, items: JList[_ <: MenuItem]) extends Panel(id) {

  val itemMenu = new ListView[MenuItem]("items", items) {
    def populateItem(item: ListItem[MenuItem]) {
      val menuItem = item.getModelObject
      item.setVisible(menuItem.isVisibleForLoggedInUser)

      menuItem match {
        case LinkItem(_, _, _) => {
          item.add(createLinkFragment("item", menuItem.asInstanceOf[LinkItem]))
        }
        case DropdownMenu(title, menuItems) => {
          val fragment = new Fragment("item", "linkItems", TreeBasedMenu.this)
          item.add(fragment)

          val titleLink = createLink("dropdown", menuItems.get(0))
          titleLink.add(new Label("title", new ResourceModel(title)))
          fragment.add(titleLink)

          fragment.add(new ListView[LinkItem]("subItems", menuItems) {
            def populateItem(item: ListItem[LinkItem]) {
              item.add(createLinkFragment("subItem", item.getModelObject))
            }
          })
        }
      }
    }
  }

  itemMenu.setOutputMarkupId(true)
  itemMenu.setMarkupId("menu")

  add(itemMenu)

  private def createLinkFragment(id: String, linkItem: LinkItem) = {
    val fragment = new Fragment(id, "linkItem", TreeBasedMenu.this)
    fragment.add(createLinkForItem("menuLink", linkItem))
    fragment
  }

  private def createLinkForItem(id: String, linkItem: LinkItem) = {
    val link = createLink("menuLink", linkItem)

    link.add(new Label("menuLinkText", new ResourceModel(linkItem.menuTitle)))

    link
  }

  private def createLink(id: String, linkItem: LinkItem): Link[Void] =
    new Link[Void](id) {
      def onClick() {
        if (linkItem.pageParameters.isDefined) {
          setResponsePage(linkItem.responsePageClass, linkItem.pageParameters.get)
        } else {
          setResponsePage(linkItem.responsePageClass)
        }
      }
    }
}

