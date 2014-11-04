package net.rrm.ehour.ui.common.header

import java.util.{List => JList}

import net.rrm.ehour.ui.EhourWebApplication
import org.apache.wicket.AttributeModifier
import org.apache.wicket.ajax.{AjaxEventBehavior, AjaxRequestTarget}
import org.apache.wicket.markup.html.WebPage
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.link.Link
import org.apache.wicket.markup.html.list.{ListItem, ListView}
import org.apache.wicket.markup.html.panel.{Fragment, Panel}
import org.apache.wicket.model.ResourceModel
import org.apache.wicket.request.mapper.parameter.PageParameters
import org.apache.wicket.util.string.StringValue

import scala.collection.convert.WrapAsScala
import scala.language.existentials

sealed abstract class MenuItem {
  def isVisibleForLoggedInUser: Boolean
}

case class DropdownMenu(menuTitle: String, items: JList[LinkItem]) extends MenuItem {
  def isVisibleForLoggedInUser = WrapAsScala.asScalaBuffer(items).foldLeft(false)((total, item) => total || item.isVisibleForLoggedInUser)
}

case class LinkItem(menuTitle: String, responsePageClass: Class[_ <: WebPage], pageParameters: Option[PageParameters] = None) extends MenuItem {
  override def isVisibleForLoggedInUser = LinkItem.isUserAuthorizedForPage(responsePageClass)
}

object LinkItem {
  private[header] def isUserAuthorizedForPage(pageClass: Class[_ <: WebPage]): Boolean = {
    val authorizationStrategy = EhourWebApplication.get().getAuthorizationStrategy

    authorizationStrategy.isInstantiationAuthorized(pageClass)
  }
}

import scala.collection.JavaConversions._
class TreeBasedMenu(id: String, items: JList[_ <: MenuItem]) extends Panel(id) {
  override def onInitialize(): Unit = {
    super.onInitialize()
    val pageClass = getPage.getClass
    val parameters = getPage.getPageParameters

    def onSamePage(linkItem: LinkItem): Boolean = {
      val pageHit = linkItem.responsePageClass.isAssignableFrom(pageClass)

      if (pageHit) linkItem.pageParameters match {
        case Some(p) =>
          if (parameters.isEmpty) {
            false
          } else {
            val pairs = p.getAllNamed

            pairs.foldLeft(true)((b, a) => {
              val value: StringValue = parameters.get(a.getKey)
              value != null && value.toString == a.getValue
            })
          }
        case None => parameters.isEmpty
      } else
        pageHit
    }

    val itemMenu = new ListView[MenuItem]("items", items) {
      def populateItem(item: ListItem[MenuItem]) {
        val menuItem = item.getModelObject
        item.setVisible(menuItem.isVisibleForLoggedInUser)

        menuItem match {
          case LinkItem(_, _, _) =>
            val linkItem = menuItem.asInstanceOf[LinkItem]

            if (onSamePage(linkItem)) {
              item.add(createActiveLinkFragment("item", linkItem))
              item.add(AttributeModifier.replace("class", "activeli"))
            } else {
              item.add(createLinkFragment("item", linkItem))
            }

            item.add(createLinkBehavior(linkItem))

          case DropdownMenu(title, menuItems) =>
            val highlight = menuItems.exists(onSamePage)

            if (highlight) {
              item.add(AttributeModifier.replace("class", "activeli"))
            }

            val fragment = new Fragment("item", "linkItems", TreeBasedMenu.this)
            item.add(fragment)

            val titleLink = createLink("dropdown", menuItems.get(0))
            if (highlight) {
              titleLink.add(AttributeModifier.append("class", "active"))
            }
            titleLink.add(new Label("title", new ResourceModel(title)))
            fragment.add(titleLink)

            fragment.add(new ListView[LinkItem]("subItems", menuItems) {
              def populateItem(item: ListItem[LinkItem]) {
                val linkItem = item.getModelObject

                item.setVisible(linkItem.isVisibleForLoggedInUser)

                item.add(if (onSamePage(linkItem)) {
                  createActiveLinkFragment("subItem", linkItem)
                } else {
                  createLinkFragment("subItem", linkItem)
                })


              }
            })
        }
      }
    }

    itemMenu.setOutputMarkupId(true)
    itemMenu.setMarkupId("menu")

    addOrReplace(itemMenu)
  }

  private final def createLinkFragment(id: String, linkItem: LinkItem): Fragment = createLinkFragment(id, linkItem, active = false)

  private final def createActiveLinkFragment(id: String, linkItem: LinkItem) = createLinkFragment(id, linkItem, active = true)

  private final def createLinkFragment(id: String, linkItem: LinkItem, active: Boolean) = {
    val fragment = new Fragment(id, "linkItem", TreeBasedMenu.this)
    val linkForItem = createLinkForItem("menuLink", linkItem)

    if (active)
      linkForItem.add(AttributeModifier.replace("class", "active"))

    fragment.add(linkForItem)
    fragment
  }


  private final def createLinkForItem(id: String, linkItem: LinkItem) = {
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

  private def createLinkBehavior(linkItem: LinkItem) = {
    new AjaxEventBehavior("onclick") {
      override def onEvent(target: AjaxRequestTarget) {
        if (linkItem.pageParameters.isDefined) {
          setResponsePage(linkItem.responsePageClass, linkItem.pageParameters.get)
        } else {
          setResponsePage(linkItem.responsePageClass)
        }
      }
    }
  }
}