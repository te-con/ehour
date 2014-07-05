package net.rrm.ehour.ui.common.session

import java.util.Locale

import net.rrm.ehour.AbstractSpringWebAppSpec
import net.rrm.ehour.domain.User
import net.rrm.ehour.domain.UserRole._
import net.rrm.ehour.report.criteria.UserSelectedCriteria
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
    val User = new User("thies", "password").addUserRole(USER)

    "throw exception when not allowed to impersonate" in {
      val session = new EhourWebSession(req)  {
        override def allowedToImpersonate(user: User) = false

        override def authenticate(username: String, password: String): Boolean = true

        override def getRoles: Roles = new Roles(s"$ROLE_USER,$ROLE_REPORT,$ROLE_PROJECTMANAGER")
      }

      session.signIn("a", "b")

      intercept[UnauthorizedToImpersonateException] {
        session.impersonateUser(User)
      }
    }

    "return impersonated user when impersonating is succesful" in {
      val session = new EhourWebSession(req)  {
        override def allowedToImpersonate(userToImpersonate: User) = true

        override def authenticate(username: String, password: String): Boolean = true

        override def isAdmin: Boolean = true
      }

      ThreadContext.setSession(session)

      session.signIn("a", "b")

      session.setUserSelectedCriteria(new UserSelectedCriteria)

      session.impersonateUser(User)

      val user = EhourWebSession.getUser
      user should be(User)

      session.getRoles should contain(ROLE_USER)
      session.getUserSelectedCriteria should be(null)
    }

    "return to the original user when impersonating is stopped" in {
      val session = new EhourWebSession(req) {
        override def allowedToImpersonate(userToImpersonate: User) = true

        override def authenticate(username: String, password: String): Boolean = true

        override def isAdmin: Boolean = true
      }

      ThreadContext.setSession(session)

      session.signIn("a", "b")

      session.impersonateUser(User)

      session.setUserSelectedCriteria(new UserSelectedCriteria)

      session.stopImpersonating()

      EhourWebSession.getUser should not be User
      session.getUserSelectedCriteria should be(null)
    }
  }
}
