package net.rrm.ehour.ui.common.header

import net.rrm.ehour.AbstractSpec
import net.rrm.ehour.domain.UserRole
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation
import org.apache.wicket.markup.html.WebPage

import scala.collection.mutable

class LinkItemSpec extends AbstractSpec {
  "Link Item" should {
    "authorize a page with ADMIN role required to an admin" in {
      LinkItem.isUserAuthorizedForPage(classOf[AdminPage], mutable.Set(UserRole.MANAGER, UserRole.ADMIN)) should be(true)
    }

    "do not authorize a page with ADMIN role required to a user" in {
      LinkItem.isUserAuthorizedForPage(classOf[AdminPage], mutable.Set(UserRole.USER)) should be(false)
    }

    "do  authorize a page without annotation to anyone" in {
      LinkItem.isUserAuthorizedForPage(classOf[NoAuthPage], mutable.Set(UserRole.USER)) should be(true)
    }

  }
}

@AuthorizeInstantiation(value = Array(UserRole.ROLE_ADMIN, UserRole.ROLE_MANAGER))
class AdminPage extends WebPage

class NoAuthPage extends WebPage
