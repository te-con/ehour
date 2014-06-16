package net.rrm.ehour.ui.common.session

import java.util.Locale

import net.rrm.ehour.AbstractSpringWebAppSpec
import net.rrm.ehour.domain.User
import net.rrm.ehour.domain.UserRole._
import net.rrm.ehour.ui.common.util.AuthUtil
import org.apache.wicket.ThreadContext
import org.apache.wicket.authroles.authorization.strategies.role.Roles
import org.apache.wicket.markup.html.panel.EmptyPanel
import org.apache.wicket.request.Request
import org.mockito.Mockito._

class EhourWebSessionSpec extends AbstractSpringWebAppSpec {
  val req = mock[Request]
  when(req.getLocale).thenReturn(Locale.CANADA)

  override def beforeEach() {
    super.beforeEach()

    tester.startComponentInPage(classOf[EmptyPanel])
  }

  "Ehour Web Session should" should {
    val User = new User("thies", "password").addUserRole(CONSULTANT)

    "do not allow non-admin users to impersonate" in {
      val session = new EhourWebSession(req) {
        override def authenticate(username: String, password: String): Boolean = true

        override def getRoles: Roles = new Roles(s"$ROLE_CONSULTANT,$ROLE_REPORT,$ROLE_PROJECTMANAGER")
      }

      session.signIn("a", "b")

      intercept[UnauthorizedToImpersonateException] {
        session.impersonateUser(User)
      }
    }

    "do allow admin users to impersonate and have User set to impersonating user" in {
      val session = new EhourWebSession(req) {
        override def authenticate(username: String, password: String): Boolean = true

        override def isAdmin: Boolean = true
      }

      ThreadContext.setSession(session)

      session.signIn("a", "b")

      session.impersonateUser(User)

      val user = AuthUtil.getUser
      user should be(User)

      session.getRoles should contain(ROLE_CONSULTANT)
    }

    "return to the original user when impersonating is stopped" in {
      val session = new EhourWebSession(req) {
        override def authenticate(username: String, password: String): Boolean = true

        override def isAdmin: Boolean = true
      }

      ThreadContext.setSession(session)

      session.signIn("a", "b")

      session.impersonateUser(User)

      session.stopImpersonating()

      AuthUtil.getUser should not be User
    }
  }
}
