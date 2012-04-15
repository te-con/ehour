package net.rrm.ehour.ui.common.menu

import org.apache.wicket.markup.html.WebPage
import org.apache.wicket.PageParameters
import org.apache.wicket.markup.html.list.{ListItem, ListView}
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.panel.{Fragment, Panel}
import org.apache.wicket.markup.html.link.Link
import org.apache.wicket.model.ResourceModel
import java.util.{List => JList}

sealed abstract class MenuItem

case class DropdownMenu(menuTitle: String, items: JList[LinkItem]) extends MenuItem

case class LinkItem(menuTitle: String, responsePageClass: Class[_ <: WebPage], pageParameters: Option[PageParameters] = None) extends MenuItem

class TreeBasedMenu(id: String, items: JList[_ <: MenuItem]) extends Panel(id) {

  val itemMenu = new ListView[MenuItem]("items", items) {
    def populateItem(item: ListItem[MenuItem]) {
      val menuItem = item.getModelObject

      menuItem match {
        case LinkItem(_,_,_) => {
          item.add(createLinkFragment("item", menuItem.asInstanceOf[LinkItem]))
        }
        case DropdownMenu(title, menuItems) => {
          val fragment = new Fragment("item", "linkItems", TreeBasedMenu.this)
          item.add(fragment)

          fragment.add(new Label("title", new ResourceModel(title)))

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
    fragment.add(createLink("menuLink", linkItem))
    fragment
  }

  private def createLink(id: String, linkItem: LinkItem) ={
    val link = new Link[Void]("menuLink") {
      def onClick() {
        if (linkItem.pageParameters.isDefined) {
          setResponsePage(linkItem.responsePageClass, linkItem.pageParameters.get)
        } else {
          setResponsePage(linkItem.responsePageClass)
        }
      }
    }

    link.add(new Label("menuLinkText", new ResourceModel(linkItem.menuTitle)))

    link
  }
}

